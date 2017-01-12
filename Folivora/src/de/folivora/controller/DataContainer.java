package de.folivora.controller;

import java.util.ArrayList;
import java.util.List;

import de.folivora.model.SearchRequest;
import de.folivora.model.Transaction;
import de.folivora.model.User;
import de.folivora.model.messanger.Message;

public class DataContainer {
	private List<User> userList;
	private List<SearchRequest> searchRequestList;
	private List<Transaction> transactionList;
	private List<Message> messageList;
	
	public DataContainer() {
		this.userList = new ArrayList<User>();
		this.searchRequestList = new ArrayList<SearchRequest>();
		this.transactionList = new ArrayList<Transaction>();
		this.messageList = new ArrayList<Message>();
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
}