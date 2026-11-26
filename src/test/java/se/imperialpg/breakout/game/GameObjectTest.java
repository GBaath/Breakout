package se.imperialpg.breakout.game;

import org.junit.jupiter.api.Test;

import java.awt.Graphics2D;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameObjectTest {

    @Test
    void intersectsWhenBoundsOverlap() {
        GameObject first = new TestGameObject(10, 10, 20, 20);
        GameObject overlapping = new TestGameObject(25, 25, 20, 20);

        assertTrue(first.intersects(overlapping));
        assertTrue(overlapping.intersects(first));
    }

    @Test
    void doesNotIntersectWhenBoundsOnlyTouchAtEdge() {
        GameObject first = new TestGameObject(10, 10, 20, 20);
        GameObject adjacent = new TestGameObject(30, 10, 20, 20);

        assertFalse(first.intersects(adjacent));
        assertFalse(adjacent.intersects(first));
    }

    @Test
    void doesNotIntersectWhenBoundsAreSeparated() {
        GameObject first = new TestGameObject(10, 10, 20, 20);
        GameObject separated = new TestGameObject(31, 10, 20, 20);

        assertFalse(first.intersects(separated));
    }

    private static final class TestGameObject extends GameObject {
        private TestGameObject(double x, double y, int width, int height) {
            super(x, y, width, height);
        }

        @Override
        public void initialize(Game game) {
        }

        @Override
        public void update(double deltaTime) {
        }

        @Override
        public void render(Graphics2D graphics) {
        }
    }
}
