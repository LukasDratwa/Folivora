package de.folivora.request;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;

import de.folivora.controller.ApplicationManager;
import de.folivora.controller.UserManager;
import de.folivora.model.AdditionalReward;
import de.folivora.model.Constants;
import de.folivora.model.Feedback;
import de.folivora.model.SearchRequest;
import de.folivora.model.SearchRequestStatus;
import de.folivora.model.Transaction;
import de.folivora.model.User;
import de.folivora.model.UserType;
import de.folivora.model.messenger.Message;
import de.folivora.storage.HibernateUpdate;
import de.folivora.util.Util;

/**
 * Class to implement a controlled layer between requests and controller.
 * 
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
public class AccessLayer {
	private static Logger logger = Logger.getLogger(AccessLayer.class);
	
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
	 * Method to replace the german Umlauts with their utf-8 representation.
	 * 
	 * @param input - the input
	 * @return the encoded input string
	 */
	private static String encodeUTF8(String input) {
		if(input == null) {
			return null;
		}
		
		input = input.replace("ä", "\u00e4");
		input = input.replace("Ä", "\u00c4");
		input = input.replace("ö", "\u00f6");
		input = input.replace("Ö", "\u00d6");
		input = input.replace("ü", "\u00fc");
		input = input.replace("Ü", "\u00dc");
		input = input.replace("ß", "\u00df");
		
		return input;
	}
	
	/**
	 * Method to create a new feedback.
	 * 
	 * <hr>Created on 22.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param stars - the rated stars
	 * @param feedbackText - the optional feedback text
	 * @param callingUser - the calling user
	 * @param sr - the search request
	 * @return true if the feedback could created successfully
	 */
	public static boolean createFeedback(int stars, String feedbackText, User callingUser, SearchRequest sr) {
		ApplicationManager aManager = ApplicationManager.getApplicationManagerInstance();
		User folivora = aManager.getuManager().getFolivoraUser();
		User feedbackReceiver = null;
		
		feedbackText = encodeUTF8(feedbackText);
		
		// Check if users already gave feedback for this sr
		if(callingUser.getId().toString().equals(sr.getUserCreator().getId().toString()) && sr.getFeedbackOfSearchingUser() != null
				|| callingUser.getId().toString().equals(sr.getUserStasisfier().getId().toString()) && sr.getFeedbackOfDeliveringUser() != null) {
			logger.warn("Failed to create feedback, user aldready gave feedback for: " + sr);
			return false;
		}
		
		Feedback f = aManager.createAndSaveFeedback(aManager.getRatingWithStars(stars),
				feedbackText, callingUser, sr);
		if(callingUser.getId().toString().equals(sr.getUserCreator().getId().toString())) {
			sr.setFeedbackOfSearchingUser(f);
			feedbackReceiver = sr.getUserStasisfier();
			
			logger.info("Set feedback of searching user in " + sr + ": " + f);
		} else {
			sr.setFeedbackOfDeliveringUser(f);
			feedbackReceiver = sr.getUserCreator();
			
			logger.info("Set feedback of delivering user in " + sr + ": " + f);
		}
		HibernateUpdate.updateObject(sr);
		
		// TODO link zu dem feedback 
		aManager.createAndSaveMessage("Erhaltenes Feedback", "Sie haben zu diesem Gesuch Feedback erhalten, schauen "
				+ " Sie es sich gleich an.", folivora, feedbackReceiver, sr);
		
		return true;
	}
	
	/**
	 * Method to sign up a new {@link User}.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param newUser - the data of the new user
	 * @param request - the request
	 */
	public static void signUp(User newUser, HttpServletRequest request) {
		UserManager uManager = ApplicationManager.getApplicationManagerInstance().getuManager();
		User user = uManager.createAndSaveUser(newUser.getName(), newUser.getPassword(), newUser.getBirthday(), newUser.getGender(),
				newUser.getEmail(), Constants.USERCREDIT_ENABLE_INITIAL_BALANCE ? Constants.USERCREDIT_INITIAL_BALANCE : 0.0,
						UserType.NORMAL, newUser.getHometown());
		uManager.authenticate(user.getName(), user.getPassword(), request.getRemoteAddr()); // To generate a token for the user
		user.setSession(request.getSession());
		user.setRemoteAdress(request.getRemoteAddr());
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
			title = encodeUTF8(title);
			description = encodeUTF8(description);
			address = encodeUTF8(address);
			
			ApplicationManager aManager = ApplicationManager.getApplicationManagerInstance();
			SearchRequest sr = aManager.createAndSaveSearchRequest(title, description, pathToDefaultImg,
					possibleDelivery_from, possibleDelivery_to, costsAndReward, fee, lat, lng, address, userCreator);
			
			User folivora = aManager.getuManager().getFolivoraUser();
			Transaction t = aManager.createAndSaveTransaction(sr.getCostsAndReward(), sr.getFee(), userCreator, folivora, null, sr);
			aManager.executeTransaction(null, t);
			
			aManager.createAndSaveMessage("\"" + sr.getTitle() + "\" erfolgreich erstellt",
					"Ihr Gesuch wurde erfolgreich erstellt und es wurden die Kosten von " + ("" + (t.getFee() + t.getValue())).replace('.', ',')
					+ " € von Ihrem Guthaben abgebucht.", folivora, userCreator, sr); // TODO weitere infos
			
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
		Transaction t = aManager.createAndSaveTransaction(sr.getCostsAndReward(), 0,
				folivora, callingUser, Util.getSrUnlockToken(), sr);
		
		aManager.createAndSaveMessage("\"" + sr.getTitle() + "\" angenommen.",
				"Ihr Gesuch \"" + sr.getTitle() + "\" wurde erfolgreich von <a href='user.jsp?id=" + callingUser.getId().toString() + "'>"
				+ callingUser.getName() + "</a> angenommen.<br><br>"
				+ "Bitte vergessen Sie nicht dem Lieferanten bei erfolgreicher Lieferung folgenden Code zu übergeben: "
				+ "<b class='msg-transaction-unlock-token'>" + t.getUnlockToken() + "</b>"
				+ "<br><br>"
				+ "Profil von " + callingUser.getName() + ": <a href='user.jsp?id=" + callingUser.getId().toString() + "'>link</a>",
				folivora, sr.getUserCreator(), sr);
		
		aManager.createAndSaveMessage("\"" + sr.getTitle() + "\" angenommen.",
				"Das Gesuch \"" + sr.getTitle() + "\" von <a href='user.jsp?id=" + sr.getUserCreator().getId().toString() + "'>"
				+ sr.getUserCreator().getName() + "</a> wurde erfolgreich von Ihnen angenommen. Bitte setzen Sie sich mit "
				+ "<a href='user.jsp?id=" + sr.getUserCreator().getId().toString() + "'>" + sr.getUserCreator().getName() + "</a>"
				+ " bezüglich der Lieferdetails in Verbindung. Bei erfolgreichem Abschluss erwartet Sie eine Gutschrift über "
				+ sr.getCostsAndReward() + "€.<br><br>"
				+ "Vergessen Sie nicht nach erfolgreicher Lieferung nach dem Freischaltungscode "
				+ "zu fragen, damit wir Ihnen die Kosten und die Belohnung gutschreiben können."
				+ "<br><br>"
				+ "Profil von " + sr.getUserCreator().getName() + ": <a href='user.jsp?id=" + sr.getUserCreator().getId().toString() + "'>link</a>",
				folivora, callingUser, sr);
		
		// If there are additional active rewards and wished, notfiy statisfier
		if(Constants.ADDITIONAL_REWARD_NOTFIY_STATISFIER_WITH_EMAIL && aManager.getOneValidPublishedAdditionalReward() != null) {
			aManager.createAndSaveMessage("Event: Seien Sie schnell!",
					"Derzeitig sind " + aManager.countAmountOfValidPublishedAdditionalRewards() + " zusätzliche Rewards "
					+ " in Höhe von " + aManager.getOneValidPublishedAdditionalReward().getValue() + " € ausgeschüttet. "
					+ "Befriedigen Sie das Gesuch schnellstmöglich und sichern Sie sich die zusätzliche Belohnung! "
					+ "Nur so lange der Vorrat reicht.", folivora, callingUser, sr);
		}
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
		
		// Save a ref to the transaction which represents the cancellation in the initial transaction
		Transaction initialT = aManager.getInitialTransactionOfSearchRequest(sr.getId().toString());
		initialT.setCancelled(true);
		initialT.setCacelTransactionId(t.getId().toString());
		HibernateUpdate.updateObject(initialT);
		
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
			if(! t.isExecuted() && (t.getuFrom().getId().toString().equals(callingUser.getId().toString())
					|| t.getuTo().getId().toString().equals(callingUser.getId().toString()))) {
				boolean executedSuccessfully = aManager.executeTransaction(unlockToken, t);
				
				if(executedSuccessfully) {
					sr.setStatus(SearchRequestStatus.STATISFIED);
					HibernateUpdate.updateObject(sr);
					
					User folivora = aManager.getuManager().getFolivoraUser();
					aManager.createAndSaveMessage("\"" + sr.getTitle() + "\" abgeschlossen.",
							"Das Gesuch \"" + sr.getTitle() + "\" von " + sr.getUserCreator().getName()
							+ " wurde erfolgreich von Ihnen abeschlossen. Sie erhalten in Kürze eine Gutschrift "
							+ "über " + sr.getCostsAndReward() + "€.", folivora, callingUser, sr);
					
					aManager.createAndSaveMessage("Bitte Feedback geben", "Das Gesuch wurde erfolgreich befriedigt, bitte "
							+ "geben Sie der anderen Partei noch Feedback: ", folivora, sr.getUserCreator(), sr);
					
					aManager.createAndSaveMessage("Bitte Feedback geben", "Das Gesuch wurde erfolgreich befriedigt, bitte "
							+ "geben Sie der anderen Partei noch Feedback: ", folivora, sr.getUserStasisfier(), sr);
					
					// Give the statisfier an additional reward
					if(aManager.countAmountOfValidPublishedAdditionalRewards() > 0) {
						AdditionalReward ar = aManager.getOneValidPublishedAdditionalReward();
						aManager.createAndSaveTransaction(ar.getValue(), 0.0, folivora, sr.getUserStasisfier(), "", sr);
						aManager.activateAddtionalReward(sr.getUserStasisfier(), ar);
						
						aManager.createAndSaveMessage("Herzlichen Glückwunsch",
								"Herzlichen Glückwunsch! Sie waren schnell und hab sich einen zusätzliche Belohnung von "
								+ ar.getValue() + " € verdient. Diese wird Ihnen nun gutgeschrieben.",
								folivora, sr.getUserStasisfier(), sr);
					}
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Method to get the relevant messages of a {@link User} in a JsonArray.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param callingUser - the {@link User}
	 * @param onlyUnseen - true if only unseen messages should be returned
	 * @return the relevant messages
	 */
	public static JsonArray getRelevantMsgsOfUser(User callingUser, boolean onlyUnseen) {
		return ApplicationManager.getApplicationManagerInstance().getRelevantMessagesOfUserAsJsonArray(callingUser, onlyUnseen);
	}
	
	/**
	 * Method to mark a message as seen.
	 * 
	 * <hr>Created on 15.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param callingUser - the calling user
	 * @param msgIds - the messageIds separated with a ","
	 */
	public static void setMsgSeen(User callingUser, String msgIds) {
		ApplicationManager aManager = ApplicationManager.getApplicationManagerInstance();
		for(String msgId : msgIds.split(",")) {
			Message msg = aManager.getMessageWithId(msgId);
			
			if(msg != null
					&& !msg.getSender().getId().toString().equals(callingUser.getId().toString())) {
				// Second condition, because only the receiver can set a msg to seen
				if(! msg.isSeen()) {
					msg.setSeen(true);
					HibernateUpdate.updateObject(msg);
				}
			}
			
		}
	}
	
	/**
	 * Message to send a new user message.
	 * 
	 * <hr>Created on 21.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param title - the title of the message
	 * @param text - the text of the message
	 * @param sender - the sender
	 * @param receiver - the receiver of the message
	 * @param referencedSr - the referenced search request
	 * @return the created and send message
	 */
	public static Message sendMsg(String title, String text, User sender, User receiver, SearchRequest referencedSr) {
		title = encodeUTF8(title);
		text = encodeUTF8(text);
		
		return ApplicationManager.getApplicationManagerInstance().createAndSaveMessage(title, text, sender, receiver, referencedSr);
	}
}