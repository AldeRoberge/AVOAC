package util;

import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class PasswordValidator {

	/*public static void main(String... args) {

		System.out.println(getToolTipForPassword("WADJAWdj1"));

	}*/

	public static String getToolTipForPassword(String password) {
		int passwordValidatorAnswer = PasswordValidator.validatePassword(password);
		String tooltip = null;
		switch (passwordValidatorAnswer) {
		case -2:
			tooltip = "Password must not have repeated characters";
			break;
		case -1:
			tooltip = "Password must have at least 10 characters";
			break;
		case 0:
			tooltip = "Password must have at least 1 digit";
			break;
		case 1:
			tooltip = "Password seems O.K.";
			break;
		}

		return tooltip;
	}

	private static final Pattern DECIMAL_DIGIT = Pattern.compile("\\p{Nd}");

	public static int validatePassword(final String password) {
		/*
		 * 1 = valid
		 * 0 = needs at least 1 digit
		 * -1 = needs 10 characters
		 * -2 = repeated characters
		 */

		if (containsDigit(password)) {
			
			
			byte[] s = password.getBytes(Charset.forName("UTF-8"));

			for (int i = 0; i < password.length() - 2; i++) {
				if (s[i] == s[i + 1] && s[i] == s[i + 2]) {
					return -2;
				}
			}
			
			
			
			if (hasTenCharacters(password)) {
				return 1; //10 char + digit
			} else {
				return -1; //needs 10 char
			}
		} else {
			return 0; //needs digit
		}
	}

	private static boolean hasTenCharacters(String password) {
		if (password.length() >= 10) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean containsDigit(final String str) {
		return DECIMAL_DIGIT.matcher(str).find();
	}

}