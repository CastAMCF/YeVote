package client.gui.results;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import client.app.Candidate;

public class Graph extends JPanel {

	private static final long serialVersionUID = 1L;

	public static JPanel candidatesPanel;
	
	/**
	 * Create the panel.
	 */
	public Graph() {
		
		this.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		this.add(scrollPane_1, BorderLayout.CENTER);
		
		candidatesPanel = new JPanel();
		scrollPane_1.setViewportView(candidatesPanel);
		GridBagLayout gbl_candidatesButsPanel = new GridBagLayout();
		gbl_candidatesButsPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_candidatesButsPanel.rowHeights = new int[] {0, 15, 0};
		gbl_candidatesButsPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_candidatesButsPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		candidatesPanel.setLayout(gbl_candidatesButsPanel);
		
	}
	
	public static void loadResults(HashMap<Candidate, Integer> hashMap) {
		candidatesPanel.removeAll();
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.insets = new Insets(3,3,3,3);
		gbc.weightx = 0.5;
		gbc.weighty = 0.5;
		
		int num = hashMap.size();
		int votes = hashMap.values().stream().reduce(0, Integer::sum);
	    int numCheck = 0;
	    
	    for (Candidate cand : hashMap.keySet()) {

			final int currentNumCheck = numCheck;
			JLabel label = new JLabel(cand.getCode() + " - " + cand.getName());

			label.setHorizontalTextPosition(SwingConstants.CENTER);
			label.setVerticalTextPosition(SwingConstants.BOTTOM);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setVerticalAlignment(SwingConstants.CENTER);
			label.setFocusable(false);

			ImageIcon ico = cand.getImage();
			ico.setImage(ico.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
			label.setIcon(ico);

			gbc.gridx = 0;
			gbc.gridy = currentNumCheck;
			gbc.gridwidth = 1;

			candidatesPanel.add(label, gbc);

			JProgressBar progressBar = new JProgressBar();
			progressBar.setStringPainted(true);
			progressBar.setValue((votes == 0) ? 0 : (hashMap.get(cand) * 100) / votes);
			progressBar.setPreferredSize(new Dimension(800, 100));
			progressBar.setFont(new Font("Segoe UI", Font.PLAIN, 20));

			gbc.gridx = 1;
			gbc.gridy = currentNumCheck;
			gbc.gridwidth = 1;

			candidatesPanel.add(progressBar, gbc);

			numCheck++;

			if (numCheck == num) break;
	    }
	}
	
}
