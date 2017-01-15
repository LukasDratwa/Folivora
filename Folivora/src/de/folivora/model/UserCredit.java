package de.folivora.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.bson.types.ObjectId;

import de.folivora.util.Util;

/**
 * Class to represent the credit of a {@link User}
 * 
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
@Entity
public class UserCredit {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private ObjectId id;
	private double balance;
	private Date lastModification,
				 creationTimestamp;
	
	@OneToOne(targetEntity=User.class)
	private User owner;
	
	@Transient
	private List<Transaction> referencedTransactions = new ArrayList<Transaction>();
	
	public UserCredit(double balance, User owner) {
		this.balance = balance;
		this.owner = owner;
		this.creationTimestamp = new Date();
	}
	
	/**
	 * Protected default constructor for hibernate mapping.
	 */
	protected UserCredit() {
		
	}
	
	/**
	 * Method to get the max. costs and rewards which are possible with this credit to get not a 
	 * negative credit balance of the user.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @return the max costs and rewards
	 */
	public double getMaxPossiblePriceForSr() {
		if(getBalance() <= 1) {
			return (getBalance() - 0.1 > 0) ? getBalance() - 0.1 : 0;
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
		setLastModification(new Date());
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

	/**
	 * @return the creationTimestamp
	 */
	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	/**
	 * @param creationTimestamp the creationTimestamp to set
	 */
	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}
}