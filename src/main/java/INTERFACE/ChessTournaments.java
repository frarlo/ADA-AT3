package INTERFACE;

import java.util.Scanner;
import java.util.Set;
import java.util.Date;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;

import DATA.*;
import DOMAIN.*;
import UTIL.*;

public class ChessTournaments {
	
	/*
	 * INTERFACE layer. Program in order to manage the user's input and the program's output. 
	 */

	// Keyboard instance:
	public static Scanner scKeyboard = new Scanner(System.in);
	
	// This method prints the main menu of the application using the ShowMenu() method in IOTools:
	public static int PrintMenu() {
		// Declaration of the option:
		int iOption;
		// New line:
		System.out.println();
		// We assign the value of the option by using the ShowMenu method in IOTools:
		iOption = IOTools.ShowMenu(
				"*****\nMENU\n*****",												// Option:
				"Exit",																// 0
				"Insert & List chess players",										// 1
				"Insert & List tournaments",										// 2
				"Insert & List locations",											// 3
				"Delete chess players",												// 4
				"Delete tournaments",												// 5
				"Delete locations",													// 6
				"Find a chess player",									// 7
				"Find a tournament");									// 8
		// Returning the option chosen by the user:
		return iOption;
	}
	
	// Simple method to ask any input of the user in order to continue the program's execution: 	// Exactly the same as the AT1.
	public static void PressAnyKeyToContinue() {
		// Blank line:
		System.out.println("");
		// Message:
		System.out.println("Press any key to continue...");
		try {
			// Any key read:
			System.in.read();
		}catch (Exception exe) {
			
		}
	}
	
	/*
	 * Methods to manage the different options of the Program's main menu 
	 */
	
	// Option 1 - First step - Getting the Players from the user's input by keyboard:
	public static ArrayList<Player> GetPlayersFromKeyboard() {
		
		// ArrayList initialization:
		ArrayList<Player> arlPlayers = new ArrayList<Player>();
		
		// Initialization of the variables:
		int iPlayerID;
		String stFullName;
		String stCountry;
		int iELO;
		
		// While true loop to ask for players until a "zero" is inserted as the Player's ID:
		while (true) {
			// Cleaning the keyboard buffer:
			IOTools.Reset();
			// Declaration of the boolean bExists to false to check the entered ID later:
			boolean bExists = false;
			// Asking for the player's ID:
			iPlayerID = IOTools.AskInt("Player's ID (zero to exit)");
			// Checking if the user has inserted zero:
			if (iPlayerID == 0)
				// Stopping the while loop:
				break;
			// Enhanced for loop to check the objects of the ArrayList:
			for (Player objPlayer : arlPlayers) {
				// If the player's ID in the array list matches the one inserted...
				if(objPlayer.getPlayerID() == iPlayerID){
					// Changes the boolean bExists to true:
					bExists = true;
					// And stops the execution:
					break;
				}
			}
			// Checking the value of the boolean value of exists:
			if(bExists) {
				// If the ID value exists it will print an error:
				System.err.println("The inserted ID is already in the list. ");
			}else{
				// If the execution ends here it means that everything is correct for now.
					
				// Cleaning the scanner buffer:
				IOTools.Reset();
				// Asking for the player's full name:
				stFullName = IOTools.AskStringWithSpaces("Player's full name");
				// Asking for the player's country:
				stCountry = IOTools.AskStringWithSpaces("Player's country");
				// Asking for the player's ELO:
				iELO = IOTools.AskInt("Player's ELO");
				while(iELO <= 0) {
					// The user has inserted a 0 or negative value as the ELO:
					System.err.println("You can't enter an ELO negative or zero value. Please try again.\n");
					// Asking again:
					iELO = IOTools.AskInt("Player's ELO");
				}
				// Creating an instance of player with the data entered by the user:
				Player objPlayer = new Player(iPlayerID, stFullName, stCountry, iELO);
				// Adding the player to the ArrayList:		
				arlPlayers.add(objPlayer);
			}
		}
		return arlPlayers;
	}
	
