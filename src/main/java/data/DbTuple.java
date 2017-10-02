package data;

import java.math.BigDecimal;
import java.util.Date;

import interfaces.HashValue;

public class DbTuple {

	private int tupleId;
	private int groupId;
	@HashValue
	private BigDecimal longitude;
	@HashValue
	private BigDecimal latitude;
	private Date created;
	private Date received;
	@HashValue
	private String signature;

	public int getTupleId() {
		return tupleId;
	}

	public void setTupleId(int tupleId) {
		this.tupleId = tupleId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getReceived() {
		return received;
	}

	public void setReceived(Date received) {
		this.received = received;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

}
