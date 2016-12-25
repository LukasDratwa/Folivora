package de.folivora.controller;

import java.util.List;

import de.folivora.model.SearchRequest;
import de.folivora.model.User;

public class DataContainer {
	private List<User> userList;
	private List<SearchRequest> searchRequestList;
	private long lastUsedUserId;
	private long lastUsedSearchRequestId;
	private long lastUsedFeedbackId;
	
	public DataContainer(List<User> userList, List<SearchRequest> searchRequestList,
			long lastUsedUserId, long lastUsedSearchRequestId, long lastUsedFeedbackId) {
		this.userList = userList;
		this.searchRequestList = searchRequestList;
		this.lastUsedUserId = lastUsedUserId;
		this.lastUsedSearchRequestId = lastUsedSearchRequestId;
		this.lastUsedFeedbackId = lastUsedFeedbackId;
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
}