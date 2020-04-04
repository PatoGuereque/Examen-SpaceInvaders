package spaceinvaders.sprites;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;

import spaceinvaders.Board;
import spaceinvaders.Commons;
import spaceinvaders.sound.Sound;

import java.awt.event.KeyEvent;
import spaceinvaders.util.Animation;
import spaceinvaders.util.Assets;

/**
 * @author antoniomejorado
 */
public class Player extends Sprite {

    private final Board game;
    private int lives;
    private int score;
    private final int START_X = 270;
    private final int START_Y = 560;
    private Animation idle;
    private Animation left;
    private Animation right;
    private boolean leftPress = false;
    private boolean rightPress = false;
    private Runnable nextTickRun;

    public Player(Board game) {
        this.game = game;
        initPlayer();
        lives = 3;
        score = 0;
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
        if (isDying()) {
            return;
        }

        x += dx;

        if (x <= 2) {
            x = 2;
        }

        if (x >= Commons.BOARD_WIDTH - 2 * width) {
            x = Commons.BOARD_WIDTH - 2 * width;
        }
    }

    public void damage() {
        for(Alien alien : game.getAliens()){
            alien.getBomb().setDestroyed(true);
        }
        game.getPlayerShot().die();
        Sound.EXPLOSION.play();
        setDying(true);
        setLives(getLives() - 1);
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
    
    public int getScore(){
        return this.score;
    }
    
    public void setScore(int score){
        this.score = score;
    }
    
    public void reset() {
        setVisible(true);
        initPlayer();
    }

    @Override
    public void render(Graphics2D g) {
        if (nextTickRun != null) {
            nextTickRun.run();
            nextTickRun = null;
        }

        if (isDying()) {
            renderExplosion(g);
            if (explosion.getIndex() == 4) {
                if (getLives() < 1){
                    die();
                    game.end();
                } else {
                    nextTickRun = () -> {
                        setDying(false);
                        reset();
                        explosion.reset();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    };
                }
            }
            return;
        }

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