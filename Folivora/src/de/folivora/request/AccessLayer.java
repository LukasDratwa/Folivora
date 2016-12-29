package de.folivora.request;

import de.folivora.controller.ApplicationManager;
import de.folivora.controller.UserManager;
import de.folivora.model.User;
import de.folivora.model.UserType;

public class AccessLayer {
	public static void signUp(User newUser) {
		UserManager uManager = ApplicationManager.getApplicationManagerInstance().getuManager();
		uManager.createAndSaveUser(newUser.getName(), newUser.getPassword(), newUser.getBirthday(), newUser.getGender(),
				newUser.getEmail(), 0, UserType.NORMAL);
	}
}