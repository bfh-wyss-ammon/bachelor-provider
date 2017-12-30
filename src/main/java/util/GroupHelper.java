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
 * This class handles the case when position tuples from an unknown group are sent to the provider. In this case, the provider has to get the public key from the authority.
 */

package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import com.google.gson.Gson;
import data.ProviderSettings;
import data.CommonSettings;
import data.DbGroup;

public class GroupHelper {

	public static boolean getGroupsFromAuthority(int groupId) {

		StringBuffer content = new StringBuffer();
		try {
			CommonSettings cSettings = SettingsHelper.getSettings(CommonSettings.class);
			ProviderSettings aSettings = SettingsHelper.getSettings(ProviderSettings.class);

			if (!cSettings.isTls()) {
				// TLS mode is disabled
				URL url = new URL(aSettings.getAuthorityURL() + "/api/groups/" + groupId);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();

				con.setRequestMethod("GET");
				con.setConnectTimeout(5000);
				con.setReadTimeout(5000);

				// drop the req
				int status = con.getResponseCode();

				if (status != Consts.HttpStatuscodeOk) {
					return false;
				}

				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;

				while ((inputLine = in.readLine()) != null) {
					content.append(inputLine);
				}
				in.close();

				con.disconnect();
			} else {
				// TLS mode is enables
				URL url = new URL(aSettings.getAuthorityTLSURL() + "/api/groups/" + groupId);
				HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				con.setConnectTimeout(5000);
				con.setReadTimeout(5000);

				// drop the req
				int status = con.getResponseCode();

				if (status != Consts.HttpStatuscodeOk) {
					return false;
				}

				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;

				while ((inputLine = in.readLine()) != null) {
					content.append(inputLine);
				}
				in.close();

				con.disconnect();
			}

		} catch (IOException ex) {
			ex.printStackTrace();
			Logger.errorLogger(ex);
			return false;
		}

		String result = content.toString();
		// save to db
		DbGroup group = new Gson().fromJson(result, DbGroup.class);

		if (group == null)
			return false;

		DatabaseHelper.SaveOrUpdate(group);

		return true;
	}

}