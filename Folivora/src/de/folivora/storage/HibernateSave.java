package de.folivora.storage;

import org.hibernate.Session;

public class HibernateSave {
	public static void saveObject(Object o) {
		Session session = HibernateUtil.getSessionFactory().openSession();  
        session.getTransaction().setTimeout(600);
        session.getTransaction().begin();
        
		session.persist(o);
		
		session.getTransaction().commit();
        session.close();
	}
}