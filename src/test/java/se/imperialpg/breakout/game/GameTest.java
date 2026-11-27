package se.imperialpg.breakout.game;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameTest {

    @Test
    void constructorSetsExpectedWindowProperties() {
        Game game = new Game();

        assertEquals(800, game.getPreferredSize().width);
        assertEquals(600, game.getPreferredSize().height);
        assertEquals(Color.BLACK, game.getBackground());
        assertTrue(game.isFocusable());
    }

    @Test
    void addGameObjectInitializesObjectWithThisGame() {
        Game game = new Game();
        TrackingGameObject object = new TrackingGameObject();

        game.addGameObject(object);

        assertSame(game, object.initializedWith);
    }

    @Test
    void paintsTheInitialMenu() {
        Game game = new Game();
        game.setSize(800, 600);
        BufferedImage image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        try {
            game.paint(graphics);
        } finally {
            graphics.dispose();
        }

        assertEquals(Color.BLACK.getRGB(), image.getRGB(0, 0));
    }

    private static final class TrackingGameObject extends GameObject {
        private Game initializedWith;

        private TrackingGameObject() {
            super(0, 0, 1, 1);
        }

        @Override
        public void initialize(Game game) {
            initializedWith = game;
        }

        @Override
        public void update(double deltaTime) {
        }

        @Override
        public void render(Graphics2D graphics) {
        }
    }
}
