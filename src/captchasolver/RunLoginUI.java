package captchasolver;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import accountcreation.CreatorEngine;
import util.Account;
import util.Constants;
import util.ErrorDialog;
import util.PropertyLAS;
import util.StackTraceToString;

public class RunLoginUI {

	private static JFrame frmGmailLogin;
	private static JTextField emailField;
	private static JPasswordField passwordField;

	public static JProgressBar progressBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RunLoginUI window = new RunLoginUI();
					window.frmGmailLogin.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private boolean go = true;

	/**
	 * Create the application.
	 */
	public RunLoginUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		CJAWHDHAWDJAWDHGAWJDAWGDGHAWDGAWJD();

		frmGmailLogin = new JFrame();
		frmGmailLogin.setLocationRelativeTo(null);
		frmGmailLogin.setTitle("Gmail Login");
		frmGmailLogin.setResizable(false);
		frmGmailLogin.setBounds(100, 100, 420, 242);
		frmGmailLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGmailLogin.getContentPane().setLayout(null);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {

			ErrorDialog.displayError(e1, "Error with setLookAndFeel");
			e1.printStackTrace();
		}

		try {
			frmGmailLogin.setIconImage(new ImageIcon(ImageIO.read(new URL(Constants.CAPTCHAICONURL))).getImage());
		} catch (IOException e) {
			ErrorDialog.displayError(e, "Could not get icon from URL");
			e.printStackTrace();
		}

		JLabel lblEmail = new JLabel("Email :");
		lblEmail.setBounds(15, 57, 69, 20);
		frmGmailLogin.getContentPane().add(lblEmail);

		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setBounds(15, 95, 91, 20);
		frmGmailLogin.getContentPane().add(lblPassword);

		emailField = new JTextField();
		emailField.setBounds(104, 54, 295, 26);
		frmGmailLogin.getContentPane().add(emailField);
		emailField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(104, 92, 295, 26);
		frmGmailLogin.getContentPane().add(passwordField);

		JButton btnNewButton = new JButton("Run");
		btnNewButton.grabFocus();

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int dialogButton = JOptionPane.INFORMATION_MESSAGE;

				String toD = "" + "Attempt to log into " + emailField.getText() + "? \n"; //

				String email = emailField.getText();

				if (email.equals("")) {
					displayInformation("Email field is empty", "Email is invalid");
				} else if (!email.contains("@")) {
					displayInformation("Email field does not contain an arobas.", "Email is invalid");
				} else {
					int dialogResult = JOptionPane.showConfirmDialog(null, toD, "Automatic email retreiver",
							dialogButton);
					if (dialogResult == JOptionPane.OK_OPTION) {
						new Thread() {

							public void run() {

								GMailParser.connect(email, new String(passwordField.getPassword()));

							}

						}.start();
					}
				}
			}
		});
		btnNewButton.setBounds(104, 131, 295, 29);
		frmGmailLogin.getContentPane().add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Allow");
		btnNewButton_1.setToolTipText(
				"<html>Make sure you are on the right account<br>to allow less secure apps. <br><br>You will only have to do this once.</html>");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String email = PropertyLAS.getProperty(Constants.EMAILKey) + Constants.EMAILDOMAIN;

				StringSelection selection = new StringSelection(email);
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, selection);

				int reply = JOptionPane.showConfirmDialog(null,
						"A browser tab with your email settings will be openned,\nmake sure you are on the right account. \nYou will only need to do this once.",
						"Edit privacy settings", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {

					Browser.open(Constants.GMAILLESSECUREURL);
				}

			}
		});
		btnNewButton_1.setBounds(104, 12, 295, 29);
		frmGmailLogin.getContentPane().add(btnNewButton_1);

		JLabel lblFirst = new JLabel("First :");
		lblFirst.setBounds(15, 16, 69, 20);
		frmGmailLogin.getContentPane().add(lblFirst);

		JLabel lblThen = new JLabel("Then :");
		lblThen.setBounds(15, 135, 69, 20);
		frmGmailLogin.getContentPane().add(lblThen);

		progressBar = new JProgressBar();
		progressBar.setBounds(0, 176, 414, 26);
		frmGmailLogin.getContentPane().add(progressBar);

		if (go) {
			emailField.setText(PropertyLAS.getProperty(Constants.EMAILKey) + Constants.EMAILDOMAIN);
			passwordField.setText(PropertyLAS.getProperty(Constants.PASSWORDKey));
		}

	}

	private void CJAWHDHAWDJAWDHGAWJDAWGDGHAWDGAWJD() {

		ArrayList<Account> toResend = new ArrayList<Account>();

		for (Account a : CreatorEngine.toValidate) {
			new Thread() {

				public void run() {
					String email = a.email.replace("+", "%2b");
					String password = a.password;

					String daw = "https://realmofthemadgodhrd.appspot.com/char/list?guid=" + email + "&password="
							+ password;

					String answer = TAKEOVERTHEMONDE(daw);

					if (!answer.contains("<VerifiedEmail/>")) {
						toResend.add(a);
						System.out.println("Not verified...");
					} else {
						System.out.println("Already verified... Skipping...");
					}

					CreatorEngine.toValidate.remove(a); //empty it, as a way to deal with the threading

					if (CreatorEngine.toValidate.isEmpty()) {
						System.out.println("UGH");

						for (Account resend : toResend) {

							System.out.println("Resending email for " + resend.email);

							String e = resend.email.replace("+", "%2b");
							String a = TAKEOVERTHEMONDE(
									"https://realmofthemadgodhrd.appspot.com/account/changeEmail?g=" + e);

							System.out.println(a);
						}
					}

				}

			}.start();
		}

	}

	public static void setProgressBar(int num, int denum) {
		if (denum != 0) {
			progressBar.setValue(num * 100 / denum);
		} else {
			progressBar.setValue(0);
		}

	}

	private String TAKEOVERTHEMONDE(String daw) {
		try {
			URL oracle;

			oracle = new URL(daw);

			BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
			String answer = in.readLine();
			in.close();
			return answer;

		} catch (MalformedURLException e) {
			System.err.println("URL is malformed :");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error :");
			e.printStackTrace();
		}
		return "null";
	}

	public static void displayError(String msg, String title, Exception e) {
		JOptionPane.showMessageDialog(frmGmailLogin, msg + "\n " + StackTraceToString.sTTS(e), title,
				JOptionPane.WARNING_MESSAGE);
	}

	public static void displayInformation(String msg, String title) {
		JOptionPane.showMessageDialog(frmGmailLogin, msg, title, JOptionPane.INFORMATION_MESSAGE);
	}
}
