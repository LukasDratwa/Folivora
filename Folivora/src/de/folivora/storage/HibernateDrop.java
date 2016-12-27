package de.folivora.storage;

import org.apache.log4j.Logger;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

/** 
 * Class with methods to drop Collections or Databases.
 * <br><br>
 * ---------------------------------------------------------------<br>
 *
 * @author Lukas Dratwa
*/
public class HibernateDrop {
	private static final Logger logger = Logger.getLogger(HibernateDrop.class);
	
	/**
	 * Method to drop our database "folivora" in the MongoDB.
	 */
	@SuppressWarnings("deprecation")
	public static void dropDatabase() {
		try {
			Mongo mongo = new Mongo("localhost", 27017);
			DB db = mongo.getDB("folivora");
			db.dropDatabase();
			
			mongo.close();
		} catch(Exception e) {
			logger.error("Failed to drop database \"folivora\"!", e);
		}
	}
	
	/**
	 * Method to drop a collection with the given name.
	 */
	@SuppressWarnings("deprecation")
	public static void dropCollection(String collectionName) {
		try {
			Mongo mongo = new Mongo("localhost", 27017);
			DB db = mongo.getDB("folivora");
			
			DBCollection collectionUser = db.getCollection(collectionName);
			collectionUser.drop();
			
			mongo.close();
		} catch(Exception e) {
			logger.error("Failed to drop the collection \"" + collectionName + "\" in db \"folivora\"!", e);;
		}
	}
}