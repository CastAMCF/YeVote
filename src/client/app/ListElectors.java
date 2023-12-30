package client.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class ListElectors implements Serializable {

	private static final long serialVersionUID = 1L;
	
	ArrayList<Elector> list = new ArrayList<Elector>();

	public ArrayList<Elector> getList() {
		return list;
	}

	public Elector get(String cc) {
		return list.stream().filter(e -> e.getCc().equals(cc)).findFirst().orElse(null);
	}
	
	public boolean exists(String cc) {
		return list.stream().anyMatch(e -> e.getCc().equals(cc));
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
		list = (ArrayList<Elector>) in.readObject();
		in.close();
	}
	
}
