package de.folivora.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import de.folivora.storage.HibernateSave;

@Entity
public class IdStorage {
	@Id
	private long id = 0;
	private long lastUsedUserId,
	 			 lastUsedSearchRequestId,
	 			 lastUsedFeedbackId,
	 			 lastUsedUserCreditId,
	 			 lastUsedTransactionId;
	
	public IdStorage(long lastUsedUserId, long lastUsedSearchRequestId, long lastUsedFeedbackId,
			long lastUsedUserCreditId, long lastUsedTransactionId) {
		this.lastUsedUserId = lastUsedUserId;
		this.lastUsedSearchRequestId = lastUsedSearchRequestId;
		this.lastUsedFeedbackId = lastUsedFeedbackId;
		this.lastUsedUserCreditId = lastUsedUserCreditId;
		this.lastUsedTransactionId = lastUsedTransactionId;
	}
	
	public IdStorage() {
		
	}
	
	/**
	 * Method to get a new unique id for a new {@link User}.
	 * 
	 * @return a new id
	 */
	public long getNewUserId() {
		setLastUsedUserId(getLastUsedUserId() + 1);
		HibernateSave.saveOrUpdateIdStorage(this);
		return getLastUsedUserId();
	}
	
	/**
	 * Method to get a new unique id for a new {@link UserCredit}.
	 * 
	 * @return a new id
	 */
	public long getNewUserCreditId() {
		setLastUsedUserCreditId(getLastUsedUserCreditId() + 1);
		HibernateSave.saveOrUpdateIdStorage(this);
		return getLastUsedUserCreditId();
	}
	
	/**
	 * Method to get a new unique id for a new {@link SearchRequest}.
	 * 
	 * @return a new id
	 */
	public long getNewSearchRequestId() {
		setLastUsedSearchRequestId(getLastUsedSearchRequestId() + 1);
		HibernateSave.saveOrUpdateIdStorage(this);
		return getLastUsedSearchRequestId();
	}
	
	/**
	 * Method to get a new unique id for a new {@link Feedback}.
	 * 
	 * @return a new id
	 */
	public long getNewFeedbackId() {
		setLastUsedFeedbackId(getLastUsedFeedbackId() + 1);
		HibernateSave.saveOrUpdateIdStorage(this);
		return getLastUsedFeedbackId();
	}
	
	/**
	 * Method to get a new unique id for a new {@link Transaction}.
	 * 
	 * @return a new id
	 */
	public long getNewTransactionId() {
		setLastUsedTransactionId(getLastUsedTransactionId() + 1);
		HibernateSave.saveOrUpdateIdStorage(this);
		return getLastUsedTransactionId();
	}

	/**
	 * @return the lastUsedUserId
	 */
	private long getLastUsedUserId() {
		return lastUsedUserId;
	}

	/**
	 * @param lastUsedUserId the lastUsedUserId to set
	 */
	private void setLastUsedUserId(long lastUsedUserId) {
		this.lastUsedUserId = lastUsedUserId;
	}

	/**
	 * @return the lastUsedSearchRequestId
	 */
	private long getLastUsedSearchRequestId() {
		return lastUsedSearchRequestId;
	}

	/**
	 * @param lastUsedSearchRequestId the lastUsedSearchRequestId to set
	 */
	private void setLastUsedSearchRequestId(long lastUsedSearchRequestId) {
		this.lastUsedSearchRequestId = lastUsedSearchRequestId;
	}

	/**
	 * @return the lastUsedFeedbackId
	 */
	private long getLastUsedFeedbackId() {
		return lastUsedFeedbackId;
	}

	/**
	 * @param lastUsedFeedbackId the lastUsedFeedbackId to set
	 */
	private void setLastUsedFeedbackId(long lastUsedFeedbackId) {
		this.lastUsedFeedbackId = lastUsedFeedbackId;
	}

	/**
	 * @return the lastUsedUserCreditId
	 */
	private long getLastUsedUserCreditId() {
		return lastUsedUserCreditId;
	}

	/**
	 * @param lastUsedUserCreditId the lastUsedUserCreditId to set
	 */
	private void setLastUsedUserCreditId(long lastUsedUserCreditId) {
		this.lastUsedUserCreditId = lastUsedUserCreditId;
	}

	/**
	 * @return the lastUsedTransactionId
	 */
	private long getLastUsedTransactionId() {
		return lastUsedTransactionId;
	}

	/**
	 * @param lastUsedTransactionId the lastUsedTransactionId to set
	 */
	private void setLastUsedTransactionId(long lastUsedTransactionId) {
		this.lastUsedTransactionId = lastUsedTransactionId;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
}