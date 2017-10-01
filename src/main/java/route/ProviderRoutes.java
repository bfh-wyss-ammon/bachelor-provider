package route;

import static spark.Spark.*;
import java.io.Console;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.AuthoritySettings;
import data.DbGroup;
import data.DbJoinSession;
import data.DbManagerKey;
import data.DbMembership;
import data.DbPublicKey;
import data.DbTuple;
import data.DbUser;
import requests.JoinRequest;
import responses.JoinResponse;
import signatures.Signature;
import util.BigIntegerGsonTypeAdapter;
import util.Credential;
import util.Database;
import util.GroupHelper;
import util.JoinHelper;
import util.MembershipHelper;
import util.Route;
import util.SessionHelper;
import util.SettingsHelper;
import util.VerifyHelper;

public class ProviderRoutes {

	private final static Gson gson;
	static {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(BigInteger.class, new BigIntegerGsonTypeAdapter());
		gson = builder.excludeFieldsWithoutExposeAnnotation().create();
	}

	public static void main(String[] args) {

		port(10001);

		options("/*", (request, response) -> Route.ConfigureOptions(request, response));
		before((request, response) -> Route.ConfigureBefore(request, response));

		post("/tuple", (request, response) -> {
			try {

				// parse
				String strTuple = request.body();
				DbTuple tuple = (DbTuple) gson.fromJson(request.body(), DbTuple.class);
				String strSignature = request.headers(Route.SignatureHeader);
				Signature signature = (Signature) gson.fromJson(strSignature, Signature.class);

				// get public key from db
				int groupId = tuple.getGroupId();
				DbPublicKey publicKey = Database.Get(DbPublicKey.class, "groupId = '" + groupId + "'");

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
				
				//save tuple to db
				tuple.setReceived(new Date());
				Database.Save(DbTuple.class, tuple);
				
				response.status(Route.StatuscodeOk);
			} catch (Exception ex) {

				response.status(Route.BadRequest);
			}
			return "";
		});

	}

}
