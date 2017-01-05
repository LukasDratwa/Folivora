package de.folivora.controller;

import java.util.Date;

import org.apache.log4j.Logger;

import de.folivora.model.Feedback;
import de.folivora.model.Gender;
import de.folivora.model.IdStorage;
import de.folivora.model.Rating;
import de.folivora.model.SearchRequest;
import de.folivora.model.Transaction;
import de.folivora.model.User;
import de.folivora.model.UserCredit;
import de.folivora.model.UserType;
import de.folivora.storage.HibernateSave;
import de.folivora.storage.HibernateUpdate;

public class ApplicationManager {
	private UserManager uManager = null;
	private static ApplicationManager instance = null;
	private DataContainer dC;
	private IdStorage idStorage;
	private Logger logger = Logger.getLogger(ApplicationManager.class);
	
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
			User feedbackCreator, Transaction referencedTransaction) {
		Feedback f = factory_createFeedback(rating, description, feedbackCreator, referencedTransaction);
		
		// Save the given feedback in the transaction and in the list of received feedbacks
		User feedbackReceiver = null;
		if(referencedTransaction.getUserSearching().equals(feedbackCreator)) {
			referencedTransaction.setFeedbackOfSearchingUser(f);
			feedbackReceiver = referencedTransaction.getUserDelivering();
		} else {
			referencedTransaction.setFeedbackOfDeliveringUser(f);
			feedbackReceiver = referencedTransaction.getUserSearching();
		}
		feedbackReceiver.getReceivedFeedbacks().add(f);
		
		HibernateSave.saveOrUpdateObject(referencedTransaction);
		return f;
	}
	
	private Feedback factory_createFeedback(Rating rating, String description,
			User feedbackCreator, Transaction referencedTransaction) {
		return new Feedback(dC.getIdStorage().getNewFeedbackId(), rating, description, feedbackCreator, referencedTransaction);
	}
	
	public void executeTransaction(String executionUnlockToken, Transaction t) {
		if(! executionUnlockToken.equals(t.getUnlockToken())) {
			logger.warn("Invalid unlock token! Couldn't execute transaction: " + t);
			return;
		}
		
		UserCredit userCreditSearching = t.getUserSearching().getCredit();
		UserCredit userCreditDelivering = t.getUserDelivering().getCredit();
		
		userCreditSearching.setBalance(userCreditSearching.getBalance() - t.getValue());
		userCreditDelivering.setBalance(userCreditDelivering.getBalance() + t.getValue());
		
		userCreditSearching.setLastModification(new Date());
		userCreditDelivering.setLastModification(new Date());
		
		if(! userCreditSearching.getReferencedTransactions().contains(t)) {
			userCreditSearching.getReferencedTransactions().add(t);
		}
		if(! userCreditDelivering.getReferencedTransactions().contains(t)) {
			userCreditDelivering.getReferencedTransactions().add(t);
		}
		
		t.setExecutionDate(new Date());
		t.setExecuted(true);
		
		HibernateUpdate.updateObject(t);
	}
	
	public Transaction createAndSaveTransaction(double value, User userSearching, User userDelivering, String unlockToken) {
		Transaction t = factory_createTransaction(value, userSearching, userDelivering, unlockToken);
		HibernateSave.saveOrUpdateObject(t);
		dC.getTransactionList().add(t);
		return t;
	}
	
	public SearchRequest getSearchRequestWithId(long id) {
		for(SearchRequest sr : dC.getSearchRequestList()) {
			if(sr.getId() == id) {
				return sr;
			}
		}
		
		return null;
	}
	
	private Transaction factory_createTransaction(double value, User userSearching, User userDelivering, String unlockToken) {
		return new Transaction(dC.getIdStorage().getNewTransactionId(), value, userSearching, userDelivering, unlockToken);
	}
	
	public SearchRequest createAndSaveSearchRequest(String title, String description, String pathToDefaultImg,
			Long possibleDelivery_from, Long possibleDelivery_to, Long preferredDelivery_from, Long preferredDelivery_to,
			double costsAndReward, double charges, Double lat, Double lng, String address, User userCreator) {
		Long[] possibleDelivery = {possibleDelivery_from, possibleDelivery_to};
		Long[] preferredDelivery = {preferredDelivery_from, preferredDelivery_to};
		return createAndSaveSearchRequest(title, description, pathToDefaultImg, possibleDelivery, preferredDelivery,
				costsAndReward, charges, lat, lng, address, userCreator);
	}
	
	public SearchRequest createAndSaveSearchRequest(String title, String description, String pathToDefaultImg,
			Long[] possibleDelivery, Long[] preferredDelivery, double costsAndReward, double charges, 
			Double lat, Double lng, String address, User userCreator) {
		SearchRequest sr = factory_createSearchRequest(title, description, pathToDefaultImg, possibleDelivery,
				preferredDelivery, costsAndReward, charges, lat, lng, address, userCreator);
		HibernateSave.saveOrUpdateObject(sr);
		dC.getSearchRequestList().add(sr);
		return sr;
	}
	
	private SearchRequest factory_createSearchRequest(String title, String description, String pathToDefaultImg,
			Long[] possibleDelivery, Long[] preferredDelivery, double costsAndReward, double charges, Double lat,
			Double lng, String address, User userCreator) {
		return new SearchRequest(dC.getIdStorage().getNewSearchRequestId(), title, description, pathToDefaultImg,
				possibleDelivery, preferredDelivery, costsAndReward, charges, lat, lng, address, userCreator);
	}
	
	public void createAndSaveTestData() {
		User u1 = uManager.createAndSaveUser("Lukas Test", "123", new Date(), Gender.MALE, "test@das.de", 100, UserType.NORMAL, "Aachen");
		User u2 = uManager.createAndSaveUser("Hubertus Maximus", "123", new Date(), Gender.Female, "hubert@das.de", 100, UserType.NORMAL, "Aachen");
		
		Transaction t1 = createAndSaveTransaction(50, u1, u2, uManager.getTrimmedToken(true, 5, ""));
		
		createAndSaveFeedback(Rating.BAD, "War schlecht, Lieferant kam viel zu spät!", u1, t1);
		createAndSaveFeedback(Rating.VERY_BAD, "Kam nur 5 Minuten zu spät und er war super unfreundlich!", u2, t1);
		
		Long[] possibleDelivery = {new Date().getTime(), new Date().getTime()};
		Long[] preferredDelivery = {new Date().getTime(), new Date().getTime()};
		SearchRequest sr1 = createAndSaveSearchRequest("Suche Brot", "Bis Mittag Brot.", "",
				possibleDelivery, preferredDelivery, 3.56, 1.0, 0.0, 0.0, "Testweg 21", u1);
		
		sr1.setUserStasisfier(u2);
		HibernateUpdate.updateObject(sr1);
		
		u1.setName("Manuel Neumann");
		HibernateUpdate.updateObject(u1);
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
	 * @return the idStorage
	 */
	public IdStorage getIdStorage() {
		return idStorage;
	}

	/**
	 * @param idStorage the idStorage to set
	 */
	public void setIdStorage(IdStorage idStorage) {
		this.idStorage = idStorage;
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
