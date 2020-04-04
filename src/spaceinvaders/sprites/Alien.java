package spaceinvaders.sprites;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;

import spaceinvaders.util.ImageLoader;

import java.awt.image.BufferedImage;
import spaceinvaders.Commons;
import spaceinvaders.util.Animation;
import spaceinvaders.util.Assets;

/**
 * @author antoniomejorado
 */
public class Alien extends Sprite {

    private Bomb bomb;
    private Animation animation;

    public Alien(int x, int y) {
        this.animation = new Animation(Assets.alien, 200);
        initAlien(x, y);
    }

    private void initAlien(int x, int y) {
        this.x = x;
        this.y = y;

        bomb = new Bomb(x, y);
        width = Commons.ALIEN_WIDTH;
        height = Commons.ALIEN_HEIGHT;
    }

    public void move(double direction) {
        this.x += direction;
    }

    public Bomb getBomb() {
        return bomb;
    }

    @Override
    public void render(Graphics2D g) {
        animation.tick();
        render(g, animation);
    }
}
