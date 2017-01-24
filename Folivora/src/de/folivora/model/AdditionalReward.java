package de.folivora.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.bson.types.ObjectId;

/**
 * Class to represent additional rewards for users 
 * <hr>Created on 24.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
@Entity
public class AdditionalReward {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private ObjectId id;
	private boolean activated;
	private Date 	activatedTimestamp,
					createdTimestamp,
					validFrom,
					validTill;
	private double value;
	
	@OneToOne(targetEntity=User.class)
	private User userCreator;
	
	@OneToOne(targetEntity=User.class)
	private User userActivator;

	public AdditionalReward(Date validFrom, Date validTill, User userCreator, double value) {
		this.activated = false;
		this.createdTimestamp = new Date();
		this.validFrom = validFrom;
		this.validTill = validTill;
		this.userCreator = userCreator;
		this.value = value;
	}
	
	/**
	 * Protected default constructor for hibernate mapping.
	 */
	protected AdditionalReward() {
	}
	
	@Override
	public String toString() {
		return "[activated=" + activated + ", activatedTimestamp=" + activatedTimestamp + ", value=" + value + ", "
				+ "id=" + id.toString() + ", receiver= " + userActivator + ", creator=" + userCreator + "]";
	}
	
	/**
	 * Method to check if the additional reward is in a valid time frame.
	 * 
	 * <hr>Created on 24.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @return
	 */
	public boolean isInValidTimeframe() {
		Date actDate = new Date();
		return (validFrom.compareTo(actDate) * actDate.compareTo(validTill) > 0);
	}
	
	/**
	 * @return the activated
	 */
	public boolean isActivated() {
		return activated;
	}

	/**
	 * @param activated the activated to set
	 */
	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	/**
	 * @return the activatedTimestamp
	 */
	public Date getActivatedTimestamp() {
		return activatedTimestamp;
	}

	/**
	 * @param activatedTimestamp the activatedTimestamp to set
	 */
	public void setActivatedTimestamp(Date activatedTimestamp) {
		this.activatedTimestamp = activatedTimestamp;
	}

	/**
	 * @return the validFrom
	 */
	public Date getValidFrom() {
		return validFrom;
	}

	/**
	 * @param validFrom the validFrom to set
	 */
	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	/**
	 * @return the validTill
	 */
	public Date getValidTill() {
		return validTill;
	}

	/**
	 * @param validTill the validTill to set
	 */
	public void setValidTill(Date validTill) {
		this.validTill = validTill;
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
	 * @return the userActivator
	 */
	public User getUserActivator() {
		return userActivator;
	}

	/**
	 * @param userActivator the userActivator to set
	 */
	public void setUserActivator(User userActivator) {
		this.userActivator = userActivator;
	}

	/**
	 * @return the id
	 */
	public ObjectId getId() {
		return id;
	}

	/**
	 * @return the createdTimestamp
	 */
	public Date getCreatedTimestamp() {
		return createdTimestamp;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}
}