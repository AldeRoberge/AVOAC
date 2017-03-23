package util;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

public class Notification extends javax.swing.JFrame {

	static Image image = Toolkit.getDefaultToolkit().getImage("tray.gif");
	static TrayIcon trayIcon = new TrayIcon(image, "Notification");

	static {
		SystemTray tray = SystemTray.getSystemTray();
		try {
			tray.add(trayIcon);
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void notify(String notif) {
		trayIcon.displayMessage("Notification", notif, MessageType.INFO);
	}

}