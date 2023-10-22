package blockchain;

import java.io.Serializable;

public class Block implements Serializable {

    private static final long serialVersionUID = 1L;
    
	String previousHash; // link to previous block
    String data;         // data in the block
    int nonce;           // proof of work 
    String currentHash;  // Hash of block

    public String getPreviousHash() {
        return previousHash;
    }

    public String getData() {
        return data;
    }

    public int getNonce() {
        return nonce;
    }

    public String getCurrentHash() {
        return currentHash;
    }

    
    
    public Block(String previousHash, String data, int nonce) {
        this.previousHash = previousHash;
        this.data = data;
        this.nonce = nonce;
        this.currentHash = calculateHash();
    }

    public String calculateHash() {
        return Hash.getHash(nonce + previousHash + data);
    }

    public String toString() {
        return // (isValid() ? "OK\t" : "ERROR\t")+
                 String.format("[ %8s", previousHash) + " <- " + 
                   String.format("%-10s", data) +  String.format(" %7d ] = ", nonce) + 
                String.format("%8s",currentHash);

    }

    public boolean isValid() {
        return currentHash.equals(calculateHash());
    }

}
