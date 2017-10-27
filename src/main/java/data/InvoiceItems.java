package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.annotations.Expose;

import interfaces.HashValue;
import util.HashHelper;

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
		if(items == null) {
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
