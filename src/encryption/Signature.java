package encryption;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public interface Signature {
	
	public static String sign(byte[] data, PrivateKey privateKey) throws Exception {
		java.security.Signature privateSignature = java.security.Signature.getInstance("SHA3-512withRSA");
	    privateSignature.initSign(privateKey);
	    privateSignature.update(data);

	    byte[] signature = privateSignature.sign();

	    return Base64.getEncoder().encodeToString(signature);
	}
	
	public static boolean verify(byte[] data, String signature, PublicKey publicKey) throws Exception {
		java.security.Signature publicSignature = java.security.Signature.getInstance("SHA3-512withRSA");
	    publicSignature.initVerify(publicKey);
	    publicSignature.update(data);

	    byte[] signatureBytes = Base64.getDecoder().decode(signature);

	    return publicSignature.verify(signatureBytes);
	}
	
}
