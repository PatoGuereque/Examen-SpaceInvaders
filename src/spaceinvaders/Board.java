package spaceinvaders;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import spaceinvaders.sound.Sound;
import spaceinvaders.sprites.Alien;
import spaceinvaders.sprites.Bomb;
import spaceinvaders.sprites.Player;
import spaceinvaders.sprites.Shot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import spaceinvaders.util.Assets;

/**
 * @author antoniomejorado
 */
public class Board extends JComponent {

    private BufferedImage scene;
    private Graphics2D g2d;
    private Dimension d;
    private GameState gameState;
    private List<Alien> aliens;
    private Player player;
    private Shot shot;

    private final long speed = 5;
    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private String message = "Game Over";
    private Timer timer;
    private long startTime;
    private int time;

    private final long nextMoveInterval = 50;
    private long lastMove = System.currentTimeMillis();
    private int moves = 0;
    private Sound[] moveSounds = new Sound[] {Sound.INVADER1, Sound.INVADER2, Sound.INVADER3, Sound.INVADER4};

    public Board() {
        initBoard();
        gameInit();
    }

    private void initBoard() {
        scene = new BufferedImage(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT, 1);
        g2d = scene.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gameState = new GameState(this);

        addKeyListener(new TAdapter());
        setFocusable(true);
        d = new Dimension(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
        setBackground(Color.black);
        Assets.init();
        timer = new Timer(Commons.DELAY, new GameCycle());
        timer.start();

        gameInit();
    }


    private void gameInit() {
        startTime = System.currentTimeMillis();
        aliens = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                Alien alien = new Alien(Commons.ALIEN_INIT_X + 36 * j,
                        Commons.ALIEN_INIT_Y + 36 * i);
                aliens.add(alien);
            }
        }

