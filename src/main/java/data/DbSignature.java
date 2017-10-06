package data;

import java.math.BigInteger;
import com.google.gson.annotations.Expose;
import signatures.Signature;


public class DbSignature implements Signature {
	
	private int signatureId;
	private BigInteger u;
	private BigInteger bigU1;
	private BigInteger bigU2;
	private BigInteger bigU3;
	private BigInteger zx;
	private BigInteger zr;
	private BigInteger ze;
	private BigInteger zbigR;
	private BigInteger c;
	
	public DbSignature() {
		
	}

	public DbSignature(BaseSignature signature) {
		this.u = signature.getU();
		this.bigU1 = signature.getBigU1();
		this.bigU2 = signature.getBigU2();
		this.bigU3 = signature.getBigU3();
		this.zx = signature.getZx();
		this.zr = signature.getZr();
		this.ze = signature.getZe();
		this.zbigR = signature.getZbigR();
		this.c = signature.getC();
	}

	public BigInteger getU() {
		return u;
	}

	public BigInteger getBigU1() {
		return bigU1;
	}

	public BigInteger getBigU2() {
		return bigU2;
	}

	public BigInteger getBigU3() {
		return bigU3;
	}

	public BigInteger getZx() {
		return zx;
	}

	public BigInteger getZr() {
		return zr;
	}

	public BigInteger getZe() {
		return ze;
	}

	public BigInteger getZbigR() {
		return zbigR;
	}

	public BigInteger getC() {
		return c;
	}

	public void setU(BigInteger u) {
		this.u = u;
	}

	public void setBigU1(BigInteger bigU1) {
		this.bigU1 = bigU1;
	}

	public void setBigU2(BigInteger bigU2) {
		this.bigU2 = bigU2;
	}

	public void setBigU3(BigInteger bigU3) {
		this.bigU3 = bigU3;
	}

	public void setZx(BigInteger zx) {
		this.zx = zx;
	}

	public void setZr(BigInteger zr) {
		this.zr = zr;
	}

	public void setZe(BigInteger ze) {
		this.ze = ze;
	}

	public void setZbigR(BigInteger zbigR) {
		this.zbigR = zbigR;
	}

	public void setC(BigInteger c) {
		this.c = c;
	}

	public int getSignatureId() {
		return signatureId;
	}

	public void setSignatureId(int signatureId) {
		this.signatureId = signatureId;
	}	
}
	
	