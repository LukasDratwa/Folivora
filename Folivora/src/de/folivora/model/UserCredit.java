package de.folivora.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class UserCredit {
	@Id
	private long id;
	private double balance;
	private Date lastModification;
	
	private Long lastExecutedTransactionId = null;
	
	@OneToOne(targetEntity=User.class)
	private User owner;
	
	public UserCredit(long id, double balance, User owner) {
		this.id = id;
		this.balance = balance;
		this.owner = owner;
	}
	
	/**
	 * Protected default constructor for hibernate mapping.
	 */
	protected UserCredit() {
		
	}
	
	@Override
	public String toString() {
		return this.balance + " €";
	}
	
	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}
	
	/**
	 * @return the lastModification
	 */
	public Date getLastModification() {
		return lastModification;
	}
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the owner
	 */
	public User getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * @param lastModification the lastModification to set
	 */
	public void setLastModification(Date lastModification) {
		this.lastModification = lastModification;
	}

	/**
	 * @return the lastExecutedTransactionId
	 */
	public Long getLastExecutedTransactionId() {
		return lastExecutedTransactionId;
	}

	/**
	 * @param lastExecutedTransactionId the lastExecutedTransactionId to set
	 */
	public void setLastExecutedTransactionId(Long lastExecutedTransactionId) {
		this.lastExecutedTransactionId = lastExecutedTransactionId;
	}
}