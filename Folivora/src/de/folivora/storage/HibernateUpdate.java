package de.folivora.storage;

import org.hibernate.Session;

public class HibernateUpdate {
	public static void updateObject(Object o) {
		Session session = HibernateUtil.getSessionFactory().openSession();  
        session.getTransaction().setTimeout(600);
        session.getTransaction().begin();
        
		session.saveOrUpdate(o);
		
		session.getTransaction().commit();
        session.close();
	}
}