package data;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.Expose;


public class DbVPayment implements Serializable {
	@Expose
	private int groupId;
	@Expose
	private int total;
	@Expose
	private Date period;
	
	public DbVPayment() {
		
	}
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public Date getPeriod() {
		return period;
	}
	public void setPeriod(Date period) {
		this.period = period;
	}
	
	
	
}
