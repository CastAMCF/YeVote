package encryption;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;

public interface Asymmetric {
	
	String RSA = "RSA";
	
	public static KeyPair generateRSAKkeyPair() throws Exception {
		SecureRandom secureRandom = new SecureRandom();
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
		keyPairGenerator.initialize(2048, secureRandom);
		
		return keyPairGenerator.generateKeyPair();
	}

	public static byte[] encryptionRSA(byte[] data, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance(RSA);
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		
		return cipher.doFinal(data);
	}

	public static byte[] decryptionRSA(byte[] cipherText, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance(RSA);
		
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		
		return cipher.doFinal(cipherText);
	}
	
}
