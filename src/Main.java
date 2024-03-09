import java.awt.*;
import java.nio.file.*;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Path filePath = Path.of(System.getProperty("user.dir"), "dictionary.txt");

        // Offer user to import dictionary if default doesn't exist
        if (!Files.exists(filePath)) {
            String dialogMessage = "No dictionary.txt found in the current working directory, import dictionary?";
            int confirm = JOptionPane.showConfirmDialog(null, dialogMessage, "Spelling Bee", JOptionPane.YES_NO_OPTION);
            if (confirm != 0) System.exit(1);
            FileDialog filePicker = new FileDialog((Frame)null);
            filePicker.setVisible(true);
            filePath = Path.of(filePicker.getDirectory(), filePicker.getFile());
        }

        GameLogic.importWords(filePath);
        GameLogic.newGame();
        GUI.initialize();
    }
}
