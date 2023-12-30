package client.gui.election;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.formdev.flatlaf.FlatClientProperties;

import client.app.Candidate;
import client.app.Elector;
import client.app.ListCandidates;
import client.blockchain.BlockChain;
import client.blockchain.MerkleTreeString;
import client.gui.Menu;
import utils.ComponentUtils;
import utils.ImageUtils;

public class CandidateG extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public static JTextField txtCandID;
	public static JTextField txtCandName;
	public static JLabel lblCandImage;
	
	public static String newImg = "";

	public static DefaultListModel<Candidate> modelAvaiCand = new DefaultListModel<Candidate>();
	public static DefaultListModel<Candidate> modelEleCand = new DefaultListModel<Candidate>();
	
	public static BlockChain bcAvaiCand = new BlockChain();
	public static BlockChain bcEleCand = new BlockChain();
	
	public static ListCandidates elemsAvaiCand = new ListCandidates();
	public static ListCandidates elemsEleCand = new ListCandidates();
	
	static Thread thEleCh;
	static Thread thAvaiCh;
	
	/**
	 * Create the panel.
	 */
	public CandidateG() {
		
		this.setLayout(null);
		
		JToolBar toolBarSettings = new JToolBar();
		toolBarSettings.setFloatable(false);
		toolBarSettings.setBounds(10, 5, 100, 35);
		this.add(toolBarSettings);
		
		JButton btnOpenCandsList = new JButton();
		btnOpenCandsList.setToolTipText("Abrir lista");
		toolBarSettings.add(btnOpenCandsList);
		btnOpenCandsList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("candidatos"));
				fileChooser.setFileFilter(new FileNameExtensionFilter("Candidates Files", "cand"));
				int response = fileChooser.showOpenDialog(null);
				
				if(response == JFileChooser.APPROVE_OPTION) {
					String path = fileChooser.getSelectedFile().getAbsolutePath();
					String extension = ".cand";

			        if (path.endsWith(extension)) {
			        	path = path.substring(0, path.length() - extension.length());
			        }
					
					String[] pathArray = path.replace("\\", "|").split("\\|");
					
					try {
						bcAvaiCand.load("blockchain/candidatos/" + pathArray[pathArray.length - 1] + ".db");
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(Menu.frmFrame, "Não foi possível encontrar a base de dados desta lista", "Erro", JOptionPane.ERROR_MESSAGE, null);
						return;
					}
					
					try {
						elemsAvaiCand.load(fileChooser.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				else {
					return;
				}
				
				MerkleTreeString mt = new MerkleTreeString(elemsAvaiCand.getList());
				
				//System.out.println(mt.getRoot());
				//System.out.println(bcAvaiCand.getChain().get(bcAvaiCand.getChain().size() - 1).getData());
				
				if(!mt.getRoot().equals(bcAvaiCand.getChain().get(bcAvaiCand.getChain().size() - 1).getData())) {
					JOptionPane.showMessageDialog(Menu.frmFrame, "A lista está corrumpida", "Erro", JOptionPane.ERROR_MESSAGE, null);
					return;
				}
				
				modelAvaiCand.removeAllElements();
				
				for(Candidate cand : elemsAvaiCand.getList()){
					modelAvaiCand.addElement(cand);
		        }
			}
		});
		btnOpenCandsList.setFocusable(false);
		ImageUtils.setIcon(btnOpenCandsList, Menu.class.getResourceAsStream("/images/copyOfFolder.svg"));
		
		JButton btnNewCandsList = new JButton();
		btnNewCandsList.setToolTipText("Nova lista");
		toolBarSettings.add(btnNewCandsList);
		btnNewCandsList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modelAvaiCand.removeAllElements();
				modelEleCand.removeAllElements();
				
				bcAvaiCand = new BlockChain();
				bcEleCand = new BlockChain();
				
				elemsAvaiCand = new ListCandidates();
				elemsEleCand = new ListCandidates();
				
				addBlank();
				
				txtCandName.setText("");
				txtCandID.setText("");
				
				newImg = "";
				ImageUtils.setImageByURL(lblCandImage, Menu.class.getResource("/images/candidate_default.png"));
			}
		});
		btnNewCandsList.setFocusable(false);
		ImageUtils.setIcon(btnNewCandsList, Menu.class.getResourceAsStream("/images/addAny.svg"));
		
		JButton btnSaveCandsList = new JButton();
		btnSaveCandsList.setToolTipText("Guardar lista");
		toolBarSettings.add(btnSaveCandsList);
		btnSaveCandsList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("candidatos"));
				fileChooser.setFileFilter(new FileNameExtensionFilter("Candidates Files", "cand"));
				int response = fileChooser.showSaveDialog(null);
				
				if(response == JFileChooser.APPROVE_OPTION) {
					String path = fileChooser.getSelectedFile().getAbsolutePath();
					String extension = ".cand";

			        if (path.endsWith(extension)) {
			        	path = path.substring(0, path.length() - extension.length());
			        }
					
					String[] pathArray = path.replace("\\", "|").split("\\|");
					
					try {
						bcAvaiCand.save("blockchain/candidatos/" + pathArray[pathArray.length - 1] + ".db");
						elemsAvaiCand.save(path + extension);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				else {
					return;
				}
				
			}
		});
		btnSaveCandsList.setFocusable(false);
		ImageUtils.setIcon(btnSaveCandsList, Menu.class.getResourceAsStream("/images/savedContext.svg"));
		
		txtCandName = new JTextField();
		txtCandName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nome");
		txtCandName.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		txtCandName.setBounds(10, 95, 300, 35);
		this.add(txtCandName);
		
		txtCandID = new JTextField();
		txtCandID.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Sigla");
		txtCandID.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		txtCandID.setBounds(10, 180, 300, 35);
		this.add(txtCandID);
		
		lblCandImage = new JLabel();
		lblCandImage.setBounds(60, 315, 200, 200);
		lblCandImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				newImg = ImageUtils.changeImage(lblCandImage);
			}
		});
		ImageUtils.setImageByURL(lblCandImage, Menu.class.getResource("/images/candidate_default.png"));
		this.add(lblCandImage);
		
		JLabel lblAvaiCand = new JLabel("Disponível");
		lblAvaiCand.setBounds(390, 48, 157, 25);
		this.add(lblAvaiCand);
		
		JScrollPane scrollPaneAvaiCand = new JScrollPane();
		scrollPaneAvaiCand.setBounds(390, 83, 320, 519);
		this.add(scrollPaneAvaiCand);
		
		JList<Candidate> listAvaiCand = new JList<Candidate>();
		listAvaiCand.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && listAvaiCand.getSelectedValue() != null) {
					Candidate cand = (Candidate) listAvaiCand.getSelectedValue();
					
					txtCandName.setText(cand.getName());
					txtCandID.setText(cand.getCode());
					
					ImageUtils.setImageByIcon(lblCandImage, cand.getImage());
				}
			}
		});
		listAvaiCand.setModel(modelAvaiCand);
		listAvaiCand.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneAvaiCand.setViewportView(listAvaiCand);
		
		JLabel lblEleCand = new JLabel("Eleição");
		lblEleCand.setBounds(820, 48, 157, 25);
		this.add(lblEleCand);
		
		JScrollPane scrollPaneEleCand = new JScrollPane();
		scrollPaneEleCand.setBounds(820, 83, 320, 519);
		this.add(scrollPaneEleCand);
		
		JList<Candidate> listEleCand = new JList<Candidate>();
		listEleCand.setModel(modelEleCand);
		listEleCand.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneEleCand.setViewportView(listEleCand);
		
		JButton btnMoveLeft = new JButton();
		btnMoveLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	ElectionG.btnStartElec.setEnabled(false);
            	
            	Candidate selectedValue = listEleCand.getSelectedValue();
				
				if(selectedValue.getCode().equals("BRANCO")) {
					return;
				}
				if(elemsAvaiCand.exists(selectedValue.getCode())) {
					return;
				}
				
				modelAvaiCand.addElement(selectedValue);
				elemsAvaiCand.getList().add(selectedValue);
				
				modelEleCand.removeElement(selectedValue);
				elemsEleCand.getList().remove(selectedValue);
				
				if(modelEleCand.getSize() != 0) {
					updateEleBlockchain(null);
				}
				else {
					bcEleCand = new BlockChain();
				}
				
				updateAvaiBlockchain(ElectionG.btnStartElec);
				
				int iSelected = listEleCand.getSelectedIndex();
				if (iSelected == -1) {
					return;
				}
            }
        });
		btnMoveLeft.setBounds(735, 260, 60, 60);
		ImageUtils.setIcon(btnMoveLeft, Menu.class.getResourceAsStream("/images/arrow_left.svg"));
		btnMoveLeft.setFocusable(false);
		this.add(btnMoveLeft);
		
		JButton btnMoveRight = new JButton();
		btnMoveRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	ElectionG.btnStartElec.setEnabled(false);
            	
            	Candidate selectedValue = (Candidate) listAvaiCand.getSelectedValue();
            	
            	if(elemsEleCand.exists(selectedValue.getCode())) {
					return;
				}
            	
				modelEleCand.addElement(selectedValue);
				elemsEleCand.getList().add(selectedValue);
				
				modelAvaiCand.removeElement(selectedValue);
				elemsAvaiCand.getList().remove(selectedValue);
				
				if(modelAvaiCand.getSize() != 0) {
					updateAvaiBlockchain(null);
				}
				else {
					bcAvaiCand = new BlockChain();
				}
				
				updateEleBlockchain(ElectionG.btnStartElec);
				
				int iSelected = listAvaiCand.getSelectedIndex();
				if (iSelected == -1) {
					return;
				}
            }
        });
		btnMoveRight.setBounds(735, 340, 60, 60);
		ImageUtils.setIcon(btnMoveRight, Menu.class.getResourceAsStream("/images/arrow_right.svg"));
		btnMoveRight.setFocusable(false);
		this.add(btnMoveRight);
		
		JButton btnCreate = new JButton("Criar");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCreate.setEnabled(false);
				
				if (ComponentUtils.isValid(txtCandName)) { 
					JOptionPane.showMessageDialog(Menu.frmFrame, "Nome Inválido", "Erro", JOptionPane.ERROR_MESSAGE, null);
					btnCreate.setEnabled(true);
					return; 
				}
				if (ComponentUtils.isValid(txtCandID)) { 
					JOptionPane.showMessageDialog(Menu.frmFrame, "Sigla Inválida", "Erro", JOptionPane.ERROR_MESSAGE, null);
					btnCreate.setEnabled(true);
					return; 
				}
				if (elemsAvaiCand.exists(txtCandID.getText())) { 
					JOptionPane.showMessageDialog(Menu.frmFrame, "A Sigla já existe", "Aviso", JOptionPane.INFORMATION_MESSAGE, null);
					btnCreate.setEnabled(true);
					return; 
				}
				
				Candidate newCand = null;
				
				if(newImg.equals("")) {
					
					try {
						newCand = new Candidate(
									txtCandName.getText(), 
									txtCandID.getText());
						
						modelAvaiCand.addElement(newCand);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
				}
				else 
				{
					
					try {
						newCand = new Candidate(
								txtCandName.getText(), 
								txtCandID.getText(), 
								newImg);
						
						modelAvaiCand.addElement(newCand);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
				}
				
				elemsAvaiCand.getList().add(newCand);
				updateAvaiBlockchain(btnCreate);
				
				newImg = "";
				ImageUtils.setImageByURL(lblCandImage, Menu.class.getResource("/images/candidate_default.png"));
			}
		});
		btnCreate.setFocusable(false);
		btnCreate.setBounds(10, 572, 80, 30);
		this.add(btnCreate);
		
		JButton btnDelete = new JButton("Eliminar");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDelete.setEnabled(false);
				
				Candidate selectedValue = listAvaiCand.getSelectedValue();
				modelAvaiCand.removeElement(selectedValue);
				
				elemsAvaiCand.getList().remove(selectedValue);
				updateAvaiBlockchain(btnDelete);
			}
		});
		btnDelete.setFocusable(false);
		btnDelete.setBounds(105, 572, 100, 30);
		this.add(btnDelete);
		
		JButton btnUpdate = new JButton("Alterar");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnUpdate.setEnabled(false);
				
				if (ComponentUtils.isValid(txtCandName)) { 
					JOptionPane.showMessageDialog(Menu.frmFrame, "Nome Inválido", "Erro", JOptionPane.ERROR_MESSAGE, null);
					return; 
				}
				if (ComponentUtils.isValid(txtCandID)) { 
					JOptionPane.showMessageDialog(Menu.frmFrame, "Sigla Inválida", "Erro", JOptionPane.ERROR_MESSAGE, null);
					return; 
				}
				if (elemsAvaiCand.exists(txtCandID.getText()) && elemsAvaiCand.get(txtCandID.getText()) != (Candidate) listAvaiCand.getSelectedValue()) { 
					JOptionPane.showMessageDialog(Menu.frmFrame, "A Sigla já existe", "Aviso", JOptionPane.INFORMATION_MESSAGE, null);
					return; 
				}
				
				Candidate cand = (Candidate) listAvaiCand.getSelectedValue();
				Candidate newCand = null;
				
				if(newImg.equals("")) {
					try {
						newCand = new Candidate(
								txtCandName.getText(), 
								txtCandID.getText(), 
								cand.getImageBytes());
						
						modelAvaiCand.setElementAt(newCand, listAvaiCand.getSelectedIndex());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				else 
				{
					try {
						newCand = new Candidate(
								txtCandName.getText(), 
								txtCandID.getText(), 
								newImg);
						
						modelAvaiCand.setElementAt(newCand, listAvaiCand.getSelectedIndex());
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				
				elemsAvaiCand.getList().set(listAvaiCand.getSelectedIndex(), newCand);
				updateAvaiBlockchain(btnUpdate);
				
				newImg = "";
				ImageUtils.setImageByURL(lblCandImage, Menu.class.getResource("/images/candidate_default.png"));
			}
		});
		btnUpdate.setFocusable(false);
		btnUpdate.setBounds(218, 572, 92, 30);
		this.add(btnUpdate);
		
		addBlank();
	}
	
	private static void updateAvaiBlockchain(JButton button) {
		
		thAvaiCh = new Thread(() -> {
			try {
				MerkleTreeString mt = new MerkleTreeString(elemsAvaiCand.getList());
				//System.out.println(mt.getRoot());
				bcAvaiCand.add(mt.getRoot());
				
				//System.out.println(bcAvaiCand.toString());
				//System.out.println(bcEleCand.toString());
				//System.out.println(candidateDecode(bcEleCand.getChain().get(bcEleCand.getChain().size() - 1).getData()).get(0).getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (button != null) button.setEnabled(true);
		});
		
		thAvaiCh.start();
        
    }
	
	public static void updateEleBlockchain(JButton button) {
		
		thEleCh = new Thread(() -> {
			try {
				MerkleTreeString mt = new MerkleTreeString(elemsEleCand.getList());
				//System.out.println(mt.getRoot());
				bcEleCand.add(mt.getRoot());
				
				//System.out.println(bcAvaiCand.toString());
				//System.out.println(bcEleCand.toString());
				//System.out.println(candidateDecode(bcEleCand.getChain().get(bcEleCand.getChain().size() - 1).getData()).get(0).getName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (button != null) button.setEnabled(true);
		});
		
		thEleCh.start();
        
    }
	
	public static void addBlank() {
		new Thread(() -> {
			try {
				Candidate cand = new Candidate("Voto em branco", "BRANCO", ImageUtils.iconToByteArray(Elector.class.getResource("/images/blank_vote.png")));
				modelEleCand.addElement(cand);
				elemsEleCand.getList().add(cand);
				updateEleBlockchain(null);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}).start();
	}
	
}
