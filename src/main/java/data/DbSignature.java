/**
 *   Copyright 2018 Pascal Ammon, Gabriel Wyss
 * 
 * 	 Implementation eines anonymen Mobility Pricing Systems auf Basis eines Gruppensignaturschemas
 * 
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * This class stores data related to a group signature in the database of the provider.
 */


package data;

import java.math.BigInteger;
import com.google.gson.annotations.Expose;
import signatures.Signature;


public class DbSignature implements Signature {
	
	private int signatureId;
	@Expose
	private BigInteger u;
	@Expose
	private BigInteger bigU1;
	@Expose
	private BigInteger bigU2;
	@Expose
	private BigInteger bigU3;
	@Expose
	private BigInteger zx;
	@Expose
	private BigInteger zr;
	@Expose
	private BigInteger ze;
	@Expose
	private BigInteger zbigR;
	@Expose
	private BigInteger c;
	
	public DbSignature() {
		
	}

	public DbSignature(Signature signature) {
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
	
	