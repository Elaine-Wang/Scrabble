import java.util.Scanner;

/**
 *  Gets user input.
 *
 */

public class Prompt {
	
	/**
	 *  Prompts for a string from the keyboard.
	 *
	 *  @param ask  string prompt
	 *  @return    the string gotten from the keyboard
	 */
	public static String getString (String ask) {
		Scanner keyboard = new Scanner(System.in);	//don't close System.in
		System.out.print(ask + " -> ");
		String input = keyboard.nextLine();
		return input;
	}
}