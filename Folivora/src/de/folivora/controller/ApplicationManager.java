package de.folivora.controller;

import de.folivora.model.Feedback;
import de.folivora.model.Rating;
import de.folivora.model.User;

public class ApplicationManager {
	private UserManager uManager = null;
	private static ApplicationManager instance = null;
	private DataContainer dC;
	
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
		return new Feedback(dC.getNewFeedbackId(), rating, description, feedbackCreator);
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
}
