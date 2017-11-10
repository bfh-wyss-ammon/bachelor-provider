package route;

import static spark.Spark.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import data.DbGroup;
import data.DbSession;
import data.DbSignature;
import data.DbTuple;
import data.DbVPayment;
import data.DbVTuple;
import data.InvoiceItems;
import data.ProviderSettings;
import data.Receipt;
import data.Costs;
import data.State;
import data.Tuple;
import data.Payment;
import rest.BaseRouter;
import util.Consts;
import util.DatabaseHelper;
import util.GroupHelper;
import util.HashHelper;
import util.PeriodHelper;
import util.ProviderSessionHelper;
import util.ProviderSignatureHelper;
import util.SettingsHelper;
import util.VerifyHelper;
import websocket.TupleUploadSocketHandler;

public class ProviderRouter extends BaseRouter {

	public ProviderRouter() {
		super(SettingsHelper.getSettings(ProviderSettings.class).getPort());
	}
	
	@Override
	public void WebSockets() {
		webSocket("/sockets/live", TupleUploadSocketHandler.class);
	}

	@Override
	public void Routes() {
		post("/tuples", (request, response) -> {
			try {
				Tuple tuple = gson.fromJson(request.body(), Tuple.class);

				if (tuple == null) {
					response.status(Consts.HttpBadRequest);
					return "";
				}
				String authorityURL = SettingsHelper.getSettings(ProviderSettings.class).getAuthorityURL();
				if (!DatabaseHelper.Exists(DbGroup.class, " groupId= " + tuple.getGroupId())) {
					if (!GroupHelper.getGroupsFromAuthority(authorityURL, tuple.getGroupId())) {
						response.status(Consts.HttpBadRequest);
						return "";
					}
				}

				DbGroup group = DatabaseHelper.Get(DbGroup.class, "groupId="+tuple.getGroupId());
				byte[] hash = HashHelper.getHash(tuple);
				if (!VerifyHelper.verify(group.getPublicKey(), tuple.getSignature(), hash)) {
					System.out.println("[post] /tuple bad signature");
					response.status(Consts.HttpBadRequest);
					return "";
				}
				System.out.println("hash:" + Arrays.toString(hash));

				DbTuple dbTuple = new DbTuple(tuple, group);
				dbTuple.setReceived(new Date());
				dbTuple.setHash(Base64.getEncoder().encodeToString(hash));
				System.out.println("hashenocded:" + Base64.getEncoder().encodeToString(hash));
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
			String[] periods = new String[gracePeriods + 1];
			DateTimeFormatter formatters = DateTimeFormatter
					.ofPattern(SettingsHelper.getSettings(ProviderSettings.class).getPeriodFormat());
			LocalDate date = LocalDate.now().minusDays(gracePeriods * periodLength);

			for (int i = 0; i <= gracePeriods; i++) {
				periods[i] = formatters.format(date.minusDays(-1 * i * periodLength));
			}
			return gson.toJson(periods);

		});

		get("/invoiceitems/:groupId/:periodId", (request, response) -> {

			int groupId;
			String strPeriod;
			Date period;

			try {
				groupId = Integer.parseInt(request.params(":groupId"));
				strPeriod = request.params(":periodId");
				period = PeriodHelper.parse(strPeriod);
			} catch (Exception e) {
				response.status(Consts.HttpBadRequest);
				return "";
			}

			// check the cryptogroup exists and the period is allowed for checkout
			boolean groupOK = DatabaseHelper.Exists(DbGroup.class, " groupId= ' " + groupId + "'");
			boolean periodOK = PeriodHelper.isAllowed(strPeriod);

			if (!groupOK || !periodOK) {
				response.status(Consts.HttpBadRequest);
				return "";
			}

			DbGroup group = DatabaseHelper.Get(DbGroup.class, "groupId='" + groupId + "'");

			DbSession session = ProviderSessionHelper.getSession(group);
			if (session == null) {
				response.status(Consts.HttpInternalServerError);
				return "";
			}
			// get time limit to select the correct tuples
			Date after = PeriodHelper.getAfterLimit(period);
			Date before = PeriodHelper.getBeforeLimit(period);

			// get the tuples and save the creation time in session so we can create the
			// same list again
			List<DbVTuple> tuples = DatabaseHelper.Get(DbVTuple.class, "groupId= '" + groupId + "'", "created", after,
					before);

			if (tuples.size() == 0) {
				response.status(Consts.HttpStatuscodeOk);
				return "";
			}

			session.setInvoiceItemsCreated(new Date());

			// convert to list of tuples
			InvoiceItems items = new InvoiceItems();
			tuples.forEach(tuple -> items.getItems().put(tuple.getHash(), tuple.getPrice()));
			items.setSessionId(session.getToken());
			byte[] hash = HashHelper.getHash(items);
			byte[] sig = ProviderSignatureHelper.sign(hash);
			String signature = Base64.getEncoder().encodeToString(sig);
			items.setSignature(signature);

			session.setSignatureOnTuples(signature);
			session.setPeriod(period);
			session.setState(State.TUPLESENT);
			DatabaseHelper.Save(DbSession.class, session);
			items.setSessionId(session.getToken());
			response.status(Consts.HttpStatuscodeOk);

			return gson.toJson(items);
		});

		post("/pay/:sessionId", (request, response) -> {
			String sessionId = null;
			Payment payment = null;
			try {
				sessionId = request.params(":sessionId");
				payment = gson.fromJson(request.body(), Payment.class);
			} catch (Exception e) {
				response.status(Consts.HttpBadRequest);
				return "";
			}
			DbSession session = null;

			if (sessionId != null && !sessionId.equals("")) {
				session = ProviderSessionHelper.getSession(sessionId);
			} else {
				response.status(Consts.HttpBadRequest);
				return "";
			}

			// check session, state and payment
			if (session == null || session.getState() != State.TUPLESENT || payment == null) {
				response.status(Consts.HttpBadRequest);
				return "";
			}

			// start verifying the payment signature
			DbGroup group = session.getGroup();
			DbSignature paymentSignature = payment.getSignature();
			payment.setSignatureOnTuples(session.getSignatureOnTuples());

			byte[] messageHash = HashHelper.getHash(payment);

			if (!VerifyHelper.verify(group.getPublicKey(), paymentSignature, messageHash)) {
				System.out.println("[post] /tuple bad signature");
				response.status(Consts.HttpBadRequest);
				return "";
			}

			// prepare return value (signed receipt)
			Receipt receipt = new Receipt();
			receipt.setSessionId(session.getToken());
			receipt.setSumme(payment.getSumme());
			byte[] hash = HashHelper.getHash(receipt);
			String signedReceipt = Base64.getEncoder().encodeToString(ProviderSignatureHelper.sign(hash));

			// save new session state
			session.setPaymentSignature(paymentSignature);
			session.setPaidAmount(payment.getSumme());
			session.setState(State.PAID);
			DatabaseHelper.Save(DbSignature.class, paymentSignature);
			DatabaseHelper.Update(session);
			response.status(Consts.HttpStatuscodeOk);
			return gson.toJson(signedReceipt);

		});
	}

	@Override
	public void ProtectedRoutes() {

		// private route for the web application
		get("/payments/:periodeId", (request, response) -> {

			Date period = null;

			try {
				String periodeId = request.params(":periodeId");
				period = PeriodHelper.parse(periodeId);

			} catch (Exception e) {
				e.printStackTrace();
				response.status(Consts.HttpBadRequest);
				return "";
			}

			if (period == null) {
				response.status(Consts.HttpBadRequest);
				return "";
			}

			String strCondition = PeriodHelper.dbFormat(period);
			List<DbVPayment> payments = DatabaseHelper.GetList(DbVPayment.class, "period= '" + strCondition + "'");

			if (payments.size() == 0) {
				response.status(Consts.HttpStatuscodeOk);
				return "";
			}

			response.status(Consts.HttpStatuscodeOk);
			return gson.toJson(payments);
		});

		// private route for the web application
		get("/costs/:periodeId", (request, response) -> {
			Date period = null;
			try {
				String periodeId = request.params(":periodeId");
				period = PeriodHelper.parse(periodeId);

			} catch (Exception e) {
				e.printStackTrace();
				response.status(Consts.HttpBadRequest);
				return "";
			}

			if (period == null) {
				response.status(Consts.HttpBadRequest);
				return "";
			}

			Date after = PeriodHelper.getAfterLimit(period);
			Date before = PeriodHelper.getBeforeLimit(period);
			List<DbGroup> groups = DatabaseHelper.Get(DbGroup.class);
			List<Costs> costs = new ArrayList<Costs>();

			for (DbGroup group : groups) {
				Costs cost = new Costs();
				cost.setGroupId(group.getGroupId());
				int sum = 0;

				List<DbVTuple> tuples = DatabaseHelper.Get(DbVTuple.class,
						"groupId= '" + group.getProviderGroupId() + "'", "created", after, before);

				if (tuples.size() > 0) {
					for (DbVTuple tuple : tuples) {
						sum += tuple.getPrice();
					}
				}
				cost.setSum(sum);
				costs.add(cost);
			}

			if (costs.size() == 0) {
				response.status(Consts.HttpStatuscodeOk);
				return "";
			}

			response.status(Consts.HttpStatuscodeOk);
			return gson.toJson(costs);
		});
	}

	public static void main(String[] args) {
		new ProviderRouter();

		// note (pa): just a useless request to warmup hibernate
		DatabaseHelper.Exists(DbGroup.class, "groupId = 1");
	}
}
