package de.folivora.storage;

import org.apache.log4j.Logger;
import org.hibernate.Session;

/**
 * Class with methods to update objects in the database.
 * 
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
public class HibernateUpdate {
	private static final Logger logger = Logger.getLogger(HibernateUpdate.class);
	
	/**
	 * Method to update a object in the database.
	 * 
	 * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
	 * @param o - the object
	 */
	public static void updateObject(Object o) {
		Session session = HibernateUtil.getSessionFactory().openSession();  
        session.getTransaction().setTimeout(600);
        session.getTransaction().begin();
        
		session.saveOrUpdate(o);
		
		session.getTransaction().commit();
        session.close();
        
        logger.info("Updated [" + o.getClass().getSimpleName() + "]: " + o);
	}
}