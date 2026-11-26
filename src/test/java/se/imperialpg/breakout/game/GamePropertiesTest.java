package se.imperialpg.breakout.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GamePropertiesTest {

    @Test
    void exposesConfiguredDimensionsAndFrameRate() {
        GameProperties properties = new GameProperties(800, 600, 60);

        assertEquals(800, properties.width());
        assertEquals(600, properties.height());
        assertEquals(60, properties.fps());
    }

    @Test
    void calculatesFrameDurationInMilliseconds() {
        assertEquals(16, new GameProperties(800, 600, 60).deltaTime());
        assertEquals(33, new GameProperties(800, 600, 30).deltaTime());
    }
}
