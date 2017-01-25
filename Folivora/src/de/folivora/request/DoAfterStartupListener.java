package de.folivora.request;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import de.folivora.controller.ApplicationManager;
import de.folivora.controller.DataContainer;
import de.folivora.controller.UserManager;
import de.folivora.model.AdditionalReward;
import de.folivora.model.Feedback;
import de.folivora.model.SearchRequest;
import de.folivora.model.SearchRequestStatus;
import de.folivora.model.Transaction;
import de.folivora.model.User;
import de.folivora.model.UserType;
import de.folivora.model.messenger.Message;
import de.folivora.storage.HibernateLoad;
import de.folivora.storage.HibernateUpdate;

/**
 * Listener to launch, init and shutdown the application.
 * 
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
public class DoAfterStartupListener implements ServletContextListener {
	private static final Logger logger = Logger.getLogger(DoAfterStartupListener.class);
	private UpdateDaemon updateThread;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			// 1. Init Manager
			ApplicationManager aManager = ApplicationManager.getApplicationManagerInstance(new DataContainer());
			
			// 2. Load and set data
			loadAndInitData(aManager);
			
			// 3. Be sure standard users are saved
			checkForStandardUsers(aManager);
			
			// 4. Collect all search request and save them
			enrichLoadedDatabaseData(aManager);
			
			// 5. Check for corrupted data
			checkForCorruptedData();
			
			// 6. Start update Thread
			updateThread = new UpdateDaemon();
			updateThread.start();
		} catch(Exception e) {
			logger.error("Error while startup!", e);
		}
	}
	
	/**
	 * Method to initially load the data from the database.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param aManager - the manager
	 */
	private void loadAndInitData(ApplicationManager aManager) {
		// Load users
		List<User> loadedUsers = HibernateLoad.loadUserList();
		if(loadedUsers != null && loadedUsers.size() > 0) {
			aManager.getdC().setUserList(loadedUsers);
		}
		
		// Load transactions
		List<Transaction> loadedTransactions = HibernateLoad.loadTransactionList();
		if(loadedTransactions != null && loadedTransactions.size() > 0) {
			aManager.getdC().setTransactionList(loadedTransactions);
		}
		
		// Load search requests
		List<SearchRequest> searchRequests = HibernateLoad.loadSearchRequestList();
		if(searchRequests != null && searchRequests.size() > 0) {
			aManager.getdC().setSearchRequestList(searchRequests);
		}
		
		// Load messages
		List<Message> messages = HibernateLoad.loadMessageList();
		if(messages != null && messages.size() > 0) {
			aManager.getdC().setMessageList(messages);
		}
		
		// Load addtional rewards
		List<AdditionalReward> additionalRewards = HibernateLoad.loadAdditionalRewardList();
		if(additionalRewards != null && additionalRewards.size() > 0) {
			aManager.getdC().setAdditionalRewardList(additionalRewards);
		}
	}
	
	/**
	 * Check if the standard users (actually folivora, paypal and admin) are already saved. If not, they
	 * will be created and saved. 
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param aManager - the manager
	 */
	private void checkForStandardUsers(ApplicationManager aManager) {
		UserManager uManager = aManager.getuManager();
		boolean foundAdmin = false,
				foundFolivoraUser = false,
				foundPaypalUser = false;
		
		for(User u : aManager.getdC().getUserList()) {
			if(u.getName().equals("admin") && u.getUserType() == UserType.ADMIN) {
				foundAdmin = true;
			}
			
			if(u.getUserType() == UserType.FOLIVORA) {
				foundFolivoraUser = true;
			}
			
			if(u.getUserType() == UserType.PAYPAL) {
				foundPaypalUser = true;
			}
		}
		
		if(! foundAdmin) {
			uManager.createAndSaveUser("admin", "admin", new Date(), null, "Darmstadt", 0, UserType.ADMIN, "");
		}
		
		if(! foundFolivoraUser) {
			uManager.createAndSaveUser("folivora", "folivora", new Date(), null, "Darmstadt", 0, UserType.FOLIVORA, "");
		}
		
		if(! foundPaypalUser) {
			uManager.createAndSaveUser("paypal", "paypal", new Date(), null, "Internet", 0, UserType.PAYPAL, "");
		}
	}
	
	/**
	 * Method to enrich the loaded data (map them to transient lists in objects etc.)
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param aManager - the manager
	 */
	private void enrichLoadedDatabaseData(ApplicationManager aManager) {
		DataContainer dC = aManager.getdC();
		UserManager uManager = aManager.getuManager();
		
		for(Transaction t : dC.getTransactionList()) {
			// Save transaction which influence the user credits
			uManager.getUserWithId(t.getuFrom().getId().toString()).getCredit().getReferencedTransactions().add(t);
			uManager.getUserWithId(t.getuTo().getId().toString()).getCredit().getReferencedTransactions().add(t);
		}
		
		// Save feedbacks
		for(SearchRequest sr : dC.getSearchRequestList()) {
			// Feedback of delivering user -> searching user
			Feedback fOfDeliveringUser = sr.getFeedbackOfDeliveringUser();
			if(fOfDeliveringUser != null) {
				uManager.getUserWithId(sr.getUserCreator().getId().toString()).getReceivedFeedbacks().add(fOfDeliveringUser);
			}
			
			// Feedback of searching user -> delivering user
			Feedback fOfSearchingUser = sr.getFeedbackOfSearchingUser();
			if(fOfSearchingUser != null) {
				if(sr.getUserStasisfier() != null) {
					uManager.getUserWithId(sr.getUserStasisfier().getId().toString()).getReceivedFeedbacks().add(fOfSearchingUser);
				}
			}
		}
		
		// Save relevant messages for users
		for(Message msg : dC.getMessageList()) {
			uManager.getUserWithId(msg.getSender().getId().toString()).getRelevantMessages().add(msg);
			uManager.getUserWithId(msg.getReceiver().getId().toString()).getRelevantMessages().add(msg);
		}
	}
	
	private void checkForCorruptedData() {
		ApplicationManager aManager = ApplicationManager.getApplicationManagerInstance();
		
		
		for(SearchRequest sr : aManager.getdC().getSearchRequestList()) {
			// Check for duplicated feedback references
			if(sr.getStatus() == SearchRequestStatus.STATISFIED
					&& sr.getFeedbackOfSearchingUser() != null
					&& sr.getFeedbackOfDeliveringUser() != null
					&& sr.getFeedbackOfSearchingUser().getId().toString().equals(sr.getFeedbackOfDeliveringUser().getId().toString())) {
				logger.warn("Found duplicated feedback in search request " + sr);
				
				String userCreatorIdFeedbackOfDelivering = sr.getFeedbackOfDeliveringUser().getFeedbackCreator().getId().toString();
				String srCreatorId = sr.getUserCreator().getId().toString();
				
				// Determine if feedback of delivering or searching user is placed false
				if(srCreatorId.equals(userCreatorIdFeedbackOfDelivering)) {
					// Feeback of delivering user is wrong placed
					logger.info("Deleted missplaced feedback (of delivering user) " + sr.getFeedbackOfDeliveringUser() + " in " + sr);
					sr.setFeedbackOfDeliveringUser(null);
				} else {
					logger.info("Deleted missplaced feedback (of searching user) " + sr.getFeedbackOfSearchingUser() + " in " + sr);
					sr.setFeedbackOfSearchingUser(null);
				}
				
				HibernateUpdate.updateObject(sr);
			}
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		try {
			if(updateThread != null) {
				updateThread.setRunning(false);
			}
		} catch(Exception e) {
			logger.error("Error within shutdown!", e);
		}
	}
}