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