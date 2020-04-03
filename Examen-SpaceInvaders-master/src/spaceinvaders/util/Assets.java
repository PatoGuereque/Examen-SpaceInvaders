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
    //public static BufferedImage explosionSheet;
    public static BufferedImage explosion[];
    public static BufferedImage alien[];
    
    
    public static void init(){
        SpriteSheet explosionSheet = new SpriteSheet(ImageLoader.loadImage("/images/spr.Explosion.png"));
        explosion = new BufferedImage[5];
        
        for(int i = 0; i < 5; i ++){
            explosion[i] = explosionSheet.crop(i*32, 0, 32, 32);
        }
        
        SpriteSheet alienSheet = new SpriteSheet(ImageLoader.loadImage("/images/spr.Alien.png"));
        alien = new BufferedImage[5];
        
        for(int i = 0; i < 5; i ++){
            alien[i] = alienSheet.crop(i*32, 0, 32, 32);
        }
    }
}
