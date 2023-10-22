package gui;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.extras.components.FlatButton;
import com.formdev.flatlaf.extras.components.FlatButton.ButtonType;
import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;

import com.formdev.flatlaf.intellijthemes.FlatArcIJTheme;

import app.ListCandidates;
import app.ListElectors;
import gui.election.Candidate;
import gui.election.Election;
import gui.election.Elector;
import gui.results.Graph;
import gui.results.VoteText;
import gui.vote.Electors;
import gui.vote.Vote;
import utils.ImageUtils;

import java.awt.Toolkit;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.Box;
import javax.swing.JTabbedPane;
import java.awt.GridLayout;

public class Menu {
	
	@SuppressWarnings("exports")
	public static JFrame frmFrame;
	@SuppressWarnings("exports")
	public static JTabbedPane mainTabbedPane;
	@SuppressWarnings("exports")
	public static JTabbedPane votingTabbedPane;
	
	ListCandidates lstCands = new ListCandidates();
	ListElectors lstElectors = new ListElectors();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 15));
			FlatNordIJTheme.setup();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (!Files.exists(Paths.get("default"))) {
			new File("default").mkdirs();
		}
		if (!Files.exists(Paths.get("blockchain/candidatos"))) {
			new File("blockchain/candidatos").mkdirs();
		}
		if (!Files.exists(Paths.get("blockchain/eleicao"))) {
			new File("blockchain/eleicao").mkdirs();
		}
		if (!Files.exists(Paths.get("blockchain/eleitores"))) {
			new File("blockchain/eleitores").mkdirs();
		}
		if (!Files.exists(Paths.get("candidatos"))) {
			new File("candidatos").mkdirs();
		}
		if (!Files.exists(Paths.get("eleicao"))) {
			new File("eleicao").mkdirs();
		}
		if (!Files.exists(Paths.get("eleitores"))) {
			new File("eleitores").mkdirs();
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Menu();
					Menu.frmFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Menu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFrame = new JFrame();
		frmFrame.setTitle("YeVote");
		frmFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(Menu.class.getResource("/images/logo.png")));
		frmFrame.setBounds(100, 100, 1280, 720);
		frmFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFrame.setLocationRelativeTo(null);
		
		frmFrame.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));
		mainTabbedPane = new JTabbedPane(SwingConstants.LEFT);
		frmFrame.getContentPane().add(mainTabbedPane);
		
		JMenuBar menuBar = new JMenuBar();
		frmFrame.setJMenuBar(menuBar);
		
		JMenu mnAppMenu = new JMenu("Aplicação");
		menuBar.add(mnAppMenu);
		
		JMenuItem mntmVotingMenuItem = new JMenuItem("Votar");
		mntmVotingMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		ImageUtils.setIcon(mntmVotingMenuItem, Menu.class.getResourceAsStream("/images/ProfileTracing.svg"));
		mnAppMenu.add(mntmVotingMenuItem);
		
		JMenuItem mntmConfigMenuItem = new JMenuItem("Criar Eleição");
		mntmConfigMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		ImageUtils.setIcon(mntmConfigMenuItem, Menu.class.getResourceAsStream("/images/editorconfig.svg"));
		mnAppMenu.add(mntmConfigMenuItem);
		
		JMenuItem mntmResultsMenuItem = new JMenuItem("Resultados");
		mntmResultsMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		ImageUtils.setIcon(mntmResultsMenuItem, Menu.class.getResourceAsStream("/images/properties.svg"));
		mnAppMenu.add(mntmResultsMenuItem);
		
		JSeparator separator = new JSeparator();
		mnAppMenu.add(separator);
		
		JMenuItem mntmQuitMenuItem = new JMenuItem("Sair");
		mntmQuitMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		ImageUtils.setIcon(mntmQuitMenuItem, Menu.class.getResourceAsStream("/images/close.svg"));
		mnAppMenu.add(mntmQuitMenuItem);
		
		JMenu mnHelpMenu = new JMenu("Ajuda");
		menuBar.add(mnHelpMenu);
		
		JMenuItem mntmAboutMenuItem = new JMenuItem("Sobre");
		ImageUtils.setIcon(mntmAboutMenuItem, Menu.class.getResourceAsStream("/images/info.svg"));
		mntmAboutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JOptionPane.showMessageDialog(Menu.frmFrame, new About(), "Sobre", JOptionPane.PLAIN_MESSAGE);

			}
		});
		mnHelpMenu.add(mntmAboutMenuItem);
		
		FlatButton usersButton = new FlatButton();
		usersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPopupMenu popupMenu = new JPopupMenu();
				
				JMenuItem login = new JMenuItem("Entrar");
				JMenuItem logout = new JMenuItem("Sair");
				
				if(Vote.loggedElector == null) {
					
					login.addActionListener(new ActionListener() {
	                    public void actionPerformed(ActionEvent e) {
	                    	
	                    	if(Vote.election == null) {
	    						JOptionPane.showMessageDialog(Menu.frmFrame, "Não há uma Eleição ativa", "Erro", JOptionPane.ERROR_MESSAGE, null);
	    						return;
	    					}
	                    	
	                    	String[] options = new String[2];
	        				options[0] = new String("Entrar");
	        				options[1] = new String("Cancelar");
	        				
	                    	int login = JOptionPane.showOptionDialog(Menu.frmFrame, new Login(), "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, e);
	                    	
	                    	if (login == 0) {
	                    		if(Vote.election.getLstElectors().exists(Login.txtElectorCC.getText())) {
	                    			
	                    			Vote.loggedElector = Vote.election.getLstElectors().get(Login.txtElectorCC.getText());
	                    			
	                    			try {
	                    				Vote.loggedElector.getPrivKey(String.valueOf(Login.passwordField1.getPassword()));
	                    			} catch (Exception e1) {
	                    				JOptionPane.showMessageDialog(Menu.frmFrame, "Utilizador ou Password errados", "Erro", JOptionPane.ERROR_MESSAGE, null);
	            						return;
	                    			}
	                    			Vote.loggedElectorPass = String.valueOf(Login.passwordField1.getPassword());
	                    			
	                    			ImageUtils.setIcon(usersButton, Menu.class.getResourceAsStream("/images/loggedUser.svg"));
	                    		}
	                    		else 
	                    		{
	                    			JOptionPane.showMessageDialog(Menu.frmFrame, "Utilizador ou Password errados", "Erro", JOptionPane.ERROR_MESSAGE, null);
	        						return;
	                    		}
	        				}
	                    }
	                });
	                popupMenu.add(login);
					
				}
				else 
				{
					logout.addActionListener(new ActionListener() {
	                    public void actionPerformed(ActionEvent e) {
	                    	
	                    	Vote.loggedElector = null;
	                    	Vote.loggedElectorPass = "";
	                    	
	                    	ImageUtils.setIcon(usersButton, Menu.class.getResourceAsStream("/images/notLoggedUser.svg"));
	                    }
		            });
					popupMenu.add(logout);
				}
                
				popupMenu.show(usersButton, 0, usersButton.getHeight());
			}
		});
		ImageUtils.setIcon(usersButton, Menu.class.getResourceAsStream("/images/notLoggedUser.svg"));
		usersButton.setButtonType(ButtonType.toolBarButton);
		usersButton.setFocusable(false);
		menuBar.add(Box.createGlue());
		menuBar.add(usersButton);
		
		FlatButton themeButton = new FlatButton();
		themeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if (FlatLaf.isLafDark()) {
					EventQueue.invokeLater(() -> {
						FlatAnimatedLafChange.showSnapshot();
						FlatArcIJTheme.setup();
						FlatLaf.updateUI();
						FlatAnimatedLafChange.hideSnapshotWithAnimation();
						ImageUtils.setIcon(themeButton, Menu.class.getResourceAsStream("/images/darkTheme.svg"));
						themeButton.setToolTipText("Tema Escuro");
					});
				} else {
					EventQueue.invokeLater(() -> {
						FlatAnimatedLafChange.showSnapshot();
						FlatNordIJTheme.setup();
						FlatLaf.updateUI();
						FlatAnimatedLafChange.hideSnapshotWithAnimation();
						ImageUtils.setIcon(themeButton, Menu.class.getResourceAsStream("/images/lightTheme_dark.svg"));
						themeButton.setToolTipText("Tema Claro");
					});
				}
				
			}
		});
		ImageUtils.setIcon(themeButton, Menu.class.getResourceAsStream("/images/lightTheme_dark.svg"));
		themeButton.setButtonType(ButtonType.toolBarButton);
		themeButton.setToolTipText("Tema Claro");
		themeButton.setFocusable(false);
		menuBar.add(themeButton);
		
		//===========================[[[ Votar ]]]===========================
		
		JPanel votingPanel = new JPanel();
		mainTabbedPane.addTab("Votar", null, votingPanel, null);
		votingPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		votingTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		votingPanel.add(votingTabbedPane);
		
		//============================== Eleição ==============================
		
		JPanel voteElectionPanel = new Vote();
		votingTabbedPane.addTab("Eleição", null, voteElectionPanel, null);
		
		//============================== Eleitores ==============================
		
		JPanel voteElectosListpanel = new Electors();
		votingTabbedPane.addTab("Eleitores", null, voteElectosListpanel, null);
		
		//===========================[[[ Eleição ]]]===========================
		
		JPanel electionsPanel = new JPanel();
		mainTabbedPane.addTab("Criar Eleição", null, electionsPanel, null);
		electionsPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		electionsPanel.add(tabbedPane);
		
		//============================== Eleição ==============================
		
		JPanel electionPanel = new Election();
		tabbedPane.addTab("Eleição", null, electionPanel, null);
		
		//============================== Candidatos ==============================
		
		JPanel candidatesPanel = new Candidate();
		tabbedPane.addTab("Candidatos", null, candidatesPanel, null);
		
		//============================== Eleitores ==============================
		
		JPanel electorsPanel = new Elector();
		tabbedPane.addTab("Eleitores", null, electorsPanel, null);
		
		//===========================[[[ Resultados ]]]===========================
		
		JPanel resultsPanel = new JPanel();
		mainTabbedPane.addTab("Resultados", null, resultsPanel, null);
		resultsPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		resultsPanel.add(tabbedPane_1);
		
		//============================== Gráfico ==============================
		
		JPanel graphPanel = new Graph();
		tabbedPane_1.addTab("Gráfico", null, graphPanel, null);
		
		//============================== Votos ==============================
		
		JPanel votePanel = new VoteText();
		tabbedPane_1.addTab("Votos", null, votePanel, null);
		
		//============================== Eleitores ==============================
		
		JPanel elecPanel = new gui.results.Voters();
		tabbedPane_1.addTab("Eleitores", null, elecPanel, null);
		
	}
}
