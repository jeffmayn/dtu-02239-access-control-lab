package logic;

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
			bSalt = salt.getBytes(StandardCharsets.UTF_8);
		}
		// add salt to SHA-512
		try {
			md = MessageDigest.getInstance("SHA-512");
			md.update(bSalt);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// add password to SHA-512
		assert md != null;
		byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
		return new String(hashedPassword, StandardCharsets.UTF_8);
	}
	
	public boolean compareHashes(String h1, String h2) {
		return h1.equals(h2);
	}
}