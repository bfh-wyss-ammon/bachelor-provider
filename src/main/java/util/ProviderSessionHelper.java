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
 * This class generates new instances of a toll calculation session. it is also able to get a existing session from database, and performs toll calculation session cleanup.
 */

package util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import data.DbGroup;
import data.DbSession;
import data.ProviderSettings;
import data.CommonSettings;
import data.State;

public class ProviderSessionHelper {

	public static DbSession getSession(DbGroup group) {

		DbSession session = new DbSession();
		session.setGroup(group);
		session.setToken(java.util.UUID.randomUUID().toString());
		session.setCreated(new Date());
		session.setState(State.OPEN);
		return session;
	}

	public static DbSession getSession(String token) {
		cleanSessions();
		String where = "token='" + token + "'";

		if (token != null && DatabaseHelper.Exists(DbSession.class, where)) {
			DbSession session = DatabaseHelper.Get(DbSession.class, where);
			session.setCreated(new Date());
			return session;
		}
		return null;
	}

	private static void cleanSessions() {
		long timeout = SettingsHelper.getSettings(ProviderSettings.class).getSessionTimeout();
		String where = "state='" + State.TUPLESENT + "'";
		LocalDateTime localLimit = LocalDateTime.now().minusSeconds(timeout);
		Date limit = Date.from(localLimit.atZone(ZoneId.systemDefault()).toInstant());
		DatabaseHelper.Delete(DbSession.class, where, "created", limit);
		return;
	}

}
