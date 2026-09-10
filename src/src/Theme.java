package src;


// Theme.java
// Centralized colors and fonts used across all screens.
// Change values here to restyle the whole game at once.

import java.awt.*;

public class Theme {

    // Background shades
    public static final Color BG_BASE    = new Color(18, 18, 22);   // darkest - window bg
    public static final Color BG_PANEL   = new Color(26, 26, 32);   // panel bg
    public static final Color BG_CARD    = new Color(34, 34, 42);   // card / input bg

    // Borders and separators
    public static final Color BORDER     = new Color(50, 50, 62);

    // Text
    public static final Color TEXT_PRIMARY   = new Color(220, 220, 230);
    public static final Color TEXT_SECONDARY = new Color(130, 130, 150);

    // Accent colors
    public static final Color ACCENT       = new Color(99, 179, 237);   // blue - primary action
    public static final Color ACCENT_HOVER = new Color(125, 196, 245);

    // Feedback
    public static final Color SUCCESS = new Color(72, 199, 142);    // green
    public static final Color ERROR   = new Color(235, 87,  87);    // red
    public static final Color WARNING = new Color(235, 175, 60);    // orange/yellow
    public static final Color NEUTRAL = new Color(130, 130, 150);   // gray (skip)

    // Timer colors
    public static final Color TIMER_OK   = new Color(99, 179, 237);
    public static final Color TIMER_WARN = new Color(235, 175, 60);
    public static final Color TIMER_CRIT = new Color(235, 87,  87);

    // Fonts
    public static final Font FONT_TITLE    = new Font("SansSerif", Font.BOLD,  28);
    public static final Font FONT_SUBTITLE = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_LABEL    = new Font("SansSerif", Font.BOLD,  13);
    public static final Font FONT_BODY     = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_SCRAMBLE = new Font("Monospaced", Font.BOLD, 44);
    public static final Font FONT_SCORE_BIG= new Font("SansSerif", Font.BOLD,  60);
    public static final Font FONT_BUTTON   = new Font("SansSerif", Font.BOLD,  13);
    public static final Font FONT_HINT     = new Font("SansSerif", Font.ITALIC, 13);
    public static final Font FONT_TIMER    = new Font("SansSerif", Font.BOLD,  15);
    public static final Font FONT_SMALL    = new Font("SansSerif", Font.PLAIN, 12);
}