package de.folivora.storage;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import de.folivora.model.User;

/** 
 * Class with method to load something of the database
 * <br><br>
 * ---------------------------------------------------------------<br>
 *
 * @author Lukas Dratwa
*/
public class HibernateLoad {
	/**
	 * Method to load an User of the database
	 * 
	 * @param id - the userId
	 * @return - the loaded User or null, if there is no User with the given id in the database
	 */
	public User loadUser(long id) {
		User usr = null;
		
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();	
	        usr = (User) session.createQuery("from User where id = '" + id + "'").uniqueResult();
	        session.close();
		} catch(HibernateException e) {
			e.printStackTrace();
		}
		
		return usr;
	}
	
	/**
	 * Method to load an User with his name of the database
	 * 
	 * @param name - the name
	 * @return loaded User or null, if there is no User with the given name in the database
	 */
	public User loadUser(String name) {
		User usr = null;
		
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();	
	        usr = (User) session.createQuery("from User where name = '" + name + "'").uniqueResult();
	        session.close();
		} catch(HibernateException e) {
			e.printStackTrace();
		}
		
		return usr;
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * Method to load all Users of the database
	 * 
	 * @return the loaded list of users which are saved in the database
	 */
	public List<User> loadUserList() {
		ArrayList<User> userList = null;
		
		try {
			Session session = HibernateUtil.getSessionFactory().openSession();		      
			userList = (ArrayList<User>) session.createQuery("from User order by id asc").list();
			session.close();
			
			if(userList != null) {
				for(User user : userList) {
					System.out.println(user.getName());
				}
			}
		} catch(HibernateException e) {
			e.printStackTrace();
		}
		
		return userList;
	}
}