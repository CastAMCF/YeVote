package client.gui.results;

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import client.app.Candidate;

public class VoteText extends JPanel {

	private static final long serialVersionUID = 1L;

	public static JTextArea txtrDasdadasAsDasd;
	
	/**
	 * Create the panel.
	 */
	public VoteText() {
		
		this.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		this.add(scrollPane, BorderLayout.CENTER);
		
		txtrDasdadasAsDasd = new JTextArea();
		txtrDasdadasAsDasd.setEditable(false);
		txtrDasdadasAsDasd.setFocusable(false);
		scrollPane.setViewportView(txtrDasdadasAsDasd);
	}
	
	public static void loadResults(HashMap<Candidate, Integer> hashMap) {
		int votes = hashMap.values().stream().reduce(0, Integer::sum);
		StringBuilder sb = new StringBuilder();
		
	    for (Candidate cand : hashMap.keySet()) {
	    	sb.append(String.format("%-100s\t  %10d votos  ( %3d %% )", cand.toString(), hashMap.get(cand), (votes == 0) ? 0 : (hashMap.get(cand) * 100) / votes) + "\n");
	    }
	    
	    txtrDasdadasAsDasd.setText(sb.toString());
	}
	
}
