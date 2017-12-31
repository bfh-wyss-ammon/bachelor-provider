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
 */

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
