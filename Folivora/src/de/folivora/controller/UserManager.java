package de.folivora.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import de.folivora.model.Constants;
import de.folivora.model.Gender;
import de.folivora.model.User;
import de.folivora.model.UserCredit;
import de.folivora.model.UserType;
import de.folivora.storage.HibernateSave;
import de.folivora.storage.HibernateUpdate;
import de.folivora.util.Util;

/**
 * Class to manage functions which include users. 
 * 
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
public class UserManager {
	private DataContainer dC;
	
	protected UserManager(DataContainer dC) {
		this.dC = dC;
	}
	
	/**
	 * Method to authenticate a user. The user is identified with his name or email.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param nameOrEmail - the name or email of the user
	 * @param pwd - the password
	 * @return true if the user could signed in, false if not
	 */
	public boolean authenticate(String nameOrEmail, String pwd) {
		for(User user : dC.getUserList()) {
			if(user.getName().equals(nameOrEmail)
					|| user.getEmail().equals(nameOrEmail)) {
				if(user.getPassword().equals(pwd)) {
					user.refreshTokenStorage(Util.getTrimmedToken(false, Constants.TOKEN_SESSION_LENGTH, ""));
					HibernateUpdate.updateObject(user.getTokenStorage());
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Method to get a {@link User} with a HttpSession. (When the user sign in, the referenced Session
	 * is saved in the user object.)
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param session - the session
	 * @return found {@link User} or null
	 */
	public User getUserWithSession(HttpSession session) {
		for(User usr : dC.getUserList()) {
			if(usr.getSession() != null && usr.getSession().equals(session)) {
				return usr;
			}
		}
		
		return null;
	}
	
	/**
	 * Method to check if a name or email is already used by another user.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param inputUser - the {@link User} you want to create / save / check for uniqueness
	 * @return true if unique
	 */
	public boolean isUniqueUser(User inputUser) {
		for(User u : dC.getUserList()) {
			if(u.getName().equals(inputUser.getName())
					|| u.getEmail().equals(inputUser.getEmail())) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Method to get a {link User} with his name or email.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param nameOrEmail - the name or email of the {@link User}
	 * @return found {@link User} or null
	 */
	public User getUserWithNameOrEmail(String nameOrEmail) {
		for(User user : dC.getUserList()) {
			if(user.getName().equals(nameOrEmail) || user.getEmail().equals(nameOrEmail)) {
				return user;
			}
		}
		
		return null;
	}
	
	/**
	 * Method to get a {@link User} with his id.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param id - the id of the {@link User}
	 * @return found {@link User} or null
	 */
	public User getUserWithId(String id) {
		for(User user : dC.getUserList()) {
			if(user.getId().equals(id)) {
				return user;
			}
		}
		
		return null;
	}
	
	/**
	 * Get the {@link User} where {@link User#getUserType()} == {@link UserType#FOLIVORA}.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @return the folivora {@link User}
	 */
	public User getFolivoraUser() {
		for(User user : dC.getUserList()) {
			if(user.getUserType() == UserType.FOLIVORA) {
				return user;
			}
		}
		
		return null;
	}
	
	/**
	 * Get the {@link User} where {@link User#getUserType()} == {@link UserType#PAYPAL}.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @return the paypal {@link User}
	 */
	public User getPaypalUser() {
		for(User user : dC.getUserList()) {
			if(user.getUserType() == UserType.PAYPAL) {
				return user;
			}
		}
		
		return null;
	}
	
	/**
	 * Method to get all users with the {@link UserType#ADMIN}.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @return list of admin users
	 */
	public List<User> getAdminUsers() {
		List<User> result = new ArrayList<User>();
		
		for(User user : dC.getUserList()) {
			if(user.getUserType() == UserType.ADMIN) {
				result.add(user);
			}
		}
		
		return result;
	}
	
	/**
	 * Method to create and save a {@link User} in the database.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param name - the name (unique!)
	 * @param pwd - the password
	 * @param birthday - the birthday
	 * @param gender - the gender
	 * @param email - the email (unique!)
	 * @param initialBalance - the initial balance of the {@link UserCredit}
	 * @param userType - the {@link UserType}
	 * @param hometown - the hometown
	 * @return the created user
	 */
	public User createAndSaveUser(String name, String pwd, Date birthday, Gender gender,
			String email, double initialBalance, UserType userType, String hometown) {
		User u = factory_createUser(name, pwd, birthday, gender, email, initialBalance, userType, hometown);
		HibernateSave.saveOrUpdateObject(u);
		System.out.println(u);
		return u;
	}
	
	/**
	 * Factory method to create a {@link User}.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param name - the name (unique!)
	 * @param pwd - the password
	 * @param birthday - the birthday
	 * @param gender - the gender
	 * @param email - the email (unique!)
	 * @param initialBalance - the initial balance of the {@link UserCredit}
	 * @param userType - the {@link UserType}
	 * @param hometown - the hometown
	 * @return the created user
	 */
	protected User factory_createUser(String name, String pwd, Date birthday, Gender gender,
			String email, double initialBalance, UserType userType, String hometown) {
		User u = new User(name, pwd, birthday, gender, email, null, userType, hometown);
		u.setCredit(new UserCredit(initialBalance, u));
		dC.getUserList().add(u);
		return u;
	}
}