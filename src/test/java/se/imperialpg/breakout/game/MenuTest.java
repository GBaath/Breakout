package se.imperialpg.breakout.game;

import org.junit.jupiter.api.Test;

import java.awt.Canvas;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MenuTest {

    @Test
    void playIsSelectedByDefaultAndSelectionIsConsumedOnce() {
        Menu menu = new Menu();

        menu.receiveInput(keyPressed(KeyEvent.VK_ENTER));

        assertEquals(Menu.Selection.PLAY, menu.consumeSelection());
        assertNull(menu.consumeSelection());
    }

    @Test
    void navigationCyclesThroughOptionsWithArrowAndWASDKeys() {
        Menu menu = new Menu();

        menu.receiveInput(keyPressed(KeyEvent.VK_UP));
        menu.receiveInput(keyPressed(KeyEvent.VK_ENTER));
        assertEquals(Menu.Selection.QUIT, menu.consumeSelection());

        menu.receiveInput(keyPressed(KeyEvent.VK_S));
        menu.receiveInput(keyPressed(KeyEvent.VK_ENTER));
        assertEquals(Menu.Selection.PLAY, menu.consumeSelection());

        menu.receiveInput(keyPressed(KeyEvent.VK_DOWN));
        menu.receiveInput(keyPressed(KeyEvent.VK_ENTER));
        assertEquals(Menu.Selection.HIGHSCORE, menu.consumeSelection());
    }

    @Test
    void confirmKeysSelectCurrentOption() {
        Menu menu = new Menu();
        menu.receiveInput(keyPressed(KeyEvent.VK_DOWN));
        menu.receiveInput(keyPressed(KeyEvent.VK_SPACE));

        assertEquals(Menu.Selection.HIGHSCORE, menu.consumeSelection());
    }

    @Test
    void ignoresKeysThatWereNotPressed() {
        Menu menu = new Menu();
        KeyEvent releasedDown = new KeyEvent(
                new Canvas(), KeyEvent.KEY_RELEASED, 0, 0, KeyEvent.VK_DOWN, KeyEvent.CHAR_UNDEFINED);
        KeyEvent typedEnter = new KeyEvent(
                new Canvas(), KeyEvent.KEY_TYPED, 0, 0, KeyEvent.VK_UNDEFINED, '\n');

        menu.receiveInput(releasedDown);
        menu.receiveInput(typedEnter);

        assertNull(menu.consumeSelection());
    }

    private static KeyEvent keyPressed(int keyCode) {
        return new KeyEvent(
                new Canvas(), KeyEvent.KEY_PRESSED, 0, 0, keyCode, KeyEvent.CHAR_UNDEFINED);
    }
}
