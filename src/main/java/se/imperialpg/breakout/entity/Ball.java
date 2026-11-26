package se.imperialpg.breakout.entity;

import se.imperialpg.breakout.game.Game;
import se.imperialpg.breakout.game.GameObject;

import java.awt.Color;
import java.awt.Graphics2D;

public class Ball extends GameObject {

    public Ball(double x, double y) {
        super(x, y, 16, 16);
    }

    @Override
    public void initialize(Game game) {

    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(Graphics2D graphics) {
        graphics.setColor(Color.CYAN);
        graphics.fillOval((int) x, (int) y, width, height);
    }
}
