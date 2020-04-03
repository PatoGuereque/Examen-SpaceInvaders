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
public class Assets {
    public static BufferedImage sprites;
    public static BufferedImage explosion[];
    
    
    public static void init(){
        sprites = ImageLoader.loadImage("/images/spr.Explosion.png");
        SpriteSheet spritesheet = new SpriteSheet(sprites);
        explosion = new BufferedImage[5];
        
        for(int i = 0; i < 5; i ++){
            explosion[i] = spritesheet.crop(i*32, 0, 32, 32);
        }
    }
}
