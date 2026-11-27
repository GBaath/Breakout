package se.imperialpg.breakout.game;

public record GameProperties(WindowProperties windowProperties, DifficultyProperties difficultyProperties) {

    public long deltaTime() {
        return 1000 / (long) windowProperties.fps();
    }

    public record WindowProperties(int width, int height, int fps) {
    }

    public record DifficultyProperties(int paddleSize, int ballSpeed, int blockRows, int blockColumns) {
    }
}
