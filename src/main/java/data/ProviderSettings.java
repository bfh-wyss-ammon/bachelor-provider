package data;

import com.google.gson.annotations.Expose;

import settings.Settings;

public class ProviderSettings {

	private int port;
	private String authorityURL;

	public ProviderSettings() {
		this.port = 10001;
		this.authorityURL = "http://localhost:10000/";
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getAuthorityURL() {
		return authorityURL;
	}

	public void setAuthorityURL(String authorityURL) {
		this.authorityURL = authorityURL;
	}



}