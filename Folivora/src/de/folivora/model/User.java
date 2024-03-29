package de.folivora.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.servlet.http.HttpSession;

import org.bson.types.ObjectId;

import com.google.gson.JsonObject;

import de.folivora.model.messenger.Message;

/**
 * Class to represent a user.
 * 
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
@Entity
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private ObjectId id;
	private String name;
	private String password;
	private Date birthday;
	private String email;
	private String hometown;
	private Date creationTimestamp;
	
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	@Enumerated(EnumType.STRING)
	private UserType userType;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER, targetEntity=UserCredit.class)
	private UserCredit credit;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER, targetEntity=TokenStorage.class)
	private TokenStorage tokenStorage;
	
	@Transient
	private HttpSession session = null;
	
	@Transient
	private List<Feedback> receivedFeedbacks = new ArrayList<Feedback>();
	
	@Transient
	private List<Message> relevantMessages = new ArrayList<Message>();
	
	@Transient
	private String remoteAdress = null;
	
	public User(String name, String password, Date birthday, Gender gender,
			String email, UserCredit userCredit, UserType userType, String hometown) {
		this.name = name;
		this.password = password;
		this.birthday = birthday;
		this.gender = gender;
		this.email = email;
		this.credit = userCredit;
		this.userType = userType;
		this.tokenStorage = new TokenStorage(name);
		this.hometown = hometown;
		this.creationTimestamp = new Date();
	}
	
	/**
	 * Protected default constructor for hibernate mapping.
	 */
	protected User() {
	}
	
	public void printAllRelevantMsgs() {
		System.out.println("\nPrinting all messages of: " + this);
		for(Message msg : getRelevantMessages()) {
			System.out.println("\t" + msg);
		}
		System.out.println();
	}
	
	public void printAllReceivedFeedbacks() {
		System.out.println("\nPrinting all received feedbacks of: " + this);
		for(Feedback f : getReceivedFeedbacks()) {
			System.out.println("\t" + f);
		}
		System.out.println();
	}
	
	public boolean isInvolvedIn(SearchRequest sr) {
		if(sr.getUserCreator().id.toString().equals(getId()) || sr.getUserStasisfier().getId().equals(getId())) {
			return true;
		}
		
		return false;
	}
	
	public void refreshTokenStorage(String token) {
		this.tokenStorage.setToken(token);
		this.tokenStorage.setDateCreation(new Date());
		this.tokenStorage.setDateExpiration(new Date(System.currentTimeMillis() + Constants.TOKEN_SESSION_EXPIRATION_TIME));
	}
	
	@Override
	public String toString() {
		return "[name=\"" + this.getName() + "\", email=\"" + this.getEmail() + "\", "
				+ "id=" + this.id + ", credit=" + this.credit + ", httpSession=" + this.session
				+ ", trimmedRemoteAddress=" + getTrimmedRemoteAdress() + "]";
	}
	
	/**
	 * Method to get the half of the remote address of the user.
	 * 
	 * @return "null" or the half second half of the remote address of the user
	 */
	private String getTrimmedRemoteAdress() {
		if(getRemoteAdress() != null) {
			return getRemoteAdress().substring(new Integer(getRemoteAdress().length() / 2));
		}
			
		return "null";
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the birthday
	 */
	public Date getBirthday() {
		return birthday;
	}
	
	/**
	 * @param birthday the birthday to set
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	/**
	 * @return the gender
	 */
	public Gender getGender() {
		return gender;
	}
	
	/**
	 * @param gender the gender to set
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return the credit
	 */
	public UserCredit getCredit() {
		return credit;
	}
	
	/**
	 * @param credit the credit to set
	 */
	public void setCredit(UserCredit credit) {
		this.credit = credit;
	}
	
	/**
	 * @return the session
	 */
	public HttpSession getSession() {
		return session;
	}
	
	/**
	 * @param session the session to set
	 */
	public void setSession(HttpSession session) {
		this.session = session;
	}

	/**
	 * @return the userType
	 */
	public UserType getUserType() {
		return userType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the receivedFeedbacks
	 */
	public List<Feedback> getReceivedFeedbacks() {
		return receivedFeedbacks;
	}

	/**
	 * @param receivedFeedbacks the receivedFeedbacks to set
	 */
	public void setReceivedFeedbacks(List<Feedback> receivedFeedbacks) {
		this.receivedFeedbacks = receivedFeedbacks;
	}

	/**
	 * @return the tokenStorage
	 */
	public TokenStorage getTokenStorage() {
		return tokenStorage;
	}

	/**
	 * @param tokenStorage the tokenStorage to set
	 */
	public void setTokenStorage(TokenStorage tokenStorage) {
		this.tokenStorage = tokenStorage;
	}

	/**
	 * @return the hometown
	 */
	public String getHometown() {
		return hometown;
	}

	/**
	 * @param hometown the hometown to set
	 */
	public void setHometown(String hometown) {
		this.hometown = hometown;
	}

	/**
	 * @return the relevantMessages
	 */
	public List<Message> getRelevantMessages() {
		return relevantMessages;
	}

	/**
	 * @param relevantMessages the relevantMessages to set
	 */
	public void setRelevantMessages(List<Message> relevantMessages) {
		this.relevantMessages = relevantMessages;
	}

	/**
	 * @return the id
	 */
	public ObjectId getId() {
		return id;
	}

	/**
	 * @return the creationTimestamp
	 */
	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	
	/**
	 * Method to get the user as a JsonObject where not all fields are exported.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @return the JsonObject 
	 */
	public JsonObject getAsJsonObject() {
		JsonObject jo = new JsonObject();
		
		jo.addProperty("id", this.id.toString());
		jo.addProperty("creationTimestamp", this.getCreationTimestamp().getTime());
		jo.addProperty("name", this.getName());
		jo.addProperty("hometown", this.getHometown());
		jo.addProperty("birthday", getBirthday() != null ? getBirthday().getTime() : 0);
		jo.addProperty("gender", (this.getGender() != null) ? this.getGender().toString() : "");
		jo.addProperty("credit", this.getCredit().getBalance());
		
		return jo;
	}

	/**
	 * @return the remoteAdress
	 */
	public String getRemoteAdress() {
		return remoteAdress;
	}

	/**
	 * @param remoteAdress the remoteAdress to set
	 */
	public void setRemoteAdress(String remoteAdress) {
		this.remoteAdress = remoteAdress;
	}
}