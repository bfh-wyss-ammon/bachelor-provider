/**
 * This class generates an instance of the http client which is used to connect to the authority in the dispute resolving protocol.
 */

package util;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import data.CommonSettings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.KeyManagementException;

public class HttpClientFactory {

	private static CloseableHttpClient client;

	public static HttpClient getHttpsClient() throws Exception {

		if (client != null) {
			return client;
		}
		int timeout = 20;
		RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout * 1000)
				.setConnectionRequestTimeout(timeout * 1000).setSocketTimeout(timeout * 1000).build();

		SSLContext sslcontext = getSSLContext();
		SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslcontext,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		client = HttpClients.custom().setSSLSocketFactory(factory).setDefaultRequestConfig(config).build();

		return client;
	}

	public static void releaseInstance() {
		client = null;
	}

	private static SSLContext getSSLContext() throws KeyStoreException, NoSuchAlgorithmException, CertificateException,
			IOException, KeyManagementException {
		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		CommonSettings settings = SettingsHelper.getSettings(CommonSettings.class);
		FileInputStream instream = new FileInputStream(settings.getTrustStoreFilePath());
		try {
			trustStore.load(instream, settings.getTrustStorePassword().toCharArray());
		} finally {
			instream.close();
		}
		return SSLContexts.custom().loadTrustMaterial(trustStore).build();
	}
}