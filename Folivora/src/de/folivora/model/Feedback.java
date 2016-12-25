package de.folivora.model;

public class Feedback {
	private long id;
	private Rating rating;
	private String description;
	
	public Feedback(long id, Rating rating, String description) {
		this.id = id;
		this.rating = rating;
		this.description = description;
	}
	
	/**
	 * @return the rating
	 */
	public Rating getRating() {
		return rating;
	}
	
	/**
	 * @param rating the rating to set
	 */
	public void setRating(Rating rating) {
		this.rating = rating;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
}