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
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.formdev.flatlaf.FlatClientProperties;

import client.app.ListElectors;
import client.blockchain.BlockChain;
import client.blockchain.MerkleTreeString;
import client.app.Elector;
import client.gui.Menu;
import utils.ComponentUtils;
import utils.ImageUtils;

public class ElectorG extends JPanel {

	private static final long serialVersionUID = 1L;

	public static JTextField txtElectorName;
	public static JTextField txtElectorCC;
	public static JPasswordField passwordField1;
	public static JPasswordField passwordField2;
	public static JComboBox<Object> comboBoxSex;
	public static JComboBox<Object> comboBoxDays;
	public static JComboBox<Object> comboBoxMonths;
	public static JComboBox<Object> comboBoxYears;
	public static JLabel lblElectorImage;
	
	public static String newImg = "";
	
	public static DefaultListModel<Elector> modelAvaiElector = new DefaultListModel<Elector>();
	public static DefaultListModel<Elector> modelEleElector = new DefaultListModel<Elector>();
	
	public static BlockChain bcAvaiElector = new BlockChain();
	public static BlockChain bcEleElector = new BlockChain();
	
	public static ListElectors elemsAvaiElector = new ListElectors();
	public static ListElectors elemsEleElector = new ListElectors();
	
	static Thread thEleCh;
	static Thread thAvaiCh;
	
	/**
	 * Create the panel.
	 */
	public ElectorG() {
		
		this.setLayout(null);
		
		JToolBar toolBarSettings = new JToolBar();
		toolBarSettings.setFloatable(false);
		toolBarSettings.setBounds(10, 5, 100, 35);
		this.add(toolBarSettings);
		
		JButton btnOpenElectorsList = new JButton();
		btnOpenElectorsList.setToolTipText("Abrir lista");
		toolBarSettings.add(btnOpenElectorsList);
		btnOpenElectorsList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("eleitores"));
				fileChooser.setFileFilter(new FileNameExtensionFilter("Electors Files", "cid"));
				int response = fileChooser.showOpenDialog(null);
				
