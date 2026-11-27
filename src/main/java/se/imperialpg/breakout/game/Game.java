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
        //window settings
        gameProperties = new GameProperties(800,600,60);
        setPreferredSize(new Dimension(gameProperties.width(), gameProperties.height()));
        setBackground(Color.BLACK);
        setFocusable(true);
        //setup keyevent listener
        addKeyListener(this);
    }

    /**
     * call to start the gameloothread
     */
    public void start() {
        running = true;
        //thread to manage framerate
        new Thread(this, "gameloop").start();
    }

    /**
     * runs all init functions and spawn game objects
     */
    private void initializeGame() {
        objects.clear();
        gameState = GameState.Game;
        score = 0;
        addGameObject(new Paddle(350, 550));
        addGameObject(new Ball(390, 530));
        level.initialize(this);
    }

    /**
     * set state to game over
     */
    public void endGame() {
        gameState = GameState.GameOver;
    }

    /**
     * add a new gameobject and add to subscription list, calls initialize
     * @param object the new gameobject to add
     */
    public void addGameObject(GameObject object) {
        objects.add(object);
        object.initialize(this);
    }

    /**
     * manages the game thread and update loops
     */
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

    /**
     * runs the update function for the current state
     */
    private void update() {
        switch (gameState) {
            case Menu -> updateMenu();
            case Game -> updateGame();
            case GameOver -> updateGameOver();
            case ScoreList -> updateScoreList();
        }
    }

    /**
     * check menu selection
     * we use this method to avoid references to Game from the Menu class
     */
    private void updateMenu() {
        //did we get a new selection last frame?
        Menu.Selection selection = menu.consumeSelection();

        if (selection != null) {
            switch (selection) {
                case PLAY -> initializeGame();
                case HIGHSCORE -> gameState = GameState.ScoreList;
                case QUIT -> {
                    running = false;
                    Window window = SwingUtilities.getWindowAncestor(this);
                    if (window != null) {
                        SwingUtilities.invokeLater(window::dispose);
                    }
                }
            }
        }
    }

    /**
     * updates all gameobjects from list
     */
    private void updateGame() {
        for (GameObject object : objects) {
            object.update(gameProperties.deltaTime());
        }
    }

    private void updateGameOver() {
    }

    private void updateScoreList() {
    }

    /**
     * called from jpanel repaint
     * @param graphics the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        renderState((Graphics2D) graphics);
    }

    /**
     * switch for all render states
     * @param graphics renderer to use
     */
    private void renderState(Graphics2D graphics) {
        switch (gameState) {
            case Menu -> renderMenu(graphics);
            case Game -> renderGame(graphics);
            case GameOver -> renderGameOver(graphics);
            case ScoreList -> renderScoreList(graphics);
        }
    }

    /**
     * bridge to menu.renderMenu
     * @param graphics renderer to use
     */
    private void renderMenu(Graphics2D graphics) {
        menu.render(graphics, getWidth(), getHeight());
    }

    /**
     * bridge to game.renderGame
     * @param graphics renderer to use
     */
    private void renderGame(Graphics2D graphics) {
        renderObjectsAndHud(graphics);
    }

    /**
     * renderObjectsAndHud + Gameovertexts
     * @param graphics renderer to use
     */
    private void renderGameOver(Graphics2D graphics) {
        renderObjectsAndHud(graphics);
        graphics.setColor(Color.RED);
        graphics.setFont(new Font("Arial", Font.BOLD, 48));
        String text = "GAME OVER";
        FontMetrics metrics = graphics.getFontMetrics();
        graphics.drawString(text, (WIDTH - metrics.stringWidth(text)) / 2, HEIGHT / 2);
    }

    /**
     * bridge to game.RenderScorelist
     * @param graphics renderer to use
     */
    private void renderScoreList(Graphics2D graphics) {
    }

    /**
     * calls the render function for each gameobject in list as well as render hud
     * @param graphics renderer to use
     */
    private void renderObjectsAndHud(Graphics2D graphics) {
        for (GameObject object : objects) {
            object.render(graphics);
        }

        renderHud(graphics);
    }

    /**
     * renders hud elements
     * @param graphics renderer to use
     */
    private void renderHud(Graphics2D graphics) {
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 18));
        graphics.drawString("Score: " + score, 20, 30);
        graphics.drawString("High Score: " + highScore, 20, 55);
        graphics.drawString("Player: " + playerName, WIDTH - 180, 30);
        graphics.drawString("Round: " + roundNumber, WIDTH - 180, 55);
    }

    /**
     * send Keyevents to all gameobjects
     * @param event
     */
    private void dispatchInput(KeyEvent event) {
        synchronized (this) {
            for (GameObject object : objects) {
                if (object instanceof InputReceiver receiver) {
                    receiver.receiveInput(event);
                }
            }
        }
    }

    /**
     * used to restart game
     * @param event
     */
    private void handleGameOverInput(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_R) {
            initializeGame();
        }
    }

    /**
     * bridge to handle pressedinput to gamestates
     * @param event the event to be processed
     */
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
