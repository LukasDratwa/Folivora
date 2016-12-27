package de.folivora.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Transaction {
	@Id
	private long id;
	private Date executionDate;
	private double value;
	private boolean executed;
	
	@OneToOne(targetEntity=User.class)
	private User userSearching,
				 userDelivering;

	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER, targetEntity=Feedback.class)
	private Feedback feedbackOfSearchingUser = null,
					 feedbackOfDeliveringUser = null;
	
	public Transaction(long id, Date executionDate, double value, User userSearching, User userDelivering) {
		this.id = id;
		this.executionDate = executionDate;
		this.value = value;
		this.userSearching = userSearching;
		this.userDelivering = userDelivering;
		this.setExecuted(false);
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
	 * @return the userSearching
	 */
	public User getUserSearching() {
		return userSearching;
	}

	/**
	 * @param userSearching the userSearching to set
	 */
	public void setUserSearching(User userSearching) {
		this.userSearching = userSearching;
	}

	/**
	 * @return the userDelivering
	 */
	public User getUserDelivering() {
		return userDelivering;
	}

	/**
	 * @param userDelivering the userDelivering to set
	 */
	public void setUserDelivering(User userDelivering) {
		this.userDelivering = userDelivering;
	}

	/**
	 * @return the feedbackOfSearchingUser
	 */
	public Feedback getFeedbackOfSearchingUser() {
		return feedbackOfSearchingUser;
	}

	/**
	 * @param feedbackOfSearchingUser the feedbackOfSearchingUser to set
	 */
	public void setFeedbackOfSearchingUser(Feedback feedbackOfSearchingUser) {
		this.feedbackOfSearchingUser = feedbackOfSearchingUser;
	}

	/**
	 * @return the feedbackOfDeliveringUser
	 */
	public Feedback getFeedbackOfDeliveringUser() {
		return feedbackOfDeliveringUser;
	}

	/**
	 * @param feedbackOfDeliveringUser the feedbackOfDeliveringUser to set
	 */
	public void setFeedbackOfDeliveringUser(Feedback feedbackOfDeliveringUser) {
		this.feedbackOfDeliveringUser = feedbackOfDeliveringUser;
	}

	/**
	 * @return the executed
	 */
	public boolean isExecuted() {
		return executed;
	}

	/**
	 * @param executed the executed to set
	 */
	public void setExecuted(boolean executed) {
		this.executed = executed;
	}
}