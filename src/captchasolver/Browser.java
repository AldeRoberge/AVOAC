package captchasolver;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class Browser {
	public static void open(String toOpen) {
		if (Desktop.isDesktopSupported()) {

			try {
				URL url = new URL(toOpen);
				String nullFragment = null;
				URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), nullFragment);
				System.out.println("URI " + uri.toString() + " is OK");

				try {
					Desktop.getDesktop().browse(uri);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			} catch (MalformedURLException e) {
				System.out.println("URL " + toOpen + " is a malformed URL");
			} catch (URISyntaxException e) {
				System.out.println("URI " + toOpen + " is a malformed URL");
			}

		}
	}
}
