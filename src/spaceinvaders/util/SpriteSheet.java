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
public class SpriteSheet {

    private BufferedImage sheet;            //hold spritesheet
    
    /**
     * Creates a SpriteSheet from the image to be able to get the
     * individual frames easier.
     *
     * @param sheet image with spritesheet
     */
    public SpriteSheet(BufferedImage sheet){
        this.sheet = sheet;
    }
    
    /**
     * Cut spritesheet into individual frames
     * @param x x position
     * @param y y position
     * @param width width value
     * @param height height value
     * @return individual frame
     */
    public BufferedImage crop(int x, int y, int width, int height){
        return sheet.getSubimage(x, y, width, height);
    }
}
