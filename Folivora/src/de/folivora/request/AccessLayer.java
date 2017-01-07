package de.folivora.request;

import javax.servlet.http.HttpSession;

import de.folivora.controller.ApplicationManager;
import de.folivora.controller.UserManager;
import de.folivora.model.SearchRequest;
import de.folivora.model.SearchRequestStatus;
import de.folivora.model.Transaction;
import de.folivora.model.User;
import de.folivora.model.UserType;
import de.folivora.storage.HibernateUpdate;
import de.folivora.util.Constants;
import de.folivora.util.Util;

public class AccessLayer {
	private static boolean isValidToken(User user, String token) {
		return true;
		// TODO be sure that in every call is a token ... return user.getTokenStorage().getToken().equals(token);
	}
	
	public static void signUp(User newUser, HttpSession session) {
		UserManager uManager = ApplicationManager.getApplicationManagerInstance().getuManager();
		User user = uManager.createAndSaveUser(newUser.getName(), newUser.getPassword(), newUser.getBirthday(), newUser.getGender(),
				newUser.getEmail(), Constants.USERCREDIT_ENABLE_INITIAL_BALANCE ? Constants.USERCREDIT_INITIAL_BALANCE : 0.0,
						UserType.NORMAL, newUser.getHometown());
		uManager.authenticate(user.getName(), user.getPassword()); // To generate a token for the user
		user.setSession(session);
	}
	
	public static boolean createSearchRequest(String title, String description, String pathToDefaultImg,
			Long possibleDelivery_from, Long possibleDelivery_to, double costsAndReward, double fee,
			Double lat, Double lng, String address, User userCreator, String token) {
		
		if(isValidToken(userCreator, token)) {
			ApplicationManager aManager = ApplicationManager.getApplicationManagerInstance();
			SearchRequest sr = aManager.createAndSaveSearchRequest(title, description, pathToDefaultImg,
					possibleDelivery_from, possibleDelivery_to, costsAndReward, fee, lat, lng, address, userCreator);
			
			User folivora = aManager.getuManager().getFolivoraUser();
			Transaction t = aManager.createAndSaveTransaction(sr.getCostsAndReward(), sr.getFee(), userCreator, folivora, null, sr);
			aManager.executeTransaction(null, t);
			return true;
		} else {
			return false;
		}
	}
	
	public static boolean statisfySearchRequest(User callingUser, SearchRequest sr) {
		sr.setStatus(SearchRequestStatus.IN_PROGRESS);
		sr.setUserStasisfier(callingUser);
		HibernateUpdate.updateObject(sr);
		
		ApplicationManager aManager = ApplicationManager.getApplicationManagerInstance();
		User folivora = aManager.getuManager().getFolivoraUser();
		aManager.createAndSaveTransaction(sr.getCostsAndReward(), 0,
				folivora, callingUser, Util.getSrUnlockToken(), sr);
		
		return true;
	}
	
	public static boolean cancelSearchRequest(User callingUser, SearchRequest sr) {
		ApplicationManager aManager = ApplicationManager.getApplicationManagerInstance();
		User folivora = aManager.getuManager().getFolivoraUser();
		System.out.println(folivora);
		Transaction t = aManager.createAndSaveTransaction(sr.getCostsAndReward(), sr.getFee(), folivora, callingUser, null, sr);
		aManager.executeTransaction(null, t);
		
		sr.setStatus(SearchRequestStatus.CANCELLED);
		HibernateUpdate.updateObject(sr);
		return true;
	}
	
	public static boolean finsihSr(SearchRequest sr, String unlockToken, User callingUser) {
		ApplicationManager aManager = ApplicationManager.getApplicationManagerInstance();
		
		for(Transaction t : aManager.getTransactionOfSearchRequest(sr.getId())) {
			if(! t.isExecuted() && (t.getuFrom().getId() ==  callingUser.getId() || t.getuTo().getId() ==  callingUser.getId())) {
				boolean executedSuccessfully = aManager.executeTransaction(unlockToken, t);
				
				if(executedSuccessfully) {
					sr.setStatus(SearchRequestStatus.STATISFIED);
					HibernateUpdate.updateObject(sr);
					return true;
				}
			}
		}
		
		return false;
	}
}