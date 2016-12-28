package de.folivora.storage;

import org.apache.log4j.Logger;
import org.hibernate.Session;

public class HibernateSave {
	private static final Logger logger = Logger.getLogger(HibernateSave.class);
	
	/**
	 * Method to persist a object in the database. 
	 * 
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