package DATA;

import java.util.List;
import java.util.Set;
import java.util.Date;
import java.util.Iterator;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import DOMAIN.*;
import UTIL.*;

/*
 * Data layer java class. Use: manage CRUD operations into the DB. Table: "Tournament"  
 */

public class TournamentDAO {
	
	// Global constants and variables
	
	// Table name constant:
	static final String DBTABLENAME = "Tournament";

	// INSERT Method - Used to insert Tournaments in the DB:
	public Tournament addTournament(String stCode, String stCategory, Location objLocation, Date dtStartDate, Date dtEndDate) {
		
		// Session initialization:
		Session hibSession = HibernateUtil.SFACTORY.openSession();
		
		// Transaction initialization to null:
		Transaction txDB = null;
		
		// Tournament object:
		Tournament objTournament = new Tournament(stCode, stCategory, objLocation, dtStartDate, dtEndDate);

		try{
			// Starting the transaction:
			txDB = hibSession.beginTransaction(); 

			// Persisting the object:
			hibSession.persist(objTournament);
		
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
		return objTournament;	
	}

	// UPDATE Method - Used to update Tournaments with a set of players:
	public void addPlayers(String stCode, Set<Player> setPlayers) {
		
		// Session initialization:
		Session hibSession = HibernateUtil.SFACTORY.openSession();
		
		// Transaction initialization to null:
		Transaction txDB = null;

		try{
			// Starting the transaction:
			txDB = hibSession.beginTransaction(); 
			
			// Getting the Tournament object with the stCode provided:
			Tournament objTournament = (Tournament) hibSession.get(Tournament.class, stCode);
			
			// Adding the set of players to the Tournament object:
			objTournament.setPlayers(setPlayers);
			
			// Merging the object in the DB:
			hibSession.merge(objTournament);
		
			// Ending the transaction:
			txDB.commit();
			
			// Output message:
			System.out.println("=====> Players inserted in Tournament " + stCode +"!");

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

	// SELECT METHOD - Method to LIST all the Tournaments in the Database:
	public void listTournaments() {
		
		// Session initialization:
		Session hibSession = HibernateUtil.SFACTORY.openSession();
		
		// Transaction initialization to null:
		Transaction txDB = null;

		try{
			// Starting the transaction:
			txDB = hibSession.beginTransaction();
			
			// Initialization of a list of Tournaments using the session and the object class:
			List<Tournament> listTournaments = hibSession.createQuery("FROM " + DBTABLENAME, Tournament.class).list();
			
			// If the list is empty we show it (and don't waste resources):
			if(listTournaments.isEmpty()) {
				System.out.println(" ==== No items found in the table "+DBTABLENAME+".");
			}
			// Else: the list is not empty:
			else{
				System.out.println("\n==== Listing all the items in "+DBTABLENAME+"...\n");
				// Iterator:
				Iterator<Tournament> itTournaments = listTournaments.iterator();
				// While the the iterator has a next item...
				while(itTournaments.hasNext()) {
					// Extracting the object of the list:
					Tournament objTournament = (Tournament) itTournaments.next();
					// Outputting the information of the object:
					System.out.print(" Code: " + objTournament.getCode() + " | ");
					System.out.print(" Category: " + objTournament.getCategory() + " | ");
					System.out.print(" Start date: " + objTournament.getStartDate().toString() + " | ");
					System.out.print(" End date: " + objTournament.getEndDate().toString() + " | ");
					System.out.print(" City: " + objTournament.getLocation().getCity() + "\n");
					// Getting the set of players in the Tournament:			
					Set<Player> relPlayers = objTournament.getPlayers();
					Iterator<Player> itPlayers = relPlayers.iterator();
					if(itPlayers.hasNext()) {
						System.out.println(" ==> Listing players in " + objTournament.getCode() + " tournament ==");
					}
					while(itPlayers.hasNext()) {
						Player objPlayer = (Player) itPlayers.next();
						System.out.println("	Name: " + objPlayer.getFullName());
					}
					// Blank space:
					System.out.println();
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
	public void deleteTournament (String stCode) {
		
		// Session initialization:
		Session hibSession = HibernateUtil.SFACTORY.openSession();
		
		// Transaction initialization to null:
		Transaction txDB = null;

		try{
			// Starting the transaction:
			txDB = hibSession.beginTransaction(); //starts transaction

			// Object initialization:
			Tournament objTournament;
			
			// Getting the object to delete with the given code:
			objTournament = (Tournament) hibSession.get(Tournament.class, stCode);
			
			// If the object returned is not null (exists):
			if(objTournament != null) {
				// Message:
				System.out.println("Removing Tournament with the code " +objTournament.getCode());
				// Remove method:
				hibSession.remove(objTournament);
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
		}finally {
			// Closing the Hibernate session:
			hibSession.close();	
		}	
	}
	
	// OPTIONAL - FIND A TOURNAMENT WITH HQL CRITERIA
	public void findTournament(String stCode) {
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
			CriteriaQuery<Tournament> crqHQL = crbCritBuilder.createQuery(Tournament.class);
			
			// 3. Setting the query root:
			Root<Tournament> rootTournament = crqHQL.from(Tournament.class);
			
			// 4. Specifying the result we want to show in the query:
	        crqHQL.select(rootTournament).where(crbCritBuilder.like(rootTournament.get("stCode"), stCode + "%"));
	        
	        // 4.B. - Specifying the ORDER of the result: (ORDER BY stCode ASC;)
	        crqHQL.orderBy(crbCritBuilder.asc(rootTournament.get("stCode")));	        
	        
	        // 5. Preparing the query for execution:
	        Query<Tournament> qryHQL = hibSession.createQuery(crqHQL);
	        
	        // 6. Creating a list with the query:
	        List<Tournament> lstTournament = qryHQL.getResultList();
	        
	        // Checking if the list is empty:
	     	if(lstTournament.isEmpty()) {
	     		// It is:
	     		System.out.println("No tournament found in "+DBTABLENAME+ " starting with the code '"+stCode+"'.");
	     	}else{
	     		// It is not empty:
	     		System.out.println("\n Tournament/s found starting with the '"+stCode+"' letter. Listing information... \n");
	     		
	     		// Initialization of the iterator with the list:
	     		Iterator<Tournament> itTournament = lstTournament.iterator();
	     				
	     		// While the iterator(list) has an item to loop:
	     		while(itTournament.hasNext()) {
	     			// Object:
	     			Tournament objTournament = (Tournament) itTournament.next();
	     			// Outputting the information of the object:
					System.out.print(" Code: " + objTournament.getCode() + " | ");
					System.out.print(" Category: " + objTournament.getCategory() + " | ");
					System.out.print(" City: " + objTournament.getLocation().getCity() + " | ");
					System.out.print(" Start date: " + objTournament.getStartDate().toString() + " | ");
					System.out.print(" End date: " + objTournament.getEndDate().toString() + " | \n");
					// Set of players:
					Set<Player> relPlayers = objTournament.getPlayers();
					Iterator<Player> itPlayers = relPlayers.iterator();
					if(itPlayers.hasNext()) {
						System.out.println(" ==> Listing players in " + objTournament.getCode() + " tournament ==");
					}
					while(itPlayers.hasNext()) {
						Player objPlayer = (Player) itPlayers.next();
						System.out.println("	Name: " + objPlayer.getFullName());
					}
					// Blank space:
					System.out.println();
	     		}
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
		}finally {
			// Closing the Hibernate session:
			hibSession.close();	
		}				
	}	
	
	// Side method - Method to CHECK if a city exists in order to avoid raising an SQL Error:
	public boolean checkCity(String stCity) {
		
		// Session initialization:
		Session hibSession = HibernateUtil.SFACTORY.openSession();
		// Transaction initialization to null:
		Transaction txDB = null;
		try{
			// Starting the transaction:
			txDB = hibSession.beginTransaction();

			// HQL String declaration:
			String hql = "FROM " + DBTABLENAME + " WHERE objLocation.stCity = :stCity";
			
			// Initialization of a list of Tournaments using the session and the object class:
			List<Tournament> listTournaments = hibSession.createQuery(hql, Tournament.class).setParameter("stCity", stCity).list();
	        
	        // Checking if the list is empty:
	     	if(listTournaments.isEmpty()) {
	     		txDB.commit();
	     		// If the list is empty means that there is no Tournament with that city, so it can be deleted:
	     		return false;	     	// FALSE -> do not exists
	     	}else{
	     	// Ending the transaction:
	     		txDB.commit();
				return true;			// TRUE -> city exists in one or more tournaments so it can't be deleted.
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
		// Default: the city exists
		return true;
	}

	// Side method - Method to change the City of a Tournament:
	public void changeCity(String stCode, Location objLocation) {
			// Session initialization:
		Session hibSession = HibernateUtil.SFACTORY.openSession();
		
		// Transaction initialization to null:
		Transaction txDB = null;

		try{
			// Starting the transaction:
			txDB = hibSession.beginTransaction(); 
			
			// Getting the Tournament object with the stCode provided:
			Tournament objTournament = (Tournament) hibSession.get(Tournament.class, stCode);
			
			// Adding the set of players to the Tournament object:
			objTournament.setLocation(objLocation);
			
			// Merging the object in the DB:
			hibSession.merge(objTournament);
		
			// Ending the transaction:
			txDB.commit();
			
			// Output message:
			System.out.println("=====> Tournament with the code " + stCode +" now has the City: " + objLocation.getCity());

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
	
	// Side method - Method to CHECK if the Code exists:
	public boolean checkCode(String stCode) {
		
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
			CriteriaQuery<Tournament> crqHQL = crbCritBuilder.createQuery(Tournament.class);
			
			// 3. Setting the query root:
			Root<Tournament> rootTournament = crqHQL.from(Tournament.class);
			
			// 4. Specifying the result we want to show in the query:
	        crqHQL.select(rootTournament).where(crbCritBuilder.equal(rootTournament.get("stCode"), stCode));
	        
	        // 5. Preparing the query for execution:
	        Query<Tournament> qryHQL = hibSession.createQuery(crqHQL);
	        
	        // List of the query result:
	        List<Tournament> lstTournament = qryHQL.getResultList();
	        
	        // Checking if it is empty:
	     	if(lstTournament.isEmpty()) {
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
	
}
