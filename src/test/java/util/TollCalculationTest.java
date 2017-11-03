package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Ignore;
import org.junit.Test;
public class TollCalculationTest {
	
	@Ignore
	@Test
	public void testPeriod() {
		StringBuffer content = new StringBuffer();
		String periodURL = "http://localhost:10001/invoicePeriodes/1";
		
		try {
			URL url = new URL(periodURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);

			// drop the req
			int status = con.getResponseCode();

			if (status != Consts.HttpStatuscodeOk)
			{
				throw new RuntimeException();
			}
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();

			con.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String result = content.toString();
		System.out.println(result);
		
		assert(!result.equals(""));
	}
	
	@Ignore
	@Test
	public void testInvoiceItems() {
		StringBuffer content = new StringBuffer();
		String periodURL = "http://localhost:10001/invoiceitems/1/27-10-2017/";
		
		try {
			URL url = new URL(periodURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);

			// drop the req
			int status = con.getResponseCode();

			if (status != Consts.HttpStatuscodeOk)
			{
				throw new RuntimeException();
			}
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();

			con.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String result = content.toString();
		System.out.println("result " + result);
		
		assert(!result.equals(""));
	}
	
	
	@Ignore
	@Test
	public void testPay() {
		StringBuffer content = new StringBuffer();
		String payURL = "http://localhost:10001/pay/411e6058-e513-411d-96ab-5932fd2dcb50/";
		
		try {
			URL url = new URL(payURL);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);

			// drop the req
			int status = con.getResponseCode();

			if (status != Consts.HttpStatuscodeOk)
			{
				throw new RuntimeException();
			}
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();

			con.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String result = content.toString();
		System.out.println("result " + result);
		
		assert(!result.equals(""));
		
		
		
		
		
		
		
	}
	

}
