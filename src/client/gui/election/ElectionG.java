package client.gui.election;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.formdev.flatlaf.FlatClientProperties;

import client.app.ListCandidates;
import client.app.ListElectors;
import client.blockchain.BlockChain;
import client.app.Candidate;
import client.app.Election;
import client.app.Elector;
import client.gui.Menu;
import client.gui.vote.Vote;
import utils.ComponentUtils;
import utils.ImageUtils;

public class ElectionG extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public JTextField txtElection;
	public JLabel lblElectionImage;
	public static JButton btnStartElec;
	
	public JComboBox<Object> comboBoxDays;
	public JComboBox<Object> comboBoxMonths;
	public JComboBox<Object> comboBoxYears;
	
	public JComboBox<Object> comboBoxDays_1;
	public JComboBox<Object> comboBoxMonths_1;
	public JComboBox<Object> comboBoxYears_1;
	
	public String newImg = "";
	
	int daysBegin;
	int monthsBegin;
	int yearsBegin;
	
	int daysEnd;
	int monthsEnd;
	int yearsEnd;

	/**
	 * Create the panel.
	 */
	public ElectionG() {
		
		this.setLayout(null);
		
		GregorianCalendar calendarExpected = new GregorianCalendar();
		
		JToolBar toolBarSettings = new JToolBar();
		toolBarSettings.setFloatable(false);
		toolBarSettings.setBounds(10, 5, 100, 35);
		this.add(toolBarSettings);
		
		JButton btnOpenElection = new JButton();
		btnOpenElection.setToolTipText("Abrir eleição");
		toolBarSettings.add(btnOpenElection);
		btnOpenElection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("eleicao"));
				fileChooser.setFileFilter(new FileNameExtensionFilter("Elections Files", "elect"));
				int response = fileChooser.showOpenDialog(null);
				
				Election election = null;
				
				if(response == JFileChooser.APPROVE_OPTION) {
					String path = fileChooser.getSelectedFile().getAbsolutePath();
					
					try {
						election = new Election().load(path);
					} catch (Exception e1) {
						e1.printStackTrace();
						return;
					}
				}
				else {
					return;
				}
				
				CandidateG.modelEleCand.removeAllElements();
				CandidateG.bcEleCand = new BlockChain();
				CandidateG.elemsEleCand = new ListCandidates();
				
				for(Candidate cand : election.getLstCandidates().getList()){
					CandidateG.modelEleCand.addElement(cand);
					CandidateG.elemsEleCand.getList().add(cand);
		        }
				CandidateG.updateEleBlockchain(null);
				
				ElectorG.modelEleElector.removeAllElements();
				ElectorG.bcEleElector = new BlockChain();
				ElectorG.elemsEleElector = new ListElectors();
				
				for(Elector elector : election.getLstElectors().getList()){
					ElectorG.modelEleElector.addElement(elector);
					ElectorG.elemsEleElector.getList().add(elector);
		        }
				ElectorG.updateEleBlockchain(null);
				
				
				txtElection.setText(election.getName());
				
				String[] dateBegin = election.getBeginDate().split("/");
				
				comboBoxDays.setSelectedItem(dateBegin[0]);
				comboBoxMonths.setSelectedItem(dateBegin[1]);
				comboBoxYears.setSelectedItem(dateBegin[2]);
				
				String[] dateEnd = election.getEndDate().split("/");
				
				comboBoxDays_1.setSelectedItem(dateEnd[0]);
				comboBoxMonths_1.setSelectedItem(dateEnd[1]);
				comboBoxYears_1.setSelectedItem(dateEnd[2]);
				
				ImageUtils.setImageByIcon(lblElectionImage, election.getImage());
			}
		});
		btnOpenElection.setFocusable(false);
		ImageUtils.setIcon(btnOpenElection, Menu.class.getResourceAsStream("/images/copyOfFolder.svg"));
		
		JButton btnNewElection = new JButton();
		btnNewElection.setToolTipText("Nova eleição");
		toolBarSettings.add(btnNewElection);
		btnNewElection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				CandidateG.modelEleCand.removeAllElements();
				CandidateG.bcEleCand = new BlockChain();
				CandidateG.elemsEleCand = new ListCandidates();
				
				CandidateG.addBlank();
				
				ElectorG.modelEleElector.removeAllElements();
				ElectorG.bcEleElector = new BlockChain();
				ElectorG.elemsEleElector = new ListElectors();
				
				newImg = "";
				ImageUtils.setImageByURL(lblElectionImage, Menu.class.getResource("/images/election_default.png"));
				txtElection.setText("");
				
				GregorianCalendar calendarExpected = new GregorianCalendar();
				calendarExpected.add(Calendar.DATE, -1);
				
				comboBoxDays.setSelectedItem("" + calendarExpected.get(Calendar.DATE));
				comboBoxMonths.setSelectedItem("" + calendarExpected.get(Calendar.MONTH) + 1);
				comboBoxYears.setSelectedItem("" + calendarExpected.get(Calendar.YEAR));
				
				calendarExpected.add(Calendar.DATE, 2);
				
				comboBoxDays_1.setSelectedItem("" + calendarExpected.get(Calendar.DATE));
				comboBoxMonths_1.setSelectedItem("" + calendarExpected.get(Calendar.MONTH) + 1);
				comboBoxYears_1.setSelectedItem("" + calendarExpected.get(Calendar.YEAR));
				
			}
		});
		btnNewElection.setFocusable(false);
		ImageUtils.setIcon(btnNewElection, Menu.class.getResourceAsStream("/images/addAny.svg"));
		
		JButton btnSaveElection = new JButton();
		btnSaveElection.setToolTipText("Guardar eleição");
		toolBarSettings.add(btnSaveElection);
		btnSaveElection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(validateElection()) return;
				
				Election election = null;
				
				if(newImg.equals("")) {
					
					try {
						election = new Election(
								txtElection.getText(),
								String.format("%s/%s/%s", daysBegin, (monthsBegin+1), yearsBegin),
								String.format("%s/%s/%s", daysEnd, (monthsEnd+1), yearsEnd));
						election.setLstCandidates(CandidateG.elemsEleCand);
						election.setLstElectors(ElectorG.elemsEleElector);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
				}
				else 
				{
					
					try {
						election = new Election(
								txtElection.getText(),
								String.format("%s/%s/%s", daysBegin, (monthsBegin+1), yearsBegin),
								String.format("%s/%s/%s", daysEnd, (monthsEnd+1), yearsEnd),
								newImg);
						election.setLstCandidates(CandidateG.elemsEleCand);
						election.setLstElectors(ElectorG.elemsEleElector);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					
				}
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("eleicao"));
				fileChooser.setFileFilter(new FileNameExtensionFilter("Elections Files", "elect"));
				int response = fileChooser.showSaveDialog(null);
				
				if(response == JFileChooser.APPROVE_OPTION) {
					String path = fileChooser.getSelectedFile().getAbsolutePath();
					String extension = ".elect";

			        if (path.endsWith(extension)) {
			        	path = path.substring(0, path.length() - extension.length());
			        }
					
					String[] pathArray = path.replace("\\", "|").split("\\|");
					
					try {
						Vote.bcVotes.save("blockchain/eleicao/" + pathArray[pathArray.length - 1] + ".db");
						election.save(path + extension);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				else {
					return;
				}
				
			}
		});
		btnSaveElection.setFocusable(false);
		ImageUtils.setIcon(btnSaveElection, Menu.class.getResourceAsStream("/images/savedContext.svg"));
		
		txtElection = new JTextField();
		txtElection.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nome");
		txtElection.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		txtElection.setBounds(10, 95, 300, 35);
		this.add(txtElection);
		
		calendarExpected.add(Calendar.DATE, -1);
		
		JLabel lblBeginDate = new JLabel("Data de Início");
		lblBeginDate.setBounds(10, 140, 115, 13);
		this.add(lblBeginDate);
		
		List<String> days = new ArrayList<String>();
		for (int i = 1; i <= 31; i++) {
			days.add("" + i);
        }
		List<String> months = new ArrayList<String>();
		for (int i = 1; i <= 12; i++) {
			months.add("" + i);
        }
		List<String> years = new ArrayList<String>();
		for (int i = 2023; i >= 1900; i--) {
			years.add("" + i);
        }
		
		JToolBar toolBarDate = new JToolBar();
		toolBarDate.setBounds(10, 163, 300, 35);
		this.add(toolBarDate);
		toolBarDate.setFloatable(false);
		
		comboBoxDays = new JComboBox<Object>();
		comboBoxDays.setFocusable(false);
		toolBarDate.add(comboBoxDays);
		comboBoxDays.setModel(new DefaultComboBoxModel<Object>(days.toArray()));
		comboBoxDays.setSelectedItem("" + calendarExpected.get(Calendar.DATE));
		
		JLabel lblDateLimit1 = new JLabel("/");
		toolBarDate.add(lblDateLimit1);
		
		comboBoxMonths = new JComboBox<Object>();
		comboBoxMonths.setFocusable(false);
		toolBarDate.add(comboBoxMonths);
		comboBoxMonths.setModel(new DefaultComboBoxModel<Object>(months.toArray()));
		comboBoxMonths.setSelectedIndex(calendarExpected.get(Calendar.MONTH));
		
		JLabel lblDateLimit2 = new JLabel("/");
		toolBarDate.add(lblDateLimit2);
		
		comboBoxYears = new JComboBox<Object>();
		comboBoxYears.setFocusable(false);
		toolBarDate.add(comboBoxYears);
		comboBoxYears.setModel(new DefaultComboBoxModel<Object>(years.toArray()));
		comboBoxYears.setSelectedItem("" + calendarExpected.get(Calendar.YEAR));
		
		calendarExpected.add(Calendar.DATE, 2);
		
		JLabel lblEndDate = new JLabel("Data de Fim");
		lblEndDate.setBounds(10, 208, 115, 13);
		this.add(lblEndDate);
		
		List<String> days_1 = new ArrayList<String>();
		for (int i = 1; i <= 31; i++) {
			days_1.add("" + i);
        }
		List<String> months_1 = new ArrayList<String>();
		for (int i = 1; i <= 12; i++) {
			months_1.add("" + i);
        }
		List<String> years_1 = new ArrayList<String>();
		for (int i = 2023; i >= 1900; i--) {
			years_1.add("" + i);
        }
		
		JToolBar toolBarDate_1 = new JToolBar();
		toolBarDate_1.setBounds(10, 235, 300, 35);
		this.add(toolBarDate_1);
		toolBarDate_1.setFloatable(false);
		
		comboBoxDays_1 = new JComboBox<Object>();
		comboBoxDays_1.setFocusable(false);
		toolBarDate_1.add(comboBoxDays_1);
		comboBoxDays_1.setModel(new DefaultComboBoxModel<Object>(days_1.toArray()));
		comboBoxDays_1.setSelectedItem("" + calendarExpected.get(Calendar.DATE));
		
		JLabel lblDateLimit1_1 = new JLabel("/");
		toolBarDate_1.add(lblDateLimit1_1);
		
		comboBoxMonths_1 = new JComboBox<Object>();
		comboBoxMonths_1.setFocusable(false);
		toolBarDate_1.add(comboBoxMonths_1);
		comboBoxMonths_1.setModel(new DefaultComboBoxModel<Object>(months_1.toArray()));
		comboBoxMonths_1.setSelectedIndex(calendarExpected.get(Calendar.MONTH));
		
		JLabel lblDateLimit2_1 = new JLabel("/");
		toolBarDate_1.add(lblDateLimit2_1);
		
		comboBoxYears_1 = new JComboBox<Object>();
		comboBoxYears_1.setFocusable(false);
		toolBarDate_1.add(comboBoxYears_1);
		comboBoxYears_1.setModel(new DefaultComboBoxModel<Object>(years_1.toArray()));
		comboBoxYears_1.setSelectedItem("" + calendarExpected.get(Calendar.YEAR));
		
		lblElectionImage = new JLabel();
		lblElectionImage.setBounds(60, 320, 200, 200);
		lblElectionImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				newImg = ImageUtils.changeImage(lblElectionImage);
			}
		});
		ImageUtils.setImageByURL(lblElectionImage, Menu.class.getResource("/images/election_default.png"));
		this.add(lblElectionImage);
		
		JLabel lblCandElec = new JLabel("Candidatos");
		lblCandElec.setBounds(390, 48, 157, 25);
		this.add(lblCandElec);
		
		JScrollPane scrollPaneCand = new JScrollPane();
		scrollPaneCand.setBounds(390, 83, 360, 519);
		this.add(scrollPaneCand);
		
		JList<Candidate> listCand = new JList<Candidate>();
		listCand.setModel(CandidateG.modelEleCand);
		listCand.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listCand.setEnabled(false);
		scrollPaneCand.setViewportView(listCand);
		
		JLabel lblElectorsElec = new JLabel("Eleitores");
		lblElectorsElec.setBounds(780, 48, 157, 25);
		this.add(lblElectorsElec);
		
		JScrollPane scrollPaneElector = new JScrollPane();
		scrollPaneElector.setBounds(780, 83, 360, 519);
		this.add(scrollPaneElector);
		
		JList<Elector> listElector = new JList<Elector>();
		listElector.setModel(ElectorG.modelEleElector);
		listElector.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listElector.setEnabled(false);
		scrollPaneElector.setViewportView(listElector);
		
		btnStartElec = new JButton("Criar Eleição");
		btnStartElec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Thread save = new Thread(() -> {
					if(validateElection()) return;
					
					if(!Vote.verifyConnection("criar")) {
						return;
					}
					
					CandidateG.newImg = "";
					ImageUtils.setImageByURL(CandidateG.lblCandImage, Menu.class.getResource("/images/candidate_default.png"));
					CandidateG.txtCandName.setText("");
					CandidateG.txtCandID.setText("");
					
					ElectorG.newImg = "";
					ImageUtils.setImageByURL(ElectorG.lblElectorImage, Menu.class.getResource("/images/elector_default.png"));
					ElectorG.txtElectorName.setText("");
					ElectorG.txtElectorCC.setText("");
					
					ElectorG.comboBoxSex.setSelectedIndex(0);
					
					ElectorG.comboBoxDays.setSelectedIndex(0);
					ElectorG.comboBoxMonths.setSelectedIndex(0);
					ElectorG.comboBoxYears.setSelectedIndex(0);
					
					ElectorG.passwordField1.setText("");
					ElectorG.passwordField2.setText("");
					
					Election election = null;
					
					if(newImg.equals("")) {
						
						try {
							election = new Election(
									txtElection.getText(),
									String.format("%s/%s/%s", daysBegin, (monthsBegin+1), yearsBegin),
									String.format("%s/%s/%s", daysEnd, (monthsEnd+1), yearsEnd));
							election.setLstCandidates(CandidateG.elemsEleCand);
							election.setLstElectors(ElectorG.elemsEleElector);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						
					}
					else 
					{
						
						try {
							election = new Election(
									txtElection.getText(),
									String.format("%s/%s/%s", daysBegin, (monthsBegin+1), yearsBegin),
									String.format("%s/%s/%s", daysEnd, (monthsEnd+1), yearsEnd),
									newImg);
							election.setLstCandidates(CandidateG.elemsEleCand);
							election.setLstElectors(ElectorG.elemsEleElector);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						
					}
					
					Vote.election = election;
					Vote.path = "default/temp";
					Vote.loadElection(false);
					
					Menu.votingTabbedPane.setSelectedIndex(0);
					Menu.mainTabbedPane.setSelectedIndex(0);
					
					CandidateG.modelEleCand.removeAllElements();
					CandidateG.bcEleCand = new BlockChain();
					CandidateG.elemsEleCand = new ListCandidates();
					
					CandidateG.addBlank();
					
					ElectorG.modelEleElector.removeAllElements();
					ElectorG.bcEleElector = new BlockChain();
					ElectorG.elemsEleElector = new ListElectors();
					
					newImg = "";
					ImageUtils.setImageByURL(lblElectionImage, Menu.class.getResource("/images/election_default.png"));
					txtElection.setText("");
					
					GregorianCalendar calendarExpected = new GregorianCalendar();
					calendarExpected.add(Calendar.DATE, -1);
					
					comboBoxDays.setSelectedItem("" + calendarExpected.get(Calendar.DATE));
					comboBoxMonths.setSelectedIndex(calendarExpected.get(Calendar.MONTH));
					comboBoxYears.setSelectedItem("" + calendarExpected.get(Calendar.YEAR));
					
					calendarExpected.add(Calendar.DATE, 2);
					
					comboBoxDays_1.setSelectedItem("" + calendarExpected.get(Calendar.DATE));
					comboBoxMonths_1.setSelectedIndex(calendarExpected.get(Calendar.MONTH));
					comboBoxYears_1.setSelectedItem("" + calendarExpected.get(Calendar.YEAR));
				});
				
				save.start();
			}
		});
		btnStartElec.setBounds(10, 572, 160, 30);
		ImageUtils.setIcon(btnStartElec, Menu.class.getResourceAsStream("/images/threadGroupCurrent.svg"));
		this.add(btnStartElec);
		
	}
	
	public boolean validateElection() {
		if (ComponentUtils.isValid(txtElection)) { 
			JOptionPane.showMessageDialog(Menu.frmFrame, "Nome Inválido", "Erro", JOptionPane.ERROR_MESSAGE, null);
			return true;
		}
		
		daysBegin = Integer.parseInt((String) comboBoxDays.getSelectedItem());
		monthsBegin = comboBoxMonths.getSelectedIndex();
		yearsBegin = Integer.parseInt((String) comboBoxYears.getSelectedItem());
		
		GregorianCalendar calendarExpectedBegin = new GregorianCalendar(yearsBegin, monthsBegin, daysBegin);
		
		if(calendarExpectedBegin.get(Calendar.DATE) != daysBegin || calendarExpectedBegin.get(Calendar.MONTH) != monthsBegin || calendarExpectedBegin.get(Calendar.YEAR) != yearsBegin) {
			JOptionPane.showMessageDialog(Menu.frmFrame, "Data de Início inválida", "Erro", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		
		daysEnd = Integer.parseInt((String) comboBoxDays_1.getSelectedItem());
		monthsEnd = comboBoxMonths_1.getSelectedIndex();
		yearsEnd = Integer.parseInt((String) comboBoxYears_1.getSelectedItem());
		
		GregorianCalendar calendarExpectedEnd = new GregorianCalendar(yearsEnd, monthsEnd, daysEnd);
		
		if(calendarExpectedEnd.get(Calendar.DATE) != daysEnd || calendarExpectedEnd.get(Calendar.MONTH) != monthsEnd || calendarExpectedEnd.get(Calendar.YEAR) != yearsEnd) {
			JOptionPane.showMessageDialog(Menu.frmFrame, "Data de Fim inválida", "Erro", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		if(calendarExpectedBegin.getTimeInMillis() == calendarExpectedEnd.getTimeInMillis()) {
			JOptionPane.showMessageDialog(Menu.frmFrame, "Data de Início é igual à Data de Fim", "Erro", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		if(calendarExpectedBegin.getTimeInMillis() > calendarExpectedEnd.getTimeInMillis()) {
			JOptionPane.showMessageDialog(Menu.frmFrame, "Data de Fim é antes da Data de Início", "Erro", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		
		if(CandidateG.elemsEleCand.getList().isEmpty() || 
				(CandidateG.elemsEleCand.getList().size() == 1 && CandidateG.elemsEleCand.getList().get(0).getCode() == "BRANCO")) {
			JOptionPane.showMessageDialog(Menu.frmFrame, "Número insuficiente de Candidatos", "Erro", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		if(ElectorG.elemsEleElector.getList().isEmpty()) {
			JOptionPane.showMessageDialog(Menu.frmFrame, "Número insuficiente de Eleitores", "Erro", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		
		return false;
	}
	
}
