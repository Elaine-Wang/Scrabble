import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Open files for reading.
 *
 */

public class OpenFile {
	
	public static Scanner openToRead(String fileName) {
		Scanner input = null;
		try {
			input = new Scanner(new File(fileName));
		} 
		catch(FileNotFoundException e) {
			System.err.println("ERROR: Cannot open " + fileName
							+ " for reading.");
			System.exit(21); //0 means exit without problem.
		}
		return input;
	}
}