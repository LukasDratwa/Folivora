package de.folivora.request;

import javax.servlet.http.HttpSession;

import de.folivora.controller.ApplicationManager;
import de.folivora.controller.UserManager;
import de.folivora.model.Constants;
import de.folivora.model.SearchRequest;
import de.folivora.model.SearchRequestStatus;
import de.folivora.model.Transaction;
import de.folivora.model.User;
import de.folivora.model.UserType;
import de.folivora.storage.HibernateUpdate;
import de.folivora.util.Util;

/**
 * Class to implement a controlled layer between requests and controller.
 * 
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
public class AccessLayer {
	/**
	 * TODO
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param user - the user
	 * @param token - the token
	 * @return true if the given token is equal to the user token which is saved in the database 
	 */
	private static boolean isValidToken(User user, String token) {
		return true;
		// TODO be sure that in every call is a token ... return user.getTokenStorage().getToken().equals(token);
	}
	
	/**
	 * Method to sign up a new {@link User}.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param newUser - the data of the new user
	 * @param session - the session of the request
	 */
	public static void signUp(User newUser, HttpSession session) {
		UserManager uManager = ApplicationManager.getApplicationManagerInstance().getuManager();
		User user = uManager.createAndSaveUser(newUser.getName(), newUser.getPassword(), newUser.getBirthday(), newUser.getGender(),
				newUser.getEmail(), Constants.USERCREDIT_ENABLE_INITIAL_BALANCE ? Constants.USERCREDIT_INITIAL_BALANCE : 0.0,
						UserType.NORMAL, newUser.getHometown());
		uManager.authenticate(user.getName(), user.getPassword()); // To generate a token for the user
		user.setSession(session);
	}
	
	/**
	 * Method to call {@link ApplicationManager#createAndSaveSearchRequest(String, String, String, Long, Long, double, double,
	 * Double, Double, String, User)} and create the needed {@link Transaction} between the creator and folivora.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param title - the title
	 * @param description - the description
	 * @param pathToDefaultImg - the path to the default img
	 * @param possibleDelivery_from - unix time from which the delivery is possible
	 * @param possibleDelivery_to - unix time till which the delivery is possible
	 * @param costsAndReward - the costs and reward for the delivering person
	 * @param fee - the fee which the creator have to pay folivora
	 * @param lat - lat
	 * @param lng - lng
	 * @param address - the address
	 * @param userCreator - the creator
	 * @param token - the unlock token for this search request
	 * @return the created {@link SearchRequest}
	 */
	public static SearchRequest createSearchRequest(String title, String description, String pathToDefaultImg,
			Long possibleDelivery_from, Long possibleDelivery_to, double costsAndReward, double fee,
			Double lat, Double lng, String address, User userCreator, String token) {
		
		if(isValidToken(userCreator, token)) {
			ApplicationManager aManager = ApplicationManager.getApplicationManagerInstance();
			SearchRequest sr = aManager.createAndSaveSearchRequest(title, description, pathToDefaultImg,
					possibleDelivery_from, possibleDelivery_to, costsAndReward, fee, lat, lng, address, userCreator);
			
			User folivora = aManager.getuManager().getFolivoraUser();
			Transaction t = aManager.createAndSaveTransaction(sr.getCostsAndReward(), sr.getFee(), userCreator, folivora, null, sr);
			aManager.executeTransaction(null, t);
			return sr;
		}
		
		return null;
	}
	
	/**
	 * Method to statisfy a search request.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param callingUser - the calling {@link User}
	 * @param sr - the referenced {@link SearchRequest}
	 */
	public static void statisfySearchRequest(User callingUser, SearchRequest sr) {
		sr.setStatus(SearchRequestStatus.IN_PROGRESS);
		sr.setUserStasisfier(callingUser);
		HibernateUpdate.updateObject(sr);
		
		ApplicationManager aManager = ApplicationManager.getApplicationManagerInstance();
		User folivora = aManager.getuManager().getFolivoraUser();
		aManager.createAndSaveTransaction(sr.getCostsAndReward(), 0,
				folivora, callingUser, Util.getSrUnlockToken(), sr);
		
		aManager.createAndSaveMessage("\"" + sr.getTitle() + "\" angenommen.",
				"Das Gesuch \"" + sr.getTitle() + "\" von " + sr.getUserCreator().getName()
				+ "wurde erfolgreich von Ihnen angenommen. Bitte setzen Sie sich mit "
				+ sr.getUserCreator().getName() + " bezüglich der Lieferdetails in Verbindung."
				+ "Bei erfolgreichem Abschluss erwartet Sie eine Gutschrift über "
				+ sr.getCostsAndReward() + "€.", folivora, callingUser, sr);
	}
	
	/**
	 * Method cancel a search request.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param callingUser - the calling {@link User}
	 * @param sr - the referenced {@link SearchRequest}
	 */
	public static void cancelSearchRequest(User callingUser, SearchRequest sr) {
		ApplicationManager aManager = ApplicationManager.getApplicationManagerInstance();
		User folivora = aManager.getuManager().getFolivoraUser();
		Transaction t = aManager.createAndSaveTransaction(sr.getCostsAndReward(), sr.getFee(), folivora, callingUser, null, sr);
		aManager.executeTransaction(null, t);
		
		sr.setStatus(SearchRequestStatus.CANCELLED);
		HibernateUpdate.updateObject(sr);
		
		aManager.createAndSaveMessage("\"" + sr.getTitle() + "\" zurückgezogen",
				"Ihr Gesuch \"" + sr.getTitle() + "\" vom " + sr.getCreationTimestamp()
				+ "wurde erfolgreich zurückgezogen. Sie erhalten in Kürze eine Gutschrift "
				+ "über " + (sr.getCostsAndReward() + sr.getFee()) + "€.", folivora, callingUser, sr);
	}
	
	/**
	 * Method to finish a search request.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param sr - the referenced {@link SearchRequest}
	 * @param unlockToken - the token to unlock
	 * @param callingUser - the calling {@link User}
	 * @return true if the search request could finished (entered token was correct to execute finishing transaction)
	 */
	public static boolean finsihSr(SearchRequest sr, String unlockToken, User callingUser) {
		ApplicationManager aManager = ApplicationManager.getApplicationManagerInstance();
		
		for(Transaction t : aManager.getTransactionOfSearchRequest(sr.getId().toString())) {
			if(! t.isExecuted() && (t.getuFrom().getId() ==  callingUser.getId() || t.getuTo().getId() ==  callingUser.getId())) {
				boolean executedSuccessfully = aManager.executeTransaction(unlockToken, t);
				
				if(executedSuccessfully) {
					sr.setStatus(SearchRequestStatus.STATISFIED);
					HibernateUpdate.updateObject(sr);
					
					User folivora = aManager.getuManager().getFolivoraUser();
					aManager.createAndSaveMessage("\"" + sr.getTitle() + "\" abgeschlossen.",
							"Das Gesuch \"" + sr.getTitle() + "\" von " + sr.getUserCreator().getName()
							+ "wurde erfolgreich von Ihnen abeschlossen. Sie erhalten in Kürze eine Gutschrift "
							+ "über " + sr.getCostsAndReward() + "€.", folivora, callingUser, sr);
					return true;
				}
			}
		}
		
		return false;
	}
}