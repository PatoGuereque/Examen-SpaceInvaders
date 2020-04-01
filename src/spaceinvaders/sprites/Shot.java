package spaceinvaders.sprites;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import spaceinvaders.util.ImageLoader;

/**
 * @author antoniomejorado
 */
public class Shot extends Sprite {

    public Shot() { }

    public Shot(int x, int y) {
        initShot(x, y);
    }

    private void initShot(int x, int y) {
        setImage(ImageLoader.loadImage("/resources/shot.png"));
        width *= 2;
        height *= 2;

        int H_SPACE = 6;
        setX(x + H_SPACE);

        int V_SPACE = 1;
        setY(y - V_SPACE);
    }
}