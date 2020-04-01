package spaceinvaders.sprites;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import spaceinvaders.Commons;
import spaceinvaders.util.ImageLoader;

import java.awt.event.KeyEvent;

/**
 * @author antoniomejorado
 */
public class Player extends Sprite {

    public Player() {
        initPlayer();
    }

    private void initPlayer() {
        setImage(ImageLoader.loadImage("/resources/player.png"));
        width = Commons.PLAYER_WIDTH;
        height *= 2;

        int START_X = 270;
        setX(START_X);

        int START_Y = 560;
        setY(START_Y);
    }

    public void act() {
        x += dx;

        if (x <= 2) {
            x = 2;
        }

        if (x >= Commons.BOARD_WIDTH - 2 * width) {
            x = Commons.BOARD_WIDTH - 2 * width;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = -2;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 2;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
    }
}