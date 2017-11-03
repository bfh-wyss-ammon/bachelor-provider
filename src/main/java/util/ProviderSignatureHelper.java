package util;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
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
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec( privateKeyEncoded);
	        KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
	        PrivateKey priv = keyFactory.generatePrivate(spec);
	        Signature signature = Signature.getInstance("SHA256withDSA", "SUN");
	        signature.initSign(priv);
	        signature.update(message);
	        sigBytes = signature.sign();
			
		}catch (Exception e) {
			e.printStackTrace();
			
		}

        return sigBytes;
		
	}

	



}
