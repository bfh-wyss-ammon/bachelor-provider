/**
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
