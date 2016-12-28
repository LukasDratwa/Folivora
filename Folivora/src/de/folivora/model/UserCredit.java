package de.folivora.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class UserCredit {
	@Id
	private long id;
	private double balance;
	private Date lastModification;
	
	@OneToOne(targetEntity=User.class)
	private User owner;
	
	@Transient
	private List<Transaction> executedTransactions = new ArrayList<Transaction>();
	
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
		return this.balance + " �";
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
	 * @return the executedTransactions
	 */
	public List<Transaction> getExecutedTransactions() {
		return executedTransactions;
	}

	/**
	 * @param executedTransactions the executedTransactions to set
	 */
	public void setExecutedTransactions(List<Transaction> executedTransactions) {
		this.executedTransactions = executedTransactions;
	}
}