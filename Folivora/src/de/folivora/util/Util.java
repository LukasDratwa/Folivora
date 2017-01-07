package de.folivora.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;

public class Util {
	private static SecureRandom secureRandom = new SecureRandom();
	
	/**
	 * THX: http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
	 * 
	 * @param value
	 * @param places
	 * @return
	 */
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public static String getToken(boolean uppercase) {
		if(uppercase) {
			return new BigInteger(130, secureRandom).toString(32).toUpperCase();
		} else {
			return new BigInteger(130, secureRandom).toString(32);
		}
	}
	
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
	
	public static String getSrUnlockToken() {
		return getTrimmedToken(true, Constants.TOKEN_SEARCHREQUEST_LENGTH, "");
	}
}