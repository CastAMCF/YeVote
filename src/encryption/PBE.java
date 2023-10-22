package encryption;

import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public interface PBE {
	
	private static Cipher generatePBE(int mode, String password) throws Exception {
		
		byte[] salt = password.getBytes();

		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(salt);
		
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
		
		byte[] bytesIV = new byte[16];
	    random.nextBytes(bytesIV);
	    IvParameterSpec ivspec = new IvParameterSpec(bytesIV);
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(mode, secretKey, ivspec);
        
        return cipher;
	}
	
	public static byte[] encryption(byte[] data, String password) throws Exception {
        Cipher cipher = generatePBE(Cipher.ENCRYPT_MODE, password);
		return cipher.doFinal(data);
    }
	
	public static byte[] decryption(byte[] data, String password) throws Exception {
		Cipher cipher = generatePBE(Cipher.DECRYPT_MODE, password);
		return cipher.doFinal(data);
    }
	
}
