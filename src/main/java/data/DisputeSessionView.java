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
