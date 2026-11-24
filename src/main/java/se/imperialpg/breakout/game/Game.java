package se.imperialpg.breakout.game;

import se.imperialpg.breakout.entity.Ball;
import se.imperialpg.breakout.entity.Paddle;
import se.imperialpg.breakout.level.Level;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Game extends JPanel implements Runnable, KeyListener {

    private final GameProperties gameProperties;
    private final List<GameObject> objects = new ArrayList<>();
    private final Level level = new Level();
    private volatile boolean running;
    private boolean gameOver;
    private int score;
    private int highScore;
    private String playerName = "Player";
    private int roundNumber = 1;

    public Game() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        gameProperties = new GameProperties(800,600,60);
    }

    public void start() {

        initializeGame();
        running = true;
        new Thread(this, "gameloop").start();
    }

    private void initializeGame() {
        objects.clear();
        gameOver = false;
        score = 0;
        addGameObject(new Paddle(350, 550));
        addGameObject(new Ball(390, 530));
        level.initialize(this);
    }

    public void addGameObject(GameObject object) {
        objects.add(object);
        object.initialize(this);
    }

    @Override
    public void run() {
        long frameTime = gameProperties.deltaTime();

        while (running) {
            long start = System.nanoTime();

            updateGame();
            repaint();

            long elapsed = (System.nanoTime() - start) / 1_000_000;
            long sleepTime = frameTime - elapsed;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ignored) {
                    running = false;
                }
            }
        }
    }

    private void updateGame() {
        if (gameOver) {
            return;
        }

        for (GameObject object : objects) {
            object.update(gameProperties.deltaTime());
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        renderGame((Graphics2D) graphics);
    }

    private void renderGame(Graphics2D graphics) {
        for (GameObject object : objects) {
            object.render(graphics);
        }

        renderHud(graphics);
        if (gameOver) {
            graphics.setColor(Color.RED);
            graphics.setFont(new Font("Arial", Font.BOLD, 48));
            String text = "GAME OVER";
            FontMetrics metrics = graphics.getFontMetrics();
            graphics.drawString(text, (WIDTH - metrics.stringWidth(text)) / 2, HEIGHT / 2);
        }
    }

    private void renderHud(Graphics2D graphics) {
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 18));
        graphics.drawString("Score: " + score, 20, 30);
        graphics.drawString("High Score: " + highScore, 20, 55);
        graphics.drawString("Player: " + playerName, WIDTH - 180, 30);
        graphics.drawString("Round: " + roundNumber, WIDTH - 180, 55);
    }

    private void dispatchInput(KeyEvent event) {
        synchronized (this) {
            for (GameObject object : objects) {
                if (object instanceof InputReceiver receiver) {
                    receiver.receiveInput(event);
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (gameOver && event.getKeyCode() == KeyEvent.VK_R) {
            initializeGame();
            return;
        }
        dispatchInput(event);
    }

    @Override
    public void keyReleased(KeyEvent event) {
        dispatchInput(event);
    }

    @Override
    public void keyTyped(KeyEvent event) {
    }
}
