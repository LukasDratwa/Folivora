package de.folivora.controller;

import java.util.Date;

import de.folivora.model.Feedback;
import de.folivora.model.IdStorage;
import de.folivora.model.Rating;
import de.folivora.model.Transaction;
import de.folivora.model.User;

public class ApplicationManager {
	private UserManager uManager = null;
	private static ApplicationManager instance = null;
	private DataContainer dC;
	private IdStorage idStorage;
	
	private ApplicationManager(DataContainer dC) {
		this.dC = dC;
		this.setuManager(new UserManager(dC));
	}
	
	public static ApplicationManager getApplicationManagerInstance(DataContainer dC) {
		if(instance == null) {
			instance = new ApplicationManager(dC);
		}
		return instance;
	}
	
	public static ApplicationManager getApplicationManagerInstance() {
		return getApplicationManagerInstance(null);
	}
	
	public Feedback factory_createFeedback(Rating rating, String description, User feedbackCreator) {
		return new Feedback(dC.getIdStorage().getNewFeedbackId(), rating, description, feedbackCreator);
	}
	
	public Transaction factory_createTransaction(Date executionDate, double value, User userFrom, User userTo) {
		return new Transaction(dC.getIdStorage().getNewTransactionId(), executionDate, value, userFrom, userTo);
	}

	/**
	 * @return the uManager
	 */
	public UserManager getuManager() {
		return uManager;
	}

	/**
	 * @param uManager the uManager to set
	 */
	public void setuManager(UserManager uManager) {
		this.uManager = uManager;
	}

	/**
	 * @return the idStorage
	 */
	public IdStorage getIdStorage() {
		return idStorage;
	}

	/**
	 * @param idStorage the idStorage to set
	 */
	public void setIdStorage(IdStorage idStorage) {
		this.idStorage = idStorage;
	}

	/**
	 * @return the dC
	 */
	public DataContainer getdC() {
		return dC;
	}

	/**
	 * @param dC the dC to set
	 */
	public void setdC(DataContainer dC) {
		this.dC = dC;
	}
}
