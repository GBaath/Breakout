package se.imperialpg.breakout.game;

import java.awt.Graphics2D;

public abstract class GameObject {
    protected double x;
    protected double y;
    protected final int width;
    protected final int height;

    protected GameObject(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void initialize(Game game) {
    }

    public abstract void update(double deltaTime);

    public abstract void render(Graphics2D graphics);
}
