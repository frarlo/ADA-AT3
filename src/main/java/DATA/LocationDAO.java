package DATA;

import java.util.List;
import java.util.Iterator;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import DOMAIN.*;
import UTIL.*;

/*
 * Data layer java class. Use: manage CRUD operations into the DB. Table: "Location"  
 */

public class LocationDAO {
	
	/* GLOBAL CONSTANTS AND VARIABLES */
	
	// Table name constant:
	static final String DBTABLENAME = "Location";	
	
	// INSERT METHOD - Method to INSERT a Location into the Database:
	public Location addLocation(String stCity, String stCountry, int iPopulation) {
		
		// Session initialization:
		Session hibSession = HibernateUtil.SFACTORY.openSession();
		
		// Transaction initialization to null:
		Transaction txDB = null;
		
		// Object initialization:
		Location objLocation = new Location(stCity, stCountry, iPopulation);
		
		try {
			// Starting the transaction:
			txDB = hibSession.beginTransaction();
			
			// Persisting the object:
			hibSession.persist(objLocation);
			
			// Ending the transaction:
			txDB.commit();
			
			// Output message:
			System.out.println("=====> Item inserted into table "+DBTABLENAME+"!");
			
		}catch(HibernateException hibe) {
			// If the Transaction is not null and something happens...
			if(txDB != null) {
				// Rollback now:
				txDB.rollback();
			}
			hibe.printStackTrace();
		}finally {
			// Closing the Hibernate session:
			hibSession.close();
			
		}
		// Returns the created object:
		return objLocation;
	}

	// SELECT METHOD - Method to LIST all the Location in the Database:
	public void listLocations() {
		
		// Session initialization:
		Session hibSession = HibernateUtil.SFACTORY.openSession();
		
		// Transaction initialization to null:
		Transaction txDB = null;

		try{
			// Starting the transaction:
			txDB = hibSession.beginTransaction();

			// Initialization of a list of Locations using the session and the object class:
			List<Location> listLocations = hibSession.createQuery("FROM " + DBTABLENAME, Location.class).list();
			
			// If the list is empty we show it (and don't waste resources):
			if(listLocations.isEmpty()) {
				System.out.println(" ==== No items found in the table "+DBTABLENAME+".");
			}
			else{
				System.out.println("\n==== Listing of all the items in "+DBTABLENAME+" starting.\n");
				// Iterator:
				Iterator<Location> itLocations = listLocations.iterator();
				// While the the iterator has a next item...
				while(itLocations.hasNext()) {
					// Extracting the object of the list:
					Location objLocation = (Location) itLocations.next();
					// Outputting the information of the object:
					System.out.print(" City: " + objLocation.getCity() + " | ");
					System.out.print(" Country: " + objLocation.getCountry() + " | ");
					System.out.print(" Population: " + objLocation.getPopulation() + "\n");
				}
				// Ending the transaction:
				txDB.commit();
			}
		}catch(HibernateException hibe) {
			// If the Transaction is not null and something happens...
			if(txDB != null) {
				// Rollback now:
				txDB.rollback();
			}
			hibe.printStackTrace();
		}finally {
			// Closing the Hibernate session:
			hibSession.close();	
		}
	}
	
	// DELETE METHOD - Method to DELETE a selected Location in the Database:
	public void deleteLocation(String stCity) {
		
		// Session initialization:
		Session hibSession = HibernateUtil.SFACTORY.openSession();
		
		// Transaction initialization to null:
		Transaction txDB = null;

		try{
			// Starting the transaction:
			txDB = hibSession.beginTransaction();

			// Initialization of the empty object:
			Location objLocation;
			
			// Assigning the empty object Location to the entity in the DB:
			objLocation = (Location) hibSession.get(Location.class, stCity);
			
			// Checking if the object is not null:
			if(objLocation != null) {
				// It is not, removing it:
				hibSession.remove(objLocation);
				// Message:
				System.out.println("Location with the city '" + stCity +"' removed.");
			}		
			// Ending the transaction:
			txDB.commit();
			
		}catch(HibernateException hibe) {
			// If the Transaction is not null and something happens...
			if(txDB != null) {
				// Rollback now:
				txDB.rollback();
			}
		hibe.printStackTrace();
		}finally{
			// Closing the Hibernate session:
			hibSession.close();	
		}		
	}
	
	// Side method - Method to SELECT and CHECK if a selected Location exists in the Database:
	public boolean checkLocation(String stCity) {
		
		// Session initialization:
		Session hibSession = HibernateUtil.SFACTORY.openSession();
		
		// Transaction initialization to null:
		Transaction txDB = null;

		try{
			// Starting the transaction:
			txDB = hibSession.beginTransaction();
			
			// 1. Creating the CriteriaBuilder:
			CriteriaBuilder crbCritBuilder = hibSession.getCriteriaBuilder();
			
			// 2. Creating the query object:
			CriteriaQuery<Location> crqHQL = crbCritBuilder.createQuery(Location.class);
			
			// 3. Setting the query root:
			Root<Location> rootLocation = crqHQL.from(Location.class);
			
			// 4. Specifying the result we want to show in the query:
	        crqHQL.select(rootLocation).where(crbCritBuilder.equal(rootLocation.get("stCity"), stCity));
	        
	        // 5. Preparing the query for execution:
	        Query<Location> qryHQL = hibSession.createQuery(crqHQL);
	        
	        // List of the query result:
	        List<Location> lstLocation = qryHQL.getResultList();
	        
	        // Checking if it is empty:
	     	if(lstLocation.isEmpty()) {
	     			// If it is empty the location does not exists:
	     			return false;
	     	}	
	     	// Ending the transaction:
	    	txDB.commit();
			// Otherwise it exists:
	    	return true;
		}catch(HibernateException hibe) {
			// If the Transaction is not null and something happens...
			if(txDB != null) {
				// Rollback now:
				txDB.rollback();
			}
			hibe.printStackTrace();
			return false;
		}finally {
			// Closing the Hibernate session:
			hibSession.close();	
		}			
	}

	// Side method - Method to return a Location object with the string of the city passed as parameter:
	public Location extractLocation(String stCity) {
		
		Session hibSession = HibernateUtil.SFACTORY.openSession();
		
		Transaction txDB = null;
		
		try {
	        txDB = hibSession.beginTransaction();
			
			// 1. Creating the CriteriaBuilder:
			CriteriaBuilder crbCritBuilder = hibSession.getCriteriaBuilder();
			
			// 2. Creating the query object:
			CriteriaQuery<Location> crqHQL = crbCritBuilder.createQuery(Location.class);
			
			// 3. Setting the query root:
			Root<Location> rootLocation = crqHQL.from(Location.class);
			
			// 4. Specifying the result we want to show in the query:
	        crqHQL.select(rootLocation).where(crbCritBuilder.equal(rootLocation.get("stCity"), stCity));
	        
	        // 5. Preparing the query for execution:
	        Query<Location> qryHQL = hibSession.createQuery(crqHQL);	        

	        Location objLocation = qryHQL.uniqueResult();

	        txDB.commit();

	        return objLocation;
	        
	    } catch (HibernateException hibe) {
	        if (txDB != null) {
	            txDB.rollback();
	        }
	        hibe.printStackTrace();
	        return null;
	    } finally {
	        hibSession.close();
	    }
	}
}

