package de.folivora.controller;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import de.folivora.model.Gender;
import de.folivora.model.User;
import de.folivora.model.UserCredit;
import de.folivora.model.UserType;
import de.folivora.storage.HibernateSave;
import de.folivora.storage.HibernateUpdate;
import de.folivora.util.Constants;

public class UserManager {
	private DataContainer dC;
	private SecureRandom secureRandom = new SecureRandom();
	
	protected UserManager(DataContainer dC) {
		this.dC = dC;
	}
	
	public boolean authenticate(String nameOrEmail, String pwd) {
		for(User user : dC.getUserList()) {
			if(user.getName().equals(nameOrEmail)
					|| user.getEmail().equals(nameOrEmail)) {
				if(user.getPassword().equals(pwd)) {
					user.refreshTokenStorage(getTrimmedToken(false, Constants.TOKEN_SESSION_LENGTH, ""));
					HibernateUpdate.updateObject(user.getTokenStorage());
					return true;
				}
			}
		}
		
		return false;
	}
	
	public String getToken(boolean uppercase) {
		if(uppercase) {
			return new BigInteger(130, secureRandom).toString(32).toUpperCase();
		} else {
			return new BigInteger(130, secureRandom).toString(32);
		}
	}
	
	public String getTrimmedToken(boolean uppercase, int length, String inputString) {
		String token = getToken(uppercase);
		
		if(length <= token.length()) {
			return token.substring(0, length);
		} else {
			if(length >= length + inputString.length()) {
				return getTrimmedToken(uppercase, length, token);
			} else {
				return (inputString + token).substring(0, length);
			}
		}
	}
	
	public boolean isUniqueUser(User inputUser) {
		for(User u : dC.getUserList()) {
			if(u.getName().equals(inputUser.getName())
					|| u.getEmail().equals(inputUser.getEmail())) {
				return false;
			}
		}
		
		return true;
	}
	
	public User getUserWithSession(HttpSession session) {
		for(User usr : dC.getUserList()) {
			if(usr.getSession() != null && usr.getSession().equals(session)) {
				return usr;
			}
		}
		
		return null;
	}
	
	public User getUserWithNameOrEmail(String nameOrEmail) {
		for(User user : dC.getUserList()) {
			if(user.getName().equals(nameOrEmail) || user.getEmail().equals(nameOrEmail)) {
				return user;
			}
		}
		
		return null;
	}
	
	public User getFolivoraUser() {
		for(User user : dC.getUserList()) {
			if(user.getUserType() == UserType.FOLIVORA) {
				return user;
			}
		}
		
		return null;
	}
	
	public User getPaypalUser() {
		for(User user : dC.getUserList()) {
			if(user.getUserType() == UserType.PAYPAL) {
				return user;
			}
		}
		
		return null;
	}
	
	public List<User> getAdminUsers() {
		List<User> result = new ArrayList<User>();
		
		for(User user : dC.getUserList()) {
			if(user.getUserType() == UserType.ADMIN) {
				result.add(user);
			}
		}
		
		return result;
	}
	
	public User createAndSaveUser(String name, String pwd, Date birthday, Gender gender,
			String email, double initialBalance, UserType userType, String hometown) {
		User u = factory_createUser(name, pwd, birthday, gender, email, initialBalance, userType, hometown);
		HibernateSave.saveOrUpdateObject(u);
		return u;
	}
	
	protected User factory_createUser(String name, String pwd, Date birthday, Gender gender,
			String email, double initialBalance, UserType userType, String hometown) {
		User u = new User(dC.getIdStorage().getNewUserId(), name, pwd, birthday, gender, email, null, userType, hometown);
		u.setCredit(new UserCredit(dC.getIdStorage().getNewUserCreditId(), initialBalance, u));
		dC.getUserList().add(u);
		return u;
	}
}