package se.imperialpg.breakout;

import se.imperialpg.breakout.game.Game;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
            Game game = new Game();
            //window
            JFrame window = new JFrame("Breakout");
            //close when closed
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setContentPane(game);
            //size to game
            window.pack();
            //window properties
            window.setResizable(false);
            window.setLocationRelativeTo(null);
            window.setVisible(true);
            //run
            game.start();
    }
}
