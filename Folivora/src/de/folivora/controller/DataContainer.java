package de.folivora.controller;

import java.util.ArrayList;
import java.util.List;

import de.folivora.model.AdditionalReward;
import de.folivora.model.SearchRequest;
import de.folivora.model.Transaction;
import de.folivora.model.User;
import de.folivora.model.messenger.Message;

/**
 * Class to bundle the application data.
 * 
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
public class DataContainer {
	private List<User> userList;
	private List<SearchRequest> searchRequestList;
	private List<Transaction> transactionList;
	private List<Message> messageList;
	private List<AdditionalReward> additionalRewardList;
	
	public DataContainer() {
		this.userList = new ArrayList<User>();
		this.searchRequestList = new ArrayList<SearchRequest>();
		this.transactionList = new ArrayList<Transaction>();
		this.messageList = new ArrayList<Message>();
		this.additionalRewardList = new ArrayList<AdditionalReward>();
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
	 * @return the transactionList
	 */
	public List<Transaction> getTransactionList() {
		return transactionList;
	}

	/**
	 * @param transactionList the transactionList to set
	 */
	public void setTransactionList(List<Transaction> transactionList) {
		this.transactionList = transactionList;
	}

	/**
	 * @return the messageList
	 */
	public List<Message> getMessageList() {
		return messageList;
	}

	/**
	 * @param messageList the messageList to set
	 */
	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}

	/**
	 * @return the additionalRewardList
	 */
	public List<AdditionalReward> getAdditionalRewardList() {
		return additionalRewardList;
	}

	/**
	 * @param additionalRewardList the additionalRewardList to set
	 */
	public void setAdditionalRewardList(List<AdditionalReward> additionalRewardList) {
		this.additionalRewardList = additionalRewardList;
	}
}