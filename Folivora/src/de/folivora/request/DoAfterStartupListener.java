package de.folivora.request;

import java.util.Date;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import de.folivora.controller.ApplicationManager;
import de.folivora.controller.DataContainer;
import de.folivora.controller.UserManager;
import de.folivora.model.Feedback;
import de.folivora.model.IdStorage;
import de.folivora.model.SearchRequest;
import de.folivora.model.Transaction;
import de.folivora.model.User;
import de.folivora.model.UserType;
import de.folivora.storage.HibernateLoad;
import de.folivora.storage.HibernateSave;

/** 
 * Listener to init and shutdown the application
 * <br><br>
 * ---------------------------------------------------------------<br>
 * @author Lukas Dratwa
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
			loadIdStorage(aManager);
			loadAndInitData(aManager);
			
			// 3. Be sure standard users are saved
			checkForStandardUsers(aManager);
			
			// 4. Collect all search request and save them
			enrichLoadedDatabaseData(aManager);
			
			// 5. Check for corrupted data
			// TODO
			
			// 6. Start update Thread
			updateThread = new UpdateDaemon();
			updateThread.start();
		} catch(Exception e) {
			logger.error("Error while startup!", e);
		}
	}
	
	private void loadIdStorage(ApplicationManager aManager) {
		IdStorage idStorage = HibernateLoad.loadIdStorage(0);
		if(idStorage == null) {
			logger.warn("Could not load id storage, create default one.");
			aManager.getdC().setIdStorage(new IdStorage(0, 0, 0, 0, 0));
			HibernateSave.saveOrUpdateObject(aManager.getdC().getIdStorage());
			logger.warn("Created and saved new default id storage.");
			// TODO Check if there are andy objects in the database, should be dropped then
		} else {
			aManager.getdC().setIdStorage(idStorage);
		}
	}
	
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
	}
	
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
			uManager.createAndSaveUser("admin", "admin", new Date(), null, "", 0, UserType.ADMIN, "");
		}
		
		if(! foundFolivoraUser) {
			uManager.createAndSaveUser("folivora", "folivora", new Date(), null, "", 0, UserType.FOLIVORA, "");
		}
		
		if(! foundPaypalUser) {
			uManager.createAndSaveUser("paypal", "paypal", new Date(), null, "", 0, UserType.PAYPAL, "");
		}
	}
	
	private void enrichLoadedDatabaseData(ApplicationManager aManager) {
		DataContainer dC = aManager.getdC();
		
		for(Transaction t : dC.getTransactionList()) {
			// Save transaction which influence the user credits
			t.getuFrom().getCredit().getReferencedTransactions().add(t);
			t.getuTo().getCredit().getReferencedTransactions().add(t);
		}
		
		
		for(SearchRequest sr : dC.getSearchRequestList()) {
			// Feedback of delivering user -> searching user
			Feedback fOfDeliveringUser = sr.getFeedbackOfDeliveringUser();
			if(fOfDeliveringUser != null) {
				sr.getUserCreator().getReceivedFeedbacks().add(fOfDeliveringUser);
			}
			
			// Feedback of searching user -> delivering user
			Feedback fOfSearchingUser = sr.getFeedbackOfSearchingUser();
			if(fOfSearchingUser != null) {
				if(sr.getUserStasisfier() != null) {
					sr.getUserStasisfier().getReceivedFeedbacks().add(fOfSearchingUser);
				}
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