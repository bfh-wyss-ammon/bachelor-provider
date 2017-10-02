package route;

import static spark.Spark.*;
import java.util.Date;
import data.DbPublicKey;
import data.DbTuple;
import rest.BaseRouter;
import rest.Router;
import signatures.Signature;
import util.Consts;
import util.DatabaseHelper;
import util.HashHelper;
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
				String strSignature = request.headers(Consts.SignatureHeader);
				Signature signature = (Signature) gson.fromJson(strSignature, Signature.class);

				// get public key from db
				int groupId = tuple.getGroupId();
				DbPublicKey publicKey = DatabaseHelper.Get(DbPublicKey.class, "groupId = '" + groupId + "'");

				// check public key
				if (publicKey == null) {
					response.status(Consts.HttpBadRequest);
					return "";
				}

				// verify tuple signature

				boolean signatureValid = VerifyHelper.verify(publicKey, signature, HashHelper.getHash(strTuple));

				if (!signatureValid) {
					response.status(Consts.HttpBadRequest);
					return "";
				}

				// save tuple to db
				tuple.setReceived(new Date());
				DatabaseHelper.Save(DbTuple.class, tuple);

				response.status(Consts.HttpStatuscodeOk);
			} catch (Exception ex) {

				response.status(Consts.HttpBadRequest);
			}
			return "";
		});
	}

}
