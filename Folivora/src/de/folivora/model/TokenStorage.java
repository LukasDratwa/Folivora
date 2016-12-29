package de.folivora.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TokenStorage {
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE)
	private long id;
	private String token;
	private Date dateExpiration,
				 dateCreation;
	
	private long ownerId;

	protected TokenStorage(long ownerId) {
		this.ownerId = ownerId;
	}
	
	protected TokenStorage() {
		
	}
	
	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}
	
	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
	
	/**
	 * @return the dateExpiration
	 */
	public Date getDateExpiration() {
		return dateExpiration;
	}
	
	/**
	 * @param dateExpiration the dateExpiration to set
	 */
	public void setDateExpiration(Date dateExpiration) {
		this.dateExpiration = dateExpiration;
	}
	
	/**
	 * @return the dateCreation
	 */
	public Date getDateCreation() {
		return dateCreation;
	}
	
	/**
	 * @param dateCreation the dateCreation to set
	 */
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
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
	 * @return the ownerId
	 */
	public long getOwnerId() {
		return ownerId;
	}

	/**
	 * @param ownerId the ownerId to set
	 */
	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}
}