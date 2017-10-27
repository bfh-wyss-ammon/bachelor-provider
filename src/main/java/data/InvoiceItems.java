package data;

import com.google.gson.annotations.Expose;

public class InvoiceItems {
	
	@Expose
	private InvoiceItem[] items;
	
	@Expose
	private String signature;

	public InvoiceItems(InvoiceItem[] items, String signature) {
		super();
		this.items = items;
		this.signature = signature;
	}
	public InvoiceItems() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public InvoiceItem[] getItems() {
		return items;
	}
	public void setItems(InvoiceItem[] items) {
		this.items = items;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
}
