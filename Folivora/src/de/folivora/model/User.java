package de.folivora.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.servlet.http.HttpSession;

@Entity
public class User {
	@Id
	private long id;
	private String name;
	private String hashedPwd;
	private Date birthday;
	private Gender gender;
	private String email;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER, targetEntity=UserCredit.class, mappedBy="owner")
	private UserCredit credit;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, targetEntity=Feedback.class, mappedBy="feedbackCreator")
	private List<Feedback> feedbacks = new ArrayList<Feedback>();
	
	@Transient
	private HttpSession session = null;
	
	public User(long id, String name, String hashedPwd, Date birthday, Gender gender,
			String email, UserCredit userCredit) {
		this.id = id;
		this.name = name;
		this.hashedPwd = hashedPwd;
		this.birthday = birthday;
		this.gender = gender;
		this.email = email;
		this.credit = userCredit;
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
	 * @return the hashedPwd
	 */
	public String getHashedPwd() {
		return hashedPwd;
	}
	
	/**
	 * @param hashedPwd the hashedPwd to set
	 */
	public void setHashedPwd(String hashedPwd) {
		this.hashedPwd = hashedPwd;
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
	 * @return the feedbacks
	 */
	public List<Feedback> getFeedbacks() {
		return feedbacks;
	}
	
	/**
	 * @param feedbacks the feedbacks to set
	 */
	public void setFeedbacks(List<Feedback> feedbacks) {
		this.feedbacks = feedbacks;
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
}