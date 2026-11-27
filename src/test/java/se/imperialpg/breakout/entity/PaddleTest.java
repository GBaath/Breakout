package se.imperialpg.breakout.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Component;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PaddleTest {

    private Paddle paddle;
    private Component source;
    private static final double DELTA_TIME = 0.016;
    private static final int SPAWN_X = 350;

    private static final int moveLeftKey = KeyEvent.VK_LEFT;
    private static final int moveLeftAltKey = KeyEvent.VK_A;

    private static final int moveRightKey = KeyEvent.VK_RIGHT;
    private static final int moveRightAltKey = KeyEvent.VK_D;

    private static final int boostKey = KeyEvent.VK_SHIFT;

    private static final double SPEED = 300;
    private static final double SPEEDBOOST = 1.85;

    private KeyEvent moveLeftKeyPressed;
    private KeyEvent moveLeftAltKeyPressed;
    private KeyEvent moveRightKeyPressed;
    private KeyEvent moveRightAltKeyPressed;
    private KeyEvent boostKeyPressed;

    private double initialXPos;

    @BeforeEach
    void initEach() {
        paddle = new Paddle(SPAWN_X, 0);
        // JPanel Component required as arg when constructing KeyEvents
        source = new JPanel();

        moveLeftKeyPressed = createKeyEvent(moveLeftKey);
        moveLeftAltKeyPressed = createKeyEvent(moveLeftAltKey);
        moveRightKeyPressed = createKeyEvent(moveRightKey);
        moveRightAltKeyPressed = createKeyEvent(moveRightAltKey);
        boostKeyPressed = createKeyEvent(boostKey);

        initialXPos = paddle.getXPosition();
    }

    private KeyEvent createKeyEvent(int keyCode) {
        return new KeyEvent(
                source,
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                keyCode,
                KeyEvent.CHAR_UNDEFINED);
    }

    @Test
    void paddleMovesLeftWhenLeftKeyIsPressed() {
        paddle.receiveInput(moveLeftKeyPressed);
        paddle.update(DELTA_TIME);
        assertTrue(initialXPos > paddle.getXPosition());
    }

    @Test
    void paddleMovesLeftWhenLeftAltKeyIsPressed() {
        paddle.receiveInput(moveLeftAltKeyPressed);
        paddle.update(DELTA_TIME);
        assertTrue(initialXPos > paddle.getXPosition());
    }

    @Test
    void paddleMovesRightWhenRightKeyIsPressed() {
        paddle.receiveInput(moveRightKeyPressed);
        paddle.update(DELTA_TIME);
        assertTrue(initialXPos < paddle.getXPosition());
    }

    @Test
    void paddleMovesRightWhenRightAltKeyIsPressed() {
        paddle.receiveInput(moveRightAltKeyPressed);
        paddle.update(DELTA_TIME);
        assertTrue(initialXPos < paddle.getXPosition());
    }

    @Test
    void paddleShouldMoveFourPointEightPixelsInOneFrameWhenSpeedIsDefault() {
        paddle.receiveInput(moveLeftKeyPressed);
        paddle.update(DELTA_TIME);
        double endXPos = paddle.getXPosition();
        assertEquals(4.8, initialXPos - endXPos, 0.001);
    }

    @Test
    void paddleShouldMove300PixelsInOneSecond() {
        paddle.receiveInput(moveLeftKeyPressed);
        paddle.update(1.0);
        assertEquals(initialXPos - SPEED, paddle.getXPosition(), 0.001);
    }

    @Test
    void paddleShouldMove85PercentFurtherWhenBoosting() {
        paddle.receiveInput(moveLeftKeyPressed);
        paddle.update(DELTA_TIME);

        double nonBoostEndX = paddle.getXPosition();
        double nonBoostedDistanceInPixels = initialXPos - nonBoostEndX;

        // Reset Paddle position
        paddle.resetXPosition();

        // Give Paddle Boost and MoveLeft input before updating
        paddle.receiveInput(moveLeftKeyPressed);
        paddle.receiveInput(boostKeyPressed);
        paddle.update(DELTA_TIME);

        double boostEndX = paddle.getXPosition();
        double boostedDistanceInPixels = initialXPos - boostEndX;

        assertEquals(SPEEDBOOST, (boostedDistanceInPixels / nonBoostedDistanceInPixels), 0.001);
    }
}
