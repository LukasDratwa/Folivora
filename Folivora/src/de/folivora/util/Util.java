package de.folivora.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.folivora.model.Constants;

/**
 * Utility class for folivora.
 * 
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
public class Util {
	private static SecureRandom secureRandom = new SecureRandom();
	
	/**
	 * Method to format a date.
	 * 
	 * <hr>Created on 15.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param date - the date
	 * @param pattern - the pattern, have a look at https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	/**
	 * Method to format a date to f.e. "01.01.2010, 13:14 Uhr" with the pattern "dd.MM.yyyy ', ' HH:mm 'Uhr'".
	 * Have a look at {@link Util#formatDate(Date, String)}.
	 * 
	 * <hr>Created on 15.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param date - the date
	 * @return the formatted date
	 */
	public static String formatDateToDateAndTimeString(Date date) {
		return formatDate(date, "dd.MM.yyyy', ' HH:mm 'Uhr'");
	}
	
	/**
	 * THX: http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
	 * 
	 * @param value - the input double value
	 * @param places - number of wished digits after the comma 
	 * @return the rounded double value
	 */
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	/**
	 * Method to get a random string / token.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param uppercase - true if all chars of the token should be uppercase
	 * @return the generated token with the length of ~32
	 */
	public static String getToken(boolean uppercase) {
		if(uppercase) {
			return new BigInteger(130, secureRandom).toString(32).toUpperCase();
		} else {
			return new BigInteger(130, secureRandom).toString(32);
		}
	}
	
	/**
	 * Method to get a random string / token.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param uppercase - true if all chars of the token should be uppercase
	 * @param length - the wished length of the returned token
	 * @param inputString - input for recursion, should be an empty string!
	 * @return the generated token with the specified length
	 */
	public static String getTrimmedToken(boolean uppercase, int length, String inputString) {
		String token = getToken(uppercase);
		
		if(length <= token.length()) {
			return token.substring(0, length);
		} else {
			if(length >= length + inputString.length()) {
				return getTrimmedToken(uppercase, length, token);
			} else {
				return (inputString + token).substring(0, length);
			}
		}
	}
	
	/**
	 * Method to get a token with the length of {@link Constants#TOKEN_SEARCHREQUEST_LENGTH}
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @return the generated token
	 */
	public static String getSrUnlockToken() {
		return getTrimmedToken(true, Constants.TOKEN_SEARCHREQUEST_LENGTH, "");
	}
}