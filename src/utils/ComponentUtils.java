package utils;

import javax.swing.JTextField;

public class ComponentUtils {
	
	public static boolean isValid(JTextField txtField) {
		return txtField.getText().isBlank() || !txtField.getText().matches("^(?!(?:CON|PRN|AUX|NUL|COM[1-9]|LPT[1-9])(?:\\.[^.]*)?$)[^<>:\"/\\\\|?*\\x00-\\x1F]*[^<>:\"/\\\\|?*\\x00-\\x1F\\ .]$");
	}
	
}
