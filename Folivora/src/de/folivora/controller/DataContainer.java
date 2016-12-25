package de.folivora.controller;

import java.util.ArrayList;
import java.util.List;

import de.folivora.model.Feedback;
import de.folivora.model.SearchRequest;
import de.folivora.model.Transaction;
import de.folivora.model.User;
import de.folivora.model.UserCredit;

public class DataContainer {
	private List<User> userList;
	private List<SearchRequest> searchRequestList;
	private long lastUsedUserId,
				 lastUsedSearchRequestId,
				 lastUsedFeedbackId,
				 lastUsedUserCreditId,
				 lastUsedTransactionId;
	
	public DataContainer(List<User> userList, List<SearchRequest> searchRequestList,
			long lastUsedUserId, long lastUsedSearchRequestId, long lastUsedFeedbackId) {
		this.userList = userList;
		this.searchRequestList = searchRequestList;
		this.lastUsedUserId = lastUsedUserId;
		this.lastUsedSearchRequestId = lastUsedSearchRequestId;
		this.lastUsedFeedbackId = lastUsedFeedbackId;
	}
	
	public DataContainer() {
		this.userList = new ArrayList<User>();
		this.searchRequestList = new ArrayList<SearchRequest>();
		this.lastUsedUserId = 0;
		this.lastUsedSearchRequestId = 0;
		this.lastUsedFeedbackId = 0;
		this.lastUsedUserCreditId = 0;
		this.lastUsedTransactionId = 0;
	}
	
	/**
	 * Method to get a new unique id for a new {@link User}.
	 * 
	 * @return a new id
	 */
	public long getNewUserId() {
		setLastUsedUserId(getLastUsedUserId() + 1);
		return getLastUsedUserId();
	}
	
	/**
	 * Method to get a new unique id for a new {@link UserCredit}.
	 * 
	 * @return a new id
	 */
	public long getNewUserCreditId() {
		setLastUsedUserCreditId(getLastUsedUserCreditId() + 1);
		return getLastUsedUserCreditId();
	}
	
	/**
	 * Method to get a new unique id for a new {@link SearchRequest}.
	 * 
	 * @return a new id
	 */
	public long getNewSearchRequestId() {
		setLastUsedSearchRequestId(getLastUsedSearchRequestId() + 1);
		return getLastUsedSearchRequestId();
	}
	
	/**
	 * Method to get a new unique id for a new {@link Feedback}.
	 * 
	 * @return a new id
	 */
	public long getNewFeedbackId() {
		setLastUsedFeedbackId(getLastUsedFeedbackId() + 1);
		return getLastUsedFeedbackId();
	}
	
	/**
	 * Method to get a new unique id for a new {@link Transaction}.
	 * 
	 * @return a new id
	 */
	public long getNewTransactionId() {
		setLastUsedTransactionId(getLastUsedTransactionId() + 1);
		return getLastUsedTransactionId();
	}
	
	/**
	 * @return the userList
	 */
	public List<User> getUserList() {
		return userList;
	}
	
	/**
	 * @param userList the userList to set
	 */
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	
	/**
	 * @return the searchRequestList
	 */
	public List<SearchRequest> getSearchRequestList() {
		return searchRequestList;
	}
	
	/**
	 * @param searchRequestList the searchRequestList to set
	 */
	public void setSearchRequestList(List<SearchRequest> searchRequestList) {
		this.searchRequestList = searchRequestList;
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
}