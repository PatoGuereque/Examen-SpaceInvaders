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
    public static BufferedImage shot[];
    public static BufferedImage bomb[];
    public static BufferedImage playerIdle[];
    public static BufferedImage playerLeft[];
    public static BufferedImage playerRight[];

    
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

        SpriteSheet playerSheet = new SpriteSheet(ImageLoader.loadImage("/images/spr.Player_Ship.png"));
        playerIdle = new BufferedImage[2];
        playerLeft = new BufferedImage[2];
        playerRight = new BufferedImage[2];
        for(int i=0; i<2; i++){
            playerIdle[i] = playerSheet.crop(i*32, 0, 32, 32);
            playerLeft[i] = playerSheet.crop(i*32, 32, 32, 32);
            playerRight[i] = playerSheet.crop(i*32, 64, 32, 32);
        }

        SpriteSheet shotSheet = new SpriteSheet(ImageLoader.loadImage("/images/spr.Shot.png"));
        shot = new BufferedImage[4];
        for(int i = 0; i < 4; i ++){
            shot[i] = shotSheet.crop(i*32, 0, 32, 32);
        }

        SpriteSheet bombSheet = new SpriteSheet(ImageLoader.loadImage("/images/spr.Bomb.png"));
        bomb = new BufferedImage[4];
        for(int i=0; i<4; i++){
            bomb[i] = bombSheet.crop(i*32, 0, 32, 32);
        }
    }
}
