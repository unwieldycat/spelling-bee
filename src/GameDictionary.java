import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;

public class GameDictionary {

    public static ArrayList<String> words = new ArrayList<>();

    /**
     * Create dictionary from file
     * @param path Path to file
     */
    public static void importWords(Path path) {
        if (!Files.exists(path)) {
            System.out.println("File at " + path + " doesn't exist!");
            return;
        }

        try {
            words.addAll(Files.readAllLines(path));
        } catch(IOException e) {
            System.out.println("IOException occurred: " + e.getMessage());
            System.exit(1);
        } catch(SecurityException e) {
            System.out.println("SecurityException occurred: " + e.getMessage());
            System.out.println("Did you allow Java to access the directory?");
            System.exit(1);
        }
    }

    /**
     * Check if dictionary contains word
     * @param word Word to check
     * @return Boolean
     */
    public static boolean contains(String word) {
        return words.contains(word);
    }
}
