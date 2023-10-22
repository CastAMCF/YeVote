package gui.vote;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.formdev.flatlaf.FlatClientProperties;

import app.Candidate;
import app.Election;
import app.Elector;
import app.ListElectors;
import app.ListVotes;
import app.Votes;
import blockchain.BlockChain;
import blockchain.MerkleTreeString;
import gui.Menu;
import gui.results.Graph;
import gui.results.VoteText;
import gui.results.Voters;
import utils.ImageUtils;

public class Vote extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private static JTextField textBeginDate;
	private static JTextField textEndDate;
	private static JTextField txtElection;
	private static JLabel lblElectionImage;
	private static JPanel candidatesButsPanel;
	
	public static Election election;
	
	public static ListVotes listVotes = new ListVotes();
	public static BlockChain bcVotes = new BlockChain();
	
	public static app.Elector loggedElector = null;
	public static String loggedElectorPass;
	
	public static String path = "default";
	
	/**
	 * Create the panel.
	 */
	public Vote() {
		
		this.setLayout(null);
		
		JLabel lblName = new JLabel("Nome");
		lblName.setBounds(10, 125, 115, 13);
		this.add(lblName);
		lblName.setLabelFor(txtElection);
		
		txtElection = new JTextField();
		txtElection.setBounds(10, 143, 300, 35);
		this.add(txtElection);
		txtElection.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		txtElection.setFocusable(false);
		txtElection.setEditable(false);
		
		JLabel lblBeginDate = new JLabel("Data de Início");
		lblBeginDate.setBounds(10, 199, 115, 13);
		this.add(lblBeginDate);
		lblBeginDate.setLabelFor(textBeginDate);
		
		textBeginDate = new JTextField();
		textBeginDate.setBounds(10, 217, 300, 35);
		textBeginDate.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		textBeginDate.setEditable(false);
		textBeginDate.setFocusable(false);
		textBeginDate.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON, ImageUtils.FlatSVGIcon(Menu.class.getResourceAsStream("/images/DataTables.svg")));
		this.add(textBeginDate);
		
		JLabel lblEndDate = new JLabel("Data de Fim");
		lblEndDate.setBounds(10, 273, 115, 13);
		this.add(lblEndDate);
		lblEndDate.setLabelFor(textEndDate);
		
		textEndDate = new JTextField();
		textEndDate.setBounds(10, 291, 300, 35);
		textEndDate.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		textEndDate.setEditable(false);
		textEndDate.setFocusable(false);
		textEndDate.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON, ImageUtils.FlatSVGIcon(Menu.class.getResourceAsStream("/images/DataTables.svg")));
		this.add(textEndDate);
		
		lblElectionImage = new JLabel();
		lblElectionImage.setBounds(60, 357, 200, 200);
		this.add(lblElectionImage);
		
		JLabel lblVoteList = new JLabel("Boletim");
		lblVoteList.setBounds(368, 20, 115, 13);
		this.add(lblVoteList);
		
		JLabel lblVoteListCheck = new JLabel();
		lblVoteListCheck.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		lblVoteListCheck.setBounds(368, 42, 148, 35);
		this.add(lblVoteListCheck);
		
		JLabel lblVoteCandidate = new JLabel();
		lblVoteCandidate.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblVoteCandidate.setBounds(526, 42, 389, 35);
		this.add(lblVoteCandidate);
		
		JPanel candidatesPanel = new JPanel();
		candidatesPanel.setBounds(368, 87, 735, 520);
		this.add(candidatesPanel);
		candidatesPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		candidatesPanel.add(scrollPane);
		
		candidatesButsPanel = new JPanel();
		scrollPane.setViewportView(candidatesButsPanel);
		GridBagLayout gbl_candidatesButsPanel = new GridBagLayout();
		gbl_candidatesButsPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_candidatesButsPanel.rowHeights = new int[] {0, 15, 0};
		gbl_candidatesButsPanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_candidatesButsPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		candidatesButsPanel.setLayout(gbl_candidatesButsPanel);
		
		JToolBar toolBarSettings = new JToolBar();
		toolBarSettings.setFloatable(false);
		toolBarSettings.setBounds(10, 5, 100, 35);
		this.add(toolBarSettings);
		
		JButton btnOpenElection = new JButton();
		btnOpenElection.setToolTipText("Abrir eleição");
		toolBarSettings.add(btnOpenElection);
		btnOpenElection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				loadElection();
			}
		});
		btnOpenElection.setFocusable(false);
		ImageUtils.setIcon(btnOpenElection, Menu.class.getResourceAsStream("/images/copyOfFolder.svg"));
		
		JButton btnSaveElection = new JButton();
		btnSaveElection.setToolTipText("Guardar eleição");
		toolBarSettings.add(btnSaveElection);
		btnSaveElection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				saveElection();
			}
		});
		btnSaveElection.setFocusable(false);
		ImageUtils.setIcon(btnSaveElection, Menu.class.getResourceAsStream("/images/savedContext.svg"));
		
	}
	
	private static void saveElection() {
		
		if(path.equals("default/temp")) {
			new Thread(() -> {
				election.setLstVotes(listVotes);
				try {
					bcVotes.save(path + ".db");
					election.save(path + ".elect");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}).start();
			return;
		}
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("eleicao"));
		fileChooser.setFileFilter(new FileNameExtensionFilter("Elections Files", "elect"));
		int response = fileChooser.showSaveDialog(null);
		
		if(response == JFileChooser.APPROVE_OPTION) {
			path = fileChooser.getSelectedFile().getAbsolutePath();
			String extension = ".elect";

	        if (path.endsWith(extension)) {
	        	path = path.substring(0, path.length() - extension.length());
	        }
			
			String[] pathArray = path.replace("\\", "|").split("\\|");
			
			new Thread(() -> {
				election.setLstVotes(listVotes);
				try {
					bcVotes.save("blockchain/eleicao/" + pathArray[pathArray.length - 1] + ".db");
					election.save(path + extension);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}).start();
		}
		else {
			return;
		}
		
	}
	
	public static void loadElection() {
		Menu.mainTabbedPane.setEnabledAt(2, false);
		
		new Thread(() -> {
			if(!path.equals("default/temp")) {

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("eleicao"));
				fileChooser.setFileFilter(new FileNameExtensionFilter("Elections Files", "elect"));
				int response = fileChooser.showOpenDialog(null);
				
				if(response == JFileChooser.APPROVE_OPTION) {
					path = fileChooser.getSelectedFile().getAbsolutePath();
					
					try {
						election = new Election().load(fileChooser.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				else {
					return;
				}
			}
			
			String[] beginDate = election.getBeginDate().split("/");
			GregorianCalendar calendarBegin = new GregorianCalendar(Integer.parseInt(beginDate[2]), Integer.parseInt(beginDate[1]) - 1, Integer.parseInt(beginDate[0]));
			String[] endDate = election.getEndDate().split("/");
			GregorianCalendar calendarEnd = new GregorianCalendar(Integer.parseInt(endDate[2]), Integer.parseInt(endDate[1]) - 1, Integer.parseInt(endDate[0]));
			GregorianCalendar calendarToday = new GregorianCalendar(2024, 1, 1);
			
			if(calendarToday.getTimeInMillis() < calendarBegin.getTimeInMillis()) {
				JOptionPane.showMessageDialog(Menu.frmFrame, "Eleição ainda não começou. Eleição começa a " + election.getBeginDate(), "Informação", JOptionPane.WARNING_MESSAGE, null);
				Menu.mainTabbedPane.setEnabledAt(2, true);
				election = null;
				return;
			}
			
			if(election.getLstVotes() != null) {
				try {
					String extension = ".elect";

			        if (path.endsWith(extension)) {
			        	path = path.substring(0, path.length() - extension.length());
			        }
					
					String[] pathArray = path.replace("\\", "|").split("\\|");
					
					if(path.endsWith("default\\temp")) {
						bcVotes.load("default/" + pathArray[pathArray.length - 1] + ".db");
					}
					else {
						bcVotes.load("blockchain/eleicao/" + pathArray[pathArray.length - 1] + ".db");
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(Menu.frmFrame, "Não foi possível encontrar a base de dados desta lista", "Erro", JOptionPane.ERROR_MESSAGE, null);
					return;
				}
				
				listVotes = election.getLstVotes();
				
				for(Votes vote : listVotes.getList()) {
					try {
						if(!vote.verify()) {
							JOptionPane.showMessageDialog(Menu.frmFrame, "Votos corrumpidos", "Erro", JOptionPane.ERROR_MESSAGE, null);
							return;
						}
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				
			}
			
			//===================== Resultados =====================
			
			if(calendarToday.getTimeInMillis() > calendarEnd.getTimeInMillis()) {
				
				HashMap<Candidate, Integer> hashMap = new HashMap<Candidate, Integer>();
				
				for(Candidate cand : election.getLstCandidates().getList()){
					//System.out.println(listVotes.voteCheck(cand.getCode()));
					hashMap.put(cand, listVotes.voteCheck(cand.getCode()));
		        }
				
				Graph.loadResults(hashMap);
				VoteText.loadResults(hashMap);
				
				if(election.getLstVotes() != null) {
					Voters.modelElectors.addAll(election.getLstVotes().getElectors());
					Voters.elemsElectors.getList().addAll(election.getLstVotes().getElectors());
				}
				
				Menu.mainTabbedPane.setEnabledAt(2, true);
			}
			else 
			{
				loadCandidates(election.getLstCandidates().getList());
			}
			
			txtElection.setText(election.getName());
			textBeginDate.setText(election.getBeginDate());
			textEndDate.setText(election.getEndDate());
			
			ImageUtils.setImageByIcon(lblElectionImage, election.getImage());
			
			Electors.modelElectors.removeAllElements();
			Electors.elemsElectors = new ListElectors();
			
			for(Elector elector : election.getLstElectors().getList()){
				Electors.modelElectors.addElement(elector);
				Electors.elemsElectors.getList().add(elector);
	        }
			
		}).start();
	}
	
	private static void updateBlockchain() {
		new Thread(() -> {
			ArrayList<String> arrayList = new ArrayList<String>();
			
			for(Votes vote : listVotes.getList()) {
				try {
					/*
					arrayList.add(Base64.getEncoder().encodeToString(
							Asymmetric.encryptionRSA(
									vote.sign(loggedElectorPass).getBytes()
									, election.getPrivKey()))
							);
					*/
					arrayList.add(Base64.getEncoder().encodeToString(vote.getSignature().getBytes()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			try {
				MerkleTreeString mt = new MerkleTreeString(arrayList);
				bcVotes.add(mt.getRoot(), 4);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

	}
	
	private static void loadCandidates(ArrayList<Candidate> arrayList) {
		candidatesButsPanel.removeAll();
		GridBagConstraints gbc = new GridBagConstraints();
		
		int num = arrayList.size();
	    float limit = num / 4f;
	    int numCheck = 0;
	    
	    for (int y = 0; y < limit; y++) {
	    	for (int x = 0; x < 4; x++) {
	    		
	    		final int currentNumCheck = numCheck;
	    		JButton button = new JButton(arrayList.get(currentNumCheck).getCode() + " - " + arrayList.get(currentNumCheck).getName());
	    		
	    		button.addActionListener(new ActionListener() {
	    			public void actionPerformed(ActionEvent e) {
	    				
	    				String[] options = new String[2];
        				options[0] = new String("Sim");
        				options[1] = new String("Não");
        				
                    	int voteOP = JOptionPane.showOptionDialog(Menu.frmFrame, 
                    			"Tem a certeza que votar no(a) " + arrayList.get(currentNumCheck).getCode() + " - " + arrayList.get(currentNumCheck).getName(),
                    			"Confirmação", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, e);
                    	
                    	if (voteOP == 0) {
		    				//System.out.println(arrayList.get(currentNumCheck).getCode());
		    				Votes vote = new Votes(loggedElector, arrayList.get(currentNumCheck));
		    				try {
		    					listVotes.add(vote);
		    					vote.sign(loggedElectorPass);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
		    				
		    				updateBlockchain();
		    				
		    				saveElection();
                    	}
	    			}
	    		});
	            
	            button.setHorizontalTextPosition(SwingConstants.CENTER);
	            button.setVerticalTextPosition(SwingConstants.BOTTOM);
	            button.setHorizontalAlignment(SwingConstants.CENTER);
	            button.setVerticalAlignment(SwingConstants.CENTER);
	            button.setFocusable(false);
	            
	            ImageIcon ico = arrayList.get(currentNumCheck).getImage();
	    		ico.setImage(ico.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
	            button.setIcon(ico);

	            gbc.gridx = x;
	            gbc.gridy = y;
	            gbc.gridwidth = 1;
	            
	            candidatesButsPanel.add(button, gbc);
	    		
	            numCheck++;
	            
	            if(numCheck == num) break;
		    }
	    }
	}
}
