package client.gui;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import utils.ImageUtils;

public class About extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public About() {
		
		this.setLayout(null);
		
		this.setPreferredSize(new Dimension(420, 300));
		
		JLabel jLabel3 = new JLabel();
		jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel3.setText("Afonso Castelão - 22921");
		jLabel3.setBounds(10, 195, 166, 22);
		this.add(jLabel3);
		
		JLabel jLabel4 = new JLabel();
		jLabel4.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel4.setText("Hugo André Marques - 24171");
		jLabel4.setBounds(212, 195, 200, 22);
		this.add(jLabel4);
		
		JTextArea jTextArea1 = new JTextArea();
		jTextArea1.setEditable(false);
		jTextArea1.setText("Esta aplicação foi desenvolvida no ambito da disciplina de \nComputação Distribuída do 3 ano do curso da \nLicenciatura de Engenharia Informática.\n\n\n\n");
		jTextArea1.setColumns(20);
		jTextArea1.setBounds(10, 228, 402, 104);
		this.add(jTextArea1);
		
		JLabel lblImage1 = new JLabel();
		lblImage1.setBounds(10, 10, 166, 166);
		ImageUtils.setImageByURL(lblImage1, About.class.getResource("/images/election_default.png"));
		this.add(lblImage1);
		
		JLabel lblImage2 = new JLabel();
		lblImage2.setBounds(229, 10, 166, 166);
		ImageUtils.setImageByURL(lblImage2, About.class.getResource("/images/elector_default.png"));
		this.add(lblImage2);
		
	}

}
