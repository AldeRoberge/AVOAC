package util;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ErrorDialog {

	public static void displayError(Exception e, String title) {

		System.err.println(title);

		String trace = StackTraceToString.sTTS(e);

		JOptionPane.showMessageDialog(new JFrame(), title + "\n" + trace, title, JOptionPane.ERROR_MESSAGE);

	}

}