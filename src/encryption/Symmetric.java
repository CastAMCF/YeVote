package encryption;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public interface Symmetric {
	
	public static SecretKey createAESKey() throws Exception {
		 
        SecureRandom securerandom = new SecureRandom();
        KeyGenerator keygenerator = KeyGenerator.getInstance("AES");

        keygenerator.init(256, securerandom);
        return keygenerator.generateKey();
    }
    
    private static Cipher generateAES(int mode, Key secretKey) throws Exception {

    	byte[] salt = secretKey.getEncoded();

		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(salt);
		
		byte[] bytesIV = new byte[16];
	    random.nextBytes(bytesIV);
	    IvParameterSpec ivspec = new IvParameterSpec(bytesIV);
		
	    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(mode, secretKey, ivspec);
        
        return cipher;
	}
	
	public static byte[] encryptionAES(byte[] data, Key secretKey) throws Exception {
        Cipher cipher = generateAES(Cipher.ENCRYPT_MODE, secretKey);
		return cipher.doFinal(data);
    }
	
	public static byte[] decryptionAES(byte[] data, Key secretKey) throws Exception {
		Cipher cipher = generateAES(Cipher.DECRYPT_MODE, secretKey);
		return cipher.doFinal(data);
    }
	
}
