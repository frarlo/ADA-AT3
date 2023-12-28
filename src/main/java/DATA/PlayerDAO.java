package DATA;

import java.util.List;
import java.util.Set;
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
 * Data layer java class. Use: manage CRUD operations into the DB. Table: "Player"  
 */

public class PlayerDAO {
	
	// Global constants and variables
	
	// Table name constant:
	static final String DBTABLENAME = "Player";
	
	// INSERT METHOD - Method to INSERT a Player into the Database:
	public Player addPlayer(int iPlayerID, String stFullName, String stCountry, int iELO) {
		
		// Session initialization:
		Session hibSession = HibernateUtil.SFACTORY.openSession();
		
		// Transaction initialization to null:
		Transaction txDB = null;
		
		// Object initialization:
		Player objPlayer = new Player(iPlayerID, stFullName, stCountry, iELO);

		try{
			// Starting the transaction:
			txDB = hibSession.beginTransaction(); //starts transaction

			hibSession.persist(objPlayer);
		
			// Ending the transaction:
			txDB.commit();
			
		}catch(HibernateException hibe) {
			// If the Transaction is not null and something happens...
			if(txDB != null) {
				// Rollback now:
				txDB.rollback();
			}
			// Showing what went wrong:
			hibe.printStackTrace();
		}finally {
			// Closing the Hibernate session:
			hibSession.close();	
		}	
	// Returns an object Player:
	return objPlayer;
	}

