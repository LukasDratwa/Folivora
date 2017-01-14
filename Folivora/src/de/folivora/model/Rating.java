package de.folivora.model;

/**
 * Class to represent a rating which is included in a {@link Feedback}. 
 * 
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
public enum Rating {
	VERY_BAD(0), BAD(1), OKAY(2), GOOD(3), VERY_GOOD(4);
	
	private final int val;
	
	Rating(int val) {
		this.val = val;
	}

	/**
	 * @return the val
	 */
	public int getVal() {
		return val;
	}
}