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

public class UserManager {
	private DataContainer dC;
	
	protected UserManager(DataContainer dC) {
		this.dC = dC;
	}
	
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
	
	public User getUserWithId(String id) {
		for(User user : dC.getUserList()) {
			if(user.getId().equals(id)) {
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
		System.out.println(u);
		return u;
	}
	
	protected User factory_createUser(String name, String pwd, Date birthday, Gender gender,
			String email, double initialBalance, UserType userType, String hometown) {
		User u = new User(name, pwd, birthday, gender, email, null, userType, hometown);
		u.setCredit(new UserCredit(initialBalance, u));
		dC.getUserList().add(u);
		return u;
	}
}