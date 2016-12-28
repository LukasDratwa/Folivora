package de.folivora.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class SearchRequest {
	@Id
	private long id;
	private String title;
	private String description;
	private String pathToDefaultImg;
	private double costsAndReward;
	private double lat,
				   lng;
	private boolean active;
	
	private Date possibleDelivery_from,
				 possibleDelivery_to,
	   			 preferredDelivery_from,
	   			 preferredDelivery_to;
	
	@OneToOne(targetEntity=User.class)
	private User userCreator;
	
	@OneToOne(targetEntity=User.class)
	private User userStasisfier;
	
	public SearchRequest(long id, String title, String description, String pathToDefaultImg,
			Date possibleDelivery_from, Date possibleDelivery_to, Date preferredDelivery_from, Date preferredDelivery_to,
			double costsAndReward, double lat, double lng,
			boolean active, User userCreator) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.pathToDefaultImg = pathToDefaultImg;
		this.possibleDelivery_from = possibleDelivery_from;
		this.possibleDelivery_to = possibleDelivery_to;
		this.preferredDelivery_from = preferredDelivery_from;
		this.preferredDelivery_to = preferredDelivery_to;
		this.costsAndReward = costsAndReward;
		this.lat = lat;
		this.lng = lng;
		this.active = active;
		this.userCreator = userCreator;
	}
	
	public SearchRequest(long id, String title, String description, String pathToDefaultImg,
			Date[] possibleDelivery, Date[] preferredDelivery, double costsAndReward, double lat, double lng,
			boolean active, User userCreator) {
		this(id, title, description, pathToDefaultImg, possibleDelivery[0], possibleDelivery[1],
				preferredDelivery[0], preferredDelivery[1], costsAndReward, lat, lng, active, userCreator);
	}
	
	/**
	 * Protected default constructor for hibernate mapping.
	 */
	protected SearchRequest() {
		
	}
	
	@Override
	public String toString() {
		return "[id=" + this.id + ", title=" + this.title
				+ ", userCreatorRef=\"" + this.userCreator.getName() + "\" (" + this.userCreator.getId() + ")"
				+ ", userStatisfier="
					+ ((this.userStasisfier != null)
						? "\"" + this.userStasisfier.getName() + "\" (" + this.userStasisfier.getId() + ")"
						: "null")
				+ "]";
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
	 * @return the possibleDelivery_from
	 */
	public Date getPossibleDelivery_from() {
		return possibleDelivery_from;
	}

	/**
	 * @param possibleDelivery_from the possibleDelivery_from to set
	 */
	public void setPossibleDelivery_from(Date possibleDelivery_from) {
		this.possibleDelivery_from = possibleDelivery_from;
	}

	/**
	 * @return the possibleDelivery_to
	 */
	public Date getPossibleDelivery_to() {
		return possibleDelivery_to;
	}

	/**
	 * @param possibleDelivery_to the possibleDelivery_to to set
	 */
	public void setPossibleDelivery_to(Date possibleDelivery_to) {
		this.possibleDelivery_to = possibleDelivery_to;
	}

	/**
	 * @return the preferredDelivery_from
	 */
	public Date getPreferredDelivery_from() {
		return preferredDelivery_from;
	}

	/**
	 * @param preferredDelivery_from the preferredDelivery_from to set
	 */
	public void setPreferredDelivery_from(Date preferredDelivery_from) {
		this.preferredDelivery_from = preferredDelivery_from;
	}

	/**
	 * @return the preferredDelivery_to
	 */
	public Date getPreferredDelivery_to() {
		return preferredDelivery_to;
	}

	/**
	 * @param preferredDelivery_to the preferredDelivery_to to set
	 */
	public void setPreferredDelivery_to(Date preferredDelivery_to) {
		this.preferredDelivery_to = preferredDelivery_to;
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