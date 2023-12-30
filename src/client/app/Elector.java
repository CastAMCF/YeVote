package client.app;

import java.io.IOException;
import java.io.Serializable;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.ImageIcon;

import encryption.Asymmetric;
import encryption.PBE;
import encryption.Symmetric;
import utils.ImageUtils;

public class Elector implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	String name;
	String birthday;
	String cc;
	String sex;
	byte[] image;
	byte[] pubKey;
	byte[] privKey;
	byte[] secretKey;

    public Elector(String name, String cc, String sex, String birthday, String password, String imagePath) throws Exception {
        this.name = name;
        this.cc = cc;
        this.sex = sex;
        this.birthday = birthday;
        setImage(imagePath);
        saveKeys(password);
    }
    
    public Elector(String name, String cc, String sex, String birthday, String password, byte[] image) throws Exception {
        this.name = name;
        this.cc = cc;
        this.sex = sex;
        this.birthday = birthday;
        this.image = image;
        saveKeys(password);
    }
    
    public Elector(String name, String cc, String sex, String birthday, String password) throws Exception {
        this.name = name;
        this.cc = cc;
        this.sex = sex;
        this.birthday = birthday;
        setImage();
        saveKeys(password);
    }

	public String getName() {
		return name;
	}

	public String getBirthday() {
		return birthday;
	}

	public String getCc() {
		return cc;
	}

	public String getSex() {
		return sex;
	}

	public ImageIcon getImage() {
		return new ImageIcon(image);
	}
	
	public byte[] getImageBytes() {
        return image;
    }
	
	public PublicKey getPubKey() throws Exception {
    	byte[] data = this.pubKey;
        X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(data);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        
        return keyFactory.generatePublic(pubSpec);
    }
    
    public PrivateKey getPrivKey(String password) throws Exception {
    	byte[] dataEncrpyt = this.privKey;
        byte[] dataDecrpyt = PBE.decryption(dataEncrpyt, password);
        PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(dataDecrpyt);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        
        return keyFactory.generatePrivate(privSpec);
    }

    public Key getSecretKey(PublicKey pubKey) throws Exception {
    	byte[] dataEncrpyt = this.secretKey;
        byte[] dataDecrpyt = Asymmetric.decryptionRSA(dataEncrpyt, pubKey);

        return new SecretKeySpec(dataDecrpyt, "AES");
    }

    public String getPubKeyEncoded() throws Exception {
        return Base64.getEncoder().encodeToString(getPubKey().getEncoded());
    }
    /*
    public String getPrivKeyEncoded() throws Exception {
        return Base64.getEncoder().encodeToString(getPrivKey().getEncoded());
    }

    public String getSecretKeyEncoded() throws Exception {
        return Base64.getEncoder().encodeToString(getSecretKey().getEncoded());
    }
    */
	public void setImage(String imagePath) throws IOException {
		this.image = ImageUtils.iconToByteArray(imagePath);
	}
	
	public void setImage() throws IOException {
        this.image = ImageUtils.iconToByteArray(Elector.class.getResource("/images/elector_default.png"));
    }
	
	private void saveKeys(String password) throws Exception {
		KeyPair keypair = Asymmetric.generateRSAKkeyPair();
		SecretKey secretKey = Symmetric.createAESKey();
		
		this.pubKey = keypair.getPublic().getEncoded();
		this.privKey = PBE.encryption(keypair.getPrivate().getEncoded(), password);
		this.secretKey = Asymmetric.encryptionRSA(secretKey.getEncoded(), keypair.getPrivate());
    }

	public String toString() {
		return cc + " - " + name;
	}

}
