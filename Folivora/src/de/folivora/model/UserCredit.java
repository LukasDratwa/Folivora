package de.folivora.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserCredit {
	private double balance;
	private List<Transaction> transactions = new ArrayList<Transaction>();
	private Date lastModification;
	private Transaction lastExecutedTransaction;
	
	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}
	
	/**
	 * @return the transactions
	 */
	public List<Transaction> getTransactions() {
		return transactions;
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
}