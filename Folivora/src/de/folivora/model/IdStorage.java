package de.folivora.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class IdStorage {
	@Id
	private long id;
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

	/**
	 * @return the lastUsedUserId
	 */
	public long getLastUsedUserId() {
		return lastUsedUserId;
	}

	/**
	 * @param lastUsedUserId the lastUsedUserId to set
	 */
	public void setLastUsedUserId(long lastUsedUserId) {
		this.lastUsedUserId = lastUsedUserId;
	}

	/**
	 * @return the lastUsedSearchRequestId
	 */
	public long getLastUsedSearchRequestId() {
		return lastUsedSearchRequestId;
	}

	/**
	 * @param lastUsedSearchRequestId the lastUsedSearchRequestId to set
	 */
	public void setLastUsedSearchRequestId(long lastUsedSearchRequestId) {
		this.lastUsedSearchRequestId = lastUsedSearchRequestId;
	}

	/**
	 * @return the lastUsedFeedbackId
	 */
	public long getLastUsedFeedbackId() {
		return lastUsedFeedbackId;
	}

	/**
	 * @param lastUsedFeedbackId the lastUsedFeedbackId to set
	 */
	public void setLastUsedFeedbackId(long lastUsedFeedbackId) {
		this.lastUsedFeedbackId = lastUsedFeedbackId;
	}

	/**
	 * @return the lastUsedUserCreditId
	 */
	public long getLastUsedUserCreditId() {
		return lastUsedUserCreditId;
	}

	/**
	 * @param lastUsedUserCreditId the lastUsedUserCreditId to set
	 */
	public void setLastUsedUserCreditId(long lastUsedUserCreditId) {
		this.lastUsedUserCreditId = lastUsedUserCreditId;
	}

	/**
	 * @return the lastUsedTransactionId
	 */
	public long getLastUsedTransactionId() {
		return lastUsedTransactionId;
	}

	/**
	 * @param lastUsedTransactionId the lastUsedTransactionId to set
	 */
	public void setLastUsedTransactionId(long lastUsedTransactionId) {
		this.lastUsedTransactionId = lastUsedTransactionId;
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
}