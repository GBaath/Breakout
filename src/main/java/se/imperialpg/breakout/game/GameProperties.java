package se.imperialpg.breakout.game;

public record GameProperties(WindowProperties windowProperties, DifficultyProperties difficultyProperties) {

    /**
     * target delta time in milliseconds, NOT the actual time it took since last frame
     * fps/1000
     * @return target deltatime
     */
    public long deltaTime() {
        return 1000 / (long) windowProperties.fps();
    }

    /**
     * properties for the game window
     * @param width width of the window
     * @param height heighgt of the window
     * @param fps how many times per second we want to update
     */
    public record WindowProperties(int width, int height, int fps) {
    }

    /**
     * settings for game classes
     * @param paddleSize size of thepaddle
     * @param ballSpeed how fast the ball moves
     * @param blockRows how many rows of block should spawn
     * @param blockColumns how many columns of blocks should spawn
     */
    public record DifficultyProperties(int paddleSize, int ballSpeed, int blockRows, int blockColumns) {
    }
}
