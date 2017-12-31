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
 * This class stores all data related to a dispute resolving session in the database of the provider.
 */

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
