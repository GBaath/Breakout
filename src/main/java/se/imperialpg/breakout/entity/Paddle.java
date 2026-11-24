package se.imperialpg.breakout.entity;

import se.imperialpg.breakout.game.Game;
import se.imperialpg.breakout.game.GameObject;
import se.imperialpg.breakout.game.InputReceiver;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class Paddle extends GameObject implements InputReceiver {
    private static final double SPEED = 300;

    public Paddle(double x, double y) {
        super(x, y, 100, 16);
    }

    @Override
    public void update(double deltaTime) {
    }

    @Override
    public void render(Graphics2D graphics) {
        graphics.setColor(Color.WHITE);
        graphics.fillRect((int) x, (int) y, width, height);
    }

    @Override
    public void receiveInput(KeyEvent event) {
        boolean pressed = event.getID() == KeyEvent.KEY_PRESSED;
        int keyCode = event.getKeyCode();

        //check keycode to handle input
    }
}
