package logic;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Crypto {
	
	public String hash(String password, String salt) {
		byte[] bSalt = new byte[16];
		MessageDigest md = null;
		
		// generate new salt since no salt was provided
		if(salt == null) {
			SecureRandom random = new SecureRandom();
			random.nextBytes(bSalt);	
		} else {
			bSalt = salt.getBytes(Charset.forName("UTF-8"));
		}
		// add salt to SHA-512
		try {
			md = MessageDigest.getInstance("SHA-512");
			md.update(bSalt);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// add password to SHA-512
		byte[] hashedPassword = md.digest(password.getBytes(Charset.forName("UTF-8")));
		String hash = new String(hashedPassword,Charset.forName("UTF-8"));

		return hash;
	}
	
	public boolean compareHashes(String h1, String h2) {
		if(h1.equals(h2)) {
			return true;
		} else {
			return false;
		}
	}
}
