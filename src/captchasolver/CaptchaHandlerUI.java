package captchasolver;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import util.Constants;
import util.ErrorDialog;
import util.Notification;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CaptchaHandlerUI {

	public static JFrame frmSolver;
	public static JLabel amountLeft;

	public static ArrayList<String> links = new ArrayList<String>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					CaptchaHandlerUI window = new CaptchaHandlerUI();
					window.frmSolver.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CaptchaHandlerUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSolver = new JFrame();
		frmSolver.setTitle("Solver");
		frmSolver.setLocationRelativeTo(null);

		try {
			frmSolver.setIconImage(new ImageIcon(ImageIO.read(new URL(Constants.CAPTCHAICONURL))).getImage());
		} catch (IOException e) {
			ErrorDialog.displayError(e, "Could not get icon from URL");
			e.printStackTrace();
		}
		frmSolver.setBounds(100, 100, 450, 300);
		frmSolver.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSolver.getContentPane().setLayout(null);

		amountLeft = new JLabel(links.size() + "");
		amountLeft.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 80));
		amountLeft.setHorizontalAlignment(SwingConstants.CENTER);
		amountLeft.setBounds(160, 52, 115, 108);
		frmSolver.getContentPane().add(amountLeft);

		JLabel lblAmountLeft = new JLabel("Amount left :");
		lblAmountLeft.setHorizontalAlignment(SwingConstants.CENTER);
		lblAmountLeft.setBounds(160, 16, 115, 20);
		frmSolver.getContentPane().add(lblAmountLeft);

		JButton btnStartSolving = new JButton("Start");
		btnStartSolving.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				btnStartSolving.setEnabled(false);

				GlobalKeyListener.run();

				openAndTakeNext();
			}
		});
		btnStartSolving.setBounds(160, 182, 115, 29);
		frmSolver.getContentPane().add(btnStartSolving);

	}

	public static void openAndTakeNext() {

		if (!(links.size() == 0)) {

			if (links.size() == 1) {
				Notification.notify("Last one!");
			} else if (links.size() == 5) {
				Notification.notify("Only 5 left!");
			} else if (links.size() == 10) {
				Notification.notify("Only 10 left!");
			} else if (links.size() == 15) {
				Notification.notify("Only 15 left!");
			}

			String link = links.get(0);
			links.remove(0);

			amountLeft.setText(links.size() + "");

			Browser.open(link);

		} else {
			System.out.println("End reached");
		}

	}

}
