package data;

import com.google.gson.annotations.Expose;

import interfaces.HashValue;

/**
 * @author Pascal
 *
 */
public class InvoiceItem {
	@Expose
	@HashValue
	private String id;
	@Expose
	@HashValue
	private int price;

	public InvoiceItem() {

	}

	public InvoiceItem(String id, int price) {
		super();
		this.id = id;
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

}
