package spaceinvaders.sprites;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Graphics;
import spaceinvaders.sound.Sound;
import spaceinvaders.util.ImageLoader;

import java.awt.image.BufferedImage;
import spaceinvaders.util.Animation;
import spaceinvaders.util.Assets;

/**
 * @author antoniomejorado
 */
public class Shot extends Sprite {
    public Animation animation;
    private final static BufferedImage SHOT_IMAGE = ImageLoader.loadImage("/images/shot.png");
    public Shot() { }

    public Shot(int x, int y) {
        this.animation = new Animation(Assets.shot, 200);
        initShot(x, y);
    }

    @Override
    public void die() {
        super.die();
    }

    private void initShot(int x, int y) {
        setImage(SHOT_IMAGE);
        width = 30;
        height = 20;

        int H_SPACE = 6;
        setX(x + H_SPACE);

        int V_SPACE = 1;
        setY(y - V_SPACE);
        Sound.SHOOT.play();
    }

    @Override
    public void render(Graphics g) {
        animation.tick();
        g.drawImage(animation.getCurrentFrame() ,x, y, width, height, null);
    }
}