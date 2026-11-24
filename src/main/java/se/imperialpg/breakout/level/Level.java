package se.imperialpg.breakout.level;

import se.imperialpg.breakout.entity.Block;
import se.imperialpg.breakout.game.Game;

public class Level {
    public void initialize(Game game) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 8; column++) {
                game.addGameObject(new Block(40 + column * 90, 90 + row * 30));
            }
        }
    }
}
