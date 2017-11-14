package util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import data.DbDisputeSession;
import data.DbGroup;
import data.DbSession;
import data.DbTuple;
import data.DisputeState;
import data.PaymentTuple;
import data.ProviderSettings;
import data.ResolveRequest;
import data.ResolveTuple;
import util.SettingsHelper;
import gson.BigIntegerTypeAdapter;
import signatures.Signature;

public class DisputeResolveHelper {

	
	//request to authority
	private static boolean sendResolveRequest(DbDisputeSession session, ResolveRequest r) {
		Gson gson;
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(BigInteger.class, new BigIntegerTypeAdapter());
		gson = builder.excludeFieldsWithoutExposeAnnotation().create();

		try {
			String authorityURL = SettingsHelper.getSettings(ProviderSettings.class).getAuthorityURL();
			String disputeURL = authorityURL + "dispute";
			URL url = new URL(disputeURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			// configure request
			con.setRequestMethod("POST");
			con.setConnectTimeout(20000);
			// con.setRequestProperty("User-Agent", USER_AGENT);
			// con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			String content = gson.toJson(r);

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());

			wr.writeBytes(content);

			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			if (responseCode == 200) {
				session.setState(DisputeState.SENTTOAUTHORITY);
				DatabaseHelper.Update(session);
				return true;
			} else {
				session.setState(DisputeState.SENDERROR);
				DatabaseHelper.Update(session);
				return false;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			session.setState(DisputeState.SENDERROR);
			DatabaseHelper.Update(session);
			return false;
		}

	}
	
	
	//database helper method
	private static DbDisputeSession saveDisputeSession(ResolveRequest r, DbGroup group, Date period) {
		
		DbDisputeSession session = new DbDisputeSession();
		session.setCreated(new Date());
		session.setGroup(group);
		session.setPeriod(period);
		session.setToken(r.getDisputeSessionId());
		session.setState(DisputeState.CREATED);
		DatabaseHelper.Save(DbDisputeSession.class, session);
		return session;
		
	}

	public static void createResolveRequest(Date period, DbGroup group) {

		ResolveRequest request = new ResolveRequest();

		// input data
		Date after = PeriodHelper.getAfterLimit(period);
		Date before = PeriodHelper.getBeforeLimit(period);
		int groupId = group.getGroupId();

		// getting S
		List<ResolveTuple> s = new ArrayList<ResolveTuple>();
		List<DbTuple> tuples = DatabaseHelper.Get(DbTuple.class, "groupId= '" + groupId + "'", "created", after,
				before);

		if (tuples.size() == 0) {
			return;
		}

		for (DbTuple t : tuples) {
			ResolveTuple r = new ResolveTuple();
			r.setHash(t.getHash());
			r.setPrice(1);
			r.setSignature((Signature) t.getSignature());
			s.add(r);
		}

		// getting T
		List<PaymentTuple> t = new ArrayList<PaymentTuple>();
		List<DbSession> sessions = DatabaseHelper.Get(DbSession.class, "groupId= '" + groupId + "'", "created", after,
				before);

		for (DbSession sess : sessions) {
			PaymentTuple r = new PaymentTuple();

			r.setProviderSignature(sess.getSignatureOnTuples());
			r.setTollPaid(sess.getPaidAmount());
			r.setSessionId(sess.getToken());
			r.setUserSignature((Signature) sess.getPaymentSignature());

			// now rebuild the list that was sent to the client during this toll session
			Date lCreated = sess.getInvoiceItemsCreated();
			List<DbTuple> l = DatabaseHelper.Get(DbTuple.class, "groupId= '" + groupId + "'", "created", after, before,
					"received", lCreated);
			String[] hashes = new String[l.size()];
			int i = 0;
			for (DbTuple tuple : l) {
				hashes[i] = tuple.getHash();
				i++;
			}
			r.setTupleHashlist(hashes);
			t.add(r);
		}

		request.setS(s);
		request.setT(t);
		request.setGroupId(groupId);
		request.setDisputeSessionId(java.util.UUID.randomUUID().toString());
		byte[] hash = HashHelper.getHash(request);
		String signedRequest = Base64.getEncoder().encodeToString(ProviderSignatureHelper.sign(hash));
		request.setProviderSignature(signedRequest);
		
		//now save the data to db and send request
		DbDisputeSession session = saveDisputeSession(request,group,period);
		sendResolveRequest(session, request);
		return;

	}

}
