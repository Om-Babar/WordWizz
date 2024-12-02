import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class FruitGrid {
    private String[] fruits;
    private String correctWord;
    private int ROWS = 5; // Number of rows
    private int COLS; // Number of columns determined by the chosen word length
    private JTextField[][] grid;
    private int currentRow = 0;
    private int currentCol = 0;
    private JFrame frame;
    private JPanel gridPanel; // Now declared at class level

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FruitGrid::new);
    }

    public FruitGrid() {
        // Initialize fruit names
        fruits = new String[]{
                "Fig", "Peach", "Grape", "Plum", "Mango", "Apple", "Banana",
                "Berry", "Kiwi", "Lemon", "Date", "Olive", "Guava",
                "Citrus", "Cherry", "Melon", "Lime", "Prune", "Navel",
                "Papaya", "Kumquat", "Lychee", "Starfruit", "Raisin",
                "Coconut", "Apricot", "Tangerine", "Dragon", "Passion",
                "Pine", "Black", "Prickly", "Cranberry", "Elder",
                "Quince", "Jackfruit", "Sapote", "Blood", "Persimmon",
                "Calamondin", "Rambutan", "Mulberry", "Plantain", "Atemoya",
                "Salak", "Pawpaw", "Longan", "Pitaya", "Soursop"
        };

        // Initialize the JFrame and GUI components
        createAndShowGUI();
        // Start a new game after initializing GUI
        startNewGame();
    }

    private void startNewGame() {
        Random random = new Random();
        correctWord = fruits[random.nextInt(fruits.length)].toUpperCase(); // Select a random fruit and convert to uppercase
        COLS = correctWord.length();

        // Initialize grid for the new word length
        grid = new JTextField[ROWS][COLS];
        initializeGrid();
        clearGrid();

        currentRow = 0;
        currentCol = 0;

        // Update gridPanel with new layout
        updateGridPanel();
    }

    private void initializeGrid() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                grid[i][j] = new JTextField();
                grid[i][j].setHorizontalAlignment(JTextField.CENTER);
                grid[i][j].setFont(new Font("Roboto Slab", Font.BOLD, 24));
                grid[i][j].setEditable(false); // Disable direct editing of the fields
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

    private void createAndShowGUI() {
        frame = new JFrame("Fruit Grid");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600); // Set frame size

        // Create a custom JPanel to display the background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("G:\\WordWizz project\\3.png"); // Set your fruit background image path
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

        // Initialize gridPanel with a default grid layout
        gridPanel = new JPanel();
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

        // Make sure the frame is focusable to capture key events
        frame.setFocusable(true);
        frame.requestFocusInWindow();
    }

    private void updateGridPanel() {
        gridPanel.removeAll(); // Clear previous components
        gridPanel.setLayout(new GridLayout(ROWS, COLS, 10, 10)); // Set new layout based on the word length

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                gridPanel.add(grid[i][j]);
            }
        }

        gridPanel.revalidate(); // Refresh panel layout
        gridPanel.repaint(); // Repaint the panel to show updated grid
    }

    private void processLetterInput(String letter) {
        if (currentRow < ROWS && currentCol < COLS) {
            grid[currentRow][currentCol].setText(letter);
            currentCol++;

            // If the current row is filled, move to the next row
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
            showEndGamePopup("Congratulations! You've guessed the fruit!");
        } else {
            currentRow++;
            currentCol = 0;

            if (currentRow >= ROWS) {
                showEndGamePopup("Game Over! The correct fruit was: " + correctWord);
            }
        }
    }

    private void showEndGamePopup(String message) {
        Object[] options = {"Play Again", "Main Menu"};
        int choice = JOptionPane.showOptionDialog(null, message, "Game Over",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            startNewGame();
        } else if (choice == 1) {
            frame.dispose();
            new WordWizzGUI(); // Go back to main menu
        }
    }
}
