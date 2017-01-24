package de.folivora.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;

import de.folivora.model.AdditionalReward;
import de.folivora.model.Feedback;
import de.folivora.model.Rating;
import de.folivora.model.SearchRequest;
import de.folivora.model.SearchRequestStatus;
import de.folivora.model.Transaction;
import de.folivora.model.User;
import de.folivora.model.UserCredit;
import de.folivora.model.messenger.Message;
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
	 * Method to get a {@link Rating} with a given value.
	 * 
	 * <hr>Created on 22.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param val - the value (stars)
	 * @return the found {@link Rating} or null
	 */
	public Rating getRatingWithStars(int val) {
		for(Rating r : Rating.values()) {
			if(r.getVal() == val) {
				return r;
			}
		}
		return null;
	}
	
	/**
	 * Method to check if the calling user already gave the other party already feedback for
	 * the referenced statisfied search request.
	 * 
	 * <hr>Created on 22.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param callingUser - the calling user
	 * @param sr - the referenced search request
	 * @return true if the user already gave feedback, false if not
	 */
	public boolean userGaveFeedbackAlread(User callingUser, SearchRequest sr) {
		if(callingUser.getId().toString().equals(sr.getUserCreator().getId().toString())) {
			// Feedback creator == creator of sr
			if(sr.getFeedbackOfSearchingUser() != null) {
				return true;
			}
		} else {
			if(sr.getFeedbackOfDeliveringUser() != null) {
				return true;
			}
		}
		
		return false;
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
		
		// Don't allow null descriptions
		if(description == null) {
			description = "";
		}
		
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
			return false;
		}
		
		User userFrom = getuManager().getUserWithId(t.getuFrom().getId().toString());
		User userTo = getuManager().getUserWithId(t.getuTo().getId().toString());
		UserCredit creditUserFrom = userFrom.getCredit();
		UserCredit creditUserTo = userTo.getCredit();
		
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
		HibernateUpdate.updateObject(userFrom);
		HibernateUpdate.updateObject(userTo);
		return true;
	}
	
	/**
	 * Method to get the initial transaction (user -> folivora) of a search request.
	 * 
	 * <hr>Created on 22.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param srId - the search request id
	 * @return the transaction or null
	 */
	public Transaction getInitialTransactionOfSearchRequest(String srId) {
		String userFolivoraId = getuManager().getFolivoraUser().getId().toString();
		for(Transaction t : getTransactionOfSearchRequest(srId)) {
			if(t.getuTo().getId().toString().equals(userFolivoraId)) {
				return t;
			}
		}
		
		return null;
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
	 * @param onlyUnseen - true if only unseen messages should be returned
	 * @return the messages in a JsonArray
	 */
	public JsonArray getRelevantMessagesOfUserAsJsonArray(User user, boolean onlyUnseen) {
		JsonArray jArray = new JsonArray();
		
		for(Message msg : getRelevantMessagesOfUser(user)) {
			if(onlyUnseen) {
				if(!msg.isSeen()) {
					jArray.add(msg.getAsJsonObject());
				}
			} else {
				jArray.add(msg.getAsJsonObject());
			}
		}
		
		return jArray;
	}
	
	/**
	 * Method to the relevant messages of an {@link User} mapped to get easily all messages
	 * to a specific {@link SearchRequest}
	 * 
	 * <hr>Created on 15.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param user - the {@link User}
	 * @return the mapped relevant messages
	 */
	public List<List<Message>> getRelevantMessagesOfUserForChatDisplay(User user) {
		// 1. Map data
		Map<SearchRequest, List<Message>> mappedData = new HashMap<SearchRequest, List<Message>>();
		for(Message msg : getRelevantMessagesOfUser(user)) {
			SearchRequest sr = getSearchRequestWithId(msg.getReferencedSr().getId().toString());
			
			if(mappedData.containsKey(sr)) {
				mappedData.get(sr).add(msg);
			} else {
				List<Message> messageList = new ArrayList<Message>();
				messageList.add(msg);
				mappedData.put(sr, messageList);
			}
		}
		
		// 2. Transform, sort and return
		List<List<Message>> result = new ArrayList<List<Message>>();
		for(SearchRequest sr : mappedData.keySet()) {
			List<Message> oneSrMsgList = mappedData.get(sr);
			oneSrMsgList.sort(new Comparator<Message>() {
				@Override
				public int compare(Message m1, Message m2) {
					if(m1.getCreationTimestamp().getTime() > m2.getCreationTimestamp().getTime()) {
						return 1;
					} else {
						return -1;
					}
				}
			});
			result.add(oneSrMsgList);
		}
		
		// Sort resilt -> 1. Most unreaded msgs, 2. last msg timestamp
		result.sort(new Comparator<List<Message>>() {
			@Override
			public int compare(List<Message> list1, List<Message> list2) {
				if(countUnreadMsgsInList(user, list1) > countUnreadMsgsInList(user, list2)) {
					return -1;
				} else if(countUnreadMsgsInList(user, list1) < countUnreadMsgsInList(user, list2)) {
					return 1;
				} else {
					if(list1.size() <= 0 || list2.size() <= 0) {
						return 0;
					} else {
						Date d1 = list1.get(0).getReferencedSr().getCreationTimestamp();
						Date d2 = list2.get(0).getReferencedSr().getCreationTimestamp();
						
						return d2.compareTo(d1);
					}
				}
			}
		});
		
		return result;
	}
	
	/**
	 * Method to count the unseen messages in a list of messages. Messages where the sender is
	 * equal to the calling user will not count.
	 * 
	 * <hr>Created on 15.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param user - the calling user
	 * @param inputMsgList - the input list
	 * @return the amount of unseen messages in the given list
	 */
	public int countUnreadMsgsInList(User callingUser, List<Message> inputMsgList) {
		int counter = 0;
		for(Message msg : inputMsgList) {
			if(!msg.isSeen()
					&& !callingUser.getId().toString().equals(msg.getSender().getId().toString())) {
				counter++;
			}
		}
		return counter;
	}
	
	/**
	 * Method to get the ids of messages in a list as a string and separated by a comma.
	 * 
	 * <hr>Created on 15.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param inputMsgList - the input list
	 * @return the ids as a string
	 */
	public String getMsgIdsOfMsgList(List<Message> inputMsgList) {
		StringBuilder sB = new StringBuilder();
		
		for(Message msg : inputMsgList) {
			sB.append(msg.getId().toString());
			
			if(inputMsgList.indexOf(msg) != inputMsgList.size()-1) {
				sB.append(",");
			}
		}
		
		return sB.toString();
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
	 * Method to get a {@link Message} with the id.
	 * 
	 * <hr>Created on 15.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param id - the id
	 * @return the found {@link Message} or null
	 */
	public Message getMessageWithId(String id) {
		for(Message msg : dC.getMessageList()) {
			if(msg.getId().toString().equals(id)) {
				return msg;
			}
		}
		
		return null;
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
	 * 
	 * 
	 * <hr>Created on 24.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param userReceiver
	 * @param ar
	 * @return true if the additional reward could be activated successfully
	 */
	public boolean activateAddtionalReward(User userReceiver, AdditionalReward ar) {
		if(ar.isInValidTimeframe() && !ar.isActivated()) {
			ar.setActivated(true);
			ar.setActivatedTimestamp(new Date());
			ar.setUserActivator(userReceiver);
			HibernateUpdate.updateObject(ar);
			
			User folivora = getuManager().getFolivoraUser();
			Transaction t = createAndSaveTransaction(ar.getValue(), 0.0, folivora, userReceiver, "", null);
			executeTransaction("", t);
			logger.info(userReceiver + " activated successfully the additional reward: " + ar);
			return true;
		}
		
		logger.info(userReceiver + " failed to activate the additional reward: " + ar);
		return false;
	}
	
	/**
	 * Method to get the first found published valid additional reward.
	 * 
	 * <hr>Created on 24.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @return
	 */
	public AdditionalReward getOneValidPublishedAdditionalReward() {
		for(AdditionalReward ar : dC.getAdditionalRewardList()) {
			if(ar.isInValidTimeframe() && !ar.isActivated()) {
				return ar;
			}
		}
		
		return null;
	}
	
	/**
	 * Method to count the actual published valid additional rewards.
	 * 
	 * <hr>Created on 24.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @return the amount of the actual published valid additional rewards
	 */
	public int countAmountOfValidPublishedAdditionalRewards() {
		int result = 0;
		
		for(AdditionalReward ar : dC.getAdditionalRewardList()) {
			if(ar.isInValidTimeframe() && !ar.isActivated()) {
				result++;
			}
		}
		
		return result;
	}
	
	/**
	 * Method to create, save and directly publish additional rewards
	 * 
	 * <hr>Created on 24.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param amount - the amount of the rewards
	 * @param validTill - date till the additional rewards will be valid
	 * @param userCreator - the creating user
	 * @param value - the value of the rewards
	 * @return the created additional reward list
	 */
	public List<AdditionalReward> createSaveAndPublishAdditionalRewards(int amount, Date validTill, User userCreator, double value) {
		List<AdditionalReward> result = new ArrayList<AdditionalReward>();

		for(int i=0; i<amount; i++) {
			AdditionalReward ar = createSaveAndPublishAdditionalReward(validTill, userCreator, value);
			result.add(ar);
		}
		
		return result;
	}
	
	/**
	 * Method to create, save and directly publish an additional reward
	 * 
	 * <hr>Created on 24.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param validTill - date till this additional reward will be valid
	 * @param userCreator - the creating user
	 * @param value - the value of the reward
	 * @return the created additional reward
	 */
	public AdditionalReward createSaveAndPublishAdditionalReward(Date validTill, User userCreator, double value) {
		AdditionalReward ar = factory_createAdditionalReward(new Date(), validTill, userCreator, value);
		dC.getAdditionalRewardList().add(ar);
		HibernateSave.saveOrUpdateObject(ar);
		return ar;
	}
	
	/**
	 * Factory to create a {@link AdditionalReward}
	 * 
	 * <hr>Created on 24.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param validFrom
	 * @param validTill
	 * @param userCreator
	 * @return
	 */
	private AdditionalReward factory_createAdditionalReward(Date validFrom, Date validTill, User userCreator, double value) {
		return new AdditionalReward(validFrom, validTill, userCreator, value);
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
