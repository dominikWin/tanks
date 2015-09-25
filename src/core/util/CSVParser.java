package core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CSVParser {
	public static String[][] parseCSVFile(String filePath) {
		Logger.log("Begin parsing file " + filePath);
		String text = readFile(filePath);
		return parseCSV(text);
	}

	private static String[][] parseCSV(String text) {
		Logger.log("Starting parsing on text:\n" + text);

		int lines = text.split("\n").length;

		Logger.log("Found " + lines + " lines in file");

		return null;
	}

	private static String readFile(String filePath) {
		Logger.log("Opening file " + filePath);
		File file = new File(filePath);
		assert(file.exists());
		assert(file.isFile());
		assert(file.canRead());
		Scanner s = null;
		try {
			s = new Scanner(file);
		} catch (FileNotFoundException e) {
			Logger.log("Error reading file " + filePath, Logger.ERROR);
			e.printStackTrace();
		}
		assert(s != null);
		String out = "";
		while (s.hasNextLine())
			out += s.nextLine() + "\n";
		return out;
	}
}
