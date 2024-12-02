import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WordGuessGame {
    private String correctWord;
    private final int ROWS = 5; // Fixed number of rows
    private final int COLS = 5; // Fixed word length
    private JTextField[][] grid;
    private JPanel gridPanel; // Add a panel reference to replace it
    private JFrame frame; // Add a JFrame reference to control it better
    private int currentRow = 0;
    private int currentCol = 0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WordGuessGame::new);
    }

    public WordGuessGame() {
        frame = createAndShowGUI(); // Initialize frame
        startNewGame(); // Start the first game
    }

    private void startNewGame() {
        correctWord = fetchRandomWord().toUpperCase(); // Fetch random word and convert to uppercase
        grid = new JTextField[ROWS][COLS];
        initializeGrid();
        clearGrid();
        currentRow = 0;
        currentCol = 0;
        updateGridPanel(); // Update the grid in the UI
    }

    private String fetchRandomWord() {
        try {
            URL url = new URL("https://random-word-api.herokuapp.com/word?number=1&length=5");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder jsonResponse = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                jsonResponse.append(inputLine);
            }
            in.close();

            // Parse the JSON response
            String word = jsonResponse.toString();
            word = word.replaceAll("[\\[\\]\"]", ""); // Remove brackets and quotes

            return word;

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to fetch a word, defaulting to APPLE.");
            return "APPLE"; // Default word in case of error
        }
    }

    private void initializeGrid() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                grid[i][j] = new JTextField();
                grid[i][j].setHorizontalAlignment(JTextField.CENTER);
                grid[i][j].setFont(new Font("Arial", Font.BOLD, 24));
                grid[i][j].setEditable(false);
            }
        }
    }

    private void clearGrid() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                grid[i][j].setText("");
                grid[i][j].setBackground(Color.WHITE);
            }
        }
    }

    private JFrame createAndShowGUI() {
        JFrame frame = new JFrame("Word Guess Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("path_to_your_image.png"); // Change path accordingly
                Image image = backgroundImage.getImage();

                int panelWidth = getWidth();
                int panelHeight = getHeight();

                double widthRatio = (double) panelWidth / image.getWidth(null);
                double heightRatio = (double) panelHeight / image.getHeight(null);
                double scaleFactor = Math.max(widthRatio, heightRatio);

                int newWidth = (int) (image.getWidth(null) * scaleFactor);
                int newHeight = (int) (image.getHeight(null) * scaleFactor);

                int x = (panelWidth - newWidth) / 2;
                int y = (panelHeight - newHeight) / 2;

                g.drawImage(image, x, y, newWidth, newHeight, this);
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        gridPanel = new JPanel(); // Initialize gridPanel here
        gridPanel.setLayout(new GridLayout(ROWS, COLS, 10, 10)); // Create grid layout
        gridPanel.setOpaque(false); // Transparent grid
        gridPanel.setBorder(BorderFactory.createEmptyBorder(80, 80, 80, 80)); // Add padding

        backgroundPanel.add(gridPanel, BorderLayout.CENTER);
        frame.add(backgroundPanel);
        frame.setVisible(true);

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char keyChar = e.getKeyChar();
                if (Character.isLetter(keyChar)) {
                    processLetterInput(Character.toString(Character.toUpperCase(keyChar)));
                } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    processBackspace();
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    processEnter();
                }
            }
        });

        frame.setFocusable(true);
        frame.requestFocusInWindow();

        return frame; // Return the frame
    }

    private void updateGridPanel() {
        gridPanel.removeAll(); // Clear existing grid panel
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                gridPanel.add(grid[i][j]);
            }
        }
        gridPanel.revalidate(); // Refresh the grid panel
        gridPanel.repaint(); // Repaint to reflect changes
    }

    private void processLetterInput(String letter) {
        if (currentRow < ROWS && currentCol < COLS) {
            grid[currentRow][currentCol].setText(letter);
            currentCol++;

            if (currentCol == COLS) {
                processEnter();
            }
        }
    }

    private void processBackspace() {
        if (currentCol > 0) {
            currentCol--;
            grid[currentRow][currentCol].setText("");
        }
    }

    private void processEnter() {
        if (currentCol == COLS) {
            StringBuilder guess = new StringBuilder();
            for (int i = 0; i < COLS; i++) {
                guess.append(grid[currentRow][i].getText());
            }
            checkGuess(guess.toString());
        }
    }

    private void checkGuess(String guess) {
        if (guess.length() != COLS) {
            JOptionPane.showMessageDialog(null, "Please fill all columns before submitting.");
            return;
        }

        for (int i = 0; i < COLS; i++) {
            String letter = String.valueOf(guess.charAt(i));
            if (letter.equals(String.valueOf(correctWord.charAt(i)))) {
                grid[currentRow][i].setBackground(Color.GREEN);
            } else if (correctWord.contains(letter)) {
                grid[currentRow][i].setBackground(Color.YELLOW);
            } else {
                grid[currentRow][i].setBackground(Color.LIGHT_GRAY);
            }
        }

        if (guess.equals(correctWord)) {
            showEndGameOptions(true);
        } else {
            currentRow++;
            currentCol = 0;

            if (currentRow >= ROWS) {
                showEndGameOptions(false);
            }
        }
    }

    private void showEndGameOptions(boolean won) {
        String message = won ? "You won! What would you like to do next?" : "Game Over! The correct word was: " + correctWord + ". What would you like to do next?";
        String[] options = {"Play Again", "Main Menu"};
        int choice = JOptionPane.showOptionDialog(null, message, "Game Over",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == JOptionPane.YES_OPTION) {
            startNewGame(); // Restart the game
        } else {
            goToMainMenu(); // Transition to WordWizzGUI.java
        }
    }

    private void goToMainMenu() {
        // Close the current game window
        frame.dispose(); // Close the game window directly
        WordWizzGUI.main(new String[0]); // Assuming WordWizzGUI has a main method
    }
}