	// Option 1 - Second step - Getting the Players in the ArrayList inserted into the DB:
	public static void SavePlayersToDB(ArrayList<Player> arlPlayers) {
		
		// Check if the ArrayList is empty:
		if (arlPlayers.isEmpty()) {
			// If it's empty the method will not do anything besides printing this message:
			System.err.println("Empty array of Players -- INSERT operation not possible!");
		}else{
			// If the list is not empty it will populate the Player table in the DB:
			
			// Declaration of the object Player DAO:
			PlayerDAO objPlayerDAO = new PlayerDAO();
			
			// Iterator with the array list:
			Iterator<Player> itPlayers = arlPlayers.iterator();
			
			// Looping the ArrayList:
			while(itPlayers.hasNext()) {
				// Actual player:
				Player objPlayer = itPlayers.next();
				// INSERT operation using the method in the DAO class:
				objPlayerDAO.addPlayer(objPlayer.getPlayerID(), objPlayer.getFullName(), objPlayer.getCountry(), objPlayer.getELO());
			}
			
		}
	}
	
	// Option 1 - Third step - Getting all the Players from the Database:
	public static void ListPlayersFromDB() {
		
		// Initialization of the objectDAO of Player:
		PlayerDAO objPlayerDAO = new PlayerDAO();
		
		// Invoking the SELECT method from the class:
		objPlayerDAO.listPlayers();
		
	}
	
