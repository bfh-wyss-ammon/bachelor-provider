package route;

import static spark.Spark.*;
import java.util.Date;
import data.DbPublicKey;
import data.DbTuple;
import rest.BaseRouter;
import rest.Router;
import signatures.Signature;
import util.DatabaseHelper;
import util.Route;
import util.VerifyHelper;

public class ProviderRouter extends BaseRouter implements Router {

	public ProviderRouter() {
		// load from setting
		super(10001);
	}

	@Override
	public void start() {
		post("/tuple", (request, response) -> {
			try {

				// parse
				String strTuple = request.body();
				DbTuple tuple = (DbTuple) gson.fromJson(request.body(), DbTuple.class);
				String strSignature = request.headers(Route.SignatureHeader);
				Signature signature = (Signature) gson.fromJson(strSignature, Signature.class);

				// get public key from db
				int groupId = tuple.getGroupId();
				DbPublicKey publicKey = DatabaseHelper.Get(DbPublicKey.class, "groupId = '" + groupId + "'");

				// check public key
				if (publicKey == null) {
					response.status(Route.BadRequest);
					return "";
				}

				// verify tuple signature
				
				boolean signatureValid = VerifyHelper.verify(publicKey, signature, strTuple.getBytes());

				if (!signatureValid) {
					response.status(Route.BadRequest);
					return "";
				}

				// save tuple to db
				tuple.setReceived(new Date());
				DatabaseHelper.Save(DbTuple.class, tuple);

				response.status(Route.StatuscodeOk);
			} catch (Exception ex) {

				response.status(Route.BadRequest);
			}
			return "";
		});
	}

}
