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
 */

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
	    
	    System.out.println("Private Key: " + Base64.encodeBase64String(privateKey.getEncoded()));
	    System.out.println("Public Key: " + Base64.encodeBase64String(publicKey.getEncoded()));

		
	}
	
	
}
