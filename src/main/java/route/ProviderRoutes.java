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
import data.DbUser;
import requests.JoinRequest;
import responses.JoinResponse;
import util.BigIntegerGsonTypeAdapter;
import util.Credential;
import util.Database;
import util.GroupHelper;
import util.JoinHelper;
import util.MembershipHelper;
import util.Route;
import util.SessionHelper;
import util.SettingsHelper;

public class ProviderRoutes {

	private final static Gson gson;
	static {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(BigInteger.class, new BigIntegerGsonTypeAdapter());
		gson = builder.excludeFieldsWithoutExposeAnnotation().create();
	}

	public static void main(String[] args) {
		
		
		//Need to place this code somewhere else. --> Downloading the public group data from authority
		
		

		port(10001);

		options("/*", (request, response) -> Route.ConfigureOptions(request, response));
		before((request, response) -> Route.ConfigureBefore(request, response));

		post("/tuple", (request, response) -> {
				response.status(Route.StatuscodeOk);
			return "";
		});

	}

}
