package de.folivora.request;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/** 
 * Listener to init and shutdown the application
 * <br><br>
 * ---------------------------------------------------------------<br>
 *
 * @author Lukas Dratwa
*/
public class DoAfterStartupListener implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			System.out.println("STARTUP");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		try {
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}