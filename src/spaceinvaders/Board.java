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
    private List<Alien> aliens;
    private Player player;
    private Shot shot;

    private final long speed = 5;
    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private String message = "Game Over";
    private Timer timer;

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
        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);

        int backgroundWidth = Assets.background.getWidth();
        int backgroundHeight = Assets.background.getHeight();
        for (int x = 0; x < Commons.BOARD_WIDTH + backgroundWidth; x+=backgroundWidth) {
            for (int y = 0; y < Commons.BOARD_HEIGHT + backgroundHeight; y+= backgroundHeight) {
                g.drawImage(Assets.background, x, y, backgroundWidth, backgroundHeight, null);
            }
        }

        g.setColor(Color.green);

        if (inGame) {
            g.drawLine(0, Commons.GROUND,
                    Commons.BOARD_WIDTH, Commons.GROUND);

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

    private void gameOver(Graphics g) {
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
    }

    private void tick() {
        if (deaths == Commons.NUMBER_OF_ALIENS_TO_DESTROY) {
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

    public List<Alien> getAliens() {
        return aliens;
    }

    public void end() {
        this.inGame = false;
    }

    public Shot getPlayerShot() {
        return shot;
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
            }
        }
    }
}