	// Option 2 - First step - Getting the Tournaments from the user's input by keyboard:
	public static ArrayList<Tournament> GetTournamentsFromKeyboard() {
		
		// Initialization of the ArrayList of Tournaments:
		ArrayList<Tournament> arlTournaments = new ArrayList<Tournament>();
		
		// Initialization of the variables of the Tournament:
		String stCode;
		String stCategory;
		String stCity;			// For the ->
		Location objLocation;	// <- City
		Date dtStartDate;
		Date dtEndDate;
		HashSet<Player> hsetPlayers = new HashSet<Player>();
		
		// To control the inserted players:
		ArrayList<Integer> arlPlayersID = new ArrayList<Integer>();
		int iPlayerID;
		Player objPlayer;
		
		// While true loop to ask for players until a "zero" is inserted as the Tournament's code:
		while (true) {
			// Cleaning the keyboard buffer:
			IOTools.Reset();
			// Declaration of the boolean bExists to false to check the entered Code later:
			boolean bExists = false;
			// Asking for the Tournament's Code:
			stCode = IOTools.AskString("Tournament's Code: (zero to exit)");
			// Checking if the user has inserted zero:
			if (stCode.equals("0")) {
				// Stopping the while loop:
				break;
			}
			// Enhanced for loop to check the objects of the ArrayList:
			for(Tournament objTournament : arlTournaments) {
				// If any object matches the Tournament code...
				if(objTournament.getCode() == stCode) {
					// Changes the boolean bExists to true:
					bExists = true;
					// And stops the execution:
					break;
				}
			
			}
			// Checking the value of the boolean value of exists:
			if(bExists) {
				// If the ID value exists it will print an error:
				System.err.println("The inserted Tournament CODE is already in the list. ");
			}else{
				// If the execution ends here it means that everything is correct for now.
					
				// Cleaning the scanner buffer:
				IOTools.Reset();
				// Asking for the player's full name:
				stCategory = IOTools.AskCategory("Tournament's category");
				// Cleaning the scanner buffer again:
				IOTools.Reset();
				// Asking for the tournament's start date:
				dtStartDate = IOTools.AskDate("Tournament's starting date (DD-MM-YYYY format)");
				// Asking for the tournament's ending date:
				dtEndDate = IOTools.AskDate("Tournament's end date (DD-MM-YYYY format)");
				// Cleaning again...
				IOTools.Reset();
				// Asking for the tournament's city:										
				stCity = IOTools.AskStringWithSpaces("Tournament's city");
				
				// Initialization of the Location DAO
				LocationDAO objLocationDAO = new LocationDAO();
							
				// Checking if the City inserted does not exists:
				while(!objLocationDAO.checkLocation(stCity)) {
					
					// The city does not exists, we will add it now:
					System.out.println("City inserted is not in the Location table. Inserting it now...");
					
					// Object LocationDAO to insert later:
					LocationDAO objLocationDAO2 = new LocationDAO();
					
					// Resetting the keyboard one more time...
					IOTools.Reset();
	
					// Asking the country and the city's population:
					String stCountry = IOTools.AskStringWithSpaces(stCity + "'s country");
					int iPopulation = IOTools.AskInt(stCity + "'s population");
					
					// Using the second DAO object to add the Location:
					objLocationDAO2.addLocation(stCity, stCountry, iPopulation);
						
					// Showing the successful insertion:
					System.out.println("Location inserted.");		
					
				}
				
				// Extracting the location of the city inserted in the tournament:
				objLocation = objLocationDAO.extractLocation(stCity);
				
				// Creating the instance of the Tournament inserted by the user:
				Tournament objTournament = new Tournament(stCode, stCategory, objLocation, dtStartDate, dtEndDate);
	
				System.out.println("Tournament created. Inserting players now...");
				// Resetting the keyboard one more time...
				IOTools.Reset();
				
				// Now we have a complete Tournament initializated. It's turn now to insert the players in that tournament:
				
				// While true loop to ask for players until a "zero" is inserted as the Player's ID:
				while (true) {
					// Cleaning the keyboard buffer:
					IOTools.Reset();
					// Asking for the player's ID:
					iPlayerID = IOTools.AskInt("Player's ID (zero to exit)");
					// Checking if the user has inserted zero:
					if (iPlayerID == 0) {
						// Stopping the while loop:
						break;
					}

					// Checking if the value is already in the set:
					if (arlPlayersID.contains(iPlayerID)) {
						System.err.println("Player's ID already inserted.");
					}else {
						// Adding the player ID to the arraylist:
						arlPlayersID.add(iPlayerID);
						// Initialization of the DAO object:
						PlayerDAO objPlayerDAO = new PlayerDAO();
		
						// Checking if the ID exists in the player table:
						if(objPlayerDAO.checkPlayer(iPlayerID)) {				
							// Control message:
							System.out.println("Player " + iPlayerID + " found.");	
							// It exists: METHOD to extract the Player with the INSERTED ID from the Player table to our SET:
							objPlayer = objPlayerDAO.returnPlayer(iPlayerID);
							// Adding the Player to our HashSet:
							hsetPlayers.add(objPlayer);
						}else{
							// The player does not exists and therefore can't be entered in a tournament:
							System.err.println("You can't enter a Player ID that does not exist in the Player table!");
						}
					}
				}	
				// We finally add the hashSet of Players to the Tournament object:
				objTournament.setPlayers(hsetPlayers);
				// Adding the Tournament to the arraylist:
				arlTournaments.add(objTournament);
				// Resetting the array list of players ID:
				arlPlayersID.clear();
				}
			}		
		return arlTournaments;
	}
	
	// Option 2 - Second step - Getting the Tournaments in the ArrayList inserted into the DB:
	public static void SaveTournamentsToDB(ArrayList<Tournament> arlTournaments) {
		
		// Check if the ArrayList is empty:
		if (arlTournaments.isEmpty()) {
			// If it's empty it will not create it:
			System.err.println("Empty array of Tournaments -- INSERT operation not possible!");
			
		}else{
			// If the list is not empty it will populate the Tournament table:
			
			// Declaration of the object Tournament DAO:
			TournamentDAO objTournamentDAO = new TournamentDAO();
			// Iterator with the array list:
			Iterator<Tournament> itTournaments = arlTournaments.iterator();
			
			// Looping the ArrayList:
			while(itTournaments.hasNext()) {
				// Actual player:
				Tournament objTournament = itTournaments.next();
				// INSERT operation using the method in the DAO class:
				objTournamentDAO.addTournament(objTournament.getCode(), objTournament.getCategory(), objTournament.getLocation(),
						objTournament.getStartDate(), objTournament.getEndDate());
				
				// UPDATE operation using the method in the DAO class (adding a set of players to a Tournament):
				objTournamentDAO.addPlayers(objTournament.getCode(), objTournament.getPlayers());
			}
			
		}		
	}
	
