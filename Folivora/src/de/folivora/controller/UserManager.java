package de.folivora.controller;

import java.util.Date;

import de.folivora.model.Gender;
import de.folivora.model.User;
import de.folivora.model.UserCredit;

public class UserManager {
	private DataContainer dC;
	
	protected UserManager(DataContainer dC) {
		this.dC = dC;
	}
	
	public User factory_createUser(String name, String hashedPwd, Date birthday, Gender gender,
			String email, double initialBalance) {
		User u = new User(dC.getNewUserId(), name, hashedPwd, birthday, gender, email, null);
		u.setCredit(new UserCredit(dC.getNewUserCreditId(), initialBalance, u));
		dC.getUserList().add(u);
		return u;
	}
}