package de.folivora.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;

import de.folivora.model.Feedback;
import de.folivora.model.Rating;
import de.folivora.model.SearchRequest;
import de.folivora.model.SearchRequestStatus;
import de.folivora.model.Transaction;
import de.folivora.model.User;
import de.folivora.model.UserCredit;
import de.folivora.model.messanger.Message;
import de.folivora.storage.HibernateSave;
import de.folivora.storage.HibernateUpdate;

/**
 * Singleton to bundle all methods to handle the logic of folivora.
 *
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
public class ApplicationManager {
	private UserManager uManager = null;
	private static ApplicationManager instance = null;
	private DataContainer dC;
	private static final Logger logger = Logger.getLogger(ApplicationManager.class);
	
	private ApplicationManager(DataContainer dC) {
		this.dC = dC;
		this.setuManager(new UserManager(dC));
	}
	
	/**
	 * Method to get the instance of the manager and to set the DataContainer. (This method should
	 * be called in the startup of the server!)
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param dC - the DataContainer to set
	 * @return the instance of the ApplicationManager
	 */
	public static ApplicationManager getApplicationManagerInstance(DataContainer dC) {
		if(instance == null) {
			instance = new ApplicationManager(dC);
		}
		return instance;
	}
	
	/**
	 * Method to get the instance of the manager.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @return the instance of the ApplicationManager
	 */
	public static ApplicationManager getApplicationManagerInstance() {
		return getApplicationManagerInstance(null);
	}
	
	/**
	 * Method to create and save a {@link Feedback} in the database. The Feedback 
	 * will be saved in the list of received feedbacks of the feedback.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param rating - the rating
	 * @param description - the description of the feedback, can be empty
	 * @param feedbackCreator - the creator of the feedback
	 * @param referencedSearchRequest - the receiver of the feedback
	 * @return the created {@link Feedback}
	 */
	public Feedback createAndSaveFeedback(Rating rating, String description,
			User feedbackCreator, SearchRequest referencedSearchRequest) {
		Feedback f = factory_createFeedback(rating, description, feedbackCreator, referencedSearchRequest);
		
		// Save the given feedback in the transaction and in the list of received feedbacks
		User feedbackReceiver = null;
		if(referencedSearchRequest.getUserCreator().equals(feedbackCreator)) {
			referencedSearchRequest.setFeedbackOfSearchingUser(f);
			feedbackReceiver = referencedSearchRequest.getUserStasisfier();
		} else {
			referencedSearchRequest.setFeedbackOfDeliveringUser(f);
			feedbackReceiver = referencedSearchRequest.getUserCreator();
		}
		feedbackReceiver.getReceivedFeedbacks().add(f);
		
		HibernateSave.saveOrUpdateObject(referencedSearchRequest);
		return f;
	}
	
	/**
	 * Factory method to create a {@link Feedback}.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param rating - the rating
	 * @param description - the description of the feedback, can be empty
	 * @param feedbackCreator - the creator of the feedback
	 * @param referencedSearchRequest - the referenced search request
	 * @return the created {@link Feedback}
	 */
	private Feedback factory_createFeedback(Rating rating, String description,
			User feedbackCreator, SearchRequest referencedSearchRequest) {
		return new Feedback(rating, description, feedbackCreator, referencedSearchRequest);
	}
	
	/**
	 * Method to execute a transaction.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param token - the user entered unlock token which is needed to execute the transaction
	 * @param t - the {@link Transaction}
	 * @return true if the transaction was executed successfully
	 */
	public boolean executeTransaction(String token, Transaction t) {
		if(t.getUnlockToken() != null && !token.equals(t.getUnlockToken())) {
			logger.warn("Failed to unlock " + t + " with token " + token + "!! Execution cancelled.");
			return true;
		}
		
		UserCredit creditUserFrom = t.getuFrom().getCredit();
		UserCredit creditUserTo = t.getuTo().getCredit();
		
		creditUserFrom.setBalance(creditUserFrom.getBalance() - t.getValue() - t.getFee());
		creditUserTo.setBalance(creditUserTo.getBalance() + t.getValue() + t.getFee());
		
		if(! creditUserFrom.getReferencedTransactions().contains(t)) {
			creditUserFrom.getReferencedTransactions().add(t);
		}
		if(! creditUserTo.getReferencedTransactions().contains(t)) {
			creditUserTo.getReferencedTransactions().add(t);
		}
		
		t.setExecutionDate(new Date());
		t.setExecuted(true);
		
		logger.info("Executed transaction: " + t);
		HibernateUpdate.updateObject(t);
		HibernateUpdate.updateObject(t.getuFrom());
		HibernateUpdate.updateObject(t.getuTo());
		return true;
	}
	
	/**
	 * Method to get a list of transactions which were referenced to the input 
	 * search request.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param srId - the search request
	 * @return the list of transactions, can be empty
	 */
	public List<Transaction> getTransactionOfSearchRequest(String srId) {
		List<Transaction> result = new ArrayList<Transaction>();
		for(Transaction t : dC.getTransactionList()) {
			if(t.getReferencedSr() != null && t.getReferencedSr().getId().toString().equals(srId)) {
				result.add(t);
			}
		}
		return result;
	}
	
	/**
	 * Method to create and save a {@link Transaction} in the database.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param value - the value of the transaction, <u>without</u> any fees
	 * @param fee - the fee
	 * @param userFrom - userFrom
	 * @param userTo - userTo
	 * @param unlockToken - the token which will be needed to execute the created transaction.
	 * 						Enter an empty string if no token should be needed.
	 * @param sr - the referenced search request
	 * @return the created {@link Transaction} 
	 */
	public Transaction createAndSaveTransaction(double value, double fee, User userFrom, User userTo,
			String unlockToken, SearchRequest sr) {
		Transaction t = factory_createTransaction(value, fee, userFrom, userTo, unlockToken, sr);
		HibernateSave.saveOrUpdateObject(t);
		dC.getTransactionList().add(t);
		return t;
	}
	
	/**
	 * Factory method to create a {@link Transaction}.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param value - the value of the transaction, <u>without</u> any fees
	 * @param fee - the fee
	 * @param userFrom - userFrom
	 * @param userTo - userTo
	 * @param unlockToken - the token which will be needed to execute the created transaction.
	 * 						Enter an empty string if no token should be needed.
	 * @param sr - the referenced search request
	 * @return the created {@link Transaction}
	 */
	private Transaction factory_createTransaction(double value, double fee, User userFrom,
			User userTo, String unlockToken, SearchRequest sr) {
		return new Transaction(value, fee, userFrom, userTo, unlockToken, sr);
	}
	
	/**
	 * Get all {@link SearchRequest}s as JsonObjects in a JsonArray where the user
	 * with the input id is involved in. 
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param userId - the id of the user
	 * @return all involved search requests
	 */
	public JsonArray getInvolvedSearchRequestsOfUserAsJsonArray(String userId) {
		JsonArray result = new JsonArray();
		User user = getuManager().getUserWithId(userId);
		
		if(user != null) {
			for(SearchRequest sr : dC.getSearchRequestList()) {
				if(user.isInvolvedIn(sr)) {
					result.add(sr.getAsJsonObject());
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Get all active {@link SearchRequest}s as a JsonArray to return them to the client.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @return all active search requests as a JsonArray
	 */
	public JsonArray getActiveAndInProgressSearchRequestListAsJsonArray() {
		JsonArray result = new JsonArray();
		
		for(SearchRequest sr : dC.getSearchRequestList()) {
			if(sr.getStatus() == SearchRequestStatus.ACTIVE || sr.getStatus() == SearchRequestStatus.IN_PROGRESS) {
				result.add(sr.getAsJsonObject());
			}
		}
		
		return result;
	}
	
	/**
	 * Method to find a {@link SearchRequest} with a unique id.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param srId - the unique id of the {@link SearchRequest}
	 * @return the search request with the input id or null
	 */
	public SearchRequest getSearchRequestWithId(String srId) {
		for(SearchRequest sr : dC.getSearchRequestList()) {
			if(sr.getId().toString().equals(srId)) {
				return sr;
			}
		}
		
		return null;
	}
	
	/**
	 * Method to create and save a {@link SearchRequest} in the database.
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
	 * @return the created {@link SearchRequest}
	 */
	public SearchRequest createAndSaveSearchRequest(String title, String description, String pathToDefaultImg,
			Long possibleDelivery_from, Long possibleDelivery_to, double costsAndReward, double fee,
			Double lat, Double lng, String address, User userCreator) {
		Long[] possibleDelivery = {possibleDelivery_from, possibleDelivery_to};
		return createAndSaveSearchRequest(title, description, pathToDefaultImg, possibleDelivery, costsAndReward,
				fee, lat, lng, address, userCreator);
	}
	
	/**
	 * Method to create and save a {@link SearchRequest} in the database.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param title - the title
	 * @param description - the description
	 * @param pathToDefaultImg - the path to the default img
	 * @param possibleDelivery - long array with unix time [possible delivery from, p. d. to]
	 * @param costsAndReward - the costs and reward for the delivering person
	 * @param fee - the fee which the creator have to pay folivora
	 * @param lat - lat
	 * @param lng - lng
	 * @param address - the address
	 * @param userCreator - the creator
	 * @return the created {@link SearchRequest}
	 */
	public SearchRequest createAndSaveSearchRequest(String title, String description, String pathToDefaultImg,
			Long[] possibleDelivery, double costsAndReward, double fee, 
			Double lat, Double lng, String address, User userCreator) {
		SearchRequest sr = factory_createSearchRequest(title, description, pathToDefaultImg, possibleDelivery,
				costsAndReward, fee, lat, lng, address, userCreator);
		HibernateSave.saveOrUpdateObject(sr);
		dC.getSearchRequestList().add(sr);
		return sr;
	}
	
	/**
	 * Factory method to create a {@link SearchRequest}.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param title - the title
	 * @param description - the description
	 * @param pathToDefaultImg - the path to the default img
	 * @param possibleDelivery - long array with unix time [possible delivery from, p. d. to]
	 * @param costsAndReward - the costs and reward for the delivering person
	 * @param fee - the fee which the creator have to pay folivora
	 * @param lat - lat
	 * @param lng - lng
	 * @param address - the address
	 * @param userCreator - the creator
	 * @return the created {@link SearchRequest}
	 */
	private SearchRequest factory_createSearchRequest(String title, String description, String pathToDefaultImg,
			Long[] possibleDelivery, double costsAndReward, double fee, Double lat,
			Double lng, String address, User userCreator) {
		return new SearchRequest(title, description, pathToDefaultImg,
				possibleDelivery, costsAndReward, fee, lat, lng, address, userCreator);
	}
	
	/**
	 * Method to get the relevant messages of a {@link User} in a JsonArray.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param user - the {@link User}
	 * @return the messages in a JsonArray
	 */
	public JsonArray getRelevantMessagesOfUserAsJsonArray(User user) {
		JsonArray jArray = new JsonArray();
		
		for(Message msg : getRelevantMessagesOfUser(user)) {
			jArray.add(msg.getAsJsonObject());
		}
		
		return jArray;
	}
	
	/**
	 * Method to get the relevant messages of a {@link User}.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param user - the {@link User}
	 * @return the messages
	 */
	public List<Message> getRelevantMessagesOfUser(User user) {
		if(user != null && user.getRelevantMessages() != null)  {
			return user.getRelevantMessages();
		} else {
			return new ArrayList<Message>();
		}
	}
	
	/**
	 * Method to create and a {@link Message} in the database.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param title - the title of the {@link Message}
	 * @param text - the text of the {@link Message}
	 * @param sender - the sending {@link User}
	 * @param receiver - the receiving {@link User}
	 * @param referencedSr - the referenced {@link SearchRequest}
	 * @return the created {@link Message}
	 */
	public Message createAndSaveMessage(String title, String text, User sender, User receiver, SearchRequest referencedSr) {
		Message msg = factory_createMessage(title, text, sender, receiver, referencedSr);
		HibernateSave.saveOrUpdateObject(msg);
		dC.getMessageList().add(msg);
		msg.getSender().getRelevantMessages().add(msg);
		msg.getReceiver().getRelevantMessages().add(msg);
		return msg;
	}
	
	/**
	 * Factory method create a {@link Message}.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param title - the title of the {@link Message}
	 * @param text - the text of the {@link Message}
	 * @param sender - the sending {@link User}
	 * @param receiver - the receiving {@link User}
	 * @param referencedSr - the referenced {@link SearchRequest}
	 * @return the created {@link Message}
	 */
	private Message factory_createMessage(String title, String text, User sender, User receiver, SearchRequest referencedSr) {
		return new Message(title, text, sender, receiver, referencedSr);
	}
	
	/**
	 * @return the uManager
	 */
	public UserManager getuManager() {
		return uManager;
	}

	/**
	 * @param uManager the uManager to set
	 */
	public void setuManager(UserManager uManager) {
		this.uManager = uManager;
	}

	/**
	 * @return the dC
	 */
	public DataContainer getdC() {
		return dC;
	}

	/**
	 * @param dC the dC to set
	 */
	public void setdC(DataContainer dC) {
		this.dC = dC;
	}
}
