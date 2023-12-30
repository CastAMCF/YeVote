package client.gui.results;

import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.formdev.flatlaf.FlatClientProperties;

import client.app.Elector;
import client.app.ListElectors;
import client.gui.Menu;
import utils.ImageUtils;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Voters extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JTextField txtSearch;
	private JTextField txtName;
	private JTextField txtCC;
	private JTextField txtSex;
	private JTextField txtDate;
	private JLabel lblElectorImage;
	
	public static DefaultListModel<Elector> modelElectors = new DefaultListModel<Elector>();
	public static ListElectors elemsElectors = new ListElectors();

	/**
	 * Create the panel.
	 */
	public Voters() {
		
		this.setLayout(null);
		
		txtSearch = new JTextField();
		txtSearch.setBounds(10, 10, 680, 35);
		txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Pesquisa");
		txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, ImageUtils.FlatSVGIcon(Menu.class.getResourceAsStream("/images/search.svg")));
		txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
		this.add(txtSearch);
		
		JButton btnSearch = new JButton("Pesquisar");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				modelElectors.removeAllElements();
				
				for(Elector elector : elemsElectors.getList()){
					if(elector.getName().contains(txtSearch.getText()) || elector.getCc().contains(txtSearch.getText()))
						modelElectors.addElement(elector);
		        }
				
			}
		});
		btnSearch.setBounds(700, 11, 105, 34);
		btnSearch.setFocusable(false);
		this.add(btnSearch);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 55, 795, 557);
		this.add(scrollPane_1);
		
		JList<Elector> list = new JList<Elector>();
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting() && list.getSelectedValue() != null) {
					Elector elector = (Elector) list.getSelectedValue();
					
					txtName.setText(elector.getName());
					txtCC.setText(elector.getCc());
					txtSex.setText(elector.getSex());
					txtDate.setText(elector.getBirthday());
					
					ImageUtils.setImageByIcon(lblElectorImage, elector.getImage());
				}
			}
		});
		list.setModel(modelElectors);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_1.setViewportView(list);
		
		JLabel lblElectorName = new JLabel("Nome");
		lblElectorName.setBounds(815, 75, 157, 13);
		this.add(lblElectorName);
		
		txtName = new JTextField();
		txtName.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		txtName.setFocusable(false);
		txtName.setEditable(false);
		txtName.setBounds(815, 93, 300, 35);
		this.add(txtName);
		
		JLabel lblElectorCC = new JLabel("Cartão de Cidadão");
		lblElectorCC.setBounds(815, 139, 157, 13);
		this.add(lblElectorCC);
		
		txtCC = new JTextField();
		txtCC.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		txtCC.setFocusable(false);
		txtCC.setEditable(false);
		txtCC.setBounds(815, 157, 300, 35);
		this.add(txtCC);
		
		JLabel lblElectorSex = new JLabel("Sexo");
		lblElectorSex.setBounds(815, 203, 157, 13);
		this.add(lblElectorSex);
		
		txtSex = new JTextField();
		txtSex.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		txtSex.setFocusable(false);
		txtSex.setEditable(false);
		txtSex.setBounds(815, 221, 300, 35);
		this.add(txtSex);
		
		JLabel lblElectorDate = new JLabel("Data de Nascimento");
		lblElectorDate.setBounds(815, 267, 157, 13);
		this.add(lblElectorDate);
		
		txtDate = new JTextField();
		txtDate.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		txtDate.setFocusable(false);
		txtDate.setEditable(false);
		txtDate.setBounds(815, 285, 300, 35);
		txtDate.putClientProperty(FlatClientProperties.TEXT_FIELD_TRAILING_ICON, ImageUtils.FlatSVGIcon(Menu.class.getResourceAsStream("/images/DataTables.svg")));
		this.add(txtDate);
		
		lblElectorImage = new JLabel();
		lblElectorImage.setBounds(860, 341, 200, 200);
		this.add(lblElectorImage);
		
	}

}