	// Option 2 - Third step - Getting all the Tournaments from the Database:
	public static void ListTournamentsFromDB() {
		
		// Initialization of the objectDAO of Tournament:
		TournamentDAO objTournamentDAO = new TournamentDAO();
		
		// Invoking the SELECT method from the class:
		objTournamentDAO.listTournaments();

	}
	
	// Option 3 - First step - Getting the Locations from the user's input by keyboard:
	public static ArrayList<Location> GetLocationsFromKeyboard() {
		
		// Initialization of the Locations ArrayList:
		ArrayList<Location> arlLocations = new ArrayList<Location>();
		
		// Initialization of the variables:
		String stCity;
		String stCountry;
		int iPopulation;
		
		// While true loop to ask for players until a "zero" is inserted as the Location's city:
		while (true) {
			// Cleaning the keyboard buffer:
			IOTools.Reset();
			// Declaration of the boolean bExists to false to check the entered City later:
			boolean bExists = false;
			// Asking for the Location's city:
			stCity = IOTools.AskStringWithSpaces("Location's City: (insert 0 to exit)");
			// Checking if the user has inserted zero:
			if (stCity.equals("0")) {
				// Stopping the while loop:
				break;
			}
			// Enhanced for loop to check the objects of the ArrayList:
			for (Location objLocation : arlLocations)
				if(objLocation.getCity() == stCity) {
					// Changes the boolean bExists to true:
					bExists = true;
					// And stops the execution:
					break;
				}
			
			// Checking the value of the boolean value of exists:
			if(bExists) {
				// If City value exists it will print an error:
				System.err.println("The inserted City is already in the list. ");
			}else{
			// If the execution ends here it means that everything is correct for now.
			
			// Asking the Location's country:
			stCountry = IOTools.AskStringWithSpaces("Location's country");
			// Asking the Location's population:
			iPopulation = IOTools.AskInt("Location's population");
			
			// While loop to control a 0 or negative population:
			while(iPopulation <= 0) {
				// Error message:
				System.err.println("You can't enter a 0 or negative population. Please, try again.\n");
				// Asking again:
				iPopulation = IOTools.AskInt("Location's population");
			}
			
			// Creation of the new Location:
			Location objLocation = new Location(stCity, stCountry, iPopulation);

			// Adding the Location to our ArrayList:
			arlLocations.add(objLocation);
			}
		}		
		// Returning the ArrayList of Locations:
		return arlLocations;
	}
	
	// Option 3 - Second step - Getting the Locations in the ArrayList inserted into the DB:
	public static void SaveLocationsToDB(ArrayList<Location> arlLocations) {
		
		// Check if the ArrayList is empty:
		if (arlLocations.isEmpty()) {
			// If it's empty it will not create it:
			System.err.println("Empty array of Locations -- INSERT operation not possible!");
			
		}else{
			// If the list is not empty it will populate the SQLite database
			
			// Declaration of the object DAO:
			LocationDAO objLocationDAO = new LocationDAO();
			
			// Iterator:
			Iterator<Location> itLocations = arlLocations.iterator();
			
			// While the list has a next item:
			while(itLocations.hasNext()) {
				// Extraction of the object of the list:
				Location objLocation = itLocations.next();
				// And using the INSERT method in the DAO object:
				objLocationDAO.addLocation(objLocation.getCity(), objLocation.getCountry(), objLocation.getPopulation());
			}
		}		
	}
	
