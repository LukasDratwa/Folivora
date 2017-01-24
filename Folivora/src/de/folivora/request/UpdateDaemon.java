package de.folivora.request;

import org.apache.log4j.Logger;

import de.folivora.controller.ApplicationManager;
import de.folivora.controller.DataContainer;
import de.folivora.model.Constants;
import de.folivora.model.SearchRequest;
import de.folivora.model.SearchRequestStatus;
import de.folivora.model.Transaction;
import de.folivora.model.User;
import de.folivora.storage.HibernateUpdate;

/**
 * Daemon-Thread which will check:
 * <ul>
 * 	<li>if the search requests have the right {@link SearchRequestStatus}</li>
 * </ul>
 * 
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
public class UpdateDaemon extends Thread {
	private static final Logger logger = Logger.getLogger(UpdateDaemon.class);
	private boolean isRunning = false;
	
	@Override
	public void run() {
		this.isRunning = true;
		ApplicationManager aManager = ApplicationManager.getApplicationManagerInstance();
		User folivora = aManager.getuManager().getFolivoraUser();
		DataContainer dC = aManager.getdC();
		
		while(this.isRunning) {
			// 1. Check for in-/ active SearchRequests
			for(SearchRequest sr : dC.getSearchRequestList()) {
				if(sr.shouldBeActive()) {
					if(sr.getStatus() == SearchRequestStatus.INACTIVE) {
						sr.setStatus(SearchRequestStatus.ACTIVE);
						HibernateUpdate.updateObject(sr);
					}
				} else if(sr.getStatus() == SearchRequestStatus.ACTIVE) {
					sr.setStatus(SearchRequestStatus.INACTIVE);
					HibernateUpdate.updateObject(sr);
					
					// Message to the creator of the expired search request
					aManager.createAndSaveMessage("\"" + sr.getTitle() + "\" leider abgelaufen",
							"Leider hat sich für Ihr Gesuch \"" + sr.getTitle() + "\" kein Lieferant "
							+ "finden lassen, weswegen es abgelaufen ist. Die abguchten Kosten von "
							+ (sr.getCostsAndReward() + sr.getFee()) + " € werden Ihnen in Kürze erstattet.",
							folivora, sr.getUserCreator(), sr);
					
					// Refund paid fees, costs and reward
					Transaction t = aManager.createAndSaveTransaction(sr.getCostsAndReward(), sr.getFee(),
							folivora, sr.getUserCreator(), "", sr);
					aManager.executeTransaction("", t);
					logger.info("Refund for " + sr.getUserCreator() + " because of the expired " + sr);
				}
			}
			
			// TODO 2. Check if transactions in progress should be finished or cancelled 
			
			
			// 3. Check for remote addresses
			for(User usr : aManager.getdC().getUserList()) {
				if(usr.getSession() == null && usr.getRemoteAdress() == null) {
					usr.setRemoteAdress(null);
				}
			}
			try {
				Thread.sleep(Constants.UPDATE_THREAD_INTERVAL);
			} catch (InterruptedException e) {
				logger.error("Failed to set UpdateDaemon to sleep!", e);
			}
		}
	}

	/**
	 * @return the isRunning
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * @param isRunning the isRunning to set
	 */
	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
}
