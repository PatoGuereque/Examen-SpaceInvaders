package spaceinvaders.sprites;

import spaceinvaders.util.ImageLoader;

import java.awt.image.BufferedImage;

public class Bomb extends Sprite {

    private final static BufferedImage BOMB_IMAGE = ImageLoader.loadImage("/resources/bomb.png");
    private boolean destroyed;

    public Bomb(int x, int y) {
        initBomb(x, y);
        width *= 2;
        height *= 2;
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
}