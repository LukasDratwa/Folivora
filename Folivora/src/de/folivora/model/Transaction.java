package de.folivora.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * @author Lukas
 */
@Entity
public class Transaction {
	@Id
	private long id;
	private Date executionDate;
	private double value;
	private double fee;
	private boolean executed;
	private boolean cancelled;
	private Long cacelTransactionId;
	private String unlockToken;
	
	@OneToOne(targetEntity=SearchRequest.class)
	private SearchRequest referencedSr;
	
	@OneToOne(targetEntity=User.class)
	private User uFrom,
				 uTo;

	public Transaction(long id, double value, double fee, User uFrom, User uTo,
			String unlockToken, SearchRequest referencedSr) {
		this.id = id;
		this.value = value;
		this.fee = fee;
		this.uFrom = uFrom;
		this.uTo = uTo;
		this.executed = false;
		this.cancelled = false;
		this.unlockToken = unlockToken;
		this.referencedSr = referencedSr;
	}
	
	@Override
	public String toString() {
		return "[userFromRef=\"" + this.uFrom.getName() + "\" (" + this.uFrom.getId() + ")"
			+ ", userToRef=\"" + this.uTo.getName() + "\" (" + this.uTo.getId() + ")"
			+ ", value=" + this.value + ", fee=" + this.fee + ", executed=" + this.executed + "]";
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

	/**
	 * @return the cancelled
	 */
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * @param cancelled the cancelled to set
	 */
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	/**
	 * @return the cacelTransactionId
	 */
	public Long getCacelTransactionId() {
		return cacelTransactionId;
	}

	/**
	 * @param cacelTransactionId the cacelTransactionId to set
	 */
	public void setCacelTransactionId(Long cacelTransactionId) {
		this.cacelTransactionId = cacelTransactionId;
	}

	/**
	 * Get the token which the delivering user has to enter in the application to
	 * execute the transaction and get his money. The searching user has to provide
	 * this token.
	 * 
	 * @return the unlockToken
	 */
	public String getUnlockToken() {
		return unlockToken;
	}

	/**
	 * @return the referencedSr
	 */
	public SearchRequest getReferencedSr() {
		return referencedSr;
	}

	/**
	 * @param referencedSr the referencedSr to set
	 */
	public void setReferencedSr(SearchRequest referencedSr) {
		this.referencedSr = referencedSr;
	}

	/**
	 * @return the fee
	 */
	public double getFee() {
		return fee;
	}

	/**
	 * @param fee the fee to set
	 */
	public void setFee(double fee) {
		this.fee = fee;
	}

	/**
	 * @return the uFrom
	 */
	public User getuFrom() {
		return uFrom;
	}

	/**
	 * @param uFrom the uFrom to set
	 */
	public void setuFrom(User uFrom) {
		this.uFrom = uFrom;
	}

	/**
	 * @return the uTo
	 */
	public User getuTo() {
		return uTo;
	}

	/**
	 * @param uTo the uTo to set
	 */
	public void setuTo(User uTo) {
		this.uTo = uTo;
	}
}