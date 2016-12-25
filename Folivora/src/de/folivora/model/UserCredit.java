package de.folivora.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class UserCredit {
	@Id
	private long id;
	private double balance;
	private Date lastModification;
	
	@ElementCollection
	private List<Long> transactionIds = new ArrayList<Long>();
	
	@OneToOne(targetEntity=Transaction.class)
	private Transaction lastExecutedTransaction;
	
	@OneToOne(targetEntity=User.class)
	private User owner;
	
	public UserCredit(long id, double balance, User owner) {
		this.id = id;
		this.balance = balance;
		this.owner = owner;
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
	 * @return the lastExecutedTransaction
	 */
	public Transaction getLastExecutedTransaction() {
		return lastExecutedTransaction;
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
	 * @return the transactionIds
	 */
	public List<Long> getTransactionIds() {
		return transactionIds;
	}

	/**
	 * @param transactionIds the transactionIds to set
	 */
	public void setTransactionIds(List<Long> transactionIds) {
		this.transactionIds = transactionIds;
	}
}