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
    
    public BufferedImage getCurrentFrame(){
        return frames[index];
    }
    
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
}
