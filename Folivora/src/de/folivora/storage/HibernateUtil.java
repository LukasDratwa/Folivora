package de.folivora.storage;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.ogm.cfg.OgmConfiguration;
import org.hibernate.service.ServiceRegistry;

/**
 * @author Lukas Dratwa
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
     * @since 1.0
     * @return initialized session factory instance
     */
    public static SessionFactory getSessionFactory() {  
        return sessionFactory;  
    }
}