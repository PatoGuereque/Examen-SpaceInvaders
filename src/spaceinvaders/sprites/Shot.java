package spaceinvaders.sprites;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;

import spaceinvaders.sound.Sound;
import spaceinvaders.util.Animation;
import spaceinvaders.util.Assets;

/**
 * @author antoniomejorado
 */
public class Shot extends Sprite {
    private final Animation animation;      //hold shot animation
    
    /**
     * shot constructor
     */
    public Shot() {
        this.animation = new Animation(Assets.shot, 200);
    }

    /**
     * shot constructor
     * @param x double
     * @param y double
     */
    public Shot(double x, double y) {
        this.animation = new Animation(Assets.shot, 200);
        initShot(x, y);
    }

    /**
     * kills shot
     */
    @Override
    public void die() {
        super.die();
    }

    /**
     * initialize shot
     * @param x double
     * @param y double
     */
    private void initShot(double x, double y) {
        width = 30;
        height = 20;

        int H_SPACE = 6;
        setX(x + H_SPACE);

        int V_SPACE = 1;
        setY(y - V_SPACE);
        Sound.SHOOT.play();
    }
    
    /**
     * draw shot
     * @param g graphics
     */
    @Override
    public void render(Graphics2D g) {
        animation.tick();
        render(g, animation);
    }
}