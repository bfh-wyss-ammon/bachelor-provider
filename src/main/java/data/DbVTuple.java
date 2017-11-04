package data;

import java.util.Date;

import interfaces.HashValue;

public class DbVTuple {
	private Date created;
	@HashValue
	private String hash;
	@HashValue
	private int price;

	private int tupleId;

	public DbVTuple() {

	}

	public int getTupleId() {
		return tupleId;
	}

	public void setTupleId(int tupleId) {
		this.tupleId = tupleId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

}
