package UTIL;

import java.util.logging.Level;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/*
 * Class to manage the Hibernate session. 
 * @authors Abelardo Martínez and Sergio Badal. 
 */

public class HibernateUtil {

	// Global constants and variables
	
	// Persistent session:
	public static final SessionFactory SFACTORY = buildSessionFactory();
	
	
	/* -------------------
	 * SESSION MANAGEMENT
	 * -------------------
	 */
	
	// Method to create a new Hibernate session:
	private static SessionFactory buildSessionFactory() {
		// Deactivating the logger:
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
		
		try {
			// Creation of the SessionFactory from our file "hibernate.cfg.xml":
			return new Configuration().configure().buildSessionFactory();
			
		}catch (Throwable sfe) {
			// Printing the exception:
			System.err.println("SessionFactory creation failed. Reason: " + sfe);
			throw new ExceptionInInitializerError(sfe);
			
		}
	}
	
	// Method to close the Hibernate session:
	public static void shutdownSessionFactory() {
		// Closes all the opened caches and connection pools:
		getSessionFactory().close();
	}
	
	// Method to get the Hibernate session:
	public static SessionFactory getSessionFactory() {
		return SFACTORY;
	}
	
	
}
