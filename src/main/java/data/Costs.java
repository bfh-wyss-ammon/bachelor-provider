package data;

import java.util.Date;

import com.google.gson.annotations.Expose;

public class Costs {
	
	@Expose
	private int groupId;
	@Expose
	private int sum;
	
	
	public Costs() {
		
	}
	
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	
	
	

}