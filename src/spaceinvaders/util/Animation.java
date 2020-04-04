/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvaders.util;

import java.awt.image.BufferedImage;

/**
 * 
 * @author insan
 */
public class Animation {

    private int speed;
    private int index;
    private long timer;
    private long lastTime;
    private BufferedImage[] frames;

    /**
     * Creates a new Animation with the array of images
     *
     * @param frames animation frames
     * @param speed animation play speed
     */
    public Animation(BufferedImage frames[], int speed){
        this.frames = frames;
        this.speed = speed;
        index = 0;
        timer = 0;
        lastTime = System.currentTimeMillis();
    }
    
    /**
     * @return current animation frame
     */
    public BufferedImage getCurrentFrame(){
        return frames[index];
    }
    
    /**
     * Reset the animation frame
     */
    public void reset() {
        this.index = 0;
    }
    
    /**
     * Update animation frame, if the current time is greater
     * than the last time plus the speed. This will also loop
     * the animation if it reached the end.
     */
    public void tick(){
        timer += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();
        
        if(timer > speed){
            index++;
            timer=0;
            if(index >= frames.length){
                index = 0;
            }
        }
    }
    
    /**
     * @return frame array index
     */
    public int getIndex() {
        return index;
    }
}
