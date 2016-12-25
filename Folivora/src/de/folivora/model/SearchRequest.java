package de.folivora.model;

import java.util.Date;

public class SearchRequest {
	private long id;
	private String title;
	private String description;
	private String pathToDefaultImg;
	private Date[] possibleDelivery,
				   preferredDelivery;
	private double costsAndReward;
	private double lat,
				   lng;
	private boolean active;
	private User userCreator,
				 userStasisfier = null;
	
	public SearchRequest(long id, String title, String description, String pathToDefaultImg,
			Date[] possibleDelivery, Date[] preferredDelivery, double costsAndReward, double lat, double lng,
			boolean active, User userCreator) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.pathToDefaultImg = pathToDefaultImg;
		this.possibleDelivery = possibleDelivery;
		this.preferredDelivery = preferredDelivery;
		this.costsAndReward = costsAndReward;
		this.lat = lat;
		this.lng = lng;
		this.active = active;
		this.userCreator = userCreator;
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
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @return the pathToDefaultImg
	 */
	public String getPathToDefaultImg() {
		return pathToDefaultImg;
	}
	
	/**
	 * @param pathToDefaultImg the pathToDefaultImg to set
	 */
	public void setPathToDefaultImg(String pathToDefaultImg) {
		this.pathToDefaultImg = pathToDefaultImg;
	}
	
	/**
	 * @return the possibleDelivery
	 */
	public Date[] getPossibleDelivery() {
		return possibleDelivery;
	}
	
	/**
	 * @param possibleDelivery the possibleDelivery to set
	 */
	public void setPossibleDelivery(Date[] possibleDelivery) {
		this.possibleDelivery = possibleDelivery;
	}
	
	/**
	 * @return the preferredDelivery
	 */
	public Date[] getPreferredDelivery() {
		return preferredDelivery;
	}
	
	/**
	 * @param preferredDelivery the preferredDelivery to set
	 */
	public void setPreferredDelivery(Date[] preferredDelivery) {
		this.preferredDelivery = preferredDelivery;
	}
	
	/**
	 * @return the costsAndReward
	 */
	public double getCostsAndReward() {
		return costsAndReward;
	}
	
	/**
	 * @param costsAndReward the costsAndReward to set
	 */
	public void setCostsAndReward(double costsAndReward) {
		this.costsAndReward = costsAndReward;
	}
	
	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}
	
	/**
	 * @param lat the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	/**
	 * @return the lng
	 */
	public double getLng() {
		return lng;
	}
	
	/**
	 * @param lng the lng to set
	 */
	public void setLng(double lng) {
		this.lng = lng;
	}
	
	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	/**
	 * @return the userCreator
	 */
	public User getUserCreator() {
		return userCreator;
	}
	
	/**
	 * @param userCreator the userCreator to set
	 */
	public void setUserCreator(User userCreator) {
		this.userCreator = userCreator;
	}
	
	/**
	 * @return the userStasisfier
	 */
	public User getUserStasisfier() {
		return userStasisfier;
	}
	
	/**
	 * @param userStasisfier the userStasisfier to set
	 */
	public void setUserStasisfier(User userStasisfier) {
		this.userStasisfier = userStasisfier;
	}
}