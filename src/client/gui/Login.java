package client.gui;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.formdev.flatlaf.FlatClientProperties;

public class Login extends JPanel {

	private static final long serialVersionUID = 1L;

	public static JTextField txtElectorCC;
	public static JPasswordField passwordField1;
	
	/**
	 * Create the panel.
	 */
	public Login() {
		
		this.setLayout(null);
		
		this.setPreferredSize(new Dimension(320, 100));
		
		txtElectorCC = new JTextField();
		txtElectorCC.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Cartão de Cidadão");
		txtElectorCC.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		txtElectorCC.setBounds(10, 10, 300, 35);
		this.add(txtElectorCC);
		
		passwordField1 = new JPasswordField();
		passwordField1.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
		passwordField1.putClientProperty( FlatClientProperties.STYLE, "showRevealButton: true");
		passwordField1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		passwordField1.setBounds(10, 55, 300, 35);
		this.add(passwordField1);
		
	}

}
