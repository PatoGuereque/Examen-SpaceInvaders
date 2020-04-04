package spaceinvaders.sprites;

import java.awt.*;
import spaceinvaders.util.Animation;
import spaceinvaders.util.Assets;

public class Bomb extends Sprite {
    private Animation animation;
    private boolean destroyed;

    /**
     * bomb constructor
     * @param x 
     * @param y 
     */
    public Bomb(int x, int y) {
        this.animation = new Animation(Assets.bomb, 200);
        initBomb(x, y);
        width = 30;
        height = 20;
    }

    /**
     * initialize bomb
     * @param x
     * @param y 
     */
    private void initBomb(int x, int y) {
        setDestroyed(true);
        this.x = x;
        this.y = y;
    }

    /**
     * set destroyed state
     * @param destroyed 
     */
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
    
    /**
     * evaluate destroyed state
     * @return destroyed
     */
    public boolean isDestroyed() {
        return destroyed;
    }
    
    /**
     * draw bomb in game board
     * @param g graphics
     */
    @Override
    public void render(Graphics2D g) {
        animation.tick();
        render(g, animation);
    }
}