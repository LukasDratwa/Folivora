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
import de.folivora.model.Rating;
import de.folivora.model.Transaction;
import de.folivora.model.User;
import de.folivora.storage.HibernateDrop;
import de.folivora.storage.HibernateSave;
import de.folivora.storage.HibernateUpdate;

/** 
 * Listener to init and shutdown the application
 * <br><br>
 * ---------------------------------------------------------------<br>
 * @author Lukas Dratwa
*/
public class DoAfterStartupListener implements ServletContextListener {
	final static Logger logger = Logger.getLogger(DoAfterStartupListener.class);
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			logger.debug("TEST TEST TEST");
			
			// 1. Init Manager
			ApplicationManager aManager = ApplicationManager.getApplicationManagerInstance(new DataContainer());
			UserManager uManager = aManager.getuManager();
			
			// 2. Load and map data
			
			HibernateDrop.dropDatabase();
			
			User u1 = uManager.factory_createUser("Lukas Test", "123", new Date(), Gender.MALE, "test@das.de", 100);
			User u2 = uManager.factory_createUser("Hubertus Maximus", "123", new Date(), Gender.Female, "hubert@das.de", 100);
			
			Feedback f1 = aManager.factory_createFeedback(Rating.BAD, "War schlecht!", u1);
			u1.getFeedbacks().add(f1);
			
			Transaction t1 = new Transaction(0, new Date(), 50, u1, u2);
			HibernateSave.saveObject(t1);
			
			u1.getCredit().getTransactionIds().add(t1.getId());
			u2.getCredit().getTransactionIds().add(t1.getId());
			
			HibernateSave.saveObject(u1);
			HibernateSave.saveObject(u2);
			
			u1.setName("Manuel Neumann");
			HibernateUpdate.updateObject(u1);
			System.out.println("STARTUP COMPLETE");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		try {
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}