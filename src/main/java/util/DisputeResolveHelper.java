package util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClientBuilder;

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
import data.ResolveResult;
import data.ResolveTuple;
import util.SettingsHelper;
import gson.BigIntegerTypeAdapter;
import signatures.Signature;

public class DisputeResolveHelper {

	// request to authority
	private static boolean sendResolveRequest(DbDisputeSession session, ResolveRequest r) {
		Gson gson;
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(BigInteger.class, new BigIntegerTypeAdapter());
		gson = builder.excludeFieldsWithoutExposeAnnotation().create();

		try {
			String authorityURL = SettingsHelper.getSettings(ProviderSettings.class).getAuthorityURL();
			String disputeURL = authorityURL + "dispute";
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(disputeURL);
			// TODO set headers
			// post.setHeader(arg0, arg1);
			String contentToSend = gson.toJson(r);
			HttpEntity entity = new ByteArrayEntity(contentToSend.getBytes("UTF-8"));
			post.setEntity(entity);

			HttpResponse response = client.execute(post);
			InputStream inputStream = response.getEntity().getContent();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuilder stringBuilder = new StringBuilder();
			String bufferedStrChunk = null;
			while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
				stringBuilder.append(bufferedStrChunk);
			}

			int responseCode = response.getStatusLine().getStatusCode();
			if (responseCode == 200) {
				session.setState(DisputeState.SENTTOAUTHORITY);
				DatabaseHelper.Update(session);
				String strResponse = stringBuilder.toString();

				if (strResponse != "") {
					ResolveResult res = gson.fromJson(strResponse, ResolveResult.class);
					session.setDisputeResults(res.getRes());
					session.setState(DisputeState.RESULTRECEIVED);
					DatabaseHelper.Update(session);

				} else {
					session.setState(DisputeState.RESULTERROR);
					DatabaseHelper.Update(session);
					return false;
				}

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

	// database helper method
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
			r.setHash(sess.getHash());

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

		// now save the data to db and send request
		DbDisputeSession session = saveDisputeSession(request, group, period);
		sendResolveRequest(session, request);
		return;

	}

}
