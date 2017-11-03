package data;
import interfaces.HashValue;


public class Receipt {

	@HashValue
	private int summe;
	
	@HashValue
	private String sessionId;
	
	
	public Receipt() {
		
	}


	public int getSumme() {
		return summe;
	}


	public void setSumme(int summe) {
		this.summe = summe;
	}


	public String getSessionId() {
		return sessionId;
	}


	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}
