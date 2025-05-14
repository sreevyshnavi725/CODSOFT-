import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class NumberGuessingGameGUI extends JFrame {

    private int MAX_ATTEMPTS = 7;
    private final int LOWER_BOUND = 1;
    private final int UPPER_BOUND = 100;

    private int targetNumber;
    private int attempts;
    private int roundsPlayed = 0;
    private int roundsWon = 0;

    private JLabel instructionLabel, feedbackLabel, attemptsLabel, scoreLabel, timerLabel;
    private JTextField guessField;
    private JButton guessButton, playAgainButton, resetButton, exitButton;
    private JTextArea logArea;
    private JComboBox<String> difficultyBox;
    private Timer countdownTimer;
    private int timeLeft = 60; // seconds

    public NumberGuessingGameGUI() {
        setTitle("Number Guessing Game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top Panel - Instructions and Difficulty
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        instructionLabel = new JLabel("Guess a number between " + LOWER_BOUND + " and " + UPPER_BOUND + ":", SwingConstants.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(instructionLabel);

        String[] difficulties = {"Easy (10 attempts)", "Medium (7 attempts)", "Hard (5 attempts)"};
        difficultyBox = new JComboBox<>(difficulties);
        difficultyBox.setSelectedIndex(1); // Medium
        difficultyBox.setFont(new Font("Arial", Font.PLAIN, 14));
        difficultyBox.addActionListener(e -> {
            switch (difficultyBox.getSelectedIndex()) {
                case 0 -> MAX_ATTEMPTS = 10;
                case 1 -> MAX_ATTEMPTS = 7;
                case 2 -> MAX_ATTEMPTS = 5;
            }
            resetGame();
        });
        JPanel diffPanel = new JPanel();
        diffPanel.add(new JLabel("Select Difficulty:"));
        diffPanel.add(difficultyBox);
        topPanel.add(diffPanel);

        add(topPanel, BorderLayout.NORTH);

        // Center Panel - Main Game Area
        JPanel centerPanel = new JPanel(new GridLayout(6, 1, 10, 10));

        guessField = new JTextField();
        guessField.setFont(new Font("Arial", Font.PLAIN, 18));
        guessButton = new JButton("Submit Guess");
        guessButton.setFont(new Font("Arial", Font.BOLD, 16));

        feedbackLabel = new JLabel("You have " + MAX_ATTEMPTS + " attempts.", SwingConstants.CENTER);
        attemptsLabel = new JLabel("Attempts used: 0", SwingConstants.CENTER);
        scoreLabel = new JLabel("Score: 0 win(s) out of 0 round(s)", SwingConstants.CENTER);
        timerLabel = new JLabel("Time left: 60s", SwingConstants.CENTER);

        centerPanel.add(guessField);
        centerPanel.add(guessButton);
        centerPanel.add(feedbackLabel);
        centerPanel.add(attemptsLabel);
        centerPanel.add(scoreLabel);
        centerPanel.add(timerLabel);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom Panel - Controls and Logs
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));

        // Control buttons
        JPanel buttonPanel = new JPanel();
        playAgainButton = new JButton("Play Again");
        playAgainButton.setEnabled(false);
        resetButton = new JButton("Reset Game");
        exitButton = new JButton("Exit");

        buttonPanel.add(playAgainButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(exitButton);
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);

        // Game Log
        logArea = new JTextArea(8, 50);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        // Action listeners
        guessButton.addActionListener(new GuessButtonListener());
        playAgainButton.addActionListener(e -> resetGame());
        resetButton.addActionListener(e -> {
            roundsWon = 0;
            roundsPlayed = 0;
            resetGame();
        });
        exitButton.addActionListener(e -> System.exit(0));

        // Start the game
        startNewRound();
    }

    private void startNewRound() {
        Random random = new Random();
        targetNumber = random.nextInt(UPPER_BOUND - LOWER_BOUND + 1) + LOWER_BOUND;
        attempts = 0;
        timeLeft = 60;

        guessField.setText("");
        guessField.setEnabled(true);
        guessButton.setEnabled(true);
        playAgainButton.setEnabled(false);

        feedbackLabel.setText("You have " + MAX_ATTEMPTS + " attempts.");
        attemptsLabel.setText("Attempts used: 0");
        instructionLabel.setText("Guess a number between " + LOWER_BOUND + " and " + UPPER_BOUND + ":");
        timerLabel.setText("Time left: 60s");
        logArea.setText("New round started. Good luck!\n");

        if (countdownTimer != null && countdownTimer.isRunning()) {
            countdownTimer.stop();
        }

        countdownTimer = new Timer(1000, e -> {
            timeLeft--;
            timerLabel.setText("Time left: " + timeLeft + "s");
            if (timeLeft <= 0) {
                countdownTimer.stop();
                feedbackLabel.setText("Time's up! The number was: " + targetNumber);
                logArea.append("Time ran out. You lost this round.\n");
                endRound(false);
            }
        });
        countdownTimer.start();
    }

    private void resetGame() {
        startNewRound();
        scoreLabel.setText("Score: " + roundsWon + " win(s) out of " + roundsPlayed + " round(s)");
    }

    private class GuessButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String input = guessField.getText().trim();

            if (input.isEmpty()) {
                feedbackLabel.setText("Please enter a number.");
                return;
            }

            int guess;
            try {
                guess = Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                feedbackLabel.setText("Invalid input. Enter a number.");
                return;
            }

            attempts++;
            attemptsLabel.setText("Attempts used: " + attempts);
            logArea.append("Attempt " + attempts + ": " + guess + "\n");

            if (guess == targetNumber) {
                feedbackLabel.setText("Congratulations! You guessed it!");
                logArea.append("Correct guess! You won this round.\n");
                roundsWon++;
                endRound(true);
            } else if (guess < targetNumber) {
                feedbackLabel.setText("Too low!");
                logArea.append("Too low.\n");
            } else {
                feedbackLabel.setText("Too high!");
                logArea.append("Too high.\n");
            }

            if (attempts >= MAX_ATTEMPTS && guess != targetNumber) {
                feedbackLabel.setText("Out of attempts! The number was: " + targetNumber);
                logArea.append("You lost this round.\n");
                endRound(false);
            }
        }
    }

    private void endRound(boolean won) {
        roundsPlayed++;
        guessField.setEnabled(false);
        guessButton.setEnabled(false);
        playAgainButton.setEnabled(true);
        countdownTimer.stop();
        scoreLabel.setText("Score: " + roundsWon + " win(s) out of " + roundsPlayed + " round(s)");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            NumberGuessingGameGUI game = new NumberGuessingGameGUI();
            game.setVisible(true);
        });
    }
}