	// Option 3 - Third step - Getting all the Locations from the Database:
	public static void ListLocationsFromDB() {
		
		// Initialization of the objectDAO of Location:
		LocationDAO objLocationDAO = new LocationDAO();
		
		// Invoking the SELECT method from the class:
		objLocationDAO.listLocations();
		
	}
	
	// Option 4 - Deleting an ArrayList of Players:
	public static void DeleteChessPlayer() {
		
		// ArrayList:
		ArrayList<Integer> arlPlayersToDelete = new ArrayList<Integer>();
		
		// Initialization of the variable that identifies the player to delete (its ID):
		int iPlayerID;
		
		// While true loop to ask for players until a "zero" is inserted as the Player's ID:
		while (true) {
			// Cleaning the keyboard buffer:
			IOTools.Reset();
			// Declaration of the boolean bExists to false to check the entered ID later:
			boolean bExists = false;
			// Asking for the player's ID:
			iPlayerID = IOTools.AskInt("Player's ID to delete (zero to exit)");
			// Checking if the user has inserted zero:
			if (iPlayerID == 0)
				// Stopping the while loop:
				break;
			// Enhanced for loop to check the objects of the ArrayList:
			for (Integer iValue : arlPlayersToDelete) {
				// If the player's ID in the array list matches the one inserted...
				if(iValue == iPlayerID){
					// Changes the boolean bExists to true:
					bExists = true;
					// And stops the execution:
					break;
				}
			}
			// Checking the value of the boolean value of exists:
			if(bExists) {
				// If the ID value exists it will print an error:
				System.err.println("The inserted ID is already in the list. ");
			}
			
			// Adding the ID to our ArrayList:
			arlPlayersToDelete.add(iPlayerID);

			}
			// Now we delete all the players with the ID's in our arraylist:

			// Declaration of the iterator of the players list:
			Iterator<Integer> itPlayers = arlPlayersToDelete.iterator();
			
			// Initialization of the DAO object:
			PlayerDAO objPlayerDAO = new PlayerDAO();
			
			// While the list has a next:
			while(itPlayers.hasNext()) {
				// Extracting the ID of the list...
				int iPlayerIDToDelete = itPlayers.next();
				// ... and deleting it:
				objPlayerDAO.deletePlayer(iPlayerIDToDelete);
			}
	}
	
	// Option 5 - Deleting an ArrayList of Tournaments:
	public static void DeleteTournament() {
		
		// ArrayList:
		ArrayList<String> arlTournamentsToDelete = new ArrayList<String>();
		
		// String that will identify the tournament's code:
		String stCode;
		
		// While true loop to ask for players until a "zero" is inserted as the Player's ID:
		while (true) {
			// Cleaning the keyboard buffer:
			IOTools.Reset();
			// Declaration of the boolean bExists to false to check the entered ID later:
			boolean bExists = false;
			// Asking for the player's ID:
			stCode = IOTools.AskString("Tournament's code to delete (zero to exit)");
			// Checking if the user has inserted zero:
			if (stCode.equals("0"))
				// Stopping the while loop:
				break;
			// Enhanced for loop to check the objects of the ArrayList:
			for (String stCheck : arlTournamentsToDelete) {
				// If the player's ID in the array list matches the one inserted...
				if(stCheck.equals(stCode)){
					// Changes the boolean bExists to true:
					bExists = true;
					// And stops the execution:
					break;
				}
			}
			// Checking the value of the boolean value of exists:
			if(bExists) {
				// If the ID value exists it will print an error:
				System.err.println("The inserted code is already in the list. ");
			}
			// Adding the City to our deletion list:
			arlTournamentsToDelete.add(stCode);
			}
			// Now we delete all the players with the ID's in our arraylist:
		
			// Initialization of the iterator of our list:
			Iterator<String> itTournaments = arlTournamentsToDelete.iterator();
			
			// Initialization of the DAO object of Tournament:
			TournamentDAO objTournamentDAO = new TournamentDAO();
			
			// While the list has a next item...
			while(itTournaments.hasNext()) {
				// it will extract its String to delete...
				String stToDelete = itTournaments.next();
				// and the DAO Object with the delete method will do the rest...
				objTournamentDAO.deleteTournament(stToDelete);
			}		
	}
	
