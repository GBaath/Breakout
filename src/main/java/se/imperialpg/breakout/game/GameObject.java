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

    public abstract void initialize(Game game);

    /**
     * check this gameobject dimensions overlaps against another
     * 
     * @param other the gameobject to check overlap to
     * @return found overlap
     */
    public boolean intersects(GameObject other) {
        return x < other.x + other.width
                && x + width > other.x
                && y < other.y + other.height
                && y + height > other.y;
    }

    public abstract void update(double deltaTime);

    public abstract void render(Graphics2D graphics);
}
