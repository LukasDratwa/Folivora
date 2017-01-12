package de.folivora.model;

public class Constants {
	public static final double VERSION = 1.1;
	public static final long SERIAL_VERSION_UID = 1841986427873375189L;
	
	public static final boolean USERCREDIT_ENABLE_INITIAL_BALANCE = true;
	public static final double USERCREDIT_INITIAL_BALANCE = 100.00;
	
	/**
	 * Token will be valid for one hour.
	 */
	public static final long TOKEN_SESSION_EXPIRATION_TIME = 3600000;
	public static final int TOKEN_SESSION_LENGTH = 35;
	public static final int TOKEN_SEARCHREQUEST_LENGTH = 5;
	
	public static final long UPDATE_THREAD_INTERVAL = 5000;
	
	public static final String MAP_MARKER_RED_URL = "http://maps.google.com/mapfiles/ms/icons/red-dot.png";
	public static final String MAP_MARKER_ORANGE_URL = "http://maps.google.com/mapfiles/ms/icons/orange-dot.png";
	public static final String MAP_MARKER_GREEN_URL = "http://maps.google.com/mapfiles/ms/icons/green-dot.png";
	public static final String MAP_MARKER_BLUE_URL = "http://maps.google.com/mapfiles/ms/icons/blue-dot.png";
	
	private static final long ONE_HOUR = 3600000;
	public static final long MAP_MARKER_ORANGE_TIME_LEFT = ONE_HOUR * 4;
	public static final long MAP_MARKER_RED_TIME_LEFT = ONE_HOUR;
}