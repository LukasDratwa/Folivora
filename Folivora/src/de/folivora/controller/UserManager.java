package de.folivora.controller;

import java.util.Date;

import de.folivora.model.Gender;
import de.folivora.model.User;
import de.folivora.model.UserCredit;
import de.folivora.model.UserType;
import de.folivora.storage.HibernateSave;

public class UserManager {
	private DataContainer dC;
	
	protected UserManager(DataContainer dC) {
		this.dC = dC;
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
	
	public User createAndSaveUser(String name, String pwd, Date birthday, Gender gender,
			String email, double initialBalance, UserType userType) {
		User u = factory_createUser(name, pwd, birthday, gender, email, initialBalance, userType);
		HibernateSave.saveOrUpdateObject(u);
		return u;
	}
	
	protected User factory_createUser(String name, String pwd, Date birthday, Gender gender,
			String email, double initialBalance, UserType userType) {
		User u = new User(dC.getIdStorage().getNewUserId(), name, pwd, birthday, gender, email, null, userType);
		u.setCredit(new UserCredit(dC.getIdStorage().getNewUserCreditId(), initialBalance, u));
		dC.getUserList().add(u);
		return u;
	}
}