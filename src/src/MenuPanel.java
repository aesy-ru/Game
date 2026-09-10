package src;

// MenuPanel.java
// The home/category selection screen shown at startup and when returning from results.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuPanel extends JPanel {

    private JToggleButton[] categoryButtons;
    private JLabel          errorLabel;
    private int             selectedIndex = -1;
    private Runnable        onStartGame;  // callback to DSAWordRush

    // ------------------------------------------------------------------
    // Constructor
    // ------------------------------------------------------------------
    public MenuPanel(Runnable onStartGame) {
        this.onStartGame = onStartGame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Theme.BG_BASE);
        setBorder(BorderFactory.createEmptyBorder(40, 70, 40, 70));
        buildUI();
    }

    // ------------------------------------------------------------------
    // Build all UI elements
    // ------------------------------------------------------------------
    private void buildUI() {
        // Title
        JLabel titleLabel = new JLabel("DSA Word Rush");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Unscramble DSA terms before time runs out.");
        subtitleLabel.setFont(Theme.FONT_SUBTITLE);
        subtitleLabel.setForeground(Theme.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Divider
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(400, 1));
        sep.setForeground(Theme.BORDER);
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Category label
        JLabel chooseLabel = new JLabel("Select a Category");
        chooseLabel.setFont(Theme.FONT_LABEL);
        chooseLabel.setForeground(Theme.TEXT_SECONDARY);
        chooseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Category toggle buttons
        JPanel catGrid = new JPanel(new GridLayout(2, 3, 8, 8));
        catGrid.setBackground(Theme.BG_BASE);
        catGrid.setMaximumSize(new Dimension(480, 90));
        catGrid.setAlignmentX(Component.CENTER_ALIGNMENT);

        ButtonGroup group = new ButtonGroup();
        categoryButtons = new JToggleButton[WordBank.CATEGORY_NAMES.length];

        for (int i = 0; i < WordBank.CATEGORY_NAMES.length; i++) {
            JToggleButton btn = makeToggleButton(WordBank.CATEGORY_NAMES[i]);
            final int idx = i;
            btn.addActionListener(e -> selectedIndex = idx);
            group.add(btn);
            categoryButtons[i] = btn;
            catGrid.add(btn);
        }

        // Error label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(Theme.FONT_SMALL);
        errorLabel.setForeground(Theme.ERROR);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Rules hint
        JLabel rulesLabel = new JLabel(
            "+1s on correct  |  -2s on skip  |  Guess all 10 to win"
        );
        rulesLabel.setFont(Theme.FONT_SMALL);
        rulesLabel.setForeground(Theme.TEXT_SECONDARY);
        rulesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Start button
        JButton startBtn = makePrimaryButton("Start Game");
        startBtn.addActionListener(e -> {
            if (selectedIndex == -1) {
                errorLabel.setText("Please select a category first.");
            } else {
                errorLabel.setText(" ");
                onStartGame.run();
            }
        });

        // Layout
        add(titleLabel);
        add(Box.createVerticalStrut(6));
        add(subtitleLabel);
        add(Box.createVerticalStrut(22));
        add(sep);
        add(Box.createVerticalStrut(22));
        add(chooseLabel);
        add(Box.createVerticalStrut(12));
        add(catGrid);
        add(Box.createVerticalStrut(6));
        add(errorLabel);
        add(Box.createVerticalStrut(10));
        add(rulesLabel);
        add(Box.createVerticalStrut(20));
        add(startBtn);
    }

    // ------------------------------------------------------------------
    // Returns which category the player selected
    // ------------------------------------------------------------------
    public int getSelectedIndex() {
        return selectedIndex;
    }

    // ------------------------------------------------------------------
    // Resets selection state (called when returning from results)
    // ------------------------------------------------------------------
    public void resetSelection() {
        selectedIndex = -1;
        for (JToggleButton btn : categoryButtons) {
            btn.setSelected(false);
        }
        errorLabel.setText(" ");
    }

    // ------------------------------------------------------------------
    // Factory: styled toggle button
    // ------------------------------------------------------------------
    private JToggleButton makeToggleButton(String text) {
        JToggleButton btn = new JToggleButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                if (isSelected()) {
                    g2.setColor(Theme.ACCENT);
                } else if (getModel().isRollover()) {
                    g2.setColor(Theme.BG_CARD.brighter());
                } else {
                    g2.setColor(Theme.BG_CARD);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        btn.setFont(Theme.FONT_BODY);
        btn.setForeground(Theme.TEXT_PRIMARY);
        btn.setBackground(Theme.BG_CARD);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addItemListener(e -> {
            btn.setForeground(btn.isSelected() ? Color.WHITE : Theme.TEXT_PRIMARY);
        });
        return btn;
    }

    // ------------------------------------------------------------------
    // Factory: primary action button
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
        btn.setBackground(Theme.ACCENT);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setPreferredSize(new Dimension(200, 40));
        return btn;
    }
}