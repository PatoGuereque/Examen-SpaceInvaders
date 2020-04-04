package spaceinvaders.sprites;

import java.awt.*;

import spaceinvaders.util.ImageLoader;

import java.awt.image.BufferedImage;
import spaceinvaders.util.Animation;
import spaceinvaders.util.Assets;

public class Bomb extends Sprite {

    private Animation animation;
    private boolean destroyed;

    public Bomb(int x, int y) {
        this.animation = new Animation(Assets.bomb, 200);
        initBomb(x, y);
        width = 30;
        height = 20;
    }

    private void initBomb(int x, int y) {
        setDestroyed(true);

        this.x = x;
        this.y = y;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void render(Graphics2D g) {
        animation.tick();
        render(g, animation);
    }
}