import java.util.ArrayList;
public class GameLogic {
    private static int score = 0;
    private static String rootWord;
    private static String requiredLetter;

    private static final ArrayList<String> guesses = new ArrayList<>();

    /**
     * Get the number of letters in a word
     * @param word Word to evaluate
     * @return Integer
     */
    private static int getNumLetters(String word) {
        int num = 0;
        ArrayList<Character> letters = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            if (letters.contains(letter)) continue;
            letters.add(letter);
            num++;
        }
        return num;
    }

    /**
     * Finds a random word with 7 distinct letters
     */
    private static void generateRootWord() {
        ArrayList<String> sevenCharWords = new ArrayList<>();

        for (String word : GameDictionary.words) {
            if (word.length() != 7) continue;

            boolean uniqueChars = true;
            for (int i = 0; i < word.length(); i++) {
                char letter = word.charAt(i);
                if (word.lastIndexOf(letter) != i || word.indexOf(letter) != i) {
                    uniqueChars = false;
                    break;
                }
            }

            if (uniqueChars) sevenCharWords.add(word);
        }

        int randWordIndex = (int)(Math.random() * sevenCharWords.size());
        GameLogic.rootWord = sevenCharWords.get(randWordIndex);

        int randLetterIndex = (int)(Math.random() * GameLogic.rootWord.length());
        GameLogic.requiredLetter = rootWord.substring(randLetterIndex, randLetterIndex + 1);
    }

    // =========================== Public Methods =========================== //

    /**
     * Response returned by checkWord
     */
    public enum GuessResponse {
        OK,
        NOT_FOUR,
        INVALID_WORD,
        MISSING_REQ_CHAR,
        ILLEGAL_CHAR,
        ALREADY_GUESSED,
    }

    /**
     * Check the word, and score if valid
     * @param word Word to check
     */
    public static GuessResponse checkWord(String word) {
        word = word.toLowerCase();

        if (word.length() < 4) return GuessResponse.NOT_FOUR;
        if (!GameDictionary.contains(word)) return GuessResponse.INVALID_WORD;
        if (!word.contains(GameLogic.getRequiredLetter())) return GuessResponse.MISSING_REQ_CHAR;
        if (guesses.contains(word)) return GuessResponse.ALREADY_GUESSED;

        for (String letter : word.split("")) {
            if (!rootWord.contains(letter)) return GuessResponse.ILLEGAL_CHAR;
        }

        score += getNumLetters(word) - 2;
        guesses.add(word);
        return GuessResponse.OK;
    }

    public static int getPossibleWords() {
        int count = 0;
        for (String word : GameDictionary.words) {
            if (word.length() < 4) continue;
            boolean valid = true;
            for (int i = 0; i < word.length(); i++) {
                String letter = word.substring(i, i + 1);
                if (!rootWord.contains(letter) && !requiredLetter.equals(letter)) {
                    valid = false;
                    break;
                }
            }
            if (valid) count++;
        }
        return count;
    }

    public static void newGame() {
        guesses.clear();
        score = 0;
        generateRootWord();
    }

    /**
     * Get the current score
     * @return Current score
     */
    public static int getScore() {
        return score;
    }

    /**
     * Get the root word
     * @return Root word string
     */
    public static String getRootWord() {
        return rootWord;
    }

    /**
     * Get the required letter
     * @return Required letter string
     */
    public static String getRequiredLetter() {
        return requiredLetter;
    }

    /**
     * Get the previous guesses
     * @return List of guesses
     */
    public static ArrayList<String> getGuesses() {
        return guesses;
    }
}
