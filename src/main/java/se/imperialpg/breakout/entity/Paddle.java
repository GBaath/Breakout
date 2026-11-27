package se.imperialpg.breakout.entity;

import se.imperialpg.breakout.game.Game;
import se.imperialpg.breakout.game.GameObject;
import se.imperialpg.breakout.game.InputReceiver;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 * Represents the player-controlled paddle in the game.
 * 
 * @author Stefan Markan
 */
public class Paddle extends GameObject implements InputReceiver {
    private static final int moveLeftKey = KeyEvent.VK_LEFT;
    private static final int moveLeftAltKey = KeyEvent.VK_A;

    private static final int moveRightKey = KeyEvent.VK_RIGHT;
    private static final int moveRightAltKey = KeyEvent.VK_D;

    private static final int boostKey = KeyEvent.VK_SHIFT;

    private static final double SPEED = 300;
    private static final double SPEEDBOOST = 1.85;

    private boolean movingLeft;
    private boolean movingRight;
    private boolean isBoosting;

    /**
     * Constructs a new paddle at the specified position.
     *
     * @param x the initial horizontal position of the paddle
     * @param y the initial vertical position of the paddle
     */
    public Paddle(double x, double y) {
        super(x, y, 100, 16);
    }

    /**
     * Updates the paddle's position based on the current movement input.
     * <p>
     * The paddle moves at its normal speed or boosted speed depending on
     * whether the speed boost is active. Its movement is restricted to
     * the horizontal boundaries of the game window.
     *
     * @param deltaTime the time elapsed since the previous update, in seconds
     */
    @Override
    public void initialize(Game game) {

    }

    /**
     * Updates the paddle's position based on the current movement input.
     * <p>
     * The paddle moves at its normal speed or boosted speed depending on
     * whether the speed boost is active. Its movement is restricted to
     * the horizontal boundaries of the game window.
     *
     * @param deltaTime the time elapsed since the previous update, in milli seconds
     */
    @Override
    public void update(double deltaTimeMillis) {
        double speed = isBoosting ? SPEED * SPEEDBOOST : SPEED;

        if (movingLeft && x > 0) {
            x -= speed * deltaTimeMillis / 1000;
        }
        if (movingRight && x < 800 - width) {
            x += speed * deltaTimeMillis / 1000;
        }
    }

    /**
     * Renders the paddle on the screen as a white rectangle.
     *
     * @param graphics the graphics context used for rendering the paddle
     */
    @Override
    public void render(Graphics2D graphics) {
        graphics.setColor(Color.WHITE);
        graphics.fillRect((int) x, (int) y, width, height);
    }

    /**
     * Processes keyboard input to control the paddle's movement and speed.
     *
     * @param event the keyboard event containing the key code and event type
     */
    @Override
    public void receiveInput(KeyEvent event) {
        boolean keyIsPressed = event.getID() == KeyEvent.KEY_PRESSED;

        switch (event.getKeyCode()) {
            case moveLeftKey, moveLeftAltKey -> {
                movingLeft = keyIsPressed;
            }
            case moveRightKey, moveRightAltKey -> {
                movingRight = keyIsPressed;
            }
            case boostKey -> {
                isBoosting = keyIsPressed;
            }
        }
    }

    /**
     * Returns the x position of the Paddle
     * 
     * @return this.x
     */
    public double getXPosition() {
        return this.x;
    }

    /**
     * Resets the x position of the Paddle
     */
    public void resetXPosition() {
        this.x = 350;
    }
}
