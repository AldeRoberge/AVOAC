package resendemail;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import util.Account;
import util.Constants;
import util.ErrorDialog;
import util.Name;
import util.PasswordValidator;
import util.PropertyLAS;

public class ResendEmailUI {

	public static JFrame frmAldesVeryOwn;
	public JTextField emailField;
	public JPasswordField passwordField;
	public JTextField beginIndexField;
	public JTextField amountField;
	public JButton runBtn;
	public JLabel lblgmailcom;
	public static JTextArea consoleArea;

	public final static String ICONURL = "http://i.imgur.com/ictMuzY.png";

	public final static String TITLE = "AVOAC - Email Resender";
	public static JProgressBar progressBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ResendEmailUI window = new ResendEmailUI();
					window.frmAldesVeryOwn.setVisible(true);

					//Update field values from property file
					window.emailField.setText(PropertyLAS.getProperty(Constants.EMAILKey));

					window.beginIndexField.setText(PropertyLAS.getProperty(Constants.BEGININDEXKey));
					window.amountField.setText(PropertyLAS.getProperty(Constants.AMOUNTKey));

					if (PropertyLAS.getProperty(Constants.PASSWORDSAVEKey).equals("1")) {
						window.passwordField.setText(PropertyLAS.getProperty(Constants.PASSWORDKey));
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ResendEmailUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAldesVeryOwn = new JFrame();

		ToolTipManager.sharedInstance().setInitialDelay(50);

		//http://i.imgur.com/NXfqAPA.png

		frmAldesVeryOwn.setLocationRelativeTo(null);
		frmAldesVeryOwn.setResizable(false);
		frmAldesVeryOwn.setTitle(Constants.TITLEACRONYM + " - Resend email");
		frmAldesVeryOwn.setBounds(100, 100, 415, 352);
		frmAldesVeryOwn.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			frmAldesVeryOwn.setIconImage(new ImageIcon(ImageIO.read(new URL(ICONURL))).getImage());
		} catch (IOException e2) {
			ErrorDialog.displayError(e2, "Could not get icon image from URL");
			e2.printStackTrace();
		}
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {

			ErrorDialog.displayError(e1, "Error with setLookAndFeel");
			e1.printStackTrace();
		}

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmAldesVeryOwn.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel UI = new JPanel();
		tabbedPane.addTab("UI", null, UI, null);
		UI.setLayout(null);

		//LABELS

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(12, 16, 40, 16);
		UI.add(lblEmail);

		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(12, 48, 77, 16);
		UI.add(lblPassword);

		JLabel lblBeginAt = new JLabel("Begin at");
		lblBeginAt.setBounds(12, 80, 77, 16);
		UI.add(lblBeginAt);

		JLabel lblEndAfter = new JLabel("Amount");
		lblEndAfter.setBounds(12, 115, 77, 16);
		UI.add(lblEndAfter);

		//TEXTFIELDS

		emailField = new JTextField();
		emailField.setBounds(90, 13, 158, 22);
		emailField.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (c == ' ' || c == '+') {
					e.consume(); // consume non-numbers
				}
			}
		});
		UI.add(emailField);
		emailField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(90, 45, 301, 22);
		UI.add(passwordField);

		passwordField.setColumns(10);

		beginIndexField = new JTextField();
		beginIndexField.setBounds(90, 80, 301, 22);
		beginIndexField.setText("The number which to begin at");
		beginIndexField.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
					e.consume(); // consume non-numbers
				}
			}
		});

		UI.add(beginIndexField);
		beginIndexField.setColumns(10);

		amountField = new JTextField();
		amountField.setToolTipText(
				"Amount of accounts to verify.");
		amountField.setBounds(90, 112, 301, 22);
		amountField.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (((c < '0') || (c > '9')) && (c != KeyEvent.VK_BACK_SPACE)) {
					e.consume(); // consume non-numbers
				}
			}
		});

		UI.add(amountField);
		amountField.setColumns(10);

		runBtn = new JButton("Let's go");
		runBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {

					print("Let's go...");

					int beginIndex = Integer.parseInt(beginIndexField.getText());
					int amount = Integer.parseInt(amountField.getText());

					String password = new String(passwordField.getPassword());

					print("Revalidating "+amount+" mules...");
					
					for (int i = beginIndex; i < beginIndex+amount; i++) {
						Account a = new Account();
						a.email = emailField.getText() + "+" + i + Constants.EMAILDOMAIN;
						a.password = password;
						ResendEngine.toResend.add(a);
					}

					ResendEngine.verifyAccounts();

				} catch (Exception e) {
					e.printStackTrace();
					ErrorDialog.displayError(e, "Fatal error");
				}
			}
		});
		runBtn.setBounds(12, 185, 379, 77);
		UI.add(runBtn);

		ArrayList<Name> names = new ArrayList<Name>(); //to sort the array alphabetically
		for (Name e : Name.values()) {
			names.add(e);
		}

		beginIndexField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				updateUI();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateUI();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateUI();
			}
		});
		amountField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				updateUI();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateUI();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateUI();
			}
		});
		passwordField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				updateUI();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				updateUI();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				updateUI();
			}
		});

		lblgmailcom = new JLabel("+0" + Constants.EMAILDOMAIN); //ex : +0@gmail.com 
		lblgmailcom.setHorizontalAlignment(SwingConstants.CENTER);
		lblgmailcom.setBounds(245, 16, 146, 16);
		UI.add(lblgmailcom);

		JPanel Console = new JPanel();
		tabbedPane.addTab("Console", null, Console, null);
		Console.setLayout(null);

		progressBar = new JProgressBar();
		progressBar.setBounds(0, 0, 404, 29);
		Console.add(progressBar);

		JScrollPane consolePane = new JScrollPane();
		consolePane.setBounds(0, 29, 409, 249);
		Console.add(consolePane);

		consoleArea = new JTextArea();
		consoleArea.setEditable(false);
		consolePane.setViewportView(consoleArea);

		print("Welcome to " + TITLE + "!");
		print("Ready...");
	}

	public static void print(String printToConsole) {
		consoleArea.append(printToConsole + "\n");
	}

	protected void updateUI() {

		//PASSWORD VALIDATION

		String password = passwordField.getText();

		// check for duplicates

		boolean hasDuplicate = false;

		int passwordValidaterAnswer = PasswordValidator.validatePassword(password);

		hasDuplicate = false;

		//RUNBTN TEXT

		if (!amountField.getText().equals("")) {
			runBtn.setText("Create " + amountField.getText() + " mule(s)");
		} else {
			runBtn.setText("Creating 0 mules");
		}

		//RUNBTN VALIDITY

		if (emailField.getText().equals("") || passwordField.getText().equals("")
				|| beginIndexField.getText().equals("") || amountField.getText().equals("")) {
			runBtn.setEnabled(false);
		} else {
			if (passwordValidaterAnswer == 1) {
				runBtn.setEnabled(true);
			}
		}

		//EMAIL LABEL TEXT

		lblgmailcom.setText("+" + beginIndexField.getText() + Constants.EMAILDOMAIN);

	}

	public static void setProgressBar(int num, int denum) {
		if (denum != 0) {
			int percentage = (num * 100 / denum);
			if (percentage == 100) {
				progressBar.setVisible(false);
			} //set visible again in CreatorEngine
			progressBar.setValue(percentage);

		} else {
			progressBar.setValue(0);
		}

	}

}
