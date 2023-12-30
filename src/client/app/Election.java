package client.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.ImageIcon;

import encryption.Asymmetric;
import encryption.Symmetric;
import utils.ImageUtils;

public class Election implements Serializable {

	private static final long serialVersionUID = 1L;
	
	ListCandidates lstCandidates;
	ListElectors lstElectors;
	ListVotes votes;
	
	String name;
	String beginDate;
	String endDate;
	byte[] image;
	
	byte[] pubKey;
	byte[] privKey;
	byte[] secretKey;

	public Election(String name, String beginDate, String endDate, String imagePath) throws Exception {
		this.name = name;
		this.beginDate = beginDate;
		this.endDate = endDate;
		setImage(imagePath);
		saveKeys();
	}
	
	public Election(String name, String beginDate, String endDate, byte[] image) throws Exception {
		this.name = name;
		this.beginDate = beginDate;
		this.endDate = endDate;
        this.image = image;
        saveKeys();
    }
    
    public Election(String name, String beginDate, String endDate) throws Exception {
    	this.name = name;
		this.beginDate = beginDate;
		this.endDate = endDate;
        setImage();
        saveKeys();
    }
	
	public Election() {
	}

	public String getName() {
        return name;
    }

    public String getBeginDate() {
        return beginDate;
    }
    
    public String getEndDate() {
        return endDate;
    }

    public ImageIcon getImage() {
        return new ImageIcon(image);
    }

    public void setImage(String imagePath) throws IOException {
        this.image = ImageUtils.iconToByteArray(imagePath);
    }
    
    public void setImage() throws IOException {
        this.image = ImageUtils.iconToByteArray(Elector.class.getResource("/images/election_default.png"));
    }
	
    public void setLstCandidates(ListCandidates lstCandidates) {
		this.lstCandidates = lstCandidates;
	}

	public void setLstElectors(ListElectors lstElectors) {
		this.lstElectors = lstElectors;
	}
	
	public void setLstVotes(ListVotes votes) {
		this.votes = votes;
	}
    
	public ListCandidates getLstCandidates() {
		return lstCandidates;
	}

	public ListElectors getLstElectors() {
		return lstElectors;
	}
	
	public ListVotes getLstVotes() {
		return votes;
	}
	
	public PublicKey getPubKey() throws Exception {
    	byte[] data = this.pubKey;
        X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(data);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        
        return keyFactory.generatePublic(pubSpec);
    }
    
    public PrivateKey getPrivKey() throws Exception {
    	byte[] dataDecrpyt = this.privKey;
        PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(dataDecrpyt);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        
        return keyFactory.generatePrivate(privSpec);
    }
    
    public Key getSecretKey(PublicKey pubKey) throws Exception {
    	byte[] dataEncrpyt = this.secretKey;
        byte[] dataDecrpyt = Asymmetric.decryptionRSA(dataEncrpyt, pubKey);

        return new SecretKeySpec(dataDecrpyt, "AES");
    }
	
	public void save(String f) throws Exception {
		String[] path = Arrays.copyOf(f.split("/"), f.split("/").length - 1);
    	new File(String.join("/", path)).mkdirs();
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
		out.writeObject(this);
		out.close();
	}

	public Election load(String f) throws Exception {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
		Election e = (Election) in.readObject();
		this.lstCandidates = e.lstCandidates;
		this.lstElectors = e.lstElectors;
		this.votes = e.votes;
		in.close();
		return e;
	}
    
	private void saveKeys() throws Exception {
		KeyPair keypair = Asymmetric.generateRSAKkeyPair();
		SecretKey secretKey = Symmetric.createAESKey();
		
		this.pubKey = keypair.getPublic().getEncoded();
		this.privKey = keypair.getPrivate().getEncoded();
		this.secretKey = Asymmetric.encryptionRSA(secretKey.getEncoded(), keypair.getPrivate());
    }
}
    

