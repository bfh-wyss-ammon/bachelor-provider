package data;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import interfaces.HashValue;

public class InvoiceItems {

	@Expose
	@HashValue
	private Map<String, Integer> items;

	@Expose
	private String signature;

	@HashValue
	private String sessionId;

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public InvoiceItems() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Map<String, Integer> getItems() {
		if (items == null) {
			items = new HashMap<String, Integer>();
		}
		return items;
	}

	public void setItems(Map<String, Integer> items) {
		this.items = items;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}
}
