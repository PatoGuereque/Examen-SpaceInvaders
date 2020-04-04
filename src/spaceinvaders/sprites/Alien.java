package spaceinvaders.sprites;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;

import spaceinvaders.Commons;
import spaceinvaders.util.Animation;
import spaceinvaders.util.Assets;

/**
 * @author antoniomejorado
 */
public class Alien extends Sprite {

    private Bomb bomb;
    private final Animation animation;
    
    /**
     * Alien constructor
     * @param x coordinate
     * @param y coordinate
     */
    public Alien(int x, int y) {
        this.animation = new Animation(Assets.alien, 200);
        initAlien(x, y);
    }
    
    /**
     * Initialize alien
     * @param x coordinate
     * @param y coordinate
     */
    private void initAlien(int x, int y) {
        this.x = x;
        this.y = y;

        bomb = new Bomb(x, y);
        width = Commons.ALIEN_WIDTH;
        height = Commons.ALIEN_HEIGHT;
    }
    
    /**
     * Move alien according to direction
     * @param direction double
     */
    public void move(double direction) {
        this.x += direction;
    }

    /**
     * Get bomb for this alien
     * @return bomb
     */
    public Bomb getBomb() {
        return bomb;
    }

    /**
     * Draw alien in game board
     * @param g graphics
     */
    @Override
    public void render(Graphics2D g) {
        animation.tick();
        render(g, animation);
    }
}
