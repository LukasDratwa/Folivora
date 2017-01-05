package de.folivora.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Util {
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
}