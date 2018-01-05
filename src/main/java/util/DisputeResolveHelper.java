/**
 *   Copyright 2018 Pascal Ammon, Gabriel Wyss
 * 
 * 	 Implementation eines anonymen Mobility Pricing Systems auf Basis eines Gruppensignaturschemas
 * 
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * This class executes the dispute resolving protocol with the authority (implements provider part).
 */

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
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import data.BaseSignature;
import data.DbDisputeSession;
import data.DbGroup;
import data.DbSession;
import data.DbTuple;
import data.Discrepancy;
import data.DisputeState;
import data.PaymentTuple;
import data.ProviderSettings;
import data.CommonSettings;
import data.ResolveRequest;
import data.ResolveResult;
import data.ResolveTuple;
import util.SettingsHelper;
import gson.BigIntegerTypeAdapter;
import signatures.Signature;

public class DisputeResolveHelper {

	private static boolean sendResolveRequest(DbDisputeSession session, ResolveRequest r) {
		CommonSettings settings = SettingsHelper.getSettings(CommonSettings.class);
		if (settings.isTls()) {
			return sendResolveRequestHTTPS(session, r);
		} else {
			return sendResolveRequestHTTP(session, r);
		}
	}

	private static boolean sendResolveRequestHTTPS(DbDisputeSession session, ResolveRequest r) {
		Gson gson;
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(BigInteger.class, new BigIntegerTypeAdapter());
		gson = builder.excludeFieldsWithoutExposeAnnotation().create();

		try {
			CloseableHttpClient client = (CloseableHttpClient) HttpClientFactory.getHttpsClient();

			String authorityURL = SettingsHelper.getSettings(ProviderSettings.class).getAuthorityTLSURL();
			String disputeURL = authorityURL + "api/dispute";
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

				System.out.println(strResponse);

				if (strResponse != "") {
					ResolveResult res = gson.fromJson(strResponse, ResolveResult.class);

					byte[] sigBytes = Base64.getDecoder().decode(res.getAuthoritySignature());
					byte[] message = HashHelper.getHash(res);

					// check the authority signature on res
					if (!ProviderSignatureHelper.verifyAuthorityMessage(message, sigBytes)) {
						session.setState(DisputeState.RESULTERROR);
						DatabaseHelper.Update(session);
						return false;
					}

					List<Discrepancy> discrepancies = res.getRes();

					for (int i = 0; i < discrepancies.size(); i++) {
						Discrepancy d = discrepancies.get(i);
						d.setDisputeSessionId(session.getSessionId());
						int id = DatabaseHelper.Save(Discrepancy.class, d);
						d.setResultId(id);
					}

					session.setDisputeResults(discrepancies);
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

		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			session.setState(DisputeState.SENDERROR);
			DatabaseHelper.Update(session);
			Logger.errorLogger(ex);
			return false;
		}

	}

	// request to authority
	private static boolean sendResolveRequestHTTP(DbDisputeSession session, ResolveRequest r) {
		Gson gson;
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(BigInteger.class, new BigIntegerTypeAdapter());
		gson = builder.excludeFieldsWithoutExposeAnnotation().create();

		try {

			// timout handling
			int timeout = 20;
			RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000)
					.setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();
			CloseableHttpClient client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();

			String authorityURL = SettingsHelper.getSettings(ProviderSettings.class).getAuthorityURL();
			String disputeURL = authorityURL + "api/dispute";
			// HttpClient client = HttpClientBuilder.create().build();
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

				System.out.println(strResponse);

				if (strResponse != "") {
					ResolveResult res = gson.fromJson(strResponse, ResolveResult.class);

					byte[] sigBytes = Base64.getDecoder().decode(res.getAuthoritySignature());
					byte[] message = HashHelper.getHash(res);

					// check the authority signature on res
					if (!ProviderSignatureHelper.verifyAuthorityMessage(message, sigBytes)) {
						session.setState(DisputeState.RESULTERROR);
						DatabaseHelper.Update(session);
						return false;
					}

					List<Discrepancy> discrepancies = res.getRes();

					for (int i = 0; i < discrepancies.size(); i++) {
						Discrepancy d = discrepancies.get(i);
						d.setDisputeSessionId(session.getSessionId());
						int id = DatabaseHelper.Save(Discrepancy.class, d);
						d.setResultId(id);
					}

					session.setDisputeResults(discrepancies);
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

		} catch (Exception ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
			session.setState(DisputeState.SENDERROR);
			DatabaseHelper.Update(session);
			Logger.errorLogger(ex);

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
		int groupId = group.getProviderGroupId();

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
			r.setSignature(new BaseSignature(t.getSignature()));
			s.add(r);
		}

		// getting T
		List<PaymentTuple> t = new ArrayList<PaymentTuple>();
		List<DbSession> sessions = DatabaseHelper.Get(DbSession.class, "groupId= '" + groupId + "' AND state= 'PAID'", "created", after,
				before);

		for (DbSession sess : sessions) {
			PaymentTuple r = new PaymentTuple();

			r.setProviderSignature(sess.getSignatureOnTuples());
			r.setTollPaid(sess.getPaidAmount());
			r.setSessionId(sess.getToken());
			r.setUserSignature(new BaseSignature(sess.getPaymentSignature()));
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
		request.setGroupId(group.getGroupId());
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
