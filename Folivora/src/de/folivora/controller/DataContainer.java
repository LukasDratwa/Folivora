package de.folivora.controller;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.folivora.model.IdStorage;
import de.folivora.model.SearchRequest;
import de.folivora.model.Transaction;
import de.folivora.model.User;
import de.folivora.util.Constants;

public class DataContainer {
	private List<User> userList;
	private List<SearchRequest> searchRequestList;
	private List<Transaction> transactionList;
	private IdStorage idStorage;
	
	public DataContainer(List<User> userList, List<SearchRequest> searchRequestList, IdStorage idStorage) {
		this.userList = userList;
		this.searchRequestList = searchRequestList;
		this.idStorage = idStorage;
	}
	
	public DataContainer() {
		this.userList = new ArrayList<User>();
		this.searchRequestList = new ArrayList<SearchRequest>();
		this.transactionList = new ArrayList<Transaction>();
		this.idStorage = new IdStorage(0, 0, 0, 0, 0);
	}
	
	public JsonArray getActiveSearchRequestListAsJsonArray() {
		JsonArray result = new JsonArray();
		
		for(SearchRequest sr : getSearchRequestList()) {
			if(sr.isActive()) {
				JsonObject jo = new JsonObject();
				
				jo.addProperty("id", sr.getId());
				jo.addProperty("title", sr.getTitle());
				jo.addProperty("description", sr.getDescription());
				jo.addProperty("pathToDefaultImg", sr.getPathToDefaultImg());
				jo.addProperty("costsAndReward", sr.getCostsAndReward());
				jo.addProperty("charges", sr.getCharges());
				jo.addProperty("lat", sr.getLat());
				jo.addProperty("lng", sr.getLng());
				jo.addProperty("address", sr.getAddress());
				jo.addProperty("possibleDelivery_from", sr.getPossibleDelivery_from());
				jo.addProperty("possibleDelivery_to", sr.getPossibleDelivery_to());
				
				jo.addProperty("marker_icon_path", getMarkerIconPath(sr.getPossibleDelivery_to()));
				
				JsonObject creator = new JsonObject();
				User user = sr.getUserCreator();
				creator.addProperty("id", user.getId());
				creator.addProperty("name", user.getName());
				creator.addProperty("hometown", user.getHometown());
				jo.add("userCreator", creator);
				
				
				result.add(jo);
			}
		}
		
		return result;
	}
	
	private String getMarkerIconPath(long possibleDeliveryDo) {
		long act = System.currentTimeMillis();
		
		if(possibleDeliveryDo - act < Constants.MAP_MARKER_RED_TIME_LEFT) {
			return Constants.MAP_MARKER_RED_URL;
		} else if(possibleDeliveryDo - act < Constants.MAP_MARKER_ORANGE_TIME_LEFT){
			return Constants.MAP_MARKER_ORANGE_URL;
		} else {
			return Constants.MAP_MARKER_GREEN_URL;
		}
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
}