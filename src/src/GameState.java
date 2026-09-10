package src;

// GameState.java
// Manages the game's logic: word queue, scoring, timer, win/loss detection.
// Separated from the UI so the logic is independent of how it is displayed.
//
// Skip behaviour: the skipped word is pushed to the END of the queue so the
// player will encounter it again later. It is NOT removed from the game.
// Win condition: the player must correctly guess EVERY word in the category.
import java.util.ArrayList;
import java.util.Random;

public class GameState {

    public static final int START_TIME = 60; // starting seconds
    public static final int BONUS_TIME = 1; // seconds added on correct guess
    public static final int SKIP_PENALTY = 2; // seconds deducted on skip

    // Current game data
    public int categoryIndex;
    public String categoryName;
    public int totalWords;        // how many unique words in the category

    // Queue of remaining words (ArrayList used as a simple queue).
    // Skipped words are added back to the END of this list.
    // When the list is empty, the player has guessed every word.
    private ArrayList<String[]> wordQueue;

    public String correctAnswer; // the unscrambled word currently being shown
    public String scrambledWord; // the jumbled version shown to the player
    public String currentHint;  // hint for the current word

    // Scoring and time
    public int score;
    public int timeLeft;
    public boolean gameRunning;
    public boolean playerWon;   // true only when score == totalWords

    // ------------------------------------------------------------------
    // Set up a fresh game for the given category index
    // ------------------------------------------------------------------
    public void startGame(int catIndex) {
        categoryIndex = catIndex;
        categoryName = WordBank.CATEGORY_NAMES[catIndex];
        score = 0;
        timeLeft = START_TIME;
        gameRunning = true;
        playerWon = false;

        // Build a shuffled queue from the word bank
        String[][] original = WordBank.getWords(catIndex);
        totalWords = original.length;

        wordQueue = new ArrayList<String[]>();
        for (int i = 0; i < original.length; i++) {
            wordQueue.add(original[i].clone());
        }
        shuffleQueue();

        loadCurrentWord();
    }

    // ------------------------------------------------------------------
    // Load the word at the front of the queue into display fields
    // ------------------------------------------------------------------
    private void loadCurrentWord() {
        String[] entry = wordQueue.get(0);
        correctAnswer = entry[0];
        currentHint = entry[1];
        scrambledWord = scramble(correctAnswer);
    }

    // ------------------------------------------------------------------
    // Called when the player submits an answer.
    // Returns true if correct, false if wrong.
    // ------------------------------------------------------------------
    public boolean submitAnswer(String typed) {
        if (!gameRunning) {
            return false;
        }
        if (typed == null || typed.trim().isEmpty()) {
            return false;
        }

        if (typed.trim().toUpperCase().equals(correctAnswer)) {
            score++;
            timeLeft += BONUS_TIME;

            // Remove the correctly guessed word from the queue
            wordQueue.remove(0);

            // Win: queue is empty = every word was guessed correctly
            if (wordQueue.isEmpty()) {
                playerWon = true;
                gameRunning = false;
            } else {
                loadCurrentWord();
            }
            return true;
        }
        return false;
    }

    // ------------------------------------------------------------------
    // Called when the player clicks Skip.
    // Deducts 2 seconds and moves the current word to the END of the
    // queue so the player will see it again later.
    // ------------------------------------------------------------------
    public void skipWord() {
        if (!gameRunning) {
            return;
        }

        timeLeft -= SKIP_PENALTY;
        if (timeLeft < 0) {
            timeLeft = 0;
        }

        // Move the skipped word to the back of the queue
        String[] skipped = wordQueue.remove(0);
        wordQueue.add(skipped);

        // Load the next word (which is now at the front)
        loadCurrentWord();
    }

    // ------------------------------------------------------------------
    // Called every second by the UI timer.
    // Returns true if time just ran out (so the UI can react immediately).
    // ------------------------------------------------------------------
    public boolean tickTimer() {
        if (!gameRunning) {
            return false;
        }
        timeLeft--;
        if (timeLeft <= 0) {
            timeLeft = 0;
            gameRunning = false;
            playerWon = false;
            return true;
        }
        return false;
    }

    // ------------------------------------------------------------------
    // How many words are still left to guess (including current one)
    // ------------------------------------------------------------------
    public int getRemainingCount() {
        return wordQueue != null ? wordQueue.size() : 0;
    }

    // ------------------------------------------------------------------
    // Progress shown in UI: "Guessed X / totalWords"
    // ------------------------------------------------------------------
    public int getTotalWords() {
        return totalWords;
    }

    // ------------------------------------------------------------------
    // Scramble: Fisher-Yates shuffle on the characters of a word.
    // Retries if the result happens to equal the original word.
    // ------------------------------------------------------------------
    private String scramble(String word) {
        char[] chars = word.toCharArray();
        Random rand = new Random();
        int attempts = 0;

        do {
            for (int i = chars.length - 1; i > 0; i--) {
                int j = rand.nextInt(i + 1);
                char tmp = chars[i];
                chars[i] = chars[j];
                chars[j] = tmp;
            }
            attempts++;
        } while (new String(chars).equals(word) && attempts < 30);

        return new String(chars);
    }

    // ------------------------------------------------------------------
    // Shuffle the word queue so order is random each game
    // ------------------------------------------------------------------
    private void shuffleQueue() {
        Random rand = new Random();
        for (int i = wordQueue.size() - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            String[] tmp = wordQueue.get(i);
            wordQueue.set(i, wordQueue.get(j));
            wordQueue.set(j, tmp);
        }
    }
}
