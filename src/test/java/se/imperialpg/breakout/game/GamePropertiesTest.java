package se.imperialpg.breakout.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GamePropertiesTest {

    @Test
    void exposesConfiguredDimensionsAndFrameRate() {
        GameProperties properties = new GameProperties(
                new GameProperties.WindowProperties(800, 600, 60),
                new GameProperties.DifficultyProperties(100, 300, 3, 8)
        );

        assertEquals(800, properties.windowProperties().width());
        assertEquals(600, properties.windowProperties().height());
        assertEquals(60, properties.windowProperties().fps());
        assertEquals(100, properties.difficultyProperties().paddleSize());
        assertEquals(300, properties.difficultyProperties().ballSpeed());
        assertEquals(3, properties.difficultyProperties().blockRows());
        assertEquals(8, properties.difficultyProperties().blockColumns());
    }

    @Test
    void calculatesFrameDurationInMilliseconds() {
        assertEquals(16, propertiesWithFrameRate(60).deltaTime());
        assertEquals(33, propertiesWithFrameRate(30).deltaTime());
    }

    private GameProperties propertiesWithFrameRate(int fps) {
        return new GameProperties(
                new GameProperties.WindowProperties(800, 600, fps),
                new GameProperties.DifficultyProperties(100, 300, 3, 8)
        );
    }
}
