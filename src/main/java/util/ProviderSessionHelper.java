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
