package se.imperialpg.breakout.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public final class Menu implements InputReceiver {
    private static final int SELECTION_SPACING = 50;

    public enum Selection {
        PLAY,
        HIGHSCORE,
        QUIT
    }

    private Selection selected = Selection.PLAY;
    private Selection pendingSelection;

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

    private void moveSelection(int direction) {
        Selection[] options = Selection.values();
        int selectedIndex = (selected.ordinal() + direction + options.length) % options.length;
        selected = options[selectedIndex];
    }

    public Selection consumeSelection() {
        Selection selection = pendingSelection;
        pendingSelection = null;
        return selection;
    }

    public void render(Graphics2D graphics, int width, int height) {
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 48));
        String title = "BREAKOUT";
        FontMetrics titleMetrics = graphics.getFontMetrics();
        graphics.drawString(title, (width - titleMetrics.stringWidth(title)) / 2, height / 3);

        graphics.setFont(new Font("Arial", Font.BOLD, 28));
        FontMetrics optionMetrics = graphics.getFontMetrics();
        Selection[] options = Selection.values();
        for (int i = 0; i < options.length; i++) {
            renderOption(graphics, optionMetrics, options[i].name(), options[i], width,
                    height / 2 + i * SELECTION_SPACING);
        }
    }

    private void renderOption(Graphics2D graphics, FontMetrics metrics, String text,
                              Selection option, int width, int y) {
        boolean isSelected = selected == option;

        graphics.setColor(isSelected ? Color.YELLOW : Color.WHITE);
        String label = (isSelected ? "> " : "  ") + text;
        graphics.drawString(label, (width - metrics.stringWidth(label)) / 2, y);
    }
}