	// Option 6 - Deleting an ArrayList of Locations:
	public static void DeleteLocation() {
		
		// ArrayList:
		ArrayList<String> arlLocationsToDelete = new ArrayList<String>();
		
		// String of the city to delete:
		String stCity;
		
		// While true loop to ask for players until a "zero" is inserted as the Player's ID:
		while (true) {
			// Cleaning the keyboard buffer:
			IOTools.Reset();
			// Declaration of the boolean bExists to false to check the entered ID later:
			boolean bExists = false;
			// Asking for the player's ID:
			stCity = IOTools.AskString("Location's city to delete (zero to exit)");
			// Checking if the user has inserted zero:
			if (stCity.equals("0"))
				// Stopping the while loop:
				break;
			// Enhanced for loop to check the objects of the ArrayList:
			for (String stCheck : arlLocationsToDelete) {
				// If the player's ID in the array list matches the one inserted...
				if(stCheck.equals(stCity)){
					// Changes the boolean bExists to true:
					bExists = true;
					// And stops the execution:
					break;
				}
			}
			// Checking the value of the boolean value of exists:
			if(bExists) {
				// If the ID value exists it will print an error:
				System.err.println("The inserted city is already on the list. ");
			}
			// Adding the city to our list:
			arlLocationsToDelete.add(stCity);
			}
			// Now we delete all the Locations with the City on the list:
		
			// Iterator of the list:
			Iterator<String> itLocations = arlLocationsToDelete.iterator();
			
			// Since we have to avoid raising a Hibernate exception when deleting Locations (we must check if a Location is linked to a
			// Tournament) we initiate two DAO objects, one for Location and the other for the Tournament:
			LocationDAO objLocationDAO = new LocationDAO();
		    TournamentDAO objTournamentDAO = new TournamentDAO();
		    
		    // While the list has a Next:
			while(itLocations.hasNext()) {
				// Extraction of the city to delete:
				String stToDelete = itLocations.next();
				// Checking if the city IS NOT linked to any tournament using the DAO method:
				if(!objTournamentDAO.checkCity(stToDelete)) {
					// It is not, the location will be deleted:
					objLocationDAO.deleteLocation(stToDelete);
				}else {
					// The Location's City is referenced in some Tournament so in order to avoid raising an exception we 
					// print an error message.
					System.err.println("You can't delete a Location referenced in the Tournament table! [INTEGRITY ERROR]");
				}
			}		
	}
	
	// Option 7 - OPTIONAL - Find a Player by inserting its ID:
	public static void FindChessPlayer() {
		
		// Initialization of the ID player:
		int iPlayerID;
		
		// Initialization of the object DAO:
		PlayerDAO objPlayerDAO = new PlayerDAO();
		
		// Asking an integer for ID:
		iPlayerID = IOTools.AskInt("Insert the ID of the player you want to look up (Insert 0 to exit)");
		
		if (!(iPlayerID == 0)) {
			// Using the method to find the player:
			objPlayerDAO.findPlayer(iPlayerID);
		}else {
			System.out.println("Operation aborted.");
		}
	}
	
	// Option 8 - OPTIONAL - Find a Tournament by inserting its Code:
	public static void FindTournament() {
		
		// Initialization of the Tournament Code string:
		String stCode;
		
		// Initialization of the object DAO:
		TournamentDAO objTournamentDAO = new TournamentDAO();

		// Asking a string without spaces for the Code:
		stCode = IOTools.AskString("Insert the Code of the tournament you want to look up (Insert 0 to exit)");
		
		if(!stCode.equals("0")) {
			// Using the method to find the tournament:
			objTournamentDAO.findTournament(stCode);	
		}else {
			System.out.println("Operation aborted");
		}
	}

