package route;

import static spark.Spark.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import data.DbGroup;
import data.DbSession;
import data.DbTuple;
import data.DbVTuple;
import data.InvoiceItems;
import data.ProviderSettings;
import data.Tuple;
import rest.BaseRouter;
import rest.Router;
import util.Consts;
import util.DatabaseHelper;
import util.GroupHelper;
import util.HashHelper;
import util.PeriodHelper;
import util.ProviderSessionHelper;
import util.ProviderSignatureHelper;
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
				byte[] hash = HashHelper.getHash(tuple);
				if (!VerifyHelper.verify(group.getPublicKey(), tuple.getSignature(), hash))
				{
					System.out.println("[post] /tuple bad signature");
					response.status(Consts.HttpBadRequest);
					return "";
				}

				DbTuple dbTuple = new DbTuple(tuple, group);
				dbTuple.setReceived(new Date());
				dbTuple.setHash(Base64.getEncoder().encodeToString(hash));
				DatabaseHelper.Save(DbTuple.class, dbTuple);
				response.status(Consts.HttpStatuscodeOk);
			} catch (Exception ex) {
				System.out.println("[post] /tuple error:" + ex.getMessage());
				response.status(Consts.HttpBadRequest);
			}
			return "";
		});
		
		get("/invoicePeriodes/:groupId", (request, response) -> {
			
			ProviderSettings settings = SettingsHelper.getSettings(ProviderSettings.class);
			int gracePeriods = settings.getGracePeriods();
			int periodLength = settings.getPeriodLengthDays();
			String[] periods = new String[gracePeriods+1];
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			
			for (int i =0 ; i<=gracePeriods;i++) {
				periods[i]= formatters.format(LocalDate.now().minusDays(i*periodLength));
			}
			return gson.toJson(periods);
			
		});
		
		get("/invoiceitems/:periodId", (request, response) -> {
			
			int groupId;
			String strPeriod;
			Date period;
			
			try {
				//groupId = Integer.parseInt( request.params(":groupId"));
				groupId=2;
				strPeriod = request.params(":periodId");
				DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				period = formatter.parse(strPeriod);
			} catch (Exception e){
				response.status(Consts.HttpBadRequest);
				return "";
			}

			boolean groupOK = DatabaseHelper.Exists(DbGroup.class, " groupId= ' " + groupId + "'");
			boolean periodOK = PeriodHelper.isAllowed(strPeriod);
			
			if(!groupOK || !periodOK) {
				response.status(Consts.HttpBadRequest);
				return "";		
			}

			DbGroup group = DatabaseHelper.Get(DbGroup.class, "groupId='" + groupId + "'");
			
			DbSession session = ProviderSessionHelper.getSession(group);
			if (session == null) {
				response.status(Consts.HttpInternalServerError);
				return "";
			}
			
			Date after = PeriodHelper.getAfterLimit(period);
			Date before = PeriodHelper.getBeforeLimit(period);
			List<DbVTuple> tuples = DatabaseHelper.Get(DbVTuple.class,"groupId='" + groupId + "'","created",after,before);
			InvoiceItems items = new InvoiceItems();
			tuples.forEach(tuple -> items.getItems().put(tuple.getHash(), tuple.getPrice()));
			items.setSessionId(session.getToken());
			byte[] hash = HashHelper.getHash(items);
			items.setSignature(Base64.getEncoder().encodeToString(ProviderSignatureHelper.sign(hash)));

			DatabaseHelper.SaveOrUpdate(session);
			response.header(Consts.ProviderTokenHeader, session.getToken());
			response.status(Consts.HttpStatuscodeOk);
			return gson.toJson(items);
		});
		
		post("/pay/:sessionId", (request, response) -> {
			int id = Integer.parseInt(request.params(":pay:sessionId"));
			
			return "";
			
		});
	}

}
