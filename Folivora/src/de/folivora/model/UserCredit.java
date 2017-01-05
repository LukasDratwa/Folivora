package de.folivora.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import de.folivora.util.Util;

@Entity
public class UserCredit {
	@Id
	private long id;
	private double balance;
	private Date lastModification;
	
	@OneToOne(targetEntity=User.class)
	private User owner;
	
	@Transient
	private List<Transaction> referencedTransactions = new ArrayList<Transaction>();
	
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
	
	public double getMaxPossiblePriceForSr() {
		if(getBalance() <= 1) {
			return getBalance() - 0.1;
		} else if(getBalance() < 10) {
			double max = getBalance() / 1.1;
			return Util.round(max, 2);
		} else {
			return getBalance() - 1;
		}
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
	 * @return the referencedTransactions
	 */
	public List<Transaction> getReferencedTransactions() {
		return referencedTransactions;
	}

	/**
	 * @param referencedTransactions the executedTransactions to set
	 */
	public void setReferencedTransactions(List<Transaction> referencedTransactions) {
		this.referencedTransactions = referencedTransactions;
	}
}