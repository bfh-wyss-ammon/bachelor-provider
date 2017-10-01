package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import data.DbTuple;

public class HashHelper {
	
	public static String getHash(DbTuple tuple) {
		
		String latitude = tuple.getLatitude().toString();
		String longitude = tuple.getLongitude().toString();
		String time = tuple.getCreated().toString();
		
		String concatenated = latitude + longitude + time;
		byte[] message = concatenated.getBytes();
		
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		byte[] hash = digest.digest(message);
		return Base64.getEncoder().encodeToString(hash);
		
		
		
	}

}
