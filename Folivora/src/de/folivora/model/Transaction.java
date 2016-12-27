package de.folivora.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Transaction {
	@Id
	private long id;
	private Date executionDate;
	private double value;
	
	/**
	 * userFrom = item searching person, userTo = delivering person
	 */
	@OneToOne(targetEntity=User.class)
	private User userFrom,
				 userTo;
	
	/**
	 * feedbackFrom = feedback of the searching for the delivering person,
	 * feedbackTo = feedback of the delivering for the searching person
	 */
	@OneToOne(targetEntity=Feedback.class)
	private Feedback feedbackFrom = null,
					 feedbackTo = null;
	
	public Transaction(long id, Date executionDate, double value, User userFrom, User userTo) {
		this.id = id;
		this.executionDate = executionDate;
		this.value = value;
		this.userFrom = userFrom;
		this.userTo = userTo;
	}
	
	/**
	 * Protected default constructor for hibernate mapping.
	 */
	protected Transaction() {
		
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
	 * @return the executionDate
	 */
	public Date getExecutionDate() {
		return executionDate;
	}
	
	/**
	 * @param executionDate the executionDate to set
	 */
	public void setExecutionDate(Date executionDate) {
		this.executionDate = executionDate;
	}
	
	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}
	
	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}
	
	/**
	 * @return the userFrom
	 */
	public User getUserFrom() {
		return userFrom;
	}
	
	/**
	 * @param userFrom the userFrom to set
	 */
	public void setUserFrom(User userFrom) {
		this.userFrom = userFrom;
	}
	
	/**
	 * @return the userTo
	 */
	public User getUserTo() {
		return userTo;
	}
	
	/**
	 * @param userTo the userTo to set
	 */
	public void setUserTo(User userTo) {
		this.userTo = userTo;
	}

	/**
	 * @return the feedbackTo
	 */
	public Feedback getFeedbackTo() {
		return feedbackTo;
	}

	/**
	 * @param feedbackTo the feedbackTo to set
	 */
	public void setFeedbackTo(Feedback feedbackTo) {
		this.feedbackTo = feedbackTo;
	}

	/**
	 * @return the feedbackFrom
	 */
	public Feedback getFeedbackFrom() {
		return feedbackFrom;
	}

	/**
	 * @param feedbackFrom the feedbackFrom to set
	 */
	public void setFeedbackFrom(Feedback feedbackFrom) {
		this.feedbackFrom = feedbackFrom;
	}
}