package de.folivora.request;

import java.util.Date;

import org.apache.log4j.Logger;

import de.folivora.controller.ApplicationManager;
import de.folivora.controller.DataContainer;
import de.folivora.model.SearchRequest;
import de.folivora.storage.HibernateUpdate;
import de.folivora.util.Constants;

public class UpdateDaemon extends Thread {
	private static final Logger logger = Logger.getLogger(UpdateDaemon.class);
	private boolean isRunning = false;
	
	@Override
	public void run() {
		this.isRunning = true;
		ApplicationManager aManager = ApplicationManager.getApplicationManagerInstance();
		DataContainer dC = aManager.getdC();
		
		while(this.isRunning) {
			// 1. Check for in-/ active SearchRequests
			Date actDate = new Date();
			
			for(SearchRequest sr : dC.getSearchRequestList()) {
				Date a = new Date(sr.getPossibleDelivery_from());
				Date b = new Date(sr.getPossibleDelivery_to());
				
				if(a.compareTo(actDate) * actDate.compareTo(b) > 0) {
					if(!sr.isActive()) {
						sr.setActive(true);
						HibernateUpdate.updateObject(sr);
					}
				} else if(sr.isActive()) {
					sr.setActive(false);
					HibernateUpdate.updateObject(sr);
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
