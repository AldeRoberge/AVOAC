package accountcreation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
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

import captchasolver.GMailParser;
import captchasolver.RunLoginUI;
import util.Account;
import util.Constants;
import util.ErrorDialog;
import util.Name;
import util.PasswordValidator;
import util.PropertyLAS;
import javax.swing.JProgressBar;

public class RunCreatorUI {

	public static JFrame frmAldesVeryOwn;
	public JTextField emailField;
	public JPasswordField passwordField;
	public JTextField beginIndexField;
	public JTextField amountField;
	public JButton runBtn;
	public JLabel lblgmailcom;
	public JComboBox<Name> nameSelectorBox;
	public static JTextArea consoleArea;

	public final static String ICONURL = "http://i.imgur.com/ictMuzY.png";

	private JLabel passwordStatus;
	public static JProgressBar progressBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RunCreatorUI window = new RunCreatorUI();
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
	public RunCreatorUI() {
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
		frmAldesVeryOwn.setTitle(Constants.TITLEACRONYM + " - Account Creator");
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
		passwordField.setBounds(90, 45, 267, 22);
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
				"Note that you can only create 30 account on each IP. Either change network or get a VPN.");
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

					String passwordSave = PropertyLAS.getProperty(Constants.PASSWORDSAVEKey);

					if (passwordSave.equals("-1")) { //-1 = unset
						int reply = JOptionPane.showConfirmDialog(null,
								"Save password for future use? \n WARNING : Saved passwords are stored as plain text in myprops.properties.",
								"Save password?", JOptionPane.YES_NO_OPTION);
						if (reply == JOptionPane.YES_OPTION) {
							PropertyLAS.saveProperty(Constants.PASSWORDSAVEKey, "1"); //do save
							passwordSave = "1";
						} else {
							PropertyLAS.saveProperty(Constants.PASSWORDSAVEKey, "0"); //do not save
							PropertyLAS.saveProperty(Constants.PASSWORDKey, ""); //empty
							passwordSave = "0";
						}
					}

					if (passwordSave.equals("1")) {
						PropertyLAS.saveProperty(Constants.PASSWORDKey, passwordField.getText());
					}

					PropertyLAS.saveProperty(Constants.EMAILKey, emailField.getText());
					PropertyLAS.saveProperty(Constants.BEGININDEXKey, beginIndexField.getText());
					PropertyLAS.saveProperty(Constants.AMOUNTKey, amountField.getText());

					tabbedPane.setSelectedIndex(1); //show the console

					/* BURRRRRRRRRRP, SOWY NOT SOWY */

					int selectedGUID = Name.randomName().getValue();

					if (nameSelectorBox.isEnabled()) {
						Name selectedGUIDName = Name.valueOf(nameSelectorBox.getSelectedItem().toString());
						selectedGUID = selectedGUIDName.getValue();
					}

					CreatorEngine.toValidate.clear(); //make sure there is no leftovers

					String emailPrefix = emailField.getText();
					String emailSubfix = Constants.EMAILDOMAIN;

					String password = passwordField.getText();

					int beginIndex = Integer.parseInt(beginIndexField.getText());
					int amount = Integer.parseInt(amountField.getText());
					int endIndex = beginIndex + amount;

					for (int i = beginIndex; i < endIndex; i++) {

						Account e = new Account();

						e.email = emailPrefix + "+" + i + emailSubfix;
						e.password = password;
						e.guid = selectedGUID;

						CreatorEngine.toValidate.add(e);
					}

					if (amount == 1) {
						print("Registering 1 mule.");

					} else if (amount > 1) {
						print("Registering " + amount + " mules.");

					} else {
						print("No mules to be created.");
					}

					if (amount > 0) {
						new Thread() {

							public void run() {

								try {
									CreatorEngine.registerAccounts();
								} catch (ExecutionException e) {
									e.printStackTrace();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}.start();
					}

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

		nameSelectorBox = new JComboBox<Name>();
		nameSelectorBox.setModel(new DefaultComboBoxModel(names.toArray()));
		nameSelectorBox.setBounds(12, 147, 236, 22);
		UI.add(nameSelectorBox);

		JCheckBox selectANameChckbx = new JCheckBox("Select a name");
		selectANameChckbx.setToolTipText("If unchecked, a random name will be assigned");
		selectANameChckbx.setSelected(true);

		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				if (selectANameChckbx.isSelected()) {
					nameSelectorBox.setEnabled(true);
				} else {
					nameSelectorBox.setEnabled(false);
				}
			}
		};
		selectANameChckbx.addActionListener(actionListener);

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

		selectANameChckbx.setBounds(259, 146, 132, 25);
		UI.add(selectANameChckbx);

		lblgmailcom = new JLabel("+0" + Constants.EMAILDOMAIN); //ex : +0@gmail.com 
		lblgmailcom.setHorizontalAlignment(SwingConstants.CENTER);
		lblgmailcom.setBounds(245, 16, 146, 16);
		UI.add(lblgmailcom);

		passwordStatus = new JLabel("\u2022");
		passwordStatus.setHorizontalAlignment(SwingConstants.CENTER);
		String tooltipText = "<html>Password must be at least 10 characters long,<br>contain at least one number,<br>and not have repeating characters.</html>";

		passwordStatus.setToolTipText(tooltipText);
		passwordStatus.setBounds(357, 48, 34, 16);
		passwordStatus.setForeground(Color.BLACK);
		UI.add(passwordStatus);

		JPanel Console = new JPanel();
		tabbedPane.addTab("Console", null, Console, null);
		Console.setLayout(null);

		progressBar = new JProgressBar();
		progressBar.setBounds(0, 205, 404, 73);
		Console.add(progressBar);

		JScrollPane consolePane = new JScrollPane();
		consolePane.setBounds(0, 0, 409, 206);
		Console.add(consolePane);

		consoleArea = new JTextArea();
		consoleArea.setEditable(false);
		consolePane.setViewportView(consoleArea);

		JButton saveBtn = new JButton("Save to output.js");
		saveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SaveToFileMachine.saveToFile();
			}
		});
		saveBtn.setBounds(0, 205, 409, 37);
		Console.add(saveBtn);

		JButton solvaCaptchaBtn = new JButton("Solve captchas");
		solvaCaptchaBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				RunLoginUI.main(null);
				RunLoginUI.displayInformation(
						"1. Allow 'less-secure' apps in gmail \n2. Enter credentials\n 3. Captchas will open in browser when you press enter.",
						"Captcha help");
			}
		});
		solvaCaptchaBtn.setBounds(0, 241, 404, 37);
		Console.add(solvaCaptchaBtn);

		print("Welcome to " + Constants.TITLE + "!");
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

		passwordStatus.setToolTipText(PasswordValidator.getToolTipForPassword(password));

		if (password.equals("")) {
			passwordStatus.setToolTipText("Password is empty");
			passwordStatus.setForeground(Color.RED);
		} else {
			if (passwordValidaterAnswer == 1) {
				passwordStatus.setForeground(Color.GREEN);
			} else {
				passwordStatus.setForeground(Color.RED);
			}
		}

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
