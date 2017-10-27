package util;

import java.util.Date;
import data.DbGroup;
import data.DbSession;
public class ProviderSessionHelper {

	public static DbSession getSession(DbGroup group) {

			DbSession session = new DbSession();
			session.setGroup(group);
			session.setToken(java.util.UUID.randomUUID().toString());
			session.setCreated(new Date());
			session.setState(DbSession.State.OPEN);
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
		//TODO return all sessions older than 24 hours that did not end.
		return;
	}

}
