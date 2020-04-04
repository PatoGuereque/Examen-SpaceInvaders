package spaceinvaders.sprites;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;

import spaceinvaders.Commons;
import spaceinvaders.util.ImageLoader;

import java.awt.event.KeyEvent;
import spaceinvaders.util.Animation;
import spaceinvaders.util.Assets;

/**
 * @author antoniomejorado
 */
public class Player extends Sprite {

    private int lives;
    private final int START_X = 270;
    private final int START_Y = 560;
    private Animation idle;
    private Animation left;
    private Animation right;
    private boolean leftPress = false;
    private boolean rightPress = false;

    public Player() {
        initPlayer();
        lives = 3;
    }

    private void initPlayer() {
        width = Commons.PLAYER_WIDTH;
        height = Commons.PLAYER_HEIGHT;
        setX(START_X);
        setY(START_Y);
        
        idle = new Animation(Assets.playerIdle, 200);
        left = new Animation(Assets.playerLeft, 200);
        right = new Animation(Assets.playerRight,200);
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
            leftPress = true;
            dx = -2;
        }

        if (key == KeyEvent.VK_RIGHT) {
            rightPress = true;
            dx = 2;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            if (rightPress) {
                dx = 2;
            } else {
                dx = 0;
            }
            leftPress = false;
        }

        if (key == KeyEvent.VK_RIGHT) {
            if (leftPress) {
                dx = -2;
            } else {
                dx = 0;
            }
            rightPress = false;
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
        initPlayer();
    }

    @Override
    public void render(Graphics2D g) {
        //idle
        if(dx == 0){
            idle.tick();
            render(g, idle);
        }
        //left
        if(dx == -2){
            left.tick();
            render(g, left);
        }
        //right
        if(dx == 2){
            right.tick();
            render(g, right);
        }
    }
}