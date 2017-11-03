package util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class SignatureTest {
	
	@Test
	public void SigTest() 
	{
		KeyPairGenerator keyGen = null;
		SecureRandom rand = null;

		try {
		    keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
		    rand = SecureRandom.getInstance("SHA1PRNG", "SUN");
		}catch (Exception e) {
			
		}
		keyGen.initialize(2048, rand);
	    KeyPair keyPair = keyGen.generateKeyPair();
	    PrivateKey privateKey = keyPair.getPrivate();
	    PublicKey publicKey = keyPair.getPublic();
	    
	    System.out.println(Base64.encodeBase64String(privateKey.getEncoded()));
	    System.out.println(Base64.encodeBase64String(publicKey.getEncoded()));

		
	}
	
	
}
