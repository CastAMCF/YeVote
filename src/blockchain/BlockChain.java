package blockchain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class BlockChain implements Serializable {

	private static final long serialVersionUID = 1L;
	
    ArrayList<Block> chain = new ArrayList<>();

    
    public ArrayList<Block> getChain() {
        return chain;
    }

    /**
     * gets the last block hash of the chain
     *
     * @return last hash in the chain
     */
    public String getLastBlockHash() {
        //Genesis block
        if (chain.isEmpty()) {
            return String.format("%08d", 0);
        }
        //hash of the last in the list
        return chain.get(chain.size() - 1).currentHash;
    }

    /**
     * adds data to the blockChain
     *
     * @param data data to add in the block
     * @param dificulty dificulty of block to miners (POW)
     */
    public void add(String data, int dificulty) {
        //hash of previous block
        String prevHash = getLastBlockHash();
        //mining block
        //int nonce = Miner.getNonce(prevHash + data, dificulty);
        
        int nonce = 0;
		try {
			nonce = Miner.getNonce(prevHash + data, dificulty);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
        //build new block
        Block newBlock = new Block(prevHash, data, nonce);
        //add new block to the chain
        chain.add(newBlock);
    }

    public Block get(int index) {
        return chain.get(index);
    }

    @Override
    public String toString() {
        StringBuilder txt = new StringBuilder();
        txt.append("Blochain size = ").append(chain.size()).append("\n");
        for (Block block : chain) {
            txt.append(block.toString()).append("\n");
        }
        return txt.toString();
    }

    public void save(String fileName) throws Exception {
    	String[] path = Arrays.copyOf(fileName.split("/"), fileName.split("/").length - 1);
    	new File(String.join("/", path)).mkdirs();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(chain);
        }
    }

    @SuppressWarnings("unchecked")
	public void load(String fileName) throws Exception {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            this.chain = (ArrayList<Block>) in.readObject();
        }
    }

    public boolean isValid() {
        //Validate blocks
        for (Block block : chain) {
            if (!block.isValid()) {
                return false;
            }
        }
        //validate Links
        //starts in the second block
        for (int i = 1; i < chain.size(); i++) {
            //previous hash !=  hash of previous
            if (chain.get(i).previousHash != chain.get(i - 1).currentHash) {
                return false;
            }
        }
        return true;
    }

}
