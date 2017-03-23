package accountcreation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import util.Account;
import util.AccountComparator;

public class SaveToFileMachine {
	private static ArrayList<Account> toSave = new ArrayList<Account>();

	public static final String outputJsFileName = "output.js";
	static File d = new File(outputJsFileName);

	public static void addData(Account toAdd) {

		synchronized (toSave) {
			Iterator<Account> iter = toSave.iterator();

			while (iter.hasNext()) {
				Account a = iter.next();
				if (!a.email.equals(toAdd.email) && !a.password.equals(toAdd.password)) {
					iter.remove();
				}

			}

			toSave.add(toAdd);
		}

	}

	public static void saveToFile() {

		if (toSave.isEmpty()) {
			RunCreatorUI.print("Error, no accounts to save...");
		} else {
			RunCreatorUI.print("Saving to file...");

			//we need to sort first

			Collections.sort(toSave, new AccountComparator());

			// 'email+0@gmail.com': 'Password69',

			try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(d, true)))) {
				out.println("//BEGINNING OF NEW SAVE");
				for (Account a : toSave) {
					out.println("'" + a.email + "': '" + a.password + "', " + a.comment);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			RunCreatorUI.print("Save to file ended.");

			openInBrowser();
			//end
			toSave.clear();

		}

	}

	private static void openInBrowser() {
		//Opens and select the file in explorer

		File currentDirectory = new File(new File(".").getAbsolutePath());

		try {

			String filePathOnDisk = currentDirectory.getCanonicalPath() + "\\" + outputJsFileName;
			RunCreatorUI.print("Trying to open " + filePathOnDisk + ".");

			System.out.println(filePathOnDisk);

			Runtime.getRuntime().exec("explorer.exe /select," + filePathOnDisk);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getOutputFileDirectory() {

		File currentDirectory = new File(new File(".").getAbsolutePath());

		try {
			return currentDirectory.getCanonicalPath() + "\\" + SaveToFileMachine.outputJsFileName;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
