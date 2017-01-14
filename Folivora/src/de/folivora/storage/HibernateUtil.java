package de.folivora.storage;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.ogm.cfg.OgmConfiguration;
import org.hibernate.service.ServiceRegistry;

/**
 * Methods with utility methods for hibernate.
 * 
 * <hr>Created on 14.01.2017<hr>
 * @author <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a>
 */
public class HibernateUtil {
	private static final SessionFactory sessionFactory;  
    private static final ServiceRegistry serviceRegistry;  
      
    static {  
    	OgmConfiguration conf = new OgmConfiguration();  
    	
        conf.configure();  
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
        try {  
            sessionFactory = conf.buildSessionFactory(serviceRegistry);  
        } catch (Exception e) {  
            throw new ExceptionInInitializerError(e);  
        }         
    }  
    
    /**
     * <hr>Created on 14.01.2017 by <a href="mailto:lukasdratwa@yahoo.de">Lukas Dratwa</a><hr>
     * @return initialized session factory instance
     */
    public static SessionFactory getSessionFactory() {  
        return sessionFactory;  
    }
}