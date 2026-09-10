package src;

// ResultPanel.java
// Displayed when the game ends — either time ran out or the player guessed all words.
// Shows the final score, win/loss status, and navigation buttons.
import javax.swing.*;
import java.awt.*;

public class ResultPanel extends JPanel {

    private JLabel statusLabel;
    private JLabel finalScoreLabel;
    private JLabel subtextLabel;

    private Runnable onPlayAgain;
    private Runnable onMainMenu;

    // ------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------
    public ResultPanel(Runnable onPlayAgain, Runnable onMainMenu) {
        this.onPlayAgain = onPlayAgain;
        this.onMainMenu = onMainMenu;
        setBackground(Theme.BG_BASE);
        setLayout(new GridBagLayout());
        buildUI();
    }

    // ------------------------------------------------------------------
    // Build UI — everything centered in a card
    // ------------------------------------------------------------------
    private void buildUI() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Theme.BG_PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                BorderFactory.createEmptyBorder(36, 56, 36, 56)
        ));

        statusLabel = new JLabel("Game Over");
        statusLabel.setFont(Theme.FONT_TITLE);
        statusLabel.setForeground(Theme.TEXT_PRIMARY);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel scoreTitle = new JLabel("Words Guessed");
        scoreTitle.setFont(Theme.FONT_BODY);
        scoreTitle.setForeground(Theme.TEXT_SECONDARY);
        scoreTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        finalScoreLabel = new JLabel("0");
        finalScoreLabel.setFont(Theme.FONT_SCORE_BIG);
        finalScoreLabel.setForeground(Theme.ACCENT);
        finalScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        subtextLabel = new JLabel(" ");
        subtextLabel.setFont(Theme.FONT_SMALL);
        subtextLabel.setForeground(Theme.TEXT_SECONDARY);
        subtextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(300, 1));
        sep.setForeground(Theme.BORDER);

        JButton playAgainBtn = makePrimaryButton("Play Again");
        playAgainBtn.addActionListener(e -> onPlayAgain.run());

        JButton menuBtn = makeSecondaryButton("Main Menu");
        menuBtn.addActionListener(e -> onMainMenu.run());

        card.add(statusLabel);
        card.add(Box.createVerticalStrut(20));
        card.add(scoreTitle);
        card.add(Box.createVerticalStrut(4));
        card.add(finalScoreLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(subtextLabel);
        card.add(Box.createVerticalStrut(24));
        card.add(sep);
        card.add(Box.createVerticalStrut(20));
        card.add(playAgainBtn);
        card.add(Box.createVerticalStrut(10));
        card.add(menuBtn);

        add(card);
    }

    // ------------------------------------------------------------------
    // Called before showing this panel — update labels from GameState
    // ------------------------------------------------------------------
    public void refresh(GameState state) {
        if (state.playerWon) {
            // playerWon is only true when score == totalWords (every word guessed correctly)
            statusLabel.setText("You Win!");
            statusLabel.setForeground(Theme.SUCCESS);
            subtextLabel.setText("All " + state.getTotalWords()
                    + " words guessed!  " + state.timeLeft + "s remaining.");
        } else {
            statusLabel.setText("Game Over");
            statusLabel.setForeground(Theme.TEXT_PRIMARY);
            subtextLabel.setText("Guessed " + state.score
                    + " out of " + state.getTotalWords() + " words.");
        }
        finalScoreLabel.setText(String.valueOf(state.score));
    }

    // ------------------------------------------------------------------
    // Factory: primary button
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
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 38));
        btn.setPreferredSize(new Dimension(200, 38));
        return btn;
    }

    // ------------------------------------------------------------------
    // Factory: secondary button
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
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 38));
        btn.setPreferredSize(new Dimension(200, 38));
        return btn;
    }
}
