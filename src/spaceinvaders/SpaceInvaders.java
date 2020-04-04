package spaceinvaders;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.EventQueue;
import javax.swing.JFrame;

/**
 *
 * @author antoniomejorado
 */
public class SpaceInvaders extends JFrame  {

    /**
     * Creates a new instance of the game
     */
    public SpaceInvaders() {
        initUI();
    }

    /**
     * Initializes the game board and window
     */
    private void initUI() {
        add(new Board());

        setTitle("Space Invaders");
        setSize(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
    }

    /**
     * Main method called to create the game objecy
     *
     * @param args unused
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {

            SpaceInvaders ex = new SpaceInvaders();
            ex.setVisible(true);
        });
    }
}
