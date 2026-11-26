package se.imperialpg.breakout.game;

import se.imperialpg.breakout.entity.Ball;
import se.imperialpg.breakout.entity.Paddle;
import se.imperialpg.breakout.level.Level;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Window;
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
    private final Menu menu = new Menu();
    private volatile boolean running;
    private volatile GameState gameState = GameState.Menu; //start in menu

    //score things to extract later
    private int score;
    private int highScore;
    private String playerName = "Player";
    private int roundNumber = 1;

    private enum GameState{
        Menu,
        Game,
        GameOver,
        ScoreList
    }


    public Game() {
        gameProperties = new GameProperties(800,600,60);
        setPreferredSize(new Dimension(gameProperties.width(), gameProperties.height()));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
    }

    public void start() {
        running = true;
        new Thread(this, "gameloop").start();
    }

    private void initializeGame() {
        objects.clear();
        gameState = GameState.Game;
        score = 0;
        addGameObject(new Paddle(350, 550));
        addGameObject(new Ball(390, 530));
        level.initialize(this);
    }

    public void endGame() {
        gameState = GameState.GameOver;
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

            update();
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

    private void update() {
        switch (gameState) {
            case Menu -> updateMenu();
            case Game -> updateGame();
            case GameOver -> updateGameOver();
            case ScoreList -> updateScoreList();
        }
    }

    private void updateMenu() {
        Menu.Selection selection = menu.consumeSelection();
        if (selection == Menu.Selection.PLAY) {
            initializeGame();
        } else if (selection == Menu.Selection.QUIT) {
            running = false;
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                SwingUtilities.invokeLater(window::dispose);
            }
        }
    }

    private void updateGame() {
        for (GameObject object : objects) {
            object.update(gameProperties.deltaTime());
        }
    }

    private void updateGameOver() {
    }

    private void updateScoreList() {
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        renderState((Graphics2D) graphics);
    }

    private void renderState(Graphics2D graphics) {
        switch (gameState) {
            case Menu -> renderMenu(graphics);
            case Game -> renderGame(graphics);
            case GameOver -> renderGameOver(graphics);
            case ScoreList -> renderScoreList(graphics);
        }
    }

    private void renderMenu(Graphics2D graphics) {
        menu.render(graphics, getWidth(), getHeight());
    }

    private void renderGame(Graphics2D graphics) {
        renderObjectsAndHud(graphics);
    }

    private void renderGameOver(Graphics2D graphics) {
        renderObjectsAndHud(graphics);
        graphics.setColor(Color.RED);
        graphics.setFont(new Font("Arial", Font.BOLD, 48));
        String text = "GAME OVER";
        FontMetrics metrics = graphics.getFontMetrics();
        graphics.drawString(text, (WIDTH - metrics.stringWidth(text)) / 2, HEIGHT / 2);
    }

    private void renderScoreList(Graphics2D graphics) {
    }

    private void renderObjectsAndHud(Graphics2D graphics) {
        for (GameObject object : objects) {
            object.render(graphics);
        }

        renderHud(graphics);
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

    private void handleGameOverInput(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_R) {
            initializeGame();
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        switch (gameState) {
            case Menu -> handleMenuInput(event);
            case Game -> dispatchInput(event);
            case GameOver -> handleGameOverInput(event);
            case ScoreList -> handleScoreListInput(event);
        }
    }

    private void handleMenuInput(KeyEvent event) {
        menu.receiveInput(event);
    }

    private void handleScoreListInput(KeyEvent event) {
    }

    @Override
    public void keyReleased(KeyEvent event) {
        if (gameState == GameState.Game) {
            dispatchInput(event);
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {
    }
}
