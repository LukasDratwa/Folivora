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

public class ApplicationManager {
	private UserManager uManager = null;
	private static ApplicationManager instance = null;
	private DataContainer dC;
	private static final Logger logger = Logger.getLogger(ApplicationManager.class);
	
	private ApplicationManager(DataContainer dC) {
		this.dC = dC;
		this.setuManager(new UserManager(dC));
	}
	
	public static ApplicationManager getApplicationManagerInstance(DataContainer dC) {
		if(instance == null) {
			instance = new ApplicationManager(dC);
		}
		return instance;
	}
	
	public static ApplicationManager getApplicationManagerInstance() {
		return getApplicationManagerInstance(null);
	}
	
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
	
	private Feedback factory_createFeedback(Rating rating, String description,
			User feedbackCreator, SearchRequest referencedSearchRequest) {
		return new Feedback(rating, description, feedbackCreator, referencedSearchRequest);
	}
	
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
	
	public List<Transaction> getTransactionOfSearchRequest(String srId) {
		List<Transaction> result = new ArrayList<Transaction>();
		for(Transaction t : dC.getTransactionList()) {
			if(t.getReferencedSr() != null && t.getReferencedSr().getId().toString().equals(srId)) {
				result.add(t);
			}
		}
		return result;
	}
	
	public Transaction createAndSaveTransaction(double value, double fee, User userFrom, User userTo,
			String unlockToken, SearchRequest sr) {
		Transaction t = factory_createTransaction(value, fee, userFrom, userTo, unlockToken, sr);
		HibernateSave.saveOrUpdateObject(t);
		dC.getTransactionList().add(t);
		return t;
	}
	
	private Transaction factory_createTransaction(double value, double fee, User userFrom,
			User userTo, String unlockToken, SearchRequest sr) {
		return new Transaction(value, fee, userFrom, userTo, unlockToken, sr);
	}
	
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
	
	public JsonArray getActiveAndInProgressSearchRequestListAsJsonArray() {
		JsonArray result = new JsonArray();
		
		for(SearchRequest sr : dC.getSearchRequestList()) {
			if(sr.getStatus() == SearchRequestStatus.ACTIVE || sr.getStatus() == SearchRequestStatus.IN_PROGRESS) {
				result.add(sr.getAsJsonObject());
			}
		}
		
		return result;
	}
	
	public SearchRequest getSearchRequestWithId(String srId) {
		for(SearchRequest sr : dC.getSearchRequestList()) {
			if(sr.getId().toString().equals(srId)) {
				return sr;
			}
		}
		
		return null;
	}
	
	public SearchRequest createAndSaveSearchRequest(String title, String description, String pathToDefaultImg,
			Long possibleDelivery_from, Long possibleDelivery_to, double costsAndReward, double fee,
			Double lat, Double lng, String address, User userCreator) {
		Long[] possibleDelivery = {possibleDelivery_from, possibleDelivery_to};
		return createAndSaveSearchRequest(title, description, pathToDefaultImg, possibleDelivery, costsAndReward,
				fee, lat, lng, address, userCreator);
	}
	
	public SearchRequest createAndSaveSearchRequest(String title, String description, String pathToDefaultImg,
			Long[] possibleDelivery, double costsAndReward, double fee, 
			Double lat, Double lng, String address, User userCreator) {
		SearchRequest sr = factory_createSearchRequest(title, description, pathToDefaultImg, possibleDelivery,
				costsAndReward, fee, lat, lng, address, userCreator);
		HibernateSave.saveOrUpdateObject(sr);
		dC.getSearchRequestList().add(sr);
		return sr;
	}
	
	private SearchRequest factory_createSearchRequest(String title, String description, String pathToDefaultImg,
			Long[] possibleDelivery, double costsAndReward, double fee, Double lat,
			Double lng, String address, User userCreator) {
		return new SearchRequest(title, description, pathToDefaultImg,
				possibleDelivery, costsAndReward, fee, lat, lng, address, userCreator);
	}
	
	public Message createAndSaveMessage(String title, String text, User sender, User receiver, SearchRequest referencedSr) {
		Message msg = factory_createMessage(title, text, sender, receiver, referencedSr);
		HibernateSave.saveOrUpdateObject(msg);
		dC.getMessageList().add(msg);
		msg.getSender().getRelevantMessages().add(msg);
		msg.getReceiver().getRelevantMessages().add(msg);
		return msg;
	}
	
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
