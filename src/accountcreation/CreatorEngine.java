package accountcreation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import captchasolver.RunLoginUI;
import util.Account;
import util.Name;

public class CreatorEngine implements Runnable {

	public static ArrayList<Account> toValidate = new ArrayList<Account>();

	private Account accountToVerify;

	public CreatorEngine(Account account) {
		accountToVerify = account;
	}

	public static int current = 0;
	public static int total = 0;

	public static int success = 0;
	public static int fail = 0;

	private static int poolSize = 1;

	public static boolean isRunning = false;

	public static ArrayList<Account> waitingForAnswer = new ArrayList<Account>();

	public void run() {

		/*THANKS TO 059 and Einaras for the URL! <3
		 */

		waitingForAnswer.add(accountToVerify);

		Account a = accountToVerify;

		String email = a.email.replace("+", "%2B");

		String request = "https://realmofthemadgodhrd.appspot.com/account/register?isAgeVerified=1&guid=" + a.guid
				+ "&newGUID=" + email + "&newPassword=" + a.password;

		try {

			URI uri = new URI(request);
			URL url = uri.toURL();
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String answer = in.readLine();
			in.close();

			current++;
			RunCreatorUI.setProgressBar(current, total);

			if (answer.contains("<Success>")) {
				success++;
				RunCreatorUI.print(">Success!");

				//set comment, so we can save the name in the .js
				for (Name e : Name.values()) {
					if (e.getValue() == a.guid) {
						a.comment = "//" + e.toString();
					}
				}

			} else if (answer.contains("<Error>")) {
				fail++;
				a.comment = "// Failed " + answer;

				if (answer.contains("Error.emailAlreadyUsed")) {
					RunCreatorUI.print(">Email " + email + " already in use.");
				} else if (answer.contains("pleaseTryAgain")) {
					RunCreatorUI.print(">Limit of accounts reached.");
				} else if (answer.contains("repeatError")) {
					RunCreatorUI.print(">Error : password contains repeated characters.");
				} else {
					RunCreatorUI.print(">Error : " + answer);
				}
			} else {
				RunCreatorUI.print(">Unknown answer, please contact Alde : " + answer);
			}

			SaveToFileMachine.addData(a);

			waitingForAnswer.remove(accountToVerify);

		} catch (Exception ex) {
			RunCreatorUI.print("ERROR, " + ex.getMessage());
			System.err.println("ERROR with " + email);
			ex.printStackTrace();
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" }) // I truly dont know
	public static void registerAccounts() throws ExecutionException, InterruptedException {

		RunCreatorUI.setProgressBar(0, 0);
		RunCreatorUI.progressBar.setVisible(true);

		total = toValidate.size();

		System.out.println("Running engine...");
		waitingForAnswer.clear();

		ExecutorService service = Executors.newFixedThreadPool(poolSize);
		List<Future<Runnable>> futures = new ArrayList<Future<Runnable>>();

		for (Account a : toValidate) {

			Future f = service.submit(new CreatorEngine(a));
			futures.add(f);

		}

		for (Future<Runnable> f : futures) {
			f.get();
		}

		System.out.println("Ended");
		RunCreatorUI.print("Registration proccess ended.");
		RunCreatorUI.print("Success : " + success + " Failed : " + fail);

		service.shutdownNow();

	}

}