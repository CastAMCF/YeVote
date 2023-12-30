package client.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class ListCandidates implements Serializable {

	private static final long serialVersionUID = 1L;
	
	ArrayList<Candidate> list = new ArrayList<Candidate>();

	public ArrayList<Candidate> getList() {
		return list;
	}

	public Candidate get(String code) {
		return list.stream().filter(c -> c.getCode().equals(code)).findFirst().orElse(null);
	}
	
	public boolean exists(String code) {
		return list.stream().anyMatch(c -> c.getCode().equals(code));
	}
	
	public void save(String f) throws Exception {
		String[] path = Arrays.copyOf(f.split("/"), f.split("/").length - 1);
    	new File(String.join("/", path)).mkdirs();
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
		out.writeObject(list);
		out.close();
	}

	@SuppressWarnings("unchecked")
	public void load(String f) throws Exception {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
		list = (ArrayList<Candidate>) in.readObject();
		in.close();
	}

}
