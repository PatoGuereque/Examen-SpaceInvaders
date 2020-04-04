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
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import spaceinvaders.util.Animation;
import spaceinvaders.util.Assets;

public abstract class Sprite {

    private boolean visible;
    private boolean dying;
    private Animation explosion;
    
    protected double x;
    protected double y;
    protected double dx;
    protected double height;
    protected double width;

    public Sprite() {
        visible = true;
        this.explosion = new Animation(Assets.explosion, 100);
    }

    public void die() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    protected void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public void setDying(boolean dying) {
        this.dying = dying;
    }

    public boolean isDying() {
        return this.dying;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }
    
    abstract public void render(Graphics2D g);

    public void renderExplosion(Graphics2D g) {
        if(explosion.getIndex()!=4){
            this.explosion.tick();
            render(g, explosion);
        }
    }

    protected void render(Graphics2D g, Animation animation) {
        AffineTransform t = new AffineTransform();
        t.translate(x, y);
        t.scale(width/animation.getCurrentFrame().getWidth(), height/animation.getCurrentFrame().getHeight()); // scale = 1
        g.drawImage(animation.getCurrentFrame(), t, null);
    }
}
