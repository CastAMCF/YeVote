package blockchain;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class Hash {
	/*
	public static String toHexString(int n) {
        return Integer.toHexString(n).toUpperCase();
    }
	
    public static String getHash(String data) {
        return toHexString(data.hashCode());
    }
    */
	
    public static String getHash(String data) {
    	byte[] h = null;
		try {
			h = calculateHash(data.getBytes());
		} catch (Exception e) { e.printStackTrace(); }
        return Base64.getEncoder().encodeToString(h);
    }
    
    public static byte[] calculateHash(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(data);
        return md.digest();
    }

    public static boolean verifyHash(byte[] data, byte[] hash) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(data);
        byte[] trueHash = md.digest();
        return Arrays.equals(trueHash, hash);
    }
    
}
