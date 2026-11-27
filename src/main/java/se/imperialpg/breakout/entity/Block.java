package se.imperialpg.breakout.entity;

import se.imperialpg.breakout.game.Game;
import se.imperialpg.breakout.game.GameObject;
import se.imperialpg.breakout.game.BallCollider;

import java.awt.Color;
import java.awt.Graphics2D;

public class Block extends GameObject implements BallCollider {
    public Block(double x, double y) {
        super(x, y, 80, 20);
    }
    private final int life =1;

    @Override
    public void initialize(Game game) {

    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void onHit() {
    }

    @Override
    public void render(Graphics2D graphics) {
        graphics.setColor(Color.ORANGE);
        graphics.fillRect((int) x, (int) y, width, height);
    }
}
