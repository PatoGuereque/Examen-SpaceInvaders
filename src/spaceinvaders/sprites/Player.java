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
    private int lives;                          //player lives left
    private int score;                          //player score
    private final int START_X = 270;            //player starting x position
    private final int START_Y = 560;            //player starting y position
    private Animation idle;                     //player idle animation
    private Animation left;                     //player left animation
    private Animation right;                    //player right animation
    private boolean leftPress = false;          //if the left key is pressed
    private boolean rightPress = false;         //if the right key is pressed
    private Runnable nextTickRun;               //runnable to run next tick

    /**
     * Player constructor
     * @param game Board
     */
    public Player(Board game) {
        this.game = game;
        initPlayer();
        lives = 2;
        score = 0;
    }

    /**
     * Initialize player
     */
    private void initPlayer() {
        width = Commons.PLAYER_WIDTH;
        height = Commons.PLAYER_HEIGHT;
        setX(START_X);
        setY(START_Y);
        
        idle = new Animation(Assets.playerIdle, 200);
        left = new Animation(Assets.playerLeft, 200);
        right = new Animation(Assets.playerRight,200);
    }
    
    /**
     * Update player position
     */
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
    
    /**
     * reduce player lives and destroy all shots on the board
     */
    public void damage() {
        for(Alien alien : game.getAliens()){
            alien.getBomb().setDestroyed(true);
        }
        game.getPlayerShot().die();
        Sound.EXPLOSION.play();
        setDying(true);
        setLives(getLives() - 1);
    }

    /**
     * get key presses
     * @param e KeyEvent
     */
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

    /**
     * detect key releases
     * @param e KeyEvent
     */
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
    
    /**
     * get lives remaining
     * @return lives
     */
    public int getLives(){
        return this.lives;
    }
    
    /**
     * set lives left
     * @param lives the new lives
     */
    public void setLives(int lives){
        this.lives = lives;
    }
    
    /**
     * get player score
     * @return the player's score
     */
    public int getScore(){
        return this.score;
    }
    
    /**
     * get player score
     * @param score the new score
     */
    public void setScore(int score){
        this.score = score;
    }
    
    /**
     * reset player to starting position and visibility
     */
    public void reset() {
        setVisible(true);
        initPlayer();
    }

    /**
     * draw player on game board
     * @param g graphics
     */
    @Override
    public void render(Graphics2D g) {
        if (nextTickRun != null) {
            nextTickRun.run();
            nextTickRun = null;
        }

        if (isDying()) {
            // renders explosion
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