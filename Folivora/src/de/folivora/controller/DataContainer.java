package de.folivora.controller;

import java.util.ArrayList;
import java.util.List;

import de.folivora.model.IdStorage;
import de.folivora.model.SearchRequest;
import de.folivora.model.User;

public class DataContainer {
	private List<User> userList;
	private List<SearchRequest> searchRequestList;
	private IdStorage idStorage;
	
	public DataContainer(List<User> userList, List<SearchRequest> searchRequestList, IdStorage idStorage) {
		this.userList = userList;
		this.searchRequestList = searchRequestList;
		this.idStorage = idStorage;
	}
	
	public DataContainer() {
		this.userList = new ArrayList<User>();
		this.searchRequestList = new ArrayList<SearchRequest>();
		this.idStorage = new IdStorage(0, 0, 0, 0, 0);
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
	 * @return the idStorage
	 */
	public IdStorage getIdStorage() {
		return idStorage;
	}

	/**
	 * @param idStorage the idStorage to set
	 */
	public void setIdStorage(IdStorage idStorage) {
		this.idStorage = idStorage;
	}
}