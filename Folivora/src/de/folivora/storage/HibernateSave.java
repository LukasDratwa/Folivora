package de.folivora.storage;

import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 * Class with methods to save object in the database.
 * 
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
public class HibernateSave {
	private static final Logger logger = Logger.getLogger(HibernateSave.class);
	
	/**
	 * Method to persist a object in the database. 
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param o - the object
	 */
	public static void persistObject(Object o) {
		Session session = HibernateUtil.getSessionFactory().openSession();  
        session.getTransaction().setTimeout(600);
        session.getTransaction().begin();
        
		session.persist(o);
		
		session.getTransaction().commit();
        session.close();
        
        logger.info("Persisted [" + o.getClass().getSimpleName() + "]: " + o);
	}
	
	/**
	 * Method to save an object in the database.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param o - the object
	 */
	public static void saveOrUpdateObject(Object o) {
		Session session = HibernateUtil.getSessionFactory().openSession();  
        session.getTransaction().setTimeout(600);
        session.getTransaction().begin();
        
		session.saveOrUpdate(o);
		
		session.getTransaction().commit();
        session.close();
        
        logger.info("Saved/updated [" + o.getClass().getSimpleName() + "]: " + o);
	}
}