				if(response == JFileChooser.APPROVE_OPTION) {
					String path = fileChooser.getSelectedFile().getAbsolutePath();
					String extension = ".cid";

			        if (path.endsWith(extension)) {
			        	path = path.substring(0, path.length() - extension.length());
			        }
					
					String[] pathArray = path.replace("\\", "|").split("\\|");
					
					try {
						bcAvaiElector.load("blockchain/eleitores/" + pathArray[pathArray.length - 1] + ".db");
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(Menu.frmFrame, "Não foi possível encontrar a base de dados desta lista", "Erro", JOptionPane.ERROR_MESSAGE, null);
						return;
					}
					
					try {
						elemsAvaiElector.load(fileChooser.getSelectedFile().getAbsolutePath());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				else {
					return;
				}
				
				MerkleTreeString mt = new MerkleTreeString(elemsAvaiElector.getList());
				
				//System.out.println(mt.getRoot());
				//System.out.println(bcAvaiCand.getChain().get(bcAvaiCand.getChain().size() - 1).getData());
				
				if(!mt.getRoot().equals(bcAvaiElector.getChain().get(bcAvaiElector.getChain().size() - 1).getData())) {
					JOptionPane.showMessageDialog(Menu.frmFrame, "A lista está corrumpida", "Erro", JOptionPane.ERROR_MESSAGE, null);
					return;
				}
				
				modelAvaiElector.removeAllElements();
				
				for(Elector elector : elemsAvaiElector.getList()){
					modelAvaiElector.addElement(elector);
		        }
			}
		});
		btnOpenElectorsList.setFocusable(false);
		ImageUtils.setIcon(btnOpenElectorsList, Menu.class.getResourceAsStream("/images/copyOfFolder.svg"));
		
		JButton btnNewElectorsList = new JButton();
		btnNewElectorsList.setToolTipText("Nova lista");
		toolBarSettings.add(btnNewElectorsList);
		btnNewElectorsList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modelAvaiElector.removeAllElements();
				modelEleElector.removeAllElements();
				
				bcAvaiElector = new BlockChain();
				bcEleElector = new BlockChain();
				
				elemsAvaiElector = new ListElectors();
				elemsEleElector = new ListElectors();
				
				txtElectorName.setText("");
				txtElectorCC.setText("");
				
				comboBoxSex.setSelectedIndex(0);
				
				comboBoxDays.setSelectedIndex(0);
				comboBoxMonths.setSelectedIndex(0);
				comboBoxYears.setSelectedIndex(0);
				
				passwordField1.setText("");
				passwordField2.setText("");
				
				newImg = "";
				ImageUtils.setImageByURL(lblElectorImage, Menu.class.getResource("/images/elector_default.png"));
			}
		});
		btnNewElectorsList.setFocusable(false);
		ImageUtils.setIcon(btnNewElectorsList, Menu.class.getResourceAsStream("/images/addAny.svg"));
		
		JButton btnSaveElectorsList = new JButton();
		btnSaveElectorsList.setToolTipText("Guardar lista");
		toolBarSettings.add(btnSaveElectorsList);
		btnSaveElectorsList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("eleitores"));
				fileChooser.setFileFilter(new FileNameExtensionFilter("Electors Files", "cid"));
				int response = fileChooser.showSaveDialog(null);
				
				if(response == JFileChooser.APPROVE_OPTION) {
					String path = fileChooser.getSelectedFile().getAbsolutePath();
					String extension = ".cid";

			        if (path.endsWith(extension)) {
			        	path = path.substring(0, path.length() - extension.length());
			        }
					
					String[] pathArray = path.replace("\\", "|").split("\\|");
					
					try {
						bcAvaiElector.save("blockchain/eleitores/" + pathArray[pathArray.length - 1] + ".db");
						elemsAvaiElector.save(path + extension);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				else {
					return;
				}
			}
		});
		btnSaveElectorsList.setFocusable(false);
		ImageUtils.setIcon(btnSaveElectorsList, Menu.class.getResourceAsStream("/images/savedContext.svg"));
		
		txtElectorName = new JTextField();
		txtElectorName.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Nome");
		txtElectorName.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		txtElectorName.setBounds(10, 50, 300, 35);
		this.add(txtElectorName);
		
		txtElectorCC = new JTextField();
		txtElectorCC.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Cartão de Cidadão");
		txtElectorCC.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		txtElectorCC.setBounds(10, 95, 300, 35);
		this.add(txtElectorCC);
		
		passwordField1 = new JPasswordField();
		passwordField1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
		passwordField1.putClientProperty( FlatClientProperties.STYLE, "showRevealButton: true");
		passwordField1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		passwordField1.setBounds(10, 140, 300, 35);
		this.add(passwordField1);
		
		passwordField2 = new JPasswordField();
		passwordField2.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Confirmar Password");
		passwordField2.putClientProperty( FlatClientProperties.STYLE, "showRevealButton: true");
		passwordField2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		passwordField2.setBounds(10, 185, 300, 35);
		this.add(passwordField2);
		
		JLabel lblElectorSex = new JLabel("Sexo");
		lblElectorSex.setBounds(10, 226, 157, 13);
		this.add(lblElectorSex);
		
		comboBoxSex = new JComboBox<Object>();
		comboBoxSex.setModel(new DefaultComboBoxModel<Object>(new String[] {"Masculino", "Feminino", "Não Binário"}));
		comboBoxSex.setBounds(10, 249, 300, 34);
		this.add(comboBoxSex);
		
		JLabel lblElectorDate = new JLabel("Data de Nascimento");
		lblElectorDate.setBounds(10, 293, 157, 13);
		this.add(lblElectorDate);
		
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
		toolBarDate.setBounds(10, 316, 300, 35);
		this.add(toolBarDate);
		toolBarDate.setFloatable(false);
		
		comboBoxDays = new JComboBox<Object>();
		comboBoxDays.setFocusable(false);
		toolBarDate.add(comboBoxDays);
		comboBoxDays.setModel(new DefaultComboBoxModel<Object>(days.toArray()));
		
		JLabel lblDateLimit1 = new JLabel("/");
		toolBarDate.add(lblDateLimit1);
		
		comboBoxMonths = new JComboBox<Object>();
		comboBoxMonths.setFocusable(false);
		toolBarDate.add(comboBoxMonths);
		comboBoxMonths.setModel(new DefaultComboBoxModel<Object>(months.toArray()));
		
		JLabel lblDateLimit2 = new JLabel("/");
		toolBarDate.add(lblDateLimit2);
		
		comboBoxYears = new JComboBox<Object>();
		comboBoxYears.setFocusable(false);
		toolBarDate.add(comboBoxYears);
		comboBoxYears.setModel(new DefaultComboBoxModel<Object>(years.toArray()));
		
		lblElectorImage = new JLabel();
		lblElectorImage.setBounds(60, 365, 200, 200);
		lblElectorImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				newImg = ImageUtils.changeImage(lblElectorImage);
			}
		});
		ImageUtils.setImageByURL(lblElectorImage, Menu.class.getResource("/images/elector_default.png"));
		this.add(lblElectorImage);
		
		JLabel lblAvaiElector = new JLabel("Disponível");
		lblAvaiElector.setBounds(390, 48, 157, 25);
		this.add(lblAvaiElector);
		
		JScrollPane scrollPaneAvaiElector = new JScrollPane();
		scrollPaneAvaiElector.setBounds(390, 83, 320, 519);
		this.add(scrollPaneAvaiElector);
		
		JList<Elector> listAvaiElector = new JList<Elector>();
		listAvaiElector.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && listAvaiElector.getSelectedValue() != null) {
					Elector elector = (Elector) listAvaiElector.getSelectedValue();
					
					txtElectorName.setText(elector.getName());
					txtElectorCC.setText(elector.getCc());
					comboBoxSex.setSelectedItem(elector.getSex());
					
					String[] date = elector.getBirthday().split("/");
					
					comboBoxDays.setSelectedItem(date[0]);
					comboBoxMonths.setSelectedItem(date[1]);
					comboBoxYears.setSelectedItem(date[2]);
					
					passwordField1.setText("");
					passwordField2.setText("");
					
					ImageUtils.setImageByIcon(lblElectorImage, elector.getImage());
				}
			}
		});
		listAvaiElector.setModel(modelAvaiElector);
		listAvaiElector.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneAvaiElector.setViewportView(listAvaiElector);
		
		JLabel lblEleElector = new JLabel("Eleição");
		lblEleElector.setBounds(820, 48, 157, 25);
		this.add(lblEleElector);
		
		JScrollPane scrollPaneEleElector = new JScrollPane();
		scrollPaneEleElector.setBounds(820, 83, 320, 519);
		this.add(scrollPaneEleElector);
		
		JList<Elector> listEleElector = new JList<Elector>();
		listEleElector.setModel(modelEleElector);
		listEleElector.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneEleElector.setViewportView(listEleElector);
		
		JButton btnMoveLeft = new JButton();
		btnMoveLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	ElectionG.btnStartElec.setEnabled(false);
            	
            	Elector selectedValue = listEleElector.getSelectedValue();
            	
            	if(elemsAvaiElector.exists(selectedValue.getCc())) {
					return;
				}
            	
				modelAvaiElector.addElement(selectedValue);
				elemsAvaiElector.getList().add(selectedValue);
				
				modelEleElector.removeElement(selectedValue);
				elemsEleElector.getList().remove(selectedValue);
				
				if(modelEleElector.getSize() != 0) {
					updateEleBlockchain(null);
				}
				else {
					bcEleElector = new BlockChain();
				}
				
				updateAvaiBlockchain(ElectionG.btnStartElec);
				
				int iSelected = listEleElector.getSelectedIndex();
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
            	
            	Elector selectedValue = (Elector) listAvaiElector.getSelectedValue();
            	
            	if(elemsEleElector.exists(selectedValue.getCc())) {
					return;
				}
            	
				modelEleElector.addElement(selectedValue);
				elemsEleElector.getList().add(selectedValue);
				
				modelAvaiElector.removeElement(selectedValue);
				elemsAvaiElector.getList().remove(selectedValue);
				
				if(modelAvaiElector.getSize() != 0) {
					updateAvaiBlockchain(null);
				}
				else {
					bcAvaiElector = new BlockChain();
				}
				
				updateEleBlockchain(ElectionG.btnStartElec);
				
				int iSelected = listAvaiElector.getSelectedIndex();
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
				
				if (ComponentUtils.isValid(txtElectorName)) { 
					JOptionPane.showMessageDialog(Menu.frmFrame, "Nome Inválido", "Erro", JOptionPane.ERROR_MESSAGE, null);
					return; 
				}
				if (ComponentUtils.isValid(txtElectorCC)) { 
					JOptionPane.showMessageDialog(Menu.frmFrame, "Cartão de Cidadão Inválido", "Erro", JOptionPane.ERROR_MESSAGE, null);
					return; 
				}
				if (ComponentUtils.isValid(passwordField1)) { 
					JOptionPane.showMessageDialog(Menu.frmFrame, "A Password é Inválida", "Erro", JOptionPane.ERROR_MESSAGE, null);
					return; 
				}
				if(!String.valueOf(passwordField1.getPassword()).equals(String.valueOf(passwordField2.getPassword()))) { 
					JOptionPane.showMessageDialog(Menu.frmFrame, "As passwords não são iguais", "Erro", JOptionPane.ERROR_MESSAGE);
					return; 
				}
				if (elemsAvaiElector.exists(txtElectorCC.getText())) { 
					JOptionPane.showMessageDialog(Menu.frmFrame, "O Cartão de Cidadão já existe", "Aviso", JOptionPane.INFORMATION_MESSAGE, null);
					return; 
				}
				
				int days = Integer.parseInt((String) comboBoxDays.getSelectedItem());
				int months = comboBoxMonths.getSelectedIndex();
				int years = Integer.parseInt((String) comboBoxYears.getSelectedItem());
				
				GregorianCalendar calendarExpected = new GregorianCalendar(years, months, days);
				
				if(calendarExpected.get(Calendar.DATE) != days || calendarExpected.get(Calendar.MONTH) != months || calendarExpected.get(Calendar.YEAR) != years) {
					JOptionPane.showMessageDialog(Menu.frmFrame, "Data inválida", "Erro", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				new Thread(() -> {
					Elector newElector = null;
					
					if(newImg.equals("")) {
						
						try {
							newElector = new Elector(
									txtElectorName.getText(), 
									txtElectorCC.getText(), 
									(String) comboBoxSex.getSelectedItem(), 
									String.format("%s/%s/%s", days, (months+1), years), 
									String.valueOf(passwordField1.getPassword()));
									
							modelAvaiElector.addElement(newElector);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						
					}
					else 
					{
						
						try {
							newElector = new Elector(
									txtElectorName.getText(), 
									txtElectorCC.getText(), 
									(String) comboBoxSex.getSelectedItem(), 
									String.format("%s/%s/%s", days, (months+1), years), 
									String.valueOf(passwordField1.getPassword()), newImg);
							
							modelAvaiElector.addElement(newElector);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						
					}
					
					elemsAvaiElector.getList().add(newElector);
					updateAvaiBlockchain(btnCreate);
					
					newImg = "";
					ImageUtils.setImageByURL(lblElectorImage, Menu.class.getResource("/images/elector_default.png"));
					
				}).start();
			}
		});
		btnCreate.setFocusable(false);
		btnCreate.setBounds(10, 572, 80, 30);
		this.add(btnCreate);
		
		JButton btnDelete = new JButton("Eliminar");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDelete.setEnabled(false);
				
				Object selectedValue = listAvaiElector.getSelectedValue();
				modelAvaiElector.removeElement(selectedValue);
				
				elemsAvaiElector.getList().remove(selectedValue);
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
				
				if (ComponentUtils.isValid(txtElectorName)) { 
					JOptionPane.showMessageDialog(Menu.frmFrame, "Nome Inválido", "Erro", JOptionPane.ERROR_MESSAGE, null);
					return; 
				}
				if (ComponentUtils.isValid(txtElectorCC)) { 
					JOptionPane.showMessageDialog(Menu.frmFrame, "Cartão de Cidadão Inválido", "Erro", JOptionPane.ERROR_MESSAGE, null);
					return; 
				}
				if (ComponentUtils.isValid(passwordField1)) { 
					JOptionPane.showMessageDialog(Menu.frmFrame, "A Password é Inválida", "Erro", JOptionPane.ERROR_MESSAGE, null);
					return; 
				}
				if(!String.valueOf(passwordField1.getPassword()).equals(String.valueOf(passwordField2.getPassword()))) { 
					JOptionPane.showMessageDialog(Menu.frmFrame, "As passwords não são iguais", "Erro", JOptionPane.ERROR_MESSAGE);
					return; 
				}
				if (elemsAvaiElector.exists(txtElectorCC.getText()) && elemsAvaiElector.get(txtElectorCC.getText()) != (Elector) listAvaiElector.getSelectedValue()) { 
					JOptionPane.showMessageDialog(Menu.frmFrame, "O Cartão de Cidadão já existe", "Aviso", JOptionPane.INFORMATION_MESSAGE, null);
					return; 
				}
				
				int days = Integer.parseInt((String) comboBoxDays.getSelectedItem());
				int months = comboBoxMonths.getSelectedIndex();
				int years = Integer.parseInt((String) comboBoxYears.getSelectedItem());
				
				GregorianCalendar calendarExpected = new GregorianCalendar(years, months, days);
				
				if(calendarExpected.get(Calendar.DATE) != days || calendarExpected.get(Calendar.MONTH) != months || calendarExpected.get(Calendar.YEAR) != years) {
					JOptionPane.showMessageDialog(Menu.frmFrame, "Data inválida", "Erro", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				new Thread(() -> {
					Elector elector = (Elector) listAvaiElector.getSelectedValue();
					Elector newElector = null;
					
					if(newImg.equals("")) {
						try {
							newElector = new Elector(
									txtElectorName.getText(), 
									txtElectorCC.getText(), 
									(String) comboBoxSex.getSelectedItem(),
									String.format("%s/%s/%s", days, (months+1), years),
									String.valueOf(passwordField1.getPassword()), elector.getImageBytes());
							
							modelAvaiElector.setElementAt(newElector, listAvaiElector.getSelectedIndex());
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					else 
					{
						try {
							newElector = new Elector(
									txtElectorName.getText(), 
									txtElectorCC.getText(), 
									(String) comboBoxSex.getSelectedItem(),
									String.format("%s/%s/%s", days, (months+1), years),
									String.valueOf(passwordField1.getPassword()), newImg);
							
							modelAvaiElector.setElementAt(newElector, listAvaiElector.getSelectedIndex());
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
					
					elemsAvaiElector.getList().set(listAvaiElector.getSelectedIndex(), newElector);
					updateAvaiBlockchain(btnUpdate);
					
					newImg = "";
					ImageUtils.setImageByURL(lblElectorImage, Menu.class.getResource("/images/elector_default.png"));
					
				}).start();
			}
		});
		btnUpdate.setFocusable(false);
		btnUpdate.setBounds(218, 572, 92, 30);
		this.add(btnUpdate);
		
	}
	
	private static void updateAvaiBlockchain(JButton button) {
		
		thAvaiCh = new Thread(() -> {
			try {
				MerkleTreeString mt = new MerkleTreeString(elemsAvaiElector.getList());
				//System.out.println(mt.getRoot());
				bcAvaiElector.add(mt.getRoot());
				
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
				MerkleTreeString mt = new MerkleTreeString(elemsEleElector.getList());
				//System.out.println(mt.getRoot());
				bcEleElector.add(mt.getRoot());
				
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
	
}
