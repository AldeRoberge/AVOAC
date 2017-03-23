package captchasolver;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import util.Constants;

public class GMailParser {

	public static int maxProgressBar = 0;
	public static int currentProgressBar = 0;

	public static Folder inbox;

	public static void connect(String email, String password) {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		try {

			//Progress bar
			maxProgressBar = 0;
			currentProgressBar = 0;
			RunLoginUI.setProgressBar(currentProgressBar, maxProgressBar);

			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");
			store.connect("imap.gmail.com", email, password);

			//If we made it this far, we're in!

			inbox = store.getFolder("INBOX");

			inbox.open(Folder.READ_ONLY);

			Message messages[] = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
			System.out.println("No. of Unread Messages : " + messages.length);
			maxProgressBar = messages.length;

			try {
				for (Message msg : messages) {

					Object content = msg.getContent();

					Address[] d = msg.getFrom();

					boolean isFromWildShadow = false;

					for (Address i : d) {
						if (i.toString().equals(Constants.EMAILSENDER)) {
							isFromWildShadow = true;
						}
					}

					if (isFromWildShadow) {

						if (content instanceof String) {

							String body = (String) content;
							

							String link = body.substring(body.indexOf(':') + 1, body.indexOf("If you did"));
							link = link.replaceAll("(?m)^\\s*$[\n\r]{1,}", ""); //remove empty lines and \n

							currentProgressBar++;
							RunLoginUI.setProgressBar(currentProgressBar, maxProgressBar);
							CaptchaHandlerUI.links.add(link);

							System.out.println(link);
						}

					}

				}

				//ended, now open the UI

				RunLoginUI.setProgressBar(100, 100);

				CaptchaHandlerUI.main(null); //open the captcha UI

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			inbox.close(true);
			store.close();

		} catch (

		NoSuchProviderException e) {
			e.printStackTrace();
			RunLoginUI.displayError("'NoSuchProviderException' thrown", "Title", e);
		} catch (AuthenticationFailedException e) {
			e.printStackTrace();
			RunLoginUI.displayError("'AuthenticationFailedException' probably because of invalid credentials",
					"Authentication error", e);

		} catch (MessagingException e) {
			e.printStackTrace();
			RunLoginUI.displayError("'MessagingException' probably because of invalid credentials", "Messaging exception",
					e);
		}
	}
}
