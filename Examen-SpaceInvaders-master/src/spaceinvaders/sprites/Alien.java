package spaceinvaders.sprites;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Graphics;
import spaceinvaders.util.ImageLoader;

import java.awt.image.BufferedImage;
import spaceinvaders.util.Animation;
import spaceinvaders.util.Assets;

/**
 * @author antoniomejorado
 */
public class Alien extends Sprite {

    private final static BufferedImage ALIEN_IMAGE = ImageLoader.loadImage("/images/alien.png");
    private Bomb bomb;
    public Animation animation;

    public Alien(int x, int y) {
        this.animation = new Animation(Assets.alien, 10);
        initAlien(x, y);
    }

    private void initAlien(int x, int y) {
        this.x = x;
        this.y = y;
        height *= 2;
        width *= 2;

        bomb = new Bomb(x, y);
        setImage(ALIEN_IMAGE);
    }

    public void move(int direction) {
        this.x += direction;
    }

    public Bomb getBomb() {
        return bomb;
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(animation.getCurrentFrame() ,x, y, width, height, null);
    }
}
