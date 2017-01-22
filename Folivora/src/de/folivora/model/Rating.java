package de.folivora.model;

/**
 * Class to represent a rating which is included in a {@link Feedback}. 
 * 
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
public enum Rating {
	VERY_BAD(1, "sehr schlecht"), BAD(2, "schlecht"), OKAY(3, "okay"), GOOD(4, "gut"), VERY_GOOD(5, "sehr gut");
	
	private final int val;
	private final String description;
	
	Rating(int val, String description) {
		this.val = val;
		this.description = description;
	}

	/**
	 * Method to get the rating as a string to display it in the frontend.
	 * 
	 * <hr>Created on 22.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @return the string
	 */
	public String getAsString() {
		return this.getDescription() + " (" + this.getVal() + " von 5 Sternen)";
	}
	
	/**
	 * @return the val
	 */
	public int getVal() {
		return val;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
}