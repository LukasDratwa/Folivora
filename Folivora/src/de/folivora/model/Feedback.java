package de.folivora.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.bson.types.ObjectId;

/**
 * Class to represent feedbacks between users based on a finished search request.
 * 
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
@Entity
public class Feedback {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private ObjectId id;
	private String description;
	
	@Enumerated(EnumType.STRING)
	private Rating rating;
	
	@OneToOne(targetEntity=User.class)
	private User feedbackCreator;
	
	@OneToOne(targetEntity=SearchRequest.class)
	private SearchRequest referencedSearchRequest;
	
	public Feedback(Rating rating, String description, User feedbackCreator, SearchRequest referencedSr) {
		this.rating = rating;
		this.description = description;
		this.feedbackCreator = feedbackCreator;
		this.referencedSearchRequest = referencedSr;
	}
	
	/**
	 * Protected default constructor for hibernate mapping.
	 */
	public Feedback() {
		
	}
	
	@Override
	public String toString() {
		return "[id=" + this.id + ", searchRequestRef=" + this.referencedSearchRequest.getId()
			+ ", userCreatorRef=" + this.feedbackCreator.getId().toString() + ", rating=" + this.rating + "]";
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

	/**
	 * @return the referencedSearchRequest
	 */
	public SearchRequest getReferencedSearchRequest() {
		return referencedSearchRequest;
	}

	/**
	 * @param referencedSearchRequest the referencedSearchRequest to set
	 */
	public void setReferencedSearchRequest(SearchRequest referencedSearchRequest) {
		this.referencedSearchRequest = referencedSearchRequest;
	}
}