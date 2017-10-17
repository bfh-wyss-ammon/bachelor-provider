package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import data.DbGroup;

public class GroupHelper {

	public static boolean getGroupsFromAuthority(String authorityUrl, int groupId) {

		StringBuffer content = new StringBuffer();
		try {
			URL url = new URL(authorityUrl +"groups/" + groupId);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);

			// drop the req
			int status = con.getResponseCode();

			if (status != Consts.HttpStatuscodeOk)
			{
				return false;
			}
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;

			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();

			con.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		String result = content.toString();

		// save to db
		DbGroup group = new Gson().fromJson(result, DbGroup.class);
		
		if(group == null) return false;

			DatabaseHelper.SaveOrUpdate(group);

		return true;
	}

}