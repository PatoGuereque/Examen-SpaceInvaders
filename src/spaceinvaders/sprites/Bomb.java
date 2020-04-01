package spaceinvaders.sprites;

import spaceinvaders.util.ImageLoader;

public class Bomb extends Sprite {

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
        setImage(ImageLoader.loadImage("/resources/bomb.png"));
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}