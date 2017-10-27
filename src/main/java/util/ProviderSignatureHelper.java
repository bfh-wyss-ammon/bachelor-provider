package util;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;

import org.apache.commons.codec.binary.Base64;

import data.ProviderSettings;


public class ProviderSignatureHelper {
	
	public boolean verify (byte[] message, PublicKey pub) {
		return false;
	}
	
	public static byte[] sign (byte[] message) {
		byte[] sigBytes =null;
try {
			byte[] privateKeyEncoded = Base64.decodeBase64(SettingsHelper.getSettings(ProviderSettings.class).getPrivateKey());
			X509EncodedKeySpec privateKeySpec = new X509EncodedKeySpec(privateKeyEncoded);
	        KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
	        PrivateKey priv = keyFactory.generatePrivate(privateKeySpec);
	        Signature signature = Signature.getInstance("SHA1withDSA", "SUN");
	        signature.initSign(priv);
	        signature.update(message);
	        sigBytes = signature.sign();
			
		}catch (Exception e) {
			
		}

        return sigBytes;
		
	}

	



}
