package spaceinvaders.sprites;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author antoniomejorado
 */
import java.awt.*;
import java.awt.geom.AffineTransform;
import spaceinvaders.util.Animation;
import spaceinvaders.util.Assets;

public abstract class Sprite {

    private boolean visible;
    private boolean dying;
    Animation explosion;
    
    protected double x;
    protected double y;
    protected double dx;
    protected double height;
    protected double width;

    /**
     * sprite constructor
     */
    public Sprite() {
        visible = true;
        this.explosion = new Animation(Assets.explosion, 100);
    }

    /**
     * kills sprite
     */
    public void die() {
        visible = false;
    }
    
    /**
     * evaluate visibility
     * @return visible
     */
    public boolean isVisible() {
        return visible;
    }
    
    /**
     * set visibility
     * @param visible if the sprite is visible
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    /**
     * set x position
     * @param x the x coordinate
     */
    public void setX(double x) {
        this.x = x;
    }
    
    /**
     * set y position
     * @param y the y coordinate
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * get y position
     * @return the y position
     */
    public double getY() {
        return y;
    }
    
    /**
     * get x position
     * @return the x position
     */
    public double getX() {
        return x;
    }
    
    /**
     * set dying state
     * @param dying if the sprite is dying
     */
    public void setDying(boolean dying) {
        this.dying = dying;
    }
    
    /**
     * evaluate dying state
     * @return dying
     */
    public boolean isDying() {
        return this.dying;
    }
    
    /**
     * gets sprite height
     * @return height
     */
    public double getHeight() {
        return height;
    }
    
    /**
     * gets sprite width
     * @return width
     */
    public double getWidth() {
        return width;
    }
    
    /**
     * render method
     * @param g graphics
     */
    abstract public void render(Graphics2D g);

    /**
     * render explosion
     * @param g graphics
     */
    public void renderExplosion(Graphics2D g) {
        if(explosion.getIndex()!=4){
            this.explosion.tick();
            render(g, explosion);
        }
    }
    
    /**
     * render sprite
     * @param g graphics
     * @param animation sprite animation
     */
    protected void render(Graphics2D g, Animation animation) {
        AffineTransform t = new AffineTransform();
        t.translate(x, y);
        t.scale(width/animation.getCurrentFrame().getWidth(), height/animation.getCurrentFrame().getHeight()); // scale = 1
        g.drawImage(animation.getCurrentFrame(), t, null);
    }
}
