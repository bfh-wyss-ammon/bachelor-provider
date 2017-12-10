/**
 * This class stores the settings of the provider in a plain text file on the file system.
 */


package data;

public class ProviderSettings {

	private int port;
	private String authorityURL;
	private String authorityTLSURL;
	private long sessionTimeout;
	private int periodLengthDays;
	private int gracePeriods;
	private String authorityPublicKey;
	private String privateKey;
	private String publicKey;
	private String dbFormat;
	private String periodFormat;
	private String token;

	public ProviderSettings() {

		// constants
		this.port = 10001;
		this.authorityURL = "http://localhost:10000/";
		this.authorityTLSURL = "https://mobilitypricing.ti.bfh.ch:10000/";
		this.sessionTimeout = 1800;
		this.periodLengthDays = 1;
		this.gracePeriods = 1;
		this.periodFormat = "dd-MM-yyyy";
		this.dbFormat = "yyyy-MM-dd HH:mm:ss";

		// secret configurables
		this.authorityPublicKey = "MIIDQjCCAjUGByqGSM44BAEwggIoAoIBAQCPeTXZuarpv6vtiHrPSVG28y7FnjuvNxjo6sSWHz79NgbnQ1GpxBgzObgJ58KuHFObp0dbhdARrbi0eYd1SYRpXKwOjxSzNggooi/6JxEKPWKpk0U0CaD+aWxGWPhL3SCBnDcJoBBXsZWtzQAjPbpUhLYpH51kjviDRIZ3l5zsBLQ0pqwudemYXeI9sCkvwRGMn/qdgYHnM423krcw17njSVkvaAmYchU5Feo9a4tGU8YzRY+AOzKkwuDycpAlbk4/ijsIOKHEUOThjBopo33fXqFD3ktm/wSQPtXPFiPhWNSHxgjpfyEc2B3KI8tuOAdl+CLjQr5ITAV2OTlgHNZnAh0AuvaWpoV499/e5/pnyXfHhe8ysjO65YDAvNVpXQKCAQAWplxYIEhQcE51AqOXVwQNNNo6NHjBVNTkpcAtJC7gT5bmHkvQkEq9rI837rHgnzGC0jyQQ8tkL4gAQWDt+coJsyB2p5wypifyRz6Rh5uixOdEvSCBVEy1W4AsNo0fqD7UielOD6BojjJCilx4xHjGjQUntxyaOrsLC+EsRGiWOefTznTbEBplqiuH9kxoJts+xy9LVZmDS7TtsC98kOmkltOlXVNb6/xF1PYZ9j897buHOSXC8iTgdzEpbaiH7B5HSPh++1/et1SEMWsiMt7lU92vAhErDR8C2jCXMiT+J67ai51LKSLZuovjntnhA6Y8UoELxoi34u1DFuHvF9veA4IBBQACggEAV30qapzTUuQ5NOtfY/m+rrM6PF6+iM6xnQ7MiFxX018P5ldsJws/Rn54RcpHPqI0Stpvy1XTts/jtMBNknPbgTu1QuPsXuacGOLTfnqQr8v+xJxEw8Fh7zPdZHYmnjtsTj6eSSdiDZuSVqfVb2szzxgeUzf+wMGyyNvNeI8CjYollXx8Ke3hb7CGoOHxxlp4l6t2ujoFDy4hEZPCk2ogo7LGUrsOqijXuzwbu1z5MzOTiP9iSGdu79vM/bgCO8qDk5UjRUX34ed9H55/4Dd35NHqXWJcK06Sjr5RPmL0zzciWqx2jeTBlI0yjniArOXxOK+lo/rq1snaVYOJ+z6+tw==";
		this.privateKey = "MIICXQIBADCCAjUGByqGSM44BAEwggIoAoIBAQCPeTXZuarpv6vtiHrPSVG28y7FnjuvNxjo6sSWHz79NgbnQ1GpxBgzObgJ58KuHFObp0dbhdARrbi0eYd1SYRpXKwOjxSzNggooi/6JxEKPWKpk0U0CaD+aWxGWPhL3SCBnDcJoBBXsZWtzQAjPbpUhLYpH51kjviDRIZ3l5zsBLQ0pqwudemYXeI9sCkvwRGMn/qdgYHnM423krcw17njSVkvaAmYchU5Feo9a4tGU8YzRY+AOzKkwuDycpAlbk4/ijsIOKHEUOThjBopo33fXqFD3ktm/wSQPtXPFiPhWNSHxgjpfyEc2B3KI8tuOAdl+CLjQr5ITAV2OTlgHNZnAh0AuvaWpoV499/e5/pnyXfHhe8ysjO65YDAvNVpXQKCAQAWplxYIEhQcE51AqOXVwQNNNo6NHjBVNTkpcAtJC7gT5bmHkvQkEq9rI837rHgnzGC0jyQQ8tkL4gAQWDt+coJsyB2p5wypifyRz6Rh5uixOdEvSCBVEy1W4AsNo0fqD7UielOD6BojjJCilx4xHjGjQUntxyaOrsLC+EsRGiWOefTznTbEBplqiuH9kxoJts+xy9LVZmDS7TtsC98kOmkltOlXVNb6/xF1PYZ9j897buHOSXC8iTgdzEpbaiH7B5HSPh++1/et1SEMWsiMt7lU92vAhErDR8C2jCXMiT+J67ai51LKSLZuovjntnhA6Y8UoELxoi34u1DFuHvF9veBB8CHQCtb46H+2ELXByw4R7jIvdm0yDPGB3F3Up+lKbw";
		this.publicKey = "MIIDQjCCAjUGByqGSM44BAEwggIoAoIBAQCPeTXZuarpv6vtiHrPSVG28y7FnjuvNxjo6sSWHz79NgbnQ1GpxBgzObgJ58KuHFObp0dbhdARrbi0eYd1SYRpXKwOjxSzNggooi/6JxEKPWKpk0U0CaD+aWxGWPhL3SCBnDcJoBBXsZWtzQAjPbpUhLYpH51kjviDRIZ3l5zsBLQ0pqwudemYXeI9sCkvwRGMn/qdgYHnM423krcw17njSVkvaAmYchU5Feo9a4tGU8YzRY+AOzKkwuDycpAlbk4/ijsIOKHEUOThjBopo33fXqFD3ktm/wSQPtXPFiPhWNSHxgjpfyEc2B3KI8tuOAdl+CLjQr5ITAV2OTlgHNZnAh0AuvaWpoV499/e5/pnyXfHhe8ysjO65YDAvNVpXQKCAQAWplxYIEhQcE51AqOXVwQNNNo6NHjBVNTkpcAtJC7gT5bmHkvQkEq9rI837rHgnzGC0jyQQ8tkL4gAQWDt+coJsyB2p5wypifyRz6Rh5uixOdEvSCBVEy1W4AsNo0fqD7UielOD6BojjJCilx4xHjGjQUntxyaOrsLC+EsRGiWOefTznTbEBplqiuH9kxoJts+xy9LVZmDS7TtsC98kOmkltOlXVNb6/xF1PYZ9j897buHOSXC8iTgdzEpbaiH7B5HSPh++1/et1SEMWsiMt7lU92vAhErDR8C2jCXMiT+J67ai51LKSLZuovjntnhA6Y8UoELxoi34u1DFuHvF9veA4IBBQACggEAQvc66UDrEoWlE4pdLunY/eu+D7CsbK/1sEnfuXczo7+aVs9hF2pydVfPRYZjvGuB5Q5rqh5dgUrujxuf/nbGS3Qbs20a08zX1uBG/TZ4qjNeHquLQUy3Ck1vkVEJH/B7jWJ0Hng+hjaOHCN1a6yUv3OhhrWmae6FSiHjbFPnNSARx4Ow3QMFt4+aqYf0/Ku8FeNN5GNTOf9ouHXmLSRngaQZQoH+XskVOupN0ZCkNd8Nq2kXib7ft5issXT6jn9KnrgciQ6mr57UUvi5oYzQ3DGybUICFUGwZAjIyalYYJRgt2gmTTaFs0RF/B+fiB9ZDgrLA6NQjbRio/hy+ahELg==";
		this.token = "9b8518f9-be03-4cba-a75c-18d73b0e395a";

	}

	public String getAuthorityTLSURL() {
		return authorityTLSURL;
	}

	public void setAuthorityTLSURL(String authorityTLSURL) {
		this.authorityTLSURL = authorityTLSURL;
	}

	public String getAuthorityPublicKey() {
		return authorityPublicKey;
	}

	public void setAuthorityPublicKey(String authorityPublicKey) {
		this.authorityPublicKey = authorityPublicKey;
	}

	public String getDbFormat() {
		return dbFormat;
	}

	public void setDbFormat(String dbFormat) {
		this.dbFormat = dbFormat;
	}

	public String getPeriodFormat() {
		return periodFormat;
	}

	public void setPeriodFormat(String periodFormat) {
		this.periodFormat = periodFormat;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public int getPeriodLengthDays() {
		return periodLengthDays;
	}

	public void setPeriodLengthDays(int periodLengthDays) {
		this.periodLengthDays = periodLengthDays;
	}

	public int getGracePeriods() {
		return gracePeriods;
	}

	public void setGracePeriods(int gracePeriods) {
		this.gracePeriods = gracePeriods;
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

	public long getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(long sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
