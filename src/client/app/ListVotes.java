package client.app;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import client.gui.Menu;

public class ListVotes implements Serializable {
    
    ArrayList<Votes> list = new ArrayList<Votes>();

	public ArrayList<Votes> getList() {
		return list;
	}

	public void save(String f) throws Exception {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
		out.writeObject(list);
		out.close();
	}

	@SuppressWarnings("unchecked")
	public void load(String f) throws Exception {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
		list = (ArrayList<Votes>) in.readObject();
		in.close();
	}

	public int voteCheck(String id) {
		int total = 0;

        for (Votes vote : list) {
            if (vote.getFrom().getCc().equals(id)) {
                total -= vote.getValue();
                break;
            } else if (vote.getTo().getCode().equals(id)) {
                total += vote.getValue();
            }
        }

        return total;
	}

    public boolean isValid(Votes t) throws Exception {
        if (t.getValue() <= 0 || t.getValue() > 1) {
            JOptionPane.showMessageDialog(Menu.frmFrame, "O voto é inválido", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        double value = voteCheck(t.getFrom().getCc());
        
        if (value != 0) {
            JOptionPane.showMessageDialog(Menu.frmFrame, "Já votou nesta eleição", "Aviso", JOptionPane.INFORMATION_MESSAGE, null);
            return false;
        }
        return value == 0;
    }

    public boolean add(Votes t) throws Exception {
        if (isValid(t)) {
        	list.add(t);
        	return true;
        }
        return false;
    }
    
    public List<Candidate> getCandidatesVotes() {
        List<Candidate> cands = new ArrayList<>();
        
        for (Votes transaction : list) {
            if (!cands.contains(transaction.getTo())) {
            	cands.add(transaction.getTo());
            }
        }
        /*
        ArrayList<String> candsVotes = new ArrayList<>();
        for (Candidate cand : cands) {
            balance.add(String.format("%s - %-15s %20s", cand.getCode(), cand.getName(), getVotes(user)));
        }
        return balance;
        */
        return cands;
    }
    
    public List<Elector> getElectors() {
        List<Elector> electors = new ArrayList<>();

        for (Votes votes : list) {
            if (!electors.contains(votes.getFrom())) {
            	electors.add(votes.getFrom());
            }
        }

        return electors;
    }

    private static final long serialVersionUID = 1L;

}
