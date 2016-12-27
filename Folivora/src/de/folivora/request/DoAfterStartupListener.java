package de.folivora.request;

import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import de.folivora.controller.ApplicationManager;
import de.folivora.controller.DataContainer;
import de.folivora.controller.UserManager;
import de.folivora.model.Feedback;
import de.folivora.model.Gender;
import de.folivora.model.IdStorage;
import de.folivora.model.Rating;
import de.folivora.model.SearchRequest;
import de.folivora.model.Transaction;
import de.folivora.model.User;
import de.folivora.model.UserType;
import de.folivora.storage.HibernateLoad;
import de.folivora.storage.HibernateSave;
import de.folivora.storage.HibernateUpdate;

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
			UserManager uManager = aManager.getuManager();
			
			// 2. Load and map data
			loadIdStorage(aManager);
			loadAndInitData(aManager);
			
			User u1 = uManager.factory_createUser("Lukas Test", "123", new Date(), Gender.MALE, "test@das.de", 100, UserType.NORMAL);
			User u2 = uManager.factory_createUser("Hubertus Maximus", "123", new Date(), Gender.Female, "hubert@das.de", 100, UserType.NORMAL);
			
			Feedback f1 = aManager.factory_createFeedback(Rating.BAD, "War schlecht!", u1);
			u1.getFeedbacks().add(f1);
			
			Transaction t1 = aManager.factory_createTransaction(new Date(), 50, u1, u2);
			HibernateSave.persistObject(t1);
			
			u1.getCredit().getTransactionIds().add(t1.getId());
			u2.getCredit().getTransactionIds().add(t1.getId());
			
			Date[] possibleDelivery = {new Date(), new Date()};
			Date[] preferredDelivery = {new Date(), new Date()};
			SearchRequest sr1 = aManager.factory_createSearchRequest("Suche Brot", "Bis Mittag Brot.", "",
					possibleDelivery, preferredDelivery, 3.56, 0, 0, u1);
			u1.getCreatedSearchRequests().add(sr1);
			
			sr1.setUserStasisfier_id(u2.getId());
			u2.getStasisfiedSearchRequestIds().add(sr1.getId());
			
			HibernateSave.saveOrUpdateObject(u1);
			HibernateSave.saveOrUpdateObject(u2);
			
			u1.setName("Manuel Neumann");
			HibernateUpdate.updateObject(u1);
		} catch(Exception e) {
			logger.error("Error while startup!", e);
		}
	}
	
	private void loadIdStorage(ApplicationManager aManager) {
		IdStorage idStorage = HibernateLoad.loadIdStorage(0);
		if(idStorage == null) {
			logger.warn("Could not load id storage, create default one.");
			aManager.getdC().setIdStorage(new IdStorage(0, 0, 0, 0, 0));
			HibernateSave.saveOrUpdateIdStorage(aManager.getdC().getIdStorage());
			logger.warn("Created and saved new default id storage.");
			// TODO Check if there are andy objects in the database, should be dropped then
		} else {
			aManager.getdC().setIdStorage(idStorage);
		}
	}
	
	private void loadAndInitData(ApplicationManager aManager) {
		aManager.getdC().setUserList(HibernateLoad.loadUserList());
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		try {
		} catch(Exception e) {
			logger.error("Error within shutdown!", e);
		}
	}
}