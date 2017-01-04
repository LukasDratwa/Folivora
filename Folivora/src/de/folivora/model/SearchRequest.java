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
	private Double lat,
				   lng;
	private String address;
	private boolean active;
	
	private Long possibleDelivery_from,
				 possibleDelivery_to,
	   			 preferredDelivery_from,
	   			 preferredDelivery_to;
	
	private String possibleDelivery_from_string,
	 			   possibleDelivery_to_string,
	 			   preferredDelivery_from_string,
	 			   preferredDelivery_to_string;
	
	@OneToOne(targetEntity=User.class)
	private User userCreator;
	
	@OneToOne(targetEntity=User.class)
	private User userStasisfier;
	
	public SearchRequest(long id, String title, String description, String pathToDefaultImg,
			Long  possibleDelivery_from, Long possibleDelivery_to, Long preferredDelivery_from, Long preferredDelivery_to,
			double costsAndReward, Double lat, Double lng, String address,
			User userCreator) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.pathToDefaultImg = pathToDefaultImg;
		this.possibleDelivery_from = possibleDelivery_from;
		this.possibleDelivery_from_string = new Date(possibleDelivery_from).toString();
		this.possibleDelivery_to = possibleDelivery_to;
		this.possibleDelivery_to_string = new Date(possibleDelivery_to).toString();
		this.preferredDelivery_from = preferredDelivery_from;
		this.preferredDelivery_from_string = preferredDelivery_from != null
												? new Date(preferredDelivery_from).toString()
												: null;
		this.preferredDelivery_to = preferredDelivery_to;
		this.preferredDelivery_to_string = preferredDelivery_to != null
												? new Date(preferredDelivery_to).toString()
												: null;
		this.costsAndReward = costsAndReward;
		this.lat = lat;
		this.lng = lng;
		this.active = false;
		this.userCreator = userCreator;
		this.address = address;
	}
	
	public SearchRequest(long id, String title, String description, String pathToDefaultImg,
			Long[] possibleDelivery, Long[] preferredDelivery, double costsAndReward, Double lat, Double lng,
			String address, User userCreator) {
		this(id, title, description, pathToDefaultImg, possibleDelivery[0], possibleDelivery[1],
				preferredDelivery[0], preferredDelivery[1], costsAndReward, lat, lng, address, userCreator);
	}
	
	/**
	 * Protected default constructor for hibernate mapping.
	 */
	protected SearchRequest() {
		
	}
	
	@Override
	public String toString() {
		return "[id=" + this.id + ", title=" + this.title
				+ ", userCreatorRef=\""
					+ ((this.userCreator != null)
							? this.userCreator.getName() + "\" (" + this.userCreator.getId() + ")"
							: "null")
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
	public Double getLat() {
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
	public Double getLng() {
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

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the possibleDelivery_from
	 */
	public Long getPossibleDelivery_from() {
		return possibleDelivery_from;
	}

	/**
	 * @param possibleDelivery_from the possibleDelivery_from to set
	 */
	public void setPossibleDelivery_from(Long possibleDelivery_from) {
		this.possibleDelivery_from = possibleDelivery_from;
	}

	/**
	 * @return the possibleDelivery_to
	 */
	public Long getPossibleDelivery_to() {
		return possibleDelivery_to;
	}

	/**
	 * @param possibleDelivery_to the possibleDelivery_to to set
	 */
	public void setPossibleDelivery_to(Long possibleDelivery_to) {
		this.possibleDelivery_to = possibleDelivery_to;
	}

	/**
	 * @return the preferredDelivery_from
	 */
	public Long getPreferredDelivery_from() {
		return preferredDelivery_from;
	}

	/**
	 * @param preferredDelivery_from the preferredDelivery_from to set
	 */
	public void setPreferredDelivery_from(Long preferredDelivery_from) {
		this.preferredDelivery_from = preferredDelivery_from;
	}

	/**
	 * @return the preferredDelivery_to
	 */
	public Long getPreferredDelivery_to() {
		return preferredDelivery_to;
	}

	/**
	 * @param preferredDelivery_to the preferredDelivery_to to set
	 */
	public void setPreferredDelivery_to(Long preferredDelivery_to) {
		this.preferredDelivery_to = preferredDelivery_to;
	}

	/**
	 * @return the possibleDelivery_from_string
	 */
	public String getPossibleDelivery_from_string() {
		return possibleDelivery_from_string;
	}

	/**
	 * @param possibleDelivery_from_string the possibleDelivery_from_string to set
	 */
	public void setPossibleDelivery_from_string(String possibleDelivery_from_string) {
		this.possibleDelivery_from_string = possibleDelivery_from_string;
	}

	/**
	 * @return the possibleDelivery_to_string
	 */
	public String getPossibleDelivery_to_string() {
		return possibleDelivery_to_string;
	}

	/**
	 * @param possibleDelivery_to_string the possibleDelivery_to_string to set
	 */
	public void setPossibleDelivery_to_string(String possibleDelivery_to_string) {
		this.possibleDelivery_to_string = possibleDelivery_to_string;
	}

	/**
	 * @return the preferredDelivery_from_string
	 */
	public String getPreferredDelivery_from_string() {
		return preferredDelivery_from_string;
	}

	/**
	 * @param preferredDelivery_from_string the preferredDelivery_from_string to set
	 */
	public void setPreferredDelivery_from_string(String preferredDelivery_from_string) {
		this.preferredDelivery_from_string = preferredDelivery_from_string;
	}

	/**
	 * @return the preferredDelivery_to_string
	 */
	public String getPreferredDelivery_to_string() {
		return preferredDelivery_to_string;
	}

	/**
	 * @param preferredDelivery_to_string the preferredDelivery_to_string to set
	 */
	public void setPreferredDelivery_to_string(String preferredDelivery_to_string) {
		this.preferredDelivery_to_string = preferredDelivery_to_string;
	}
}