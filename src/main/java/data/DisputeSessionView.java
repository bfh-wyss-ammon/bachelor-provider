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
 * This class gets data related to the result and state of the dispute resolving sessions for the selected period from the database of the provider.
 * It is also used for serializing to JSON for the web application.
 */


package data;

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.Expose;

public class DisputeSessionView {

	@Expose
	private int group;

	@Expose
	private DisputeState state;

	@Expose
	private List<Discrepancy> disputeResults;

	public DisputeSessionView() {

	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public List<Discrepancy> getDisputeResults() {
		return disputeResults;
	}

	public void setDisputeResults(List<Discrepancy> disputeResults) {
		this.disputeResults = disputeResults;
	}

	public DisputeState getState() {
		return state;
	}

	public void setState(DisputeState state) {
		this.state = state;
	}

}
