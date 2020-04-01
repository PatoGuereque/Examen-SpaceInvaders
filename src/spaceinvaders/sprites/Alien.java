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
public class Alien extends Sprite {

    private Bomb bomb;

    public Alien(int x, int y) {
        initAlien(x, y);
    }

    private void initAlien(int x, int y) {
        this.x = x;
        this.y = y;
        height *= 2;
        width *= 2;

        bomb = new Bomb(x, y);
        setImage(ImageLoader.loadImage("/resources/alien.png"));
    }

    public void act(int direction) {
        this.x += direction;
    }

    public Bomb getBomb() {
        return bomb;
    }
}
