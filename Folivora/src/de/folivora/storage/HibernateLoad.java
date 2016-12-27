package de.folivora.storage;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import de.folivora.model.IdStorage;
import de.folivora.model.Transaction;
import de.folivora.model.User;

/** 
 * Class with method to load something of the database
 * <br><br>
 * ---------------------------------------------------------------<br>
 *
 * @author Lukas Dratwa
*/
public class HibernateLoad {
	private static final Logger logger = Logger.getLogger(HibernateLoad.class);
	
	public static IdStorage loadIdStorage(long id) {
		IdStorage idStorage = null;
		
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();	
	        idStorage = (IdStorage) session.createQuery("from IdStorage where id = '" + id + "'").uniqueResult();
	        session.close();
		} catch(HibernateException e) {
			logger.error("Failed to load the id storage!", e);
		}
		
		return idStorage;
	}
	
	/**
	 * Method to load an User of the database
	 * 
	 * @param id - the userId
	 * @return - the loaded User or null, if there is no User with the given id in the database
	 */
	public static User loadUser(long id) {
		User usr = null;
		
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();	
	        usr = (User) session.createQuery("from User where id = '" + id + "'").uniqueResult();
	        session.close();
		} catch(HibernateException e) {
			logger.error("Failed to load user with the id " + id + "!", e);
		}
		
		if(usr == null) {
			logger.warn("User with ID " + id + " could not be loaded.");
		} else {
			logger.info("Loaded user: " + usr);
		}
		
		return usr;
	}
	
	/**
	 * Method to load an User with his email of the database
	 * 
	 * @param email - the email
	 * @return loaded User or null, if there is no User with the given email in the database
	 */
	public static User loadUser(String email) {
		User usr = null;
		
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();	
	        usr = (User) session.createQuery("from User where email = '" + email + "'").uniqueResult();
	        session.close();
		} catch(HibernateException e) {
			logger.error("Failed to load user with email \"" + email + "\"!", e);
		}
		
		if(usr == null) {
			logger.warn("User with email " + email + " could not be loaded.");
		} else {
			logger.info("Loaded user: " + usr);
		}
		
		return usr;
	}
	
	
	/**
	 * Method to load all Users of the database
	 * 
	 * @return the loaded list of users which are saved in the database
	 */
	@SuppressWarnings("unchecked")
	public static List<User> loadUserList() {
		ArrayList<User> userList = null;
		
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();		      
			userList = (ArrayList<User>) session.createQuery("from User order by id asc").list();
			session.close();
			
			if(userList != null) {
				for(User user : userList) {
					logger.info("Loaded user: " + user);
				}
			}
		} catch(HibernateException e) {
			logger.error("Failed to load user list!", e);
		}
		
		return userList;
	}
	
	/**
	 * Method to load a specific Transaction of the database
	 * 
	 * @param id - the transaction id
	 * @return - the loaded Transaction or null, if there is no Transaction with the given id in the database
	 */
	public static Transaction loadTransaction(long id) {
		Transaction t = null;
		
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();	
	        t = (Transaction) session.createQuery("from Transaction where id = '" + id + "'").uniqueResult();
	        session.close();
		} catch(HibernateException e) {
			logger.error("Failed to load transaction with the id " + id + "!", e);
		}
		
		if(t == null) {
			logger.warn("Transaction with ID " + id + " could not be loaded.");
		} else {
			logger.info("Loaded transaction: " + t);
		}
		
		return t;
	}
	
	/**
	 * Method to load all Transactions of the database
	 * 
	 * @return the loaded Transactions 
	 */
	@SuppressWarnings("unchecked")
	public static List<Transaction> loadTransactionList() {
		ArrayList<Transaction> transList = null;
		
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();		      
			transList = (ArrayList<Transaction>) session.createQuery("from Transaction order by id asc").list();
			session.close();
			
			if(transList != null) {
				for(Transaction transaction : transList) {
					logger.info("Loaded transaction: " + transaction);
				}
			}
		} catch(HibernateException e) {
			logger.error("Failed to load transaction list!", e);
		}
		
		return transList;
	}
}