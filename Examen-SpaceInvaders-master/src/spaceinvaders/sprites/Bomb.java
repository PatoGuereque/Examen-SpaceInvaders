package spaceinvaders.sprites;

import java.awt.Graphics;
import spaceinvaders.util.ImageLoader;

import java.awt.image.BufferedImage;
import spaceinvaders.util.Animation;
import spaceinvaders.util.Assets;

public class Bomb extends Sprite {
    public Animation animation;
    private final static BufferedImage BOMB_IMAGE = ImageLoader.loadImage("/images/bomb.png");
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
        setImage(BOMB_IMAGE);
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void render(Graphics g) {
        animation.tick();
        g.drawImage(animation.getCurrentFrame() ,x, y, width, height, null);
    }
}