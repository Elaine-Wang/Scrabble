import java.util.Scanner;
import java.util.ArrayList;

/**
 *  Finds all the words that can be formed with a list of letters,
 *  then finds the word with the highest Scrabble score.
 *
 */
 
public class WordUtilities {
	
	private static int numWords;
	
	public WordUtilities() {
		numWords = 0;
	}
	
	public static void main (String [] args) {
		String input = getInput();
		String [] word = findAllWords(input, "words.txt");
		printWords(word);
		
		// Score table in alphabetic order according to Scrabble
		int [] scoretable = {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
		String best = bestWord(word,scoretable);
		System.out.println("Highest scoring word: " + best + "\n");
	}
	
	/**
	 *  Enter a list of 3 to 12 letters. It also checks
	 *  all letters to insure they fall between 'a' and 'z'.
	 *
	 *  @return  A string of the letters
	 */
	public static String getInput ( ) {
		boolean done;
		String s;
		
		do {
			s = Prompt.getString("\nPlease enter a list of letters, from 3 to 12 letters long, without spaces");
			if (s.length() < 3 || s.length() > 12) {
				System.err.println("\nERROR: Must have length of 3 to 12 letters\n");
				System.exit(1);
			}
			
			//check each letter
			done = true;
			for (int count = 0; count < s.length(); count++) {
				char c = s.charAt(count);
				if (c < 'a' || c > 'z') done = false;
			}
		} while (!done);
		
		return s;
	}
	
	/**
	 *  Find all words that can be formed by a list of letters.
	 *
	 *  @param letters  String list of letters
	 *  @return   An array of strings with all words found.
	 */
	public static String [] findAllWords (String letters, String fileName) {		
		String word;
		String[] words = new String[1000];
		
		// Open the database file
		Scanner inFile = OpenFile.openToRead(fileName);
		
		while (inFile.hasNext()) {
			word = inFile.next();
			if (matchLetters(word, letters)) {
				words[numWords] = word;
				numWords++;
			}
		}
		
		return words;
	}
	
	public static ArrayList<String> findAllWords (String letters, String fileName, boolean isArrayList) {		
		String word;
		ArrayList<String> words = new ArrayList<String>();
		
		// Open the database file
		Scanner inFile = OpenFile.openToRead(fileName);
		
		while (inFile.hasNext()) {
			word = inFile.next();
			if (matchLetters(word, letters)) {
				words.add(word);
			}
		}
		
		return words;
	}
	
	/**
	 *  Determines if a word can be formed by a list of letters.
	 *
	 *  @param word  The word to be tested.
	 *  @param letters  A string of the list of letter
	 *  @return   True if word can be formed, false otherwise
	 */
	public static boolean matchLetters (String word, String letters) {
		for (int i = 0; i < word.length(); i++){
			int ind = letters.indexOf(word.charAt(i));
			if (ind == -1)
				return false;
            letters = letters.substring(0,ind) + letters.substring(ind+1);
        }
        return true;
	}
	
	/**
	 *  Print the words found to the screen.
	 *
	 *  @param word  The string array containing the words.
	 */
	public static void printWords (String [] word) {
		System.out.println();
		
		for (int i = 0; i < numWords; i++) {
			System.out.printf("%10s\t", word[i]);
			if ((i+1)%5 == 0) 
				System.out.println();
		}
		
		if (numWords > 0) 
			System.out.println("\n");
	}
	
	/**
	 *  Finds the highest scoring word according to Scrabble rules.
	 *  Compare the best word to the current word in for loop, if the best is less
	 *  make the current word the new best
	 * 
	 *  @param word  An array of words to check.
	 *  @param scoretable  An array of 26 integer scores in letter order.
	 *  @return   The word with the highest score.
	 */
	public static String bestWord (String [] word, int [] scoretable) {
		String best = word[0];
		
		for (int i = 0; i < numWords; i++) {
			if (getScore(best, scoretable) < getScore(word[i], scoretable))
				best = word[i];
		}

		return best;
	}
	
	public static String bestWord (ArrayList<String> words, int [] scoretable) {
		String best = words.get(0);
		
		for (int i = 0; i < words.size(); i++) {
			if (getScore(best, scoretable) < getScore(words.get(i), scoretable))
				best = words.get(i);
		}

		return best;
	}
	
	/**
	 *  Calculates the score of a word according to Scrabble rules.
	 *  Use ascii value and subtract 97 to get the place in the array (lowercase 'a' starts is 97, rest of alphabet follows)
	 * 
	 *  @param word  The word to score
	 *  @param scoretable  The array of 26 scores for alphabet.
	 *  @return   The integer score of the word.
	 */
	public static int getScore (String word, int [] scoretable) {
		int score = 0;

		for (int i = 0; i < word.length(); i++) 
			score += scoretable[(int)word.charAt(i)-97];
		
		return score;
	}
}