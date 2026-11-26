package se.imperialpg.breakout.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 * Class for rendering and selection game state options
 * call update and render each frame
 */
public final class Menu implements InputReceiver {
    private static final int SELECTION_SPACING = 50;

    public enum Selection {
        PLAY,
        HIGHSCORE,
        QUIT
    }

    private Selection selected = Selection.PLAY;
    private Selection pendingSelection;

    //sent from Game

    /**
     * recieve input from game class, move selection or set new pending selection
     * @param event keyevent to process
     */
    @Override
    public void receiveInput(KeyEvent event) {
        if (event.getID() != KeyEvent.KEY_PRESSED) {
            return;
        }

        switch (event.getKeyCode()) {
            case KeyEvent.VK_UP, KeyEvent.VK_W -> moveSelection(-1);
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> moveSelection(1);
            case KeyEvent.VK_ENTER, KeyEvent.VK_SPACE -> pendingSelection = selected;
        }
    }

    /**
     * move the current selection up or down the options menu
     * @param direction the direction to move in
     */
    private void moveSelection(int direction) {
        Selection[] options = Selection.values();
        int selectedIndex = (selected.ordinal() + direction + options.length) % options.length;
        selected = options[selectedIndex];
    }

    /**
     * consumes the last selection and returns
     * @return last selected
     */
    public Selection consumeSelection() {
        Selection selection = pendingSelection;
        pendingSelection = null;
        return selection;
    }

    /**
     * renders title and option selections
     * all options render in the middle of the screen based on text length and supplied screen width
     * @param graphics renderer to use
     * @param width width of window
     * @param height height of window
     */
    public void render(Graphics2D graphics, int width, int height) {
        //title
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 48));
        String title = "BREAKOUT";
        FontMetrics titleMetrics = graphics.getFontMetrics();
        graphics.drawString(title, (width - titleMetrics.stringWidth(title)) / 2, height / 3);

        //options
        graphics.setFont(new Font("Arial", Font.BOLD, 28));
        FontMetrics optionMetrics = graphics.getFontMetrics();
        Selection[] options = Selection.values();
        for (int i = 0; i < options.length; i++) {
            renderOption(graphics, optionMetrics, options[i].name(), options[i], width,
                    height / 2 + i * SELECTION_SPACING);
        }
    }

    /**
     *
     * @param graphics renderer to use
     * @param metrics font settings for the option
     * @param text the text to render
     * @param option selection enum
     * @param width how much screen width pixels does the string take up
     * @param y the y coordinate to render at
     */
    private void renderOption(Graphics2D graphics, FontMetrics metrics, String text,
                              Selection option, int width, int y) {
        boolean isSelected = selected == option;

        graphics.setColor(isSelected ? Color.YELLOW : Color.WHITE);
        String label = (isSelected ? "-> " : "  ") + text;
        graphics.drawString(label, (width - metrics.stringWidth(label)) / 2, y);
    }
}
