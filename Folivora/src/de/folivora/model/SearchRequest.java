package de.folivora.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.google.gson.JsonObject;

@Entity
public class SearchRequest {
	@Id
	private long id;
	private String title;
	private String description;
	private String pathToDefaultImg;
	private double costsAndReward;
	private double fee;
	private Double lat,
				   lng;
	private String address;
	private Date creationTimestamp;
	
	@Enumerated(EnumType.STRING)
	private SearchRequestStatus status;
	
	private Long possibleDelivery_from,
				 possibleDelivery_to;
	
	private String possibleDelivery_from_string,
	 			   possibleDelivery_to_string;
	
	@OneToOne(targetEntity=User.class)
	private User userCreator;
	
	@OneToOne(targetEntity=User.class)
	private User userStasisfier;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER, targetEntity=Feedback.class)
	private Feedback feedbackOfSearchingUser = null,
					 feedbackOfDeliveringUser = null;
	
	public SearchRequest(long id, String title, String description, String pathToDefaultImg,
			Long  possibleDelivery_from, Long possibleDelivery_to,
			double costsAndReward, double fee, Double lat, Double lng, String address,
			User userCreator) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.pathToDefaultImg = pathToDefaultImg;
		this.possibleDelivery_from = possibleDelivery_from;
		this.possibleDelivery_from_string = new Date(possibleDelivery_from).toString();
		this.possibleDelivery_to = possibleDelivery_to;
		this.possibleDelivery_to_string = new Date(possibleDelivery_to).toString();
		this.costsAndReward = costsAndReward;
		this.fee = fee;
		this.lat = lat;
		this.lng = lng;
		this.status = shouldBeActive() ? SearchRequestStatus.ACTIVE : SearchRequestStatus.INACTIVE;
		this.userCreator = userCreator;
		this.address = address;
		this.creationTimestamp = new Date();
	}
	
	public SearchRequest(long id, String title, String description, String pathToDefaultImg,
			Long[] possibleDelivery, double costsAndReward, double charges,
			Double lat, Double lng, String address, User userCreator) {
		this(id, title, description, pathToDefaultImg, possibleDelivery[0], possibleDelivery[1],
				 costsAndReward, charges, lat, lng, address, userCreator);
	}
	
	/**
	 * Protected default constructor for hibernate mapping.
	 */
	protected SearchRequest() {
		
	}
	
	public JsonObject getAsJsonObject() {
		JsonObject jo = new JsonObject();
		
		jo.addProperty("id", this.getId());
		jo.addProperty("title", this.getTitle());
		jo.addProperty("description", this.getDescription());
		jo.addProperty("pathToDefaultImg", this.getPathToDefaultImg());
		jo.addProperty("costsAndReward", this.getCostsAndReward());
		jo.addProperty("fee", this.getFee());
		jo.addProperty("lat", this.getLat());
		jo.addProperty("lng", this.getLng());
		jo.addProperty("address", this.getAddress());
		jo.addProperty("possibleDelivery_from", this.getPossibleDelivery_from());
		jo.addProperty("possibleDelivery_to", this.getPossibleDelivery_to());
		jo.addProperty("status", this.getStatus().toString());
		
		jo.addProperty("marker_icon_path", getMarkerIconPath(this.getPossibleDelivery_to()));
		
		JsonObject creator = new JsonObject();
		User userCreator = this.getUserCreator();
		creator.addProperty("id", userCreator.getId());
		creator.addProperty("name", userCreator.getName());
		creator.addProperty("hometown", userCreator.getHometown());
		jo.add("userCreator", creator);
		
		User userStatisfier = this.getUserStasisfier();
		if(userStatisfier != null) {
			JsonObject statisfier = new JsonObject();
			statisfier.addProperty("id", userStatisfier.getId());
			statisfier.addProperty("name", userStatisfier.getName());
			statisfier.addProperty("hometown", userStatisfier.getHometown());
			jo.add("userStatisfier", statisfier);
		}
		
		return jo;
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
	
	public boolean shouldBeActive() {
		Date actDate = new Date();
		Date a = new Date(this.getPossibleDelivery_from());
		Date b = new Date(this.getPossibleDelivery_to());
		
		return (a.compareTo(actDate) * actDate.compareTo(b) > 0);
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
	 * @return the status
	 */
	public SearchRequestStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(SearchRequestStatus status) {
		this.status = status;
	}

	/**
	 * @return the feedbackOfSearchingUser
	 */
	public Feedback getFeedbackOfSearchingUser() {
		return feedbackOfSearchingUser;
	}

	/**
	 * @param feedbackOfSearchingUser the feedbackOfSearchingUser to set
	 */
	public void setFeedbackOfSearchingUser(Feedback feedbackOfSearchingUser) {
		this.feedbackOfSearchingUser = feedbackOfSearchingUser;
	}

	/**
	 * @return the feedbackOfDeliveringUser
	 */
	public Feedback getFeedbackOfDeliveringUser() {
		return feedbackOfDeliveringUser;
	}

	/**
	 * @param feedbackOfDeliveringUser the feedbackOfDeliveringUser to set
	 */
	public void setFeedbackOfDeliveringUser(Feedback feedbackOfDeliveringUser) {
		this.feedbackOfDeliveringUser = feedbackOfDeliveringUser;
	}

	/**
	 * @return the fee
	 */
	public double getFee() {
		return fee;
	}

	/**
	 * @param fee the fee to set
	 */
	public void setFee(double fee) {
		this.fee = fee;
	}

	/**
	 * @return the creationTimestamp
	 */
	public Date getCreationTimestamp() {
		return creationTimestamp;
	}
}