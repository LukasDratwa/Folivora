package de.folivora.storage;

import org.hibernate.Session;

import de.folivora.model.User;

public class HibernateSave {
	public static void saveUser(User user) {
		Session session = HibernateUtil.getSessionFactory().openSession();  
        session.getTransaction().setTimeout(600);
        session.getTransaction().begin();
        session.save(user);      
		session.getTransaction().commit();        
        session.close();
	}
}