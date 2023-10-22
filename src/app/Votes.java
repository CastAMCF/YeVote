package app;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import encryption.Signature;

public class Votes implements Serializable {

    private Elector from;
    private Candidate to;
    private int value;
    private String signature;

    public Votes(Elector from, Candidate to) {
        this.from = from;
        this.to = to;
        this.value = 1;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Elector getFrom() {
        return from;
    }
    
    public String getSignature() {
        return signature;
    }

    public void setFrom(Elector from) {
        this.from = from;
    }

    public Candidate getTo() {
        return to;
    }

    public void setTo(Candidate to) {
        this.to = to;
    }
    
    public String sign(String password) throws Exception {
    	this.signature = Signature.sign(classToBytes(), this.from.getPrivKey(password));
    	return this.signature;
    }
    
    public boolean verify() throws Exception {
    	String signature = this.signature;
    	this.signature = null;
    	boolean check = Signature.verify(classToBytes(), signature, this.from.getPubKey());
    	this.signature = signature;
    	return check;
    }

    private byte[] classToBytes() throws Exception {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject(this);
		objectOutputStream.close();
		return byteArrayOutputStream.toByteArray();
	}
    /*
    private Votes bytesToClass(byte[] v) throws Exception {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(v);
		ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
		Votes vote = (Votes) objectInputStream.readObject();
		objectInputStream.close();
		return vote;
	}
    */
    @Override
    public String toString() {
        return String.format("%-10s votou %10s", from, to);
    }

    private static final long serialVersionUID = 1L;
    
}
