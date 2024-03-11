import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class GUI {
    private static JLabel hintLabel;
    private static JTextField inputBox;
    private static JButton[] stdLetterButtons;
    private static JButton reqLetterButton;
    private static JPanel wordsPanel;
    private static JLabel scoreLabel;
    private static JLabel wordCountLabel;

    // ========================== Private Methods ========================== //
    private static void shuffleButtons() {
        String[] letters = GameLogic.getRootWord().split("");

        for (int i = 0; i < letters.length; i++) {
            int randIndex = (int) (Math.random() * letters.length);
            String swap = letters[i];
            letters[i] = letters[randIndex];
            letters[randIndex] = swap;
        }

        int btnIdx = 0;
        for (String letter : letters) {
            if (Objects.equals(letter, GameLogic.getRequiredLetter())) {
                reqLetterButton.setText(letter.toUpperCase());
                continue;
            }

            stdLetterButtons[btnIdx++].setText(letter.toUpperCase());
        }
    }

    private static void updateGameInfo() {
        inputBox.setText("");
        wordsPanel.removeAll();
        for (String guess : GameLogic.getGuesses()) {
            JLabel wordLabel = new JLabel(guess, SwingConstants.CENTER);
            wordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            wordsPanel.add(wordLabel);
        }

        scoreLabel.setText("Score: " + GameLogic.getScore());
        wordCountLabel.setText(GameLogic.getGuesses().size() + " / " + GameLogic.getPossibleWords());
    }

    // ========================== Action Listeners ========================== //

    public static void shuffleAction(ActionEvent e) {
        shuffleButtons();
    }
    public static void submitAction(ActionEvent e) {
        GameLogic.GuessResponse response = GameLogic.checkWord(inputBox.getText());

        hintLabel.setForeground(Color.RED);

        switch (response) {
            case OK -> {
                hintLabel.setForeground(Color.GREEN);
                hintLabel.setText("Nice!");
            }
            case NOT_FOUR -> hintLabel.setText("Not four characters!");
            case INVALID_WORD -> hintLabel.setText("Not a word!");
            case MISSING_REQ_CHAR -> hintLabel.setText("Missing the required character!");
            case ILLEGAL_CHAR -> hintLabel.setText("You used illegal characters!");
            case ALREADY_GUESSED -> hintLabel.setText("You already guessed that one!");
        }

        updateGameInfo();
    }

    public static void clearAction(ActionEvent e) {
        inputBox.setText("");
    }

    public static void typingAction(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        inputBox.setText(inputBox.getText() + source.getText());
    }

    public static void newGameAction(ActionEvent e) {
        String dialogMessage = "Start new game? Your progress will not be saved";
        int confirm = JOptionPane.showConfirmDialog(null, dialogMessage, "Spelling Bee", JOptionPane.YES_NO_OPTION);
        if (confirm != 0) return;
        GameLogic.newGame();
        shuffleButtons();
        updateGameInfo();
        hintLabel.setText("");
    }

    // =========================== Public Methods =========================== //

    /**
     * Initialize the GUI
     */
    public static void initialize() {
        // Use Nimbus look and feel
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (Exception e) {
            return;
        }

        JFrame frame = new JFrame();
        frame.setTitle("Spelling Bee");
        frame.setSize(300, 400);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        JPanel topBar = new JPanel();
        BorderLayout topBarLayout = new BorderLayout();
        topBarLayout.setHgap(0);
        topBarLayout.setVgap(0);
        topBar.setLayout(topBarLayout);
        topBar.setBorder(BorderFactory.createEmptyBorder(8, 4, 8, 4));
        topBar.setMaximumSize(new Dimension(300, 16));
        frame.add(topBar);

        scoreLabel = new JLabel();
        scoreLabel.setText("Score: 0");
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topBar.add(scoreLabel, BorderLayout.CENTER);

        wordCountLabel = new JLabel();
        wordCountLabel.setText("? / ?");
        wordCountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topBar.add(wordCountLabel, BorderLayout.EAST);

        JButton newGame = new JButton();
        newGame.setText("New");
        newGame.addActionListener(GUI::newGameAction);
        topBar.add(newGame, BorderLayout.WEST);

        // Completed words list

        wordsPanel = new JPanel();
        wordsPanel.setMaximumSize(new Dimension(300, 200));
        wordsPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        wordsPanel.setLayout(new BoxLayout(wordsPanel, BoxLayout.Y_AXIS));

        JScrollPane wordsPanelScrollPane = new JScrollPane(wordsPanel);
        wordsPanelScrollPane.setSize(new Dimension(300, 200));
        frame.add(wordsPanelScrollPane);

        // Information label for feedback

        hintLabel = new JLabel();
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(hintLabel);

        // User input field

        inputBox = new JTextField();
        inputBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputBox.setHorizontalAlignment(SwingConstants.CENTER);
        inputBox.setMaximumSize(new Dimension(128, 18));
        inputBox.addActionListener(GUI::submitAction);
        frame.add(inputBox);

        // Letter buttons

        JPanel buttonMatrix = new JPanel();
        BoxLayout matrixLayout = new BoxLayout(buttonMatrix, BoxLayout.X_AXIS);
        buttonMatrix.setLayout(matrixLayout);
        buttonMatrix.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        frame.add(buttonMatrix);

        JPanel col1 = new JPanel();
        col1.setLayout(new BoxLayout(col1, BoxLayout.Y_AXIS));
        buttonMatrix.add(col1);

        JPanel col2 = new JPanel();
        col2.setLayout(new BoxLayout(col2, BoxLayout.Y_AXIS));
        buttonMatrix.add(col2);

        JPanel col3 = new JPanel();
        col3.setLayout(new BoxLayout(col3, BoxLayout.Y_AXIS));
        buttonMatrix.add(col3);

        // Create letter buttons
        stdLetterButtons = new JButton[6];
        for (int i = 0; i < 7; i++) {
            JButton button = new JButton();
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMargin(new Insets(0, 0, 0, 0));
            button.setMinimumSize(new Dimension(40, 40));
            button.setMaximumSize(new Dimension(40, 40));
            button.setPreferredSize(new Dimension(40, 40));
            button.addActionListener(GUI::typingAction);

            if (i < 2) col1.add(button);
            else if (i < 5) col2.add(button);
            else col3.add(button);

            if (i < 3) stdLetterButtons[i] = button;
            else if (i > 3) stdLetterButtons[i - 1] = button;
            else reqLetterButton = button;
        }

        shuffleButtons();

        reqLetterButton.setBackground(Color.YELLOW);
        reqLetterButton.setOpaque(true);

        // Action buttons

        JPanel actionButtons = new JPanel();
        BoxLayout buttonsLayout = new BoxLayout(actionButtons, BoxLayout.X_AXIS);
        actionButtons.setLayout(buttonsLayout);
        actionButtons.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        JButton clearButton = new JButton();
        clearButton.setText("Clear");
        clearButton.addActionListener(GUI::clearAction);
        actionButtons.add(clearButton);

        JButton shuffleButton = new JButton();
        shuffleButton.setText("Shuffle");
        shuffleButton.addActionListener(GUI::shuffleAction);
        actionButtons.add(shuffleButton);

        JButton enterButton = new JButton();
        enterButton.setText("Enter");
        enterButton.addActionListener(GUI::submitAction);
        actionButtons.add(enterButton);

        frame.add(actionButtons);
        frame.setVisible(true);

        updateGameInfo();
    }
}
