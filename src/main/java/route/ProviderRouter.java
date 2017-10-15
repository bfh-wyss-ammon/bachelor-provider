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
			
				Tuple tuple = gson.fromJson(request.body(), Tuple.class);
				
				if(tuple == null)
				{
					response.status(Consts.HttpBadRequest);
					return "";
				}
				String authorityURL = SettingsHelper.getSettings(ProviderSettings.class).getAuthorityURL();
				if(!DatabaseHelper.Exists(DbGroup.class, " groupId= ' " + tuple.getGroupId() + "'")) {
					if(!GroupHelper.getGroupsFromAuthority(authorityURL, tuple.getGroupId())){
						response.status(Consts.HttpBadRequest);
						return "";
					}
				}
				
				DbGroup group = DatabaseHelper.Get(DbGroup.class, "groupId='" + tuple.getGroupId() + "'");
						
				if (!VerifyHelper.verify(group.getPublicKey(), tuple.getSignature(), HashHelper.getHash(tuple)))
				{
					System.out.println("[post] /tuple bad signature");
					response.status(Consts.HttpBadRequest);
					return "";
				}

				DbTuple dpTuple = new DbTuple(tuple, group);
				dpTuple.setReceived(new Date());
				
				
				DatabaseHelper.Save(DbTuple.class, dpTuple);
				response.status(Consts.HttpStatuscodeOk);
				System.out.println("[post] /tuple new tuple saved");
				
			} catch (Exception ex) {
				System.out.println("[post] /tuple error:" + ex.getMessage());
				response.status(Consts.HttpBadRequest);
			}
			return "";
		});
	}

}
