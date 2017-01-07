package de.folivora.storage;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import de.folivora.model.IdStorage;
import de.folivora.model.SearchRequest;
import de.folivora.model.TokenStorage;
import de.folivora.model.Transaction;
import de.folivora.model.User;
import de.folivora.model.messanger.Message;

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
	        logger.info("Loaded idStorage: " + idStorage);
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
	
	/**
	 * Method to load a specific SearchRequest of the database
	 * 
	 * @param id - the search request id
	 * @return - the loaded SearchRequest or null, if there is no SearchRequest with the given id in the database
	 */
	public static SearchRequest loadSearchRequest(long id) {
		SearchRequest sr = null;
		
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();	
	        sr = (SearchRequest) session.createQuery("from SearchRequest where id = '" + id + "'").uniqueResult();
	        session.close();
		} catch(HibernateException e) {
			logger.error("Failed to load search request with the id " + id + "!", e);
		}
		
		if(sr == null) {
			logger.warn("SearchRequest with ID " + id + " could not be loaded.");
		} else {
			logger.info("Loaded search request: " + sr);
		}
		
		return sr;
	}
	
	/**
	 * Method to load all SearchRequest of the database
	 * 
	 * @return the loaded SearchRequest 
	 */
	@SuppressWarnings("unchecked")
	public static List<SearchRequest> loadSearchRequestList() {
		ArrayList<SearchRequest> srList = null;
		
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();		      
			srList = (ArrayList<SearchRequest>) session.createQuery("from SearchRequest order by id asc").list();
			session.close();
			
			if(srList != null) {
				for(SearchRequest sr : srList) {
					logger.info("Loaded search request: " + sr);
				}
			}
		} catch(HibernateException e) {
			logger.error("Failed to load search request list!", e);
		}
		
		return srList;
	}
	
	/**
	 * Method to load a specific Message of the database
	 * 
	 * @param id - the transaction id
	 * @return - the loaded Message or null, if there is no Message with the given id in the database
	 */
	public static Message loadMessage(long id) {
		Message message = null;
		
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();	
	        message = (Message) session.createQuery("from Message where id = '" + id + "'").uniqueResult();
	        session.close();
		} catch(HibernateException e) {
			logger.error("Failed to load Message with the id " + id + "!", e);
		}
		
		if(message == null) {
			logger.warn("Message with ID " + id + " could not be loaded.");
		} else {
			logger.info("Loaded Message: " + message);
		}
		
		return message;
	}
	
	/**
	 * Method to load all Messages of the database
	 * 
	 * @return the loaded Messages 
	 */
	@SuppressWarnings("unchecked")
	public static List<Message> loadMessageList() {
		ArrayList<Message> messageList = null;
		
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();		      
			messageList = (ArrayList<Message>) session.createQuery("from Message order by id asc").list();
			session.close();
			
			if(messageList != null) {
				for(Message msg : messageList) {
					logger.info("Loaded message: " + msg);
				}
			}
		} catch(HibernateException e) {
			logger.error("Failed to load message list!", e);
		}
		
		return messageList;
	}
	
	/**
	 * Method to load a specific TokenStorage of the database
	 * 
	 * @param id - the TokenStorage id
	 * @return - the loaded TokenStorage or null, if there is no TokenStorage with the given id in the database
	 */
	public static TokenStorage loadTokenStorage(long id) {
		TokenStorage ts = null;
		
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();	
	        ts = (TokenStorage) session.createQuery("from TokenStorage where id = '" + id + "'").uniqueResult();
	        session.close();
		} catch(HibernateException e) {
			logger.error("Failed to load token storage with the id " + id + "!", e);
		}
		
		if(ts == null) {
			logger.warn("TokenStorage with ID " + id + " could not be loaded.");
		} else {
			logger.info("Loaded token storage: " + ts);
		}
		
		return ts;
	}
}