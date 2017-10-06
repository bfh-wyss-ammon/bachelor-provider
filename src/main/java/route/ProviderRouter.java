package route;

import static spark.Spark.*;
import java.util.Date;

import data.DbGroup;
import data.DbTuple;
import data.ProviderSettings;
import data.Tuple;
import rest.BaseRouter;
import rest.Router;
import util.Consts;
import util.DatabaseHelper;
import util.GroupHelper;
import util.HashHelper;
import util.SettingsHelper;
import util.VerifyHelper;

public class ProviderRouter extends BaseRouter implements Router {

	public ProviderRouter() {
		// load from setting
		super(SettingsHelper.getSettings(ProviderSettings.class).getPort());
	}

	@Override
	public void start() {
		post("/tuple", (request, response) -> {
			try {

				Tuple tuple = (Tuple) gson.fromJson(request.body(), Tuple.class);
				if (tuple == null) {
					response.status(Consts.HttpBadRequest);
					return "";
				}

				int groupId = tuple.getGroupId();
				boolean groupExistsLocally = DatabaseHelper.Exists(DbGroup.class,
						"groupId='" + tuple.getGroupId() + "'");

				if (!groupExistsLocally
						&& !GroupHelper.getGroupsFromAuthority(SettingsHelper.getSettings(ProviderSettings.class).getAuthorityURL()+"groups/", groupId)) {
					response.status(Consts.HttpBadRequest);
					return "";
				}
				DbGroup group = DatabaseHelper.Get(DbGroup.class, tuple.getGroupId());

				DbTuple dpTuple = new DbTuple(tuple, group);

				if (!VerifyHelper.verify(group.getPublicKey(), tuple.getSignature(), HashHelper.getHash(tuple))) {
					response.status(Consts.HttpBadRequest);
					return "";
				}

				dpTuple.setReceived(new Date());

				DatabaseHelper.Save(DbTuple.class, dpTuple);
				response.status(Consts.HttpStatuscodeOk);

			} catch (Exception ex) {

				response.status(Consts.HttpBadRequest);
			}
			return "";
		});
	}

}
