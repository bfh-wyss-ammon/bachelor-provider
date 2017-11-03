package data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import interfaces.HashValue;
public class DbTuple {

	private int tupleId;

	private DbGroup group;
	private DbSignature signature;

	@HashValue
	private BigDecimal longitude;
	@HashValue
	private BigDecimal latitude;
	@HashValue
	private Date created;
	private Date received;
	private String hash;

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public DbTuple() {

	}

	public DbTuple(Tuple tuple, DbGroup group) {
		this.longitude = tuple.getLongitude().setScale(10, RoundingMode.HALF_UP);
		this.latitude = tuple.getLatitude().setScale(10, RoundingMode.HALF_UP);
		this.created = tuple.getCreated();
		this.group = group;
		this.signature = new DbSignature(tuple.getSignature());
	}

	public int getTupleId() {
		return tupleId;
	}

	public void setTupleId(int tupleId) {
		this.tupleId = tupleId;
	}

	public DbGroup getGroup() {
		return group;
	}

	public void setGroup(DbGroup group) {
		this.group = group;
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

	public DbSignature getSignature() {
		return signature;
	}

	public void setSignature(DbSignature signature) {
		this.signature = signature;
	}

}
