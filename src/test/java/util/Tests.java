
package util;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.hibernate.boot.model.relational.Database;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import data.BaseSignature;
import data.DbGroup;
import data.DbPublicKey;
import data.DbSignature;
import data.DbTuple;
import data.DbVPayment;
import data.InvoiceItems;
import data.Tuple;
import demo.DemoManagerKey;
import demo.DemoSecretKey;
import demo.DemoSignature;
import keys.ManagerKey;
import keys.SecretKey;
import requests.JoinRequest;
import responses.JoinResponse;

public class Tests {
	private DbPublicKey publicKey;
	private ManagerKey managerKey;
	private DbGroup group;
	private HttpClient client;
	private SecretKey memberKey;
	

	//@Before
	public void init() {
		publicKey = new DbPublicKey();
		managerKey = new DemoManagerKey();
		group = new DbGroup();
		memberKey = new DemoSecretKey();

		Generator.generate(publicKey, managerKey);

		DatabaseHelper.Save(DbPublicKey.class, publicKey);
		group.setGroupId(9999);
		group.setPublicKey(publicKey);
		DatabaseHelper.Save(DbGroup.class, group);
		client = HttpClientBuilder.create().build();
		
		JoinHelper.init(publicKey, memberKey);
		JoinRequest joinRequest = new JoinRequest(memberKey);
		JoinResponse joinResponse = JoinHelper.join(publicKey, managerKey, joinRequest);
		memberKey.maintainResponse(joinResponse);
	}
	
	@Test
	public void loadPayments() {
		List<DbVPayment> payments = (List<DbVPayment>)DatabaseHelper.Get(DbVPayment.class);
		
		assertNotNull(payments);
	}
	
	@Test
	 public void SerializeTest() {
	        InvoiceItems invoiceItems = new InvoiceItems();

	        Map<String, Integer> items = new HashMap<String, Integer>();

	        items.put("hash", 2);
	        items.put("hash2", 2);
	        items.put("hash3", 2);
	        items.put("hash4", 2);

	        invoiceItems.setItems(items);

	        String res = new Gson().toJson(invoiceItems);

	        invoiceItems = new Gson().fromJson(res, InvoiceItems.class);

	    }
	
	
	@Test
	public void hashMe() {
		
		byte[] hash = new BigInteger("16201989586969300992497351630993268961078957710117267493916413683672187089042732535418515385530563252120455073919611344756070557064633623107891402660596073150306111393432464672037992582765749751513776775216346453530578193408716332550685745290406436384102316021606633546585100617391446697892447741647370292785662057234204201370939449742439006170519322497994243458910988473727125327244519776937845274373419146707996733340715397716464507667290825236592744155819974508859356667949296722457911779420699265944813359659832143668156207022291619742145209929613743583522423637334300373962753196732463890066171293352561506835521").toByteArray();
		
		System.out.println(Base64.getEncoder().encodeToString(hash));
	}
	
	@Test
	public void DateFormat() 
	{
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
		Date n = new Date();
		
		System.out.println(dateFormat.format(n));
		
	}
	
	@Test
	public void HashTest() {
		Tuple a = new Tuple();
		Tuple b = new Tuple();
		Date d = new Date();
		
		a.setLatitude(new BigDecimal("9").setScale(10, RoundingMode.HALF_UP));
		a.setLongitude(new BigDecimal("8").setScale(10, RoundingMode.HALF_UP));
		a.setCreated(d);
		
		b.setLatitude(new BigDecimal("9").setScale(10, RoundingMode.HALF_UP));
		b.setLongitude(new BigDecimal("8").setScale(10, RoundingMode.HALF_UP));
		b.setCreated(d);
		
		assertEquals(a.getCreated(), b.getCreated());
		assertEquals(a.getLatitude(), b.getLatitude());
		assertEquals(a.getLongitude(), b.getLongitude());
		
		assertEquals(Arrays.toString(HashHelper.getHash(a)), Arrays.toString(HashHelper.getHash(b)));
		
		
	}

	@Test
	public void SendOneValidTuple() {
		try {
			Tuple tuple = new Tuple();
			tuple.setGroupId(group.getGroupId());
			tuple.setLatitude(new BigDecimal("9").setScale(10, RoundingMode.HALF_UP));
			tuple.setLongitude(new BigDecimal("8").setScale(10, RoundingMode.HALF_UP));
			tuple.setCreated(new Date());

			BaseSignature signature = new BaseSignature();
			SignHelper.sign(memberKey, publicKey, HashHelper.getHash(tuple), signature);

			//tuple.setSignature(signature);

			String url = "http://laptop:10001/tuple";
			HttpPost post = new HttpPost(url);

			post.setHeader("Content-Type", "application/json");
			post.setEntity(new StringEntity(new Gson().toJson(tuple)));

			HttpResponse response = client.execute(post);
			assertEquals(response.getStatusLine().getStatusCode(), 200);

		} catch (Exception ex) {

		} finally {
			DatabaseHelper.Delete(group);

		}
	}

}
