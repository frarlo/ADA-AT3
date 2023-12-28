package UTIL;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * Class: IOTools. Adaptation of the IOTools class seen in the corrected exercises. This class helps us to check
 * whether the data inserted by the user is between the desired bounds or check if it complies with the type variable
 * we want to insert in some point. It is also used to show menus in a simple way.
 * 
 * Note: original @authors Abelardo Martínez and Laia Méndez.
 * 
 */

public class IOTools {
	
	// Declaration of the Scanner instance to use by all the methods in the class:
	public static Scanner scKeyboard = new Scanner(System.in);
	
	/*
	 * Data input methods
	 */
	
	// Method to clean the buffer of the keyboard.
	public static void Reset() {
		scKeyboard.nextLine();
	}
	
	// Displays a text and waits for a unique string (without spaces). Since the program differentiates between strings with spaces
	// and without them I opted for maintaining the original method although it was corrected in a forum post(!):
	public static String AskString(String stQuestion) {
		System.out.print(stQuestion + ": ");
		return scKeyboard.next();
	}
	
	// Displays a text and waits for other text (with spaces) to be inserted in the keyboard:
	public static String AskStringWithSpaces(String stQuestion) {
		System.out.print(stQuestion + ": ");
		return scKeyboard.nextLine();
	}
	
	// Displays a text and waits for an specific String to be inserted:
	public static String AskCategory(String stQuestion) {
		// Initialization of the repeating value to ask the question:
		boolean bRepeat = true;
		// Initialization of the integer of the result:
		String stType = "";
		do{
			// Reads the above method question:
			stType = AskString(stQuestion);
			// String converted to upper case to comply with DB CHECKS:
			stType = stType.toUpperCase();
			// A loop will ask for a correct category if the one inserted is misspelled or non-existent:
			if(!stType.equals("AMATEUR") && !stType.equals("PROFESSIONAL") && !stType.equals("MASTER") && !stType.equals("SENIOR")){
				bRepeat = true;
				System.err.println("Please, insert a valid Category (AMATEUR, PROFESSIONAL, MASTER or SENIOR).");
			}else{
				bRepeat = false;
				break;
			}
		}while(bRepeat);
			
	return stType;			
	}
	
	// Displays a text and waits for a String that will be converted to Date and checked if it is correct:
	public static Date AskDate(String stQuestion) {
		// Declaration of HOW the date has to be outputted (following SQL order)
		//String DATE_FORMAT = "yyyy-MM-dd";
		String DATE_FORMAT = "dd-MM-yyyy";
		// Initialization of the boolean:
		boolean bRepeat = true;
		// Initialization of the Date:
		Date dtDate = null;
		do{
			// Asking the date by invoking the method AskString:
			String stDate = AskString(stQuestion);
			
			// Converting the string to "DATE":
			try {
				SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
				// I was unable to find while surfing the Internet information on how to check that the user introduces a valid date
				// i.e. 30-12-2023 instead of 33-14-2023 and I had to ask to ChatGPT, which recommended me the use of the following method:
				dateFormat.setLenient(false);
				// Moreover, and after some research: https://stackoverflow.com/a/7606437/14441036 of the method, it seems that it is a correct approach
				// in order to parse dates with much more strict controls.
				dtDate = dateFormat.parse(stDate);
				// If the conversion is successful:
				bRepeat = false;
			// Wrong format catch
			}catch (ParseException pe) {
				System.err.println("Please, insert a valid date...");
				bRepeat = true;
			}
			
		}while(bRepeat);
		
		return dtDate;
	}
	
	// Displays a text and waits for an integer to be inserted in the keyboard:
	public static int AskInt(String stQuestion){
		// Initialization of the variables:
		boolean bRepeat = true;
		// By initializing iResult to -1 we avoid possible errors:
		int iResult = -1;
		// Continuous loop until bRepeat is switched to "false":
		do {
			// We use the above method to ask for the integer to avoid line skipping:
			String stValue = AskString(stQuestion);
			try {
				// Parsing the String to an integer value:
				iResult = Integer.parseInt(stValue);
				// The loop can end:
				bRepeat = false;
				
			}catch(NumberFormatException nfe) {
				// If the user does not enter an integer (e.g. "hi" or "4.2") the method will catch the exception.
				System.err.println("Error: " + stValue + " is not a valid number.");
			}
			
		}while(bRepeat);
		
		// Returning the integer:
		return iResult;
	}
	
	// Displays a text and waits for an integer entered by the keyboard between a minimum and maximum:
	public static int AskInt(String stQuestion, int iMin, int iMax){
		// Initialization of the repeating value to ask the question:
		boolean bRepeat = true;
		// Initialization of the integer of the result:
		int iResult = -1;
		do {
			// Reads the above method question:
			String stValue = AskString(stQuestion);
			try {
				// We assign the number of the string stValue to the int iResuly by using parseInt:
				iResult = Integer.parseInt(stValue);
				// bRepeat will be switched to "false" if it is between the min or the max:
				bRepeat = (iResult < iMin) || (iResult > iMax);
				// If bRepeat is still true it will say it:
				if(bRepeat == true) {
					System.err.println("Error: " + iResult + " is out of range.");
				}
			}catch(NumberFormatException nfe){
				System.err.println("Error: " + stValue + " is not a valid number.");
			}
				
		}while(bRepeat);
			
		return iResult;
		}
	
	
	/*
	 *  Menu methods
	 */
	
	// Displays a text and waits for a integer from a list of options.
	public static int AskInt(String stQuestion, int... iValids) {
		// Boolean to keep asking for the option:
		boolean bAsk = true;
		// Integer to return with the desired option:
		int iResult;
		// Do-while loop to ask for a valid option between the declared bounds:
		do {
			// Recursive method to get the iResult by calling this same method (checking the first and last position).
			iResult = AskInt(stQuestion, iValids[0], iValids[iValids.length -1]);
			// If the option inserted is between bounds it will change the value of bAsk:
			bAsk = Arrays.binarySearch(iValids, iResult) < 0;
		}while(bAsk);
				
		// The integer is returned:
		return iResult;
	}

	// Displays a menu and waits for the user to choose an option.
	public static int ShowMenu(String stTitle, String... stOptions) {
		// New line:
		System.out.println();
		// Displaying the title:
		System.out.println(stTitle);
		// Separator:
		System.out.println("========================================================================");
		// Initialization of the option variable:
		int iResult;
		// If the string stOptions is null OR the string stOptions has a length of "zero" it will switch the result to -1:
		if((stOptions == null) || (stOptions.length == 0)) {
			iResult = -1;
		// If it is not, the menu is displayed:
		}else{
			// Initialization of the number of options:
			int iNumOptions = 0;
			// For loop to iterate between the length of the String options:
			for(int ii= 0; ii < stOptions.length; ii++) {
				// If there is no options in that position or it is null it will print a new empty line:
				if((stOptions[ii] == null) || ("".equals(stOptions[ii].trim()))){
					System.out.println();
				// If it is not empty it will render it:
				}else{
					System.out.println("  " + iNumOptions + ". " + stOptions[ii]);
					// Increment of the number of options:
					iNumOptions++;
				}
			}
			System.out.println("========================================================================");
			// We use the method AskInt to check an option between zero and the desired number of options:
			iResult = AskInt("    Select an option", 0, iNumOptions);
			// New line:
			System.out.println();
		}
		return iResult;
		
	}

}
	