	// SELECT METHOD - Method to LIST all the Players in the Database:
	public void listPlayers() {
		
		// Session initialization:
		Session hibSession = HibernateUtil.SFACTORY.openSession();
		
		// Transaction initialization to null:
		Transaction txDB = null;

		try{
			// Starting the transaction:
			txDB = hibSession.beginTransaction();
			
			// Querying the SELECT:
			List<Player> listPlayers = hibSession.createQuery("FROM "+DBTABLENAME, Player.class).list();
			
			// Checking if it is empty:
			if (listPlayers.isEmpty()) {
				// It is.
				System.out.println("No items found in "+DBTABLENAME);
			}else {
				// It is not:
				System.out.println("\nListing players...\n");
				
				// Iterator declaration with the players' list:
				Iterator<Player> itPlayers = listPlayers.iterator();
				
				// While the list has a next item...
				while(itPlayers.hasNext()) {
					// Object:
					Player objPlayer = (Player) itPlayers.next();
					// Information:
					System.out.print("ID: " + objPlayer.getPlayerID() + " | ");
					System.out.print("Full name: " + objPlayer.getFullName() + " | ");
					System.out.print("Country: " + objPlayer.getCountry() + " | ");
					System.out.print("ELO: " + objPlayer.getELO() + "\n");	
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

	// DELETE METHOD - Method to DELETE a selected Player in the Database:
	public void deletePlayer(int iPlayerID) {
		
		// Session initialization:
		Session hibSession = HibernateUtil.SFACTORY.openSession();
		
		// Transaction initialization to null:
		Transaction txDB = null;

		try{
			// Starting the transaction:
			txDB = hibSession.beginTransaction(); //starts transaction

			// Object Player initialization:
			Player objPlayer;
			
			// Filling up the player object with the details of the player that matches the ID...
			objPlayer = (Player) hibSession.get(Player.class, iPlayerID);
			
			// If it is not null -> Exists
			if(objPlayer != null) {
				// Removing the player from the database:
				hibSession.remove(objPlayer);
				// Control message:
				System.out.println("Player removed with the ID " + iPlayerID);
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
	
	// SELECT METHOD - Method to SELECT and CHECK if a selected Player exists in the Database:
	public boolean checkPlayer(int iPlayerID) {
		
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
			CriteriaQuery<Player> crqHQL = crbCritBuilder.createQuery(Player.class);
			
			// 3. Setting the query root:
			Root<Player> rootPlayer = crqHQL.from(Player.class);
			
			// 4. Specifying the result we want to show in the query:
	        crqHQL.select(rootPlayer).where(crbCritBuilder.equal(rootPlayer.get("iPlayerID"), iPlayerID));
	        
	        // 5. Preparing the query for execution:
	        Query<Player> qryHQL = hibSession.createQuery(crqHQL);
	        
	        // Executing the query and getting a list:
	        List<Player> lstPlayer = qryHQL.getResultList();
	        
	        // Checking if it is empty:
	     	if(lstPlayer.isEmpty()) {
	     		// It is empty, commit and returns false (player doesn't exist):
	     		txDB.commit();
	     		return false;
	     	}else{
	     		// Ending the transaction:
	    		txDB.commit();
	    		// The list is not empty, player exists, returns true:
	  			return true;
	     	}
		}catch(HibernateException hibe) {
			// If the Transaction is not null and something happens...
			if(txDB != null) {
				// Rollback now:
				txDB.rollback();
			}
			hibe.printStackTrace();
			// If something happens is better to return that the player doesn't exist given that this method is a control-check method:
			return false;
		}finally {
			// Closing the Hibernate session:
			hibSession.close();	
		}			
	}
	
	// OPTIONAL - FIND A PLAYER WITH HQL CRITERIA
	public void findPlayer(int iPlayerID) {
		
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
			CriteriaQuery<Player> crqHQL = crbCritBuilder.createQuery(Player.class);
			
			// 3. Setting the query root:
			Root<Player> rootPlayer = crqHQL.from(Player.class);
			
			// Solution: https://stackoverflow.com/questions/41989279/how-to-avoid-type-conversion-for-partial-matches-on-number-based-columns
			// Looking for an integer with "LIKE" is not compatible so we have to convert it to String (as.String.class)
			// 4.A. - Specifying the result we want to show in the query: (SELECT * FROM p WHERE id AS String 
	        crqHQL.select(rootPlayer).where(crbCritBuilder.like(rootPlayer.get("iPlayerID").as(String.class), iPlayerID + "%"));
	        
	        // 4.B. - Specifying the ORDER of the result: (ORDER BY iPlayerID ASC;)
	        crqHQL.orderBy(crbCritBuilder.asc(rootPlayer.get("iPlayerID")));
	        
	        // 5. Preparing the query for execution:
	        Query<Player> qryHQL = hibSession.createQuery(crqHQL);
	        
	        // Executing the query and storing it in a list of players:
	        List<Player> lstPlayer = qryHQL.getResultList();
	        
	        // Checking if it is empty:
	     	if(lstPlayer.isEmpty()) {
	     		// It is, control message:
	     		System.out.println("No players found in "+DBTABLENAME+ " with an ID starting with the numbers '"+iPlayerID+"'.");
	     	}else{
	     		// It is not, we found players matching the LIKE clause:
	     		System.out.println("\n Player/s found with an ID starting with the '"+iPlayerID+"' number. Listing information... \n");
	     		
	     		// Iterator with the list:
	     		Iterator<Player> itPlayers = lstPlayer.iterator();
	     		
	     		// While the list has a next object:
	     		while(itPlayers.hasNext()) {
	     			// Object:
	     			Player objPlayer = (Player) itPlayers.next();
	     			// Information:
	     			System.out.print("ID: " + objPlayer.getPlayerID() + " | ");
	     			System.out.print("Full name: " + objPlayer.getFullName() + " | ");
	     			System.out.print("Country: " + objPlayer.getCountry() + " | ");
	     			System.out.print("ELO: " + objPlayer.getELO() + "\n");	
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

	// Side method - Returning a player object with a given ID:
	public Player returnPlayer (int iPlayerID) {
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
			CriteriaQuery<Player> crqHQL = crbCritBuilder.createQuery(Player.class);
			
			// 3. Setting the query root:
			Root<Player> rootPlayer = crqHQL.from(Player.class);
			
			// 4. Specifying the result we want to show in the query:
	        crqHQL.select(rootPlayer).where(crbCritBuilder.equal(rootPlayer.get("iPlayerID"), iPlayerID));
	        
	        // 5. Preparing the query for execution:
	        Query<Player> qryHQL = hibSession.createQuery(crqHQL);
	        
	        // Since we are looking for a field with a unique primary key we use "uniqueResult":
	        Player objPlayer = qryHQL.uniqueResult();
	        
	        // Ending the transaction:
			txDB.commit();
			
			// Returns the object player:
	        return objPlayer;
	     	
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
		// If the player is not found (i.e. do not exists) the method returns null:
		return null;
	}
}
