package data;

import java.util.Date;
import java.util.List;

public class DbDisputeSession {

	private int sessionId;
	private DbGroup group;
	private List disputeResults;
	private DisputeState state;
	private String token;
	private Date created;
	private Date period;
	private String signatureOnResult;

	public DbDisputeSession() {
		
	}
	

	public Date getPeriod() {
		return period;
	}


	public void setPeriod(Date period) {
		this.period = period;
	}


	public List getDisputeResults() {
		return disputeResults;
	}


	public void setDisputeResults(List disputeResults) {
		this.disputeResults = disputeResults;
	}


	public DisputeState getState() {
		return state;
	}


	public void setState(DisputeState state) {
		this.state = state;
	}


	public String getSignatureOnResult() {
		return signatureOnResult;
	}


	public void setSignatureOnResult(String signatureOnResult) {
		this.signatureOnResult = signatureOnResult;
	}


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
