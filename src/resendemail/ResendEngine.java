package resendemail;

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

public class ResendEngine implements Runnable {

	public static ArrayList<Account> toResend = new ArrayList<Account>();

	private Account accountToVerify;

	public ResendEngine(Account account) {
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

		waitingForAnswer.add(accountToVerify);

		Account a = accountToVerify;

		String email = a.email;
				
				//.replace("+", "%2B");

		String request = "https://realmofthemadgodhrd.appspot.com/account/sendVerifyEmail?g=" + email;

		System.out.println(request);
		
		try {
			
			System.out.println("Yo");

			URI uri = new URI(request);
			URL url = uri.toURL();
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String answer = in.readLine();
			in.close();

			current++;
			ResendEmailUI.setProgressBar(current, total);

			if (answer.contains("<Success>")) {
				success++;
				ResendEmailUI.print(">Success!");

			} else if (answer.contains("<Error>")) {
				fail++;
				a.comment = "// Failed " + answer;

				if (answer.contains("Error.emailAlreadyUsed")) {
					ResendEmailUI.print(">Email " + email + " already in use.");
				} else if (answer.contains("pleaseTryAgain")) {
					ResendEmailUI.print(">Limit of accounts reached.");
				} else if (answer.contains("repeatError")) {
					ResendEmailUI.print(">Error : password contains repeated characters.");
				} else {
					ResendEmailUI.print(">Error : " + answer);
				}
			} else {
				ResendEmailUI.print(">Unknown answer, please contact Alde : " + answer);
			}

			waitingForAnswer.remove(accountToVerify);

		} catch (Exception ex) {
			ResendEmailUI.print("ERROR, " + ex.getMessage());
			System.err.println("ERROR with " + email);
			ex.printStackTrace();
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" }) // I truly dont know
	public static void verifyAccounts() throws ExecutionException, InterruptedException {

		ResendEmailUI.setProgressBar(0, 0);
		ResendEmailUI.progressBar.setVisible(true);

		total = toResend.size();

		System.out.println("Running engine...");
		waitingForAnswer.clear();

		ExecutorService service = Executors.newFixedThreadPool(poolSize);
		List<Future<Runnable>> futures = new ArrayList<Future<Runnable>>();

		for (Account a : toResend) {

			Future f = service.submit(new ResendEngine(a));
			futures.add(f);

		}

		for (Future<Runnable> f : futures) {
			f.get();
		}

		System.out.println("Ended");
		ResendEmailUI.print("Registration proccess ended.");
		ResendEmailUI.print("Success : " + success + " Failed : " + fail);

		service.shutdownNow();

	}

}