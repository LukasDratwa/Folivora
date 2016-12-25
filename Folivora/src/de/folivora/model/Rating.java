package de.folivora.model;

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