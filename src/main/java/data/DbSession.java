package data;

import java.util.Date;

public class DbSession {
	public enum State {
		OPEN,TUPLESENT,PAID
	}

	private int sessionId;

	private DbGroup group;
	private State state;
	private String token;
	private Date created;

	public int getSessionId() {
		return sessionId;
	}

	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}

	public DbGroup getGroup() {
		return group;
	}

	public void setGroup(DbGroup group) {
		this.group = group;
	}

	public DbSession.State getState() {
		return state;
	}

	public void setState(DbSession.State state) {
		this.state = state;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}
