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
    private int lives;
    private int START_X = 270;
    private int START_Y = 560;

    public Player() {
        initPlayer();
    }

    private void initPlayer() {
        setImage(ImageLoader.loadImage("/resources/player.png"));
        width = Commons.PLAYER_WIDTH;
        height *= 2;
        lives = 3;
        setX(START_X);
        setY(START_Y);
    }

    public void tick() {
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
    
    public int getLives(){
        return this.lives;
    }
    
    public void setLives(int lives){
        this.lives = lives;
    }

    public void reset() {
        setVisible(true);
        setX(START_X);
        setY(START_Y);
        setImage(ImageLoader.loadImage("/resources/player.png"));
    }
}