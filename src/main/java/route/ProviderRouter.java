package route;

import static spark.Spark.*;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import data.DbGroup;
import data.DbTuple;
import data.InvoiceItem;
import data.InvoiceItems;
import data.ProviderSettings;
import data.TollPeriods;
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
			} catch (Exception ex) {
				System.out.println("[post] /tuple error:" + ex.getMessage());
				response.status(Consts.HttpBadRequest);
			}
			return "";
		});
		
		get("/invoicePeriodes/:groupId", (request, response) -> {
			
			return gson.toJson(new String[]{"YESTERDAY", "TODAY"});
			
		});
		
		get("/invoiceitems/:groupId/:periodeId", (request, response) -> {
			DbTuple[] tuples = (DbTuple[]) DatabaseHelper.Get(DbTuple.class).toArray();
			response.status(Consts.HttpStatuscodeOk);
			
			int id = Integer.parseInt(request.params(":periodeId"));
			InvoiceItems items = new InvoiceItems();
			
			items.setItems(new InvoiceItem[]{new InvoiceItem("hgasfdjghsdfujjhdfusf", 1)});
			
			// Header: "x-custom-session-id"
			
			// todo gw: signature!!!
			return gson.toJson(items);
			
		});
		
		post("/pay:sessionId", (request, response) -> {
			int id = Integer.parseInt(request.params(":pay:sessionId"));
			
			return "";
			
		});
	}

}
