package de.folivora.storage;

import org.apache.log4j.Logger;
import org.hibernate.Session;

public class HibernateUpdate {
	private static final Logger logger = Logger.getLogger(HibernateUpdate.class);
	
	/**
	 * Method to update a object in the database.
	 * 
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