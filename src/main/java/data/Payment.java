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
 * This class stores data related to a payment in the toll calculation protocol in the database of the provider.
 * It is also used to generate the HashValue of a payment and serializing/parsing the payment to JSON (between provider and mobile application).
 */


package data;

import com.google.gson.annotations.Expose;

import interfaces.HashValue;

public class Payment {
	
	@Expose
	@HashValue
    private int summe;
	@Expose
	private DbSignature signature;
	@HashValue
	@Expose
	private String signatureOnTuples;
	
	public Payment() {
    }


	
    public String getSignatureOnTuples() {
		return signatureOnTuples;
	}

	public void setSignatureOnTuples(String signatureOnTuples) {
		this.signatureOnTuples = signatureOnTuples;
	}


    public Payment(int summe, DbSignature signature) {
        this.summe = summe;
        this.signature = signature;
    }

    public int getSumme() {
        return summe;
    }

    public void setSumme(int summe) {
        this.summe = summe;
    }

    public DbSignature getSignature() {
        return signature;
    }

    public void setSignature(DbSignature signature) {
        this.signature = signature;
    }
}