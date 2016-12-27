package de.folivora.storage;

import org.hibernate.Session;

import de.folivora.model.IdStorage;

public class HibernateSave {
	public static void persistObject(Object o) {
		Session session = HibernateUtil.getSessionFactory().openSession();  
        session.getTransaction().setTimeout(600);
        session.getTransaction().begin();
        
		session.persist(o);
		
		session.getTransaction().commit();
        session.close();
	}
	
	public static void saveOrUpdateObject(Object o) {
		Session session = HibernateUtil.getSessionFactory().openSession();  
        session.getTransaction().setTimeout(600);
        session.getTransaction().begin();
        
		session.saveOrUpdate(o);
		
		session.getTransaction().commit();
        session.close();
	}
	
	public static void saveOrUpdateIdStorage(IdStorage idStorage) {
		Session session = HibernateUtil.getSessionFactory().openSession();  
        session.getTransaction().setTimeout(600);
        session.getTransaction().begin();
        
		session.saveOrUpdate(idStorage);
		
		session.getTransaction().commit();
        session.close();
	}
}