	// MAIN METHOD:
	public static void main(String[] stArgs) {
		
		// ArrayList declaration:
		ArrayList<Player> arlPlayers;
		ArrayList<Tournament> arlTournaments;
		ArrayList<Location> arlLocations;
		
		
		// Boolean initialized to "false" in order to show the menu until the user enters a 0:
		boolean bQuit = false;
		// Integer declaration for the menu's option:
		int iOption;
		
		// While bQuit is false the menu will be shown:
		while (!bQuit) {
			// The option will be selected by using the method in this class PrintMenu:
			iOption = PrintMenu();
			
			// Switch:
			switch (iOption) {
			// 1: Adding players and scores:
			case 1:
				// Message:
				System.out.println("======> INSERTING AND LISTING CHESS PLAYERS\n");
				// Filling up the ArrayList of players by using the method:
				arlPlayers = GetPlayersFromKeyboard();
				// Invoking the method to save the list of players in the database:
				SavePlayersToDB(arlPlayers);
				// Finally listing all the chess players:
				ListPlayersFromDB();
				// Press any key in order to continue -> Show again the main menu.
				PressAnyKeyToContinue();
				break;
			case 2:
				// Message:
				System.out.println("======> INSERTING AND LISTING TOURNAMENTS\n");
				// Filling up the ArrayList of tournaments by using the method:
				arlTournaments = GetTournamentsFromKeyboard();
				// Invoking the method to save the list of tournaments in the database:
				SaveTournamentsToDB(arlTournaments);
				// Finally listing all the tournaments:
				ListTournamentsFromDB();
				// Press any key in order to continue -> Show again the main menu.
				PressAnyKeyToContinue();
				break;
			case 3:
				// Message:
				System.out.println("======> INSERTING AND LISTING LOCATIONS\n");
				// Filling up the ArrayList of Locations by using the method:
				arlLocations = GetLocationsFromKeyboard();
				// Invoking the method to save the list of Locations in the database:
				SaveLocationsToDB(arlLocations);
				// Finally listing all the Locations:
				ListLocationsFromDB();
				// Press any key in order to continue -> Show again the main menu.
				PressAnyKeyToContinue();
				break;
			case 4:
				// Message:
				System.out.println("======> DELETING CHESS PLAYERS\n");
				// Method to delete players:
				DeleteChessPlayer();
				// Press any key in order to continue -> Show again the main menu.
				PressAnyKeyToContinue();
				break;
			case 5:
				// Message:
				System.out.println("======> DELETING TOURNAMENTS\n");
				// Method to delete tournaments:
				DeleteTournament();
				// Press any key in order to continue -> Show again the main menu.
				PressAnyKeyToContinue();
				break;
			case 6:
				// Message:
				System.out.println("======> DELETING LOCATIONS\n");
				// Method to delete locations:
				DeleteLocation();
				// Press any key in order to continue -> Show again the main menu.
				PressAnyKeyToContinue();				
				break;
			case 7:
				// Message:
				System.out.println("======> FIND A CHESS PLAYER\n");
				// Method to find a player:
				FindChessPlayer();
				// Press any key in order to continue -> Show again the main menu.
				PressAnyKeyToContinue();
				break;
			case 8:
				// Message:
				System.out.println("======> FIND A TOURNAMENT\n");
				// Method to find a tournament:
				FindTournament();
				// Press any key in order to continue -> Show again the main menu.
				PressAnyKeyToContinue();
				break;
			case 0:
				// Switch of the boolean to true, the program will end.
				bQuit = true;
				// Message:
				System.out.println("======> Thanks for using this program. Goodbye!");
				break;
			default:
				// Message:
				System.out.println("======> Select an option or press 0 to exit.");
			}
		}	
	}
}
