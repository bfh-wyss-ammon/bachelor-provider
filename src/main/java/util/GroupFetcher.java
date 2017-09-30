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

public class GroupFetcher {

	public static boolean getGroupsFromAuthority(String authorityUrl) {

		StringBuffer content = new StringBuffer();
		try {
			// TODO Error handling
			URL url = new URL(authorityUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);

			// drop the req
			int status = con.getResponseCode();

			if (status != 200)
				return false;

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
		Type listType = new TypeToken<ArrayList<DbGroup>>() {
		}.getType();
		List<DbGroup> groupList = new Gson().fromJson(result, listType);

		if (groupList.size() < 2)
			return false;

		for (DbGroup grp : groupList) {
			Database.SaveOrUpdate(grp);
		}

		return true;
	}

}
