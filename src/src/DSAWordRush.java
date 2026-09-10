package src;


// DSAWordRush.java
// Main class and entry point. Owns the JFrame and manages screen transitions
// between MenuPanel, GamePanel, and ResultPanel using CardLayout.

import javax.swing.*;
import java.awt.*;

public class DSAWordRush extends JFrame {

    private CardLayout  cardLayout;
    private JPanel      mainPanel;

    private MenuPanel   menuPanel;
    private GamePanel   gamePanel;
    private ResultPanel resultPanel;

    private GameState   gameState;

    // ------------------------------------------------------------------
    // Constructor — builds window, panels, wires callbacks
    // ------------------------------------------------------------------
    public DSAWordRush() {
        gameState = new GameState();

        setTitle("DSA Word Rush");
        setSize(700, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);
        mainPanel.setBackground(Theme.BG_BASE);

        // Wire up panels with callbacks
        menuPanel   = new MenuPanel(this::startGame);
        gamePanel   = new GamePanel(gameState, this::endGame);
        resultPanel = new ResultPanel(this::playAgain, this::goToMenu);

        mainPanel.add(menuPanel,   "MENU");
        mainPanel.add(gamePanel,   "GAME");
        mainPanel.add(resultPanel, "RESULT");

        add(mainPanel);
        cardLayout.show(mainPanel, "MENU");
        setVisible(true);
    }

    // ------------------------------------------------------------------
    // Start game: take category from menu, init state, show game screen
    // ------------------------------------------------------------------
    private void startGame() {
        int catIndex = menuPanel.getSelectedIndex();
        gameState.startGame(catIndex);
        gamePanel.refresh();
        cardLayout.show(mainPanel, "GAME");
    }

    // ------------------------------------------------------------------
    // Game ended (timer expired or player won): show result screen
    // ------------------------------------------------------------------
    private void endGame() {
        gamePanel.stopCountdown();
        resultPanel.refresh(gameState);
        cardLayout.show(mainPanel, "RESULT");
    }

    // ------------------------------------------------------------------
    // Play again: reuse same category, restart without going to menu
    // ------------------------------------------------------------------
    private void playAgain() {
        gameState.startGame(gameState.categoryIndex);
        gamePanel.refresh();
        cardLayout.show(mainPanel, "GAME");
    }

    // ------------------------------------------------------------------
    // Main menu: reset menu selection and show menu screen
    // ------------------------------------------------------------------
    private void goToMenu() {
        menuPanel.resetSelection();
        cardLayout.show(mainPanel, "MENU");
    }

    // ------------------------------------------------------------------
    // Entry point
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DSAWordRush();
            }
        });
    }
}
