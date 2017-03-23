package ageOfAccounts;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import accountcreation.SaveToFileMachine;
import util.Account;
import util.Constants;
import util.ErrorDialog;

public class RunAgeOfAccountsUI {

	public static JFrame frmAldesVeryOwn;
	public JButton runBtn;
	public static JTextArea textArea;

	public static JProgressBar progressBar;

	public final static String ICONURL = "http://i.imgur.com/ictMuzY.png";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RunAgeOfAccountsUI window = new RunAgeOfAccountsUI();
					window.frmAldesVeryOwn.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RunAgeOfAccountsUI() {
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
		frmAldesVeryOwn.setTitle("AVOAC - Age of Accounts");
		frmAldesVeryOwn.setBounds(100, 100, 580, 352);
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

		runBtn = new JButton("Let's go");
		runBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {

					print("Let's go...");

					new Thread() {

						public void run() {

							String directoryOfAccountFileToCheck = SaveToFileMachine.getOutputFileDirectory();

							if (directoryOfAccountFileToCheck != null) {
								print("Getting accounts from " + directoryOfAccountFileToCheck);

								ArrayList<Account> accountsToVerify = new ArrayList<Account>();

								try {

									FileInputStream fstream;
									
									print("Getting account file from "+directoryOfAccountFileToCheck);

									fstream = new FileInputStream(directoryOfAccountFileToCheck);

									BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

									String strLine;

									//Read File Line By Line
									while ((strLine = br.readLine()) != null) {

										if (strLine.contains("': '")) {

											String email = strLine.substring(1, strLine.indexOf("': '"));
											String password = strLine.substring(strLine.indexOf("': '") + 4,
													strLine.indexOf("',"));

											Account a = new Account();
											a.email = email;
											a.password = password;

											accountsToVerify.add(a);

										}

									}

									//Close the input stream
									br.close();

									//begin the threaded

									try {
										AgeOfAccountEngine.toCheckAge = accountsToVerify;
										AgeOfAccountEngine.ageOfAccounts();
									} catch (ExecutionException e) {
										e.printStackTrace();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}

								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							} else {
								print("File is not valid " + directoryOfAccountFileToCheck);
							}

						}
					}.start();

				} catch (Exception e) {
					e.printStackTrace();
					ErrorDialog.displayError(e, "Fatal error");
				}
			}
		});
		runBtn.setBounds(0, 253, 569, 25);
		UI.add(runBtn);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 569, 237);
		UI.add(scrollPane);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

		progressBar = new JProgressBar();
		progressBar.setBounds(0, 236, 569, 42);
		UI.add(progressBar);

		print("Welcome to " + Constants.TITLE + " - age of mules!");
		print("Ready...");
	}

	public static void print(String printToConsole) {
		System.out.println(printToConsole);
		textArea.append(printToConsole+"\n");
	}

	protected void updateUI() {

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

	public int amountOfLinesInFile(String filePath) {
		int total = 0;
		try {
			FileInputStream fstream;
			fstream = new FileInputStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			//Read File Line By Line
			while (br.readLine() != null) {
				total++;
			}
			//Close the input stream
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return total;
	}

}
