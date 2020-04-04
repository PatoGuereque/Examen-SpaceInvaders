package spaceinvaders.sprites;

import java.awt.*;
import spaceinvaders.util.Animation;
import spaceinvaders.util.Assets;

public class Bomb extends Sprite {

    private Animation animation;
    private boolean destroyed;

    /**
     * Creates a new bomb on the coordinates
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Bomb(int x, int y) {
        this.animation = new Animation(Assets.bomb, 200);
        initBomb(x, y);
        width = 30;
        height = 20;
    }

    /**
     * initialize bomb on the specified coordinates
     * @param x the x coordinate
     * @param y the y coordinate
     */
    private void initBomb(int x, int y) {
        setDestroyed(true);
        this.x = x;
        this.y = y;
    }

    /**
     * Set destroyed state
     * @param destroyed whether it's destroyed or not
     */
    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
    
    /**
     * @return true if the bom is destroyed
     */
    public boolean isDestroyed() {
        return destroyed;
    }
    
    /**
     * Draw bomb in game board
     * @param g graphics
     */
    @Override
    public void render(Graphics2D g) {
        animation.tick();
        render(g, animation);
    }
}