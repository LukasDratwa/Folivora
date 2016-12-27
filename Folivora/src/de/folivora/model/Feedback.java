package de.folivora.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Feedback {
	@Id
	private long id;
	private String description;
	
	@Enumerated(EnumType.STRING)
	private Rating rating;
	
	@ManyToOne(targetEntity=User.class)
	private User feedbackCreator;
	
	public Feedback(long id, Rating rating, String description, User feedbackCreator) {
		this.id = id;
		this.rating = rating;
		this.description = description;
		this.feedbackCreator = feedbackCreator;
	}
	
	/**
	 * Protected default constructor for hibernate mapping.
	 */
	public Feedback() {
		
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

	/**
	 * @return the feedbackCreator
	 */
	public User getFeedbackCreator() {
		return feedbackCreator;
	}

	/**
	 * @param feedbackCreator the feedbackCreator to set
	 */
	public void setFeedbackCreator(User feedbackCreator) {
		this.feedbackCreator = feedbackCreator;
	}
}