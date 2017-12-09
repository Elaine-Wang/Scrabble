import java.util.Scanner;
import java.util.ArrayList;

/**
 *  A simple version of the Scrabble game where the user plays against the computer.
 *
 *  @author Elaine Wang
 */

public class Scrabble {
	
	private final int NUMTILES = 8;
	public int [] scores = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10,
					 1, 1, 1, 1, 4, 4, 8, 4, 10};
	private String tilesRemaining = "AAAAAAAAAABBCCDDDDEEEEEEEEEEEEEFFGGGHHIIIIIIIII" +
					"JKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ";
	private int pScore, cScore;
	private ArrayList<String> pHand, cHand;
	private String word;
		
	// Constructor
	public Scrabble() {
		pScore = cScore = 0;
		pHand = new ArrayList<String>();
		cHand = new ArrayList<String>();
		word = "";
	}
	
	public static void main(String [] args) {
		Scrabble s = new Scrabble();
		s.playGame();
	}
	
	public void playGame() {
		printIntroduction();
		
		// Play the game
		boolean endOfGame = false;
		String previousWord = "";
		updateHand(pHand);
		updateHand(cHand);
		
		printState();
		do {
			//User's turn
			String letters = "";
			for (int i = 0; i < pHand.size(); i++)
				letters += pHand.get(i);
			
			ArrayList<String> wordList = WordUtilities.findAllWords(letters.toLowerCase(), "words.txt", true);
			boolean done = false;
			while (!done) {
				word = WordUtilities.bestWord(wordList, scores);
				if (word.length() < 4 || word.length() > 8) {
					wordList.remove(word);
					if (wordList.size() == 0) {
						System.out.println("No more words can be created\n");
						System.out.printf("Player Score: %10d\n", pScore);
						System.out.printf("Computer Score: %8d\n\n", cScore);
						System.out.println("Thank you for playing ScrapplePlus!");
						System.exit(0);
					}
				}
				else 
					done = true;
			}

			word = Prompt.getString("Please enter a word created from your current set of tiles");
			
			if (!isValid(word, pHand))
				System.exit(1);
			
			pScore = updateScore(previousWord, word, pScore);
			removeLetters(pHand);
			updateHand(pHand);
			printState();
			previousWord = word;
			
			Prompt.getString("It's the computer's turn. Hit ENTER on the keyboard to continue");
			
			// Computer's turn
			letters = "";
			for (int i = 0; i < cHand.size(); i++)
				letters += cHand.get(i);
			
			wordList = WordUtilities.findAllWords(letters.toLowerCase(), "words.txt", true);
			done = false;
			while (!done) {
				word = WordUtilities.bestWord(wordList, scores);
				if (word.length() < 4 || word.length() > 8)
					wordList.remove(word);
				else
					done = true;
				if (wordList.size() == 0) {
					System.out.println("No more words can be created\n");
					System.out.printf("Player Score: %10d\n", pScore);
					System.out.printf("Computer Score: %8d\n\n", cScore);
					System.out.println("Thank you for playing ScrapplePlus!");
					System.exit(0);
				}
			}
			
			System.out.println("\nThe computer chose: " + word.toUpperCase() + "\n");
			cScore = updateScore(previousWord, word, cScore);
			removeLetters(cHand);
			updateHand(cHand);
			printState();
			
			previousWord = word;
			word = "";
			
			String playerHand = "";
			for (int i = 0; i < pHand.size(); i++) 
				playerHand += pHand.get(i);
			String compHand = "";
			for (int i = 0; i < cHand.size(); i++) 
				compHand += cHand.get(i);
			if (WordUtilities.findAllWords(playerHand.toLowerCase(), "words.txt", true).size() == 0 || WordUtilities.findAllWords(compHand.toLowerCase(), "words.txt", true).size() == 0)
				endOfGame = true;
		} while (!endOfGame);
	}
	
	private void removeLetters(ArrayList<String> hand) {
		String[] letters = new String[word.length()];
		for (int i = 0; i < word.length(); i++) {
			if (i == word.length() - 1)
				letters[i] = word.substring(word.length()-1);
			else
				letters[i] = word.substring(i, i+1);
		}
		for (int i = 0; i < word.length(); i++)
			hand.remove(letters[i].toUpperCase());
	}
	
	/**
	 * printState() prints the state of the game
	 * the remaining tiles, scores, hands
	 * updates the hands
	 */
	private void printState() {
		System.out.println("Here are the tiles remaining in the pool of letters:\n");
		System.out.print("\t");
		for (int i = 0; i < tilesRemaining.length(); i++) {
			System.out.print(tilesRemaining.charAt(i)+" ");
			if ((i+1)%20 == 0) {
				System.out.println();
				System.out.print("\t");
			}
		}
		System.out.println("\n");
		System.out.printf("Player Score: %10d\n", pScore);
		System.out.printf("Computer Score: %8d\n\n", cScore);
		System.out.print("THE TILES IN YOUR HAND ARE:");
		System.out.print("           ");
		for (int i = 0; i < pHand.size(); i++)
			System.out.print(pHand.get(i) + "  ");
		System.out.println("\n");
		System.out.print("THE TILES IN THE COMPUTER HAND ARE:");
		System.out.print("   ");
		for (int i = 0; i < cHand.size(); i++)
			System.out.print(cHand.get(i) + "  ");
		System.out.println("\n");
	}
	
	/**
	 * updateHands()  refills the hand
	 * also takes the letters from the tiles remaining
	 * use a for loop that runs the number of missing tiles in hands times
	 * get a random number from 0 to the number of tiles left-1
	 * get that index letter from the string of tiles remaining using substring
	 * add it to the hand
	 * remove the letter from the tiles remaining
	 * 
	 * @param ArrayList hand
	 **/
	private void updateHand(ArrayList<String> hand) {
		int numDraw = NUMTILES-hand.size();
		if (tilesRemaining.length() < numDraw) {
			System.out.println("Not enough tiles left for a full hand.\n");
			System.out.printf("Player Score: %10d\n", pScore);
			System.out.printf("Computer Score: %8d\n\n", cScore);
			System.out.println("Thank you for playing ScrapplePlus!\n");
			System.exit(0);
		}
		for (int i = 0; i < numDraw; i++) {
			int randNum = (int)(Math.random()*(tilesRemaining.length()));
			String randLetter = tilesRemaining.substring(randNum, randNum + 1);
			hand.add(randLetter);
			tilesRemaining = tilesRemaining.replaceFirst(randLetter, "");
		}
	}

	private boolean isValid(String word, ArrayList<String> hand) {
		if (word.length() < 4 || word.length() > 8) {
			System.err.println("The word must be between 4 and 8 letters!\nYOU LOSE!\n");
			return false;
		}
		String letters = "";
		for (int i = 0; i < hand.size(); i++)
			letters += hand.get(i);
		if (!WordUtilities.matchLetters(word.toUpperCase(), letters)) {
			System.err.println("Your hand doesn't contain all the letters in " + word + "!\nYOU LOSE!\n");
			return false;
		}
		else if (!checkList(word.toLowerCase())) {
			System.err.println(word + " isn't in the list!\nYOU LOSE!\n");
			return false;
		}
		return true;
	}
	
	private boolean checkList(String word) {
		String inWord = "";
		Scanner inFile = OpenFile.openToRead("words.txt");
		
		while (inFile.hasNext()) {
			inWord = inFile.next();
			if (inWord.equalsIgnoreCase(word)) {
				return true;
			}
		}
		return false;
	}
	
	private int updateScore(String previous, String word, int score) {
		if (previous.equals("")) {
			if (checkDouble(word.toLowerCase())) {
				System.out.println("BONUS WORD SCORE, TIMES 2!!!\n");
				score += 2*(WordUtilities.getScore(word.toLowerCase(), scores));
			}
			else
				score += WordUtilities.getScore(word.toLowerCase(), scores);				
		}
		else if ((int)previous.toLowerCase().charAt(0)+1 == (int)word.toLowerCase().charAt(0) && checkDouble(word.toLowerCase())) {
			System.out.println("BONUS WORD SCORE, TIMES 4!!!\n");
			score += 4*(WordUtilities.getScore(word.toLowerCase(), scores));
		}
		else if ((int)previous.toLowerCase().charAt(0)+1 == (int)word.toLowerCase().charAt(0) || checkDouble(word.toLowerCase())) {
			System.out.println("BONUS WORD SCORE, TIMES 2!!!\n");
			score += 2*(WordUtilities.getScore(word.toLowerCase(), scores));
		}
		else
			score += WordUtilities.getScore(word.toLowerCase(), scores);
		return score;
	}
	
	private boolean checkDouble(String word) {
		for (int i = 0; i < word.length(); i++) {
			if (i == word.length()-1)
				return false;
			if (word.charAt(i) == word.charAt(i+1))
				return true;
		}
		return false;
	}
	
	/**
	 *  Print the introduction screen for Scrapple.
	 */
	public void printIntroduction() {
		System.out.print("This game is a modified version of Scrabble. ");
		System.out.println("The game starts with a pool of letter tiles, with");
		System.out.println("the following group of 100 tiles:\n");
		
		for (int i = 0; i < tilesRemaining.length(); i ++) {
			System.out.printf("%c ", tilesRemaining.charAt(i));
			if (i == 49) System.out.println();
		}
		System.out.println("\n");
		System.out.printf("The game starts with %d tiles being chosen at ran", NUMTILES);
		System.out.println("dom to fill the player's hand. The player must");
		System.out.printf("then create a valid word, with a length from 4 to %d ", NUMTILES);
		System.out.println("letters, from the tiles in his/her hand. The");
		System.out.print("\"word\" entered by the player is then checked. It is first ");
		System.out.println("checked for length, then checked to make ");
		System.out.print("sure it is made up of letters from the letters in the ");
		System.out.println("current hand, and then it is checked against");
		System.out.print("the word text file. If any of these tests fail, the game");
		System.out.println(" terminates. If the word is valid, points");
		System.out.print("are added to the player's score according to the following table ");
		System.out.println("(These scores are taken from the");
		System.out.println("game of Scrabble):");
		
		// Print line of letter scores
		char c = 'A';
		for (int i = 0; i < 26; i++) {
			System.out.printf("%3c", c);
			c = (char)(c + 1);
		}
		System.out.println();
		for (int i = 0; i < scores.length; i++) System.out.printf("%3d", scores[i]);
		System.out.println("\n");
		
		System.out.print("The score is doubled if the word has consecutive double ");
		System.out.println("letters (e.g. ball). The score can also");
		System.out.print("double if the first character of the word follows the ");
		System.out.println("first character of the last word entered");
		System.out.print("in alphabetical order (e.g. \"catnip\" gets ");
		System.out.println("regular score, followed by \"dogma\" which earns double");
		System.out.print("points). If the word contains both, then quadruple the ");
		System.out.println("points.\n");
		
		System.out.print("The game ends when the player enters an ");
		System.out.println("invalid word, or the letters in the");
		System.out.println("pool and player's hand run out. Ready? Let's play!\n");
		
		Prompt.getString("HIT ENTER on the keyboard to continue");
	}
}