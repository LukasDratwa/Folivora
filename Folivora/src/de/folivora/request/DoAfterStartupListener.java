package de.folivora.request;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import de.folivora.controller.ApplicationManager;
import de.folivora.controller.DataContainer;
import de.folivora.model.Feedback;
import de.folivora.model.IdStorage;
import de.folivora.model.SearchRequest;
import de.folivora.model.Transaction;
import de.folivora.model.User;
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
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			// 1. Init Manager
			ApplicationManager aManager = ApplicationManager.getApplicationManagerInstance(new DataContainer());
			
			// 2. Load and set data
			loadIdStorage(aManager);
			loadAndInitData(aManager);
			
			// 3. Collect all search request and save them
			enrichLoadedDatabaseData(aManager);
			
			// 4. Check for corrupted data
			// TODO
			
			// aManager.createAndSaveTestData();
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
	
	private void enrichLoadedDatabaseData(ApplicationManager aManager) {
		DataContainer dC = aManager.getdC();
		
		for(Transaction t : dC.getTransactionList()) {
			// Save executed transactions
			if(t.isExecuted()) {
				t.getUserSearching().getCredit().getExecutedTransactions().add(t);
				t.getUserDelivering().getCredit().getExecutedTransactions().add(t);
			}
			
			// Save received feedbacks
			Feedback fOfSearchingUser = t.getFeedbackOfSearchingUser();
			if(fOfSearchingUser != null) {
				t.getUserDelivering().getReceivedFeedbacks().add(fOfSearchingUser);
			}
			Feedback fOfDeliveringUser = t.getFeedbackOfDeliveringUser();
			if(fOfDeliveringUser != null) {
				t.getUserSearching().getReceivedFeedbacks().add(fOfDeliveringUser);
			}
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		try {
		} catch(Exception e) {
			logger.error("Error within shutdown!", e);
		}
	}
}