        player = new Player(this);
        shot = new Shot();
    }

    private void drawAliens(Graphics2D g) {
        for (Alien alien : aliens) {
            if (alien.isVisible()) {
                alien.render(g);
            }

            if (alien.isDying()) {
                alien.renderExplosion(g);
                alien.die();
            }
        }
    }

    private void drawPlayer(Graphics2D g) {
        if (player.isVisible()) {
            player.render(g);
        }
    }

    private void drawShot(Graphics2D g) {
        if (shot.isVisible()) {
            shot.render(g);
        }
    }

    private void drawBombing(Graphics2D g) {
        for (Alien a : aliens) {
            Bomb b = a.getBomb();

            if (!b.isDestroyed()) {
                b.render(g);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g2d.clearRect(0, 0, Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

        doDrawing(g2d);

        g.drawImage(scene, 0, 0, this);
    }

    private void doDrawing(Graphics2D g) {
        g.drawImage(Assets.background,0,0, Commons.BOARD_HEIGHT*Assets.background.getWidth()/Assets.background.getHeight(),
                Commons.BOARD_HEIGHT,null);
        if (inGame) {
            drawLivesAndAlive(g2d);
            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawBombing(g);
        } else {
            if (timer.isRunning()) {
                timer.stop();
            }

            gameOver(g);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawLivesAndAlive(Graphics2D g) {
        String text = "Aliens Alive: " + getAliveCount();
        Font font = new Font("Helvetica", Font.BOLD, 15);
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = (Commons.BOARD_WIDTH/2 - metrics.stringWidth(text)) - 20;
        // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
        int y = 20;
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(text, x, y);

        g.setColor(Color.WHITE);
        // draw the lives and score on the top right of the screen
        g.drawString("Lives: " + player.getLives() + " ❤", Commons.BOARD_WIDTH/2 + 20, 20);
        g.drawString("Score " + player.getScore(), Commons.BOARD_WIDTH - 200, 20);
        g.drawString("Time: " + time, 20, 20);
    }

    private void gameOver(Graphics g) {
        String replay = "Press space to play again"; 
        g.setColor(Color.black);
        g.fillRect(0, 0, Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, Commons.BOARD_WIDTH / 2 - 30, Commons.BOARD_WIDTH - 100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (Commons.BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
                Commons.BOARD_WIDTH / 2);
        g.drawString(replay, (Commons.BOARD_WIDTH - fontMetrics.stringWidth(replay)) / 2,
                Commons.BOARD_WIDTH / 2 + 40);
    }

    private void tick() {
        //update time
        time =(int) (System.currentTimeMillis() - startTime)/1000;
        
        //check win state
        if (getAliveCount() == 0) {
            inGame = false;
            timer.stop();
            message = "Game won!";
        }

        // player
        player.tick();

        if (player.isDying()) {
            return;
        }

        // shot
        if (shot.isVisible()) {
            double shotX = shot.getX();
            double shotY = shot.getY();

            for (Alien alien : aliens) {
                double alienX = alien.getX();
                double alienY = alien.getY();

                if (alien.isVisible() && shot.isVisible()) {
                    if (shotX >= (alienX)
                            && shotX <= (alienX + Commons.ALIEN_WIDTH)
                            && shotY >= (alienY)
                            && shotY <= (alienY + Commons.ALIEN_HEIGHT)) {

                        alien.setDying(true);
                        Sound.DEATH.play();
                        deaths++;
                        player.setScore(player.getScore() + 50);
                        shot.die();
                    }
                }
            }

            double y = shot.getY();
            y -= 8;

             if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }

        // aliens
        moveAliens();

        // bombs
        Random generator = new Random();

        for (Alien alien : aliens) {
            int shot = generator.nextInt(Math.max(Commons.CHANCE * getAliveCount(), 1));
            Bomb bomb = alien.getBomb();

            if (shot == 1 && alien.isVisible() && bomb.isDestroyed()) {
                bomb.setDestroyed(false);
                bomb.setX(alien.getX());
                bomb.setY(alien.getY());
            }

            double bombX = bomb.getX();
            double bombY = bomb.getY();
            double playerX = player.getX();
            double playerY = player.getY();

            if (player.isVisible() && !bomb.isDestroyed()) {
                if (bombX >= (playerX)
                        && bombX <= (playerX + Commons.PLAYER_WIDTH)
                        && bombY >= (playerY)
                        && bombY <= (playerY + Commons.PLAYER_HEIGHT)) {

                    player.damage();
                    bomb.setDestroyed(true);
                }
            }

            if (!bomb.isDestroyed()) {
                bomb.setY(bomb.getY() + 2);

                if (bomb.getY() >= Commons.GROUND - Commons.BOMB_HEIGHT) {
                    bomb.setDestroyed(true);
                }
            }
        }
    }

    private void moveAliens() {
        double move = (double) (((Commons.NUMBER_OF_ALIENS_TO_DESTROY - getAliveCount() + 1)) * speed) / ((double) Commons.ALIEN_WIDTH);
        if (System.currentTimeMillis() - lastMove > getAliveCount() * nextMoveInterval) {
            lastMove = System.currentTimeMillis();
            moveSounds[moves++ % 4].play();
        }

        for (Alien alien : aliens) {
            double x = alien.getX();

            if (x >= Commons.BOARD_WIDTH - Commons.BORDER_RIGHT && direction != -1) {
                direction = -1;

                for (Alien a2 : aliens) {
                    a2.setY(a2.getY() + Commons.GO_DOWN);
                }
            }

            if (x <= Commons.BORDER_LEFT && direction != 1) {
                direction = 1;

                for (Alien a : aliens) {
                    a.setY(a.getY() + Commons.GO_DOWN);
                }
            }
        }

        for (Alien alien : aliens) {
            if (alien.isVisible()) {
                double y = alien.getY();

                if (y > Commons.GROUND - Commons.ALIEN_HEIGHT) {
                    inGame = false;
                    message = "Invasion!";
                }

                alien.move((direction * move));
            }
        }
    }

    private int getAliveCount() {
        int alive = 0;
        for (Alien alien : aliens) {
            if (alien.isVisible()) {
                alive++;
            }
        }
        return alive;
    }

    private void doGameCycle() {
        tick();
        repaint();
    }

    public int getTime(){
        return time;
    }
    
    public void setTime(int time){
        this.time = time;
    }
    
    public List<Alien> getAliens() {
        return aliens;
    }

    public void end() {
        this.inGame = false;
    }

    public Shot getPlayerShot() {
        return shot;
    }

    public void setShot(Shot shot) {
        this.shot = shot;
    }

    public Player getPlayer() {
        return player;
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);

            double x = player.getX();
            double y = player.getY();
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE) {
                if (inGame) {
                    if (!shot.isVisible()) {
                        shot = new Shot(x, y);
                    }
                }
                else{
                    inGame = true;
                    timer.start();
                    gameInit();
                }
            }

            if (key == KeyEvent.VK_G) {
                gameState.save();
            } else if (key == KeyEvent.VK_C) {
                gameState.load();
            }
        }
    }
}