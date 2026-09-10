package src;


// GamePanel.java
// The main gameplay screen: shows the scrambled word, hint, input, and controls.
// Receives a GameState reference and a callback for when the game ends.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel {

    private GameState state;
    private Runnable  onGameEnd;   // called when time runs out or player wins

    // Top bar
    private JLabel categoryLabel;
    private JLabel progressLabel;
    private JLabel timerLabel;

    // Center
    private JLabel     scrambledLabel;
    private JLabel     hintLabel;
    private JTextField answerField;
    private JLabel     feedbackLabel;

    // Bottom
    private JButton submitButton;
    private JButton skipButton;

    // Timer
    private javax.swing.Timer countdownTimer;

    // ------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------
    public GamePanel(GameState state, Runnable onGameEnd) {
        this.state     = state;
        this.onGameEnd = onGameEnd;
        setBackground(Theme.BG_BASE);
        setLayout(new BorderLayout(0, 0));
        buildUI();
    }

    // ------------------------------------------------------------------
    // Build all UI elements
    // ------------------------------------------------------------------
    private void buildUI() {
        // ── Top bar ──────────────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout(14, 0));
        topBar.setBackground(Theme.BG_PANEL);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)
        ));

        categoryLabel = new JLabel("Category");
        categoryLabel.setFont(Theme.FONT_LABEL);
        categoryLabel.setForeground(Theme.ACCENT);

        progressLabel = new JLabel("0 / 0");
        progressLabel.setFont(Theme.FONT_BODY);
        progressLabel.setForeground(Theme.TEXT_SECONDARY);
        progressLabel.setHorizontalAlignment(SwingConstants.CENTER);

        timerLabel = new JLabel("60s");
        timerLabel.setFont(Theme.FONT_TIMER);
        timerLabel.setForeground(Theme.TIMER_OK);
        timerLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        topBar.add(categoryLabel,  BorderLayout.WEST);
        topBar.add(progressLabel,  BorderLayout.CENTER);
        topBar.add(timerLabel,     BorderLayout.EAST);

        // ── Center card ───────────────────────────────────────────────
        JPanel centerCard = new JPanel();
        centerCard.setLayout(new BoxLayout(centerCard, BoxLayout.Y_AXIS));
        centerCard.setBackground(Theme.BG_BASE);
        centerCard.setBorder(BorderFactory.createEmptyBorder(30, 60, 20, 60));

        JLabel promptLabel = new JLabel("Unscramble this word:");
        promptLabel.setFont(Theme.FONT_BODY);
        promptLabel.setForeground(Theme.TEXT_SECONDARY);
        promptLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        scrambledLabel = new JLabel("--------");
        scrambledLabel.setFont(Theme.FONT_SCRAMBLE);
        scrambledLabel.setForeground(Theme.TEXT_PRIMARY);
        scrambledLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        hintLabel = new JLabel("Hint: ");
        hintLabel.setFont(Theme.FONT_HINT);
        hintLabel.setForeground(Theme.TEXT_SECONDARY);
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Answer input
        answerField = new JTextField(14);
        answerField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        answerField.setForeground(Theme.TEXT_PRIMARY);
        answerField.setBackground(Theme.BG_CARD);
        answerField.setCaretColor(Theme.ACCENT);
        answerField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        answerField.setHorizontalAlignment(JTextField.CENTER);
        answerField.setMaximumSize(new Dimension(300, 44));
        answerField.addActionListener(e -> handleSubmit());

        feedbackLabel = new JLabel(" ");
        feedbackLabel.setFont(Theme.FONT_BODY);
        feedbackLabel.setForeground(Theme.TEXT_SECONDARY);
        feedbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerCard.add(promptLabel);
        centerCard.add(Box.createVerticalStrut(12));
        centerCard.add(scrambledLabel);
        centerCard.add(Box.createVerticalStrut(6));
        centerCard.add(hintLabel);
        centerCard.add(Box.createVerticalStrut(20));
        centerCard.add(answerField);
        centerCard.add(Box.createVerticalStrut(10));
        centerCard.add(feedbackLabel);

        // ── Bottom bar ────────────────────────────────────────────────
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        bottomBar.setBackground(Theme.BG_PANEL);
        bottomBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER),
            BorderFactory.createEmptyBorder(14, 0, 14, 0)
        ));

        submitButton = makePrimaryButton("Submit");
        submitButton.addActionListener(e -> handleSubmit());

        skipButton = makeSecondaryButton("Skip  (-2s)");
        skipButton.addActionListener(e -> handleSkip());

        bottomBar.add(submitButton);
        bottomBar.add(skipButton);

        add(topBar,    BorderLayout.NORTH);
        add(centerCard, BorderLayout.CENTER);
        add(bottomBar, BorderLayout.SOUTH);
    }

    // ------------------------------------------------------------------
    // Called when this screen becomes visible — refresh UI from state
    // ------------------------------------------------------------------
    public void refresh() {
        categoryLabel.setText(state.categoryName);
        updateProgress();
        updateTimer();
        scrambledLabel.setText(state.scrambledWord);
        hintLabel.setText("Hint: " + state.currentHint);
        answerField.setText("");
        feedbackLabel.setText(" ");
        feedbackLabel.setForeground(Theme.TEXT_SECONDARY);
        answerField.requestFocusInWindow();
        startCountdown();
    }

    // ------------------------------------------------------------------
    // Countdown timer tick
    // ------------------------------------------------------------------
    private void startCountdown() {
        if (countdownTimer != null) countdownTimer.stop();

        countdownTimer = new javax.swing.Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean ranOut = state.tickTimer();
                updateTimer();
                if (ranOut || !state.gameRunning) {
                    countdownTimer.stop();
                    onGameEnd.run();
                }
            }
        });
        countdownTimer.start();
    }

    // ------------------------------------------------------------------
    // Stop timer externally (e.g. when player wins mid-round)
    // ------------------------------------------------------------------
    public void stopCountdown() {
        if (countdownTimer != null) countdownTimer.stop();
    }

    // ------------------------------------------------------------------
    // Handle Submit button / Enter key
    // ------------------------------------------------------------------
    private void handleSubmit() {
        if (!state.gameRunning) return;
        String typed = answerField.getText();
        boolean correct = state.submitAnswer(typed);

        if (correct) {
            feedbackLabel.setText("Correct!  +1s added.");
            feedbackLabel.setForeground(Theme.SUCCESS);
            updateProgress();
            updateTimer();

            if (!state.gameRunning) {
                // Player won — guessed every word
                countdownTimer.stop();
                onGameEnd.run();
            } else {
                scrambledLabel.setText(state.scrambledWord);
                hintLabel.setText("Hint: " + state.currentHint);
                answerField.setText("");
            }
        } else {
            feedbackLabel.setText("Wrong answer. Try again!");
            feedbackLabel.setForeground(Theme.ERROR);
            answerField.setText("");
        }
    }

    // ------------------------------------------------------------------
    // Handle Skip button
    // ------------------------------------------------------------------
    private void handleSkip() {
        if (!state.gameRunning) return;
        state.skipWord();
        updateTimer();
        updateProgress();

        scrambledLabel.setText(state.scrambledWord);
        hintLabel.setText("Hint: " + state.currentHint);
        answerField.setText("");
        feedbackLabel.setText("Skipped.  -2s  (word moved to end)");
        feedbackLabel.setForeground(Theme.NEUTRAL);
    }

    // ------------------------------------------------------------------
    // Update timer label text and color
    // ------------------------------------------------------------------
    private void updateTimer() {
        timerLabel.setText(state.timeLeft + "s");
        if (state.timeLeft <= 10) {
            timerLabel.setForeground(Theme.TIMER_CRIT);
        } else if (state.timeLeft <= 20) {
            timerLabel.setForeground(Theme.TIMER_WARN);
        } else {
            timerLabel.setForeground(Theme.TIMER_OK);
        }
    }

    // ------------------------------------------------------------------
    // Update progress label  e.g.  "3 / 10"
    // ------------------------------------------------------------------
    private void updateProgress() {
        // Show how many have been correctly guessed out of total
        progressLabel.setText(state.score + " / " + state.getTotalWords() + " guessed");
    }

    // ------------------------------------------------------------------
    // Factory: primary (accent) button
    // ------------------------------------------------------------------
    private JButton makePrimaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(Theme.ACCENT.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(Theme.ACCENT_HOVER);
                } else {
                    g2.setColor(Theme.ACCENT);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        btn.setFont(Theme.FONT_BUTTON);
        btn.setForeground(Theme.BG_BASE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 36));
        return btn;
    }

    // ------------------------------------------------------------------
    // Factory: secondary (muted) button
    // ------------------------------------------------------------------
    private JButton makeSecondaryButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(Theme.BG_CARD.brighter());
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(50, 50, 62));
                } else {
                    g2.setColor(Theme.BG_CARD);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Theme.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                super.paintComponent(g);
            }
        };
        btn.setFont(Theme.FONT_BUTTON);
        btn.setForeground(Theme.TEXT_SECONDARY);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 36));
        return btn;
    }
}