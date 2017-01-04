package de.folivora.request;

import javax.servlet.http.HttpSession;

import de.folivora.controller.ApplicationManager;
import de.folivora.controller.UserManager;
import de.folivora.model.User;
import de.folivora.model.UserType;

public class AccessLayer {
	private static boolean isValidToken(User user, String token) {
		return true;
		// return user.getTokenStorage().getToken().equals(token);
	}
	
	public static void signUp(User newUser, HttpSession session) {
		UserManager uManager = ApplicationManager.getApplicationManagerInstance().getuManager();
		User user = uManager.createAndSaveUser(newUser.getName(), newUser.getPassword(), newUser.getBirthday(), newUser.getGender(),
				newUser.getEmail(), 0, UserType.NORMAL, newUser.getHometown());
		uManager.authenticate(user.getName(), user.getPassword()); // To generate a token for the user
		user.setSession(session);
	}
	
	public static boolean createSearchRequest(String title, String description, String pathToDefaultImg,
			Long possibleDelivery_from, Long possibleDelivery_to, Long preferredDelivery_from, Long preferredDelivery_to,
			double costsAndReward, double charges, Double lat, Double lng, String address, User userCreator, String token) {
		
		if(isValidToken(userCreator, token)) {
			ApplicationManager.getApplicationManagerInstance().createAndSaveSearchRequest(title, description, pathToDefaultImg,
					possibleDelivery_from, possibleDelivery_to, preferredDelivery_from, preferredDelivery_to,
					costsAndReward, charges, lat, lng, address, userCreator);
			return true;
		} else {
			return false;
		}
	}
}