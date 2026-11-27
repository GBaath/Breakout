package se.imperialpg.breakout.level;

import se.imperialpg.breakout.entity.Block;
import se.imperialpg.breakout.game.Game;
import se.imperialpg.breakout.game.GameProperties;

public class Level {
    public void initialize(Game game, GameProperties.DifficultyProperties difficultyProperties) {
        for (int row = 0; row < difficultyProperties.blockRows(); row++) {
            for (int column = 0; column < difficultyProperties.blockColumns(); column++) {
                game.addGameObject(new Block(40 + column * 90, 90 + row * 30));
            }
        }
    }
}
