package ageOfAccounts;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import util.Account;
import util.Base64Utils;

public class AgeOfAccountEngine implements Runnable {

	public static ArrayList<Account> toCheckAge = new ArrayList<Account>();

	private Account accountToVerify;

	public AgeOfAccountEngine(Account account) {
		accountToVerify = account;
	}

	public static int current = 0;
	public static int total = 0;

	public static int success = 0;
	public static int fail = 0;

	private static int poolSize = 20;

	public static boolean isRunning = false;

	public static ArrayList<Account> waitingForAnswer = new ArrayList<Account>();

	public void run() {

		/*THANKS TO 059 and Einaras for the URL! <3
		 */

		waitingForAnswer.add(accountToVerify);

		Account a = accountToVerify;

		String email = a.email.replace("+", "%2B");

		String request = "https://realmofthemadgodhrd.appspot.com/char/list?guid=" + email + "&password=" + a.password;
		//RunAgeOfAccountsUI.print(request);

		try {

			URI uri = new URI(request);
			URL url = uri.toURL();
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String answer = in.readLine();
			in.close();

			current++;
			RunAgeOfAccountsUI.setProgressBar(current, total);

			if (answer.contains("<Error>")) {
				fail++;
				a.comment = "// Failed " + answer;

				if (answer.contains("Error.emailAlreadyUsed")) {
					RunAgeOfAccountsUI.print(">Email " + email + " already in use.");
				} else if (answer.contains("pleaseTryAgain")) {
					RunAgeOfAccountsUI.print(">Limit of accounts reached.");
				} else if (answer.contains("repeatError")) {
					RunAgeOfAccountsUI.print(">Error : password contains repeated characters.");
				} else {
					RunAgeOfAccountsUI.print(">Error : " + answer);
				}
			} else {

				if (answer.contains("<SalesForce>")) {

					//RunAgeOfAccountsUI.print(answer);

					//<SalesForce>bGlmZXRpbWVfc3BlbmQ9MCZnYW1lX25ldHdvcmtfaWQ9Jm5haWQ9MzQ0ODczNDExJmdhbWVfaWQ9MjQmcGxheWVyX2lkPTI0LjIxMi45OC42NSZ1c2VyX2lkPW1pZ2h0eW1pY2s5MisyMThAZ21haWwuY29tJmRhdGVfam9pbmVkPTAyMjUyMDE3JnBsYXllcl9uYW1lPSZsYW5ndWFnZT1lbiZkZXZpY2VfbW9kZWw9Q2hyb21lJmdhbWVfc2hvcnQ9cm1nJmdhbWVfbmV0d29ya19uYW1lPXJvdG1nJm9zX3N5c3RlbT1NYWMmcmVsZWFzZT1yZWxlYXNlLTExLTIwMTcwMzA5MTkwOS1iZmUxODE4LjM5OTcyMzc1NzY1Mzk3NjYwOA==</SalesForce>

					String salesForceString = answer.substring(answer.indexOf("<SalesForce>") + 12,
							answer.indexOf("</SalesForce>"));

					String decodedSalesForceString = Base64Utils.decode(salesForceString);

					String date = decodedSalesForceString.substring(
							decodedSalesForceString.indexOf("date_joined=") + 12,
							decodedSalesForceString.indexOf("&player_name"));

					int day = Integer.parseInt(date.substring(0, 2));
					int month = Integer.parseInt(date.substring(2, 4));
					int year = Integer.parseInt(date.substring(4, 8));

					RunAgeOfAccountsUI.print(
							accountToVerify.email + " was created the " + day + " " + month + " " + year + ". "+date);

					/*Date myDate = new GregorianCalendar(year, month, day).getTime();
					
					DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
					Date today = new Date();
					
					long e = getDateDiff(today,  TimeUnit.MILLISECONDS);
					
					long days = TimeUnit.MILLISECONDS.toDays(e);
					
					RunAgeOfAccountsUI.print("Created " + days + " days ago.");
					
					//da*te_joined=02252017&player_name=&language=en&device_model=Chrome&game_short=rmg&game_network_name=rotmg&os_system=Mac&release=release-11-201703091909-bfe1818.399723757653976608
					*/

				} else {
					//System.err.println("Invalid email "+accountToVerify.email);
				}
			}

			//SaveToFileMachine.addData(a);

			waitingForAnswer.remove(accountToVerify);

		} catch (Exception ex) {
			RunAgeOfAccountsUI.print("ERROR, " + ex.getMessage());
			System.err.println("ERROR with " + email);
			ex.printStackTrace();
		}

	}

	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void ageOfAccounts() throws ExecutionException, InterruptedException {

		RunAgeOfAccountsUI.setProgressBar(0, 0);
		RunAgeOfAccountsUI.progressBar.setVisible(true);

		total = toCheckAge.size();

		RunAgeOfAccountsUI.print("Running engine...");
		waitingForAnswer.clear();

		ExecutorService service = Executors.newFixedThreadPool(poolSize);
		List<Future<Runnable>> futures = new ArrayList<Future<Runnable>>();

		for (Account a : toCheckAge) {

			Future f = service.submit(new AgeOfAccountEngine(a));
			futures.add(f);

		}

		for (Future<Runnable> f : futures) {
			f.get();
		}

		RunAgeOfAccountsUI.print("Ended.");

		service.shutdownNow();

	}

}