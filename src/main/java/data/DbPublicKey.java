package data;

import java.io.Serializable;
import java.math.BigInteger;

import com.google.gson.annotations.Expose;

import keys.PublicKey;

public class DbPublicKey implements PublicKey, Serializable {
	/**
	 * default serial version id
	 */
	private static final long serialVersionUID = 1L;

	private Integer publicKeyId;

	@Expose
	private BigInteger n;
	@Expose
	private BigInteger a;
	@Expose
	private BigInteger g;
	@Expose
	private BigInteger h;
	@Expose
	private BigInteger w;
	@Expose
	private BigInteger bigQ;
	@Expose
	private BigInteger bigP;
	@Expose
	private BigInteger bigF;
	@Expose
	private BigInteger bigG;
	@Expose
	private BigInteger bigH;

	public BigInteger getN() {
		return n;
	}

	public void setN(BigInteger n) {
		this.n = n;
	}

	public BigInteger getA() {
		return a;
	}

	public void setA(BigInteger a) {
		this.a = a;
	}

	public BigInteger getG() {
		return g;
	}

	public void setG(BigInteger g) {
		this.g = g;
	}

	public BigInteger getH() {
		return h;
	}

	public void setH(BigInteger h) {
		this.h = h;
	}

	public BigInteger getW() {
		return w;
	}

	public void setW(BigInteger w) {
		this.w = w;
	}

	public BigInteger getBigQ() {
		return bigQ;
	}

	public void setBigQ(BigInteger bigQ) {
		this.bigQ = bigQ;
	}

	public BigInteger getBigP() {
		return bigP;
	}

	public void setBigP(BigInteger bigP) {
		this.bigP = bigP;
	}

	public BigInteger getBigF() {
		return bigF;
	}

	public void setBigF(BigInteger bigF) {
		this.bigF = bigF;
	}

	public BigInteger getBigG() {
		return bigG;
	}

	public void setBigG(BigInteger bigG) {
		this.bigG = bigG;
	}

	public BigInteger getBigH() {
		return bigH;
	}

	public void setBigH(BigInteger bigH) {
		this.bigH = bigH;
	}

	public Integer getPublicKeyId() {
		return publicKeyId;
	}

	public void setPublicKeyId(Integer publicKeyId) {
		this.publicKeyId = publicKeyId;
	}

}