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

    // Graphic related stuff
    // the scene to draw to
    private BufferedImage scene;
    // the 2d graphics allowing us to have pixel perfect positions
    private Graphics2D g2d;
    // the screen dimensions
    private Dimension dimension;

    // Game related objects
    private GameState gameState;
    private List<Alien> aliens;
    private Player player;
    private Shot shot;

    // the alien movement speed
    private final long alienSpeed = 5;
    // the direction in which the alien is moving
    private int alienDirection = -1;
    private int deaths = 0;

    // To know when the game is over
    private boolean inGame = true;
    private String message = "Game Over";

    // the start time and game loop timer
    private Timer timer;
    private long startTime;
    private int time;

    // variables used to know when to play the next sound
    private final long nextMoveInterval = 50;
    private long lastMove = System.currentTimeMillis();
    private int moves = 0;
    private Sound[] moveSounds = new Sound[]{Sound.INVADER1, Sound.INVADER2, Sound.INVADER3, Sound.INVADER4};

    public Board() {
        initBoard();
        gameInit();
    }

    /**
     * Initializes our window and scene
     */
    private void initBoard() {
        // initialize all the graphics
        scene = new BufferedImage(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT, 1);
        g2d = scene.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gameState = new GameState(this);

        // setup our input listener
        addKeyListener(new TAdapter());

        // setup our window properties
        setFocusable(true);
        dimension = new Dimension(Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
        setBackground(Color.black);

        // Load all of our assets
        Assets.init();

        // start the game cycle
        timer = new Timer(Commons.DELAY, new GameCycle());
        timer.start();

        // Initialize our game
        gameInit();
    }

    /**
     * Initializes our game objects
     */
    private void gameInit() {
        // Initialize our aliens
        startTime = System.currentTimeMillis();
        aliens = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                Alien alien = new Alien(Commons.ALIEN_INIT_X + 36 * j,
                        Commons.ALIEN_INIT_Y + 36 * i);
                aliens.add(alien);
            }
        }

        // Initialize our player objects
        player = new Player(this);
        shot = new Shot();
    }

    /**
     * Method to render the aliens
     * This will render the aliens if they are alive and dying
     *
     * @param g the graphics to draw to
     */
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

    /**
     * Draws the player in the graphics if visible
     *
     * @param g the graphics
     */
    private void drawPlayer(Graphics2D g) {
        if (player.isVisible()) {
            player.render(g);
        }
    }

    /**
     * Draws the player's bullet if visible
     *
     * @param g the graphics
     */
    private void drawShot(Graphics2D g) {
        if (shot.isVisible()) {
            shot.render(g);
        }
    }

    /**
     * Draws all the aliens' falling bombs
     *
     * @param g the graphics
     */
    private void drawBombing(Graphics2D g) {
        for (Alien a : aliens) {
            Bomb b = a.getBomb();

            if (!b.isDestroyed()) {
                b.render(g);
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param g the graphics
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // clear current canvas
        g2d.clearRect(0, 0, Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);

        // draw the game to the graphics
        // The way this works is that the Graphics2D is bound to an empty image
        // so we draw everything to the image and then we draw the image to the
        // real graphics
        doDrawing(g2d);

        // draw the scene
        g.drawImage(scene, 0, 0, this);
    }

    /**
     * Draws the current game
     *
     * @param g the graphics
     */
    private void doDrawing(Graphics2D g) {
        g.drawImage(Assets.background, 0, 0, Commons.BOARD_HEIGHT * Assets.background.getWidth() / Assets.background.getHeight(),
                Commons.BOARD_HEIGHT, null);
        g.setColor(Color.black);
        g.fillRect(0, 0, dimension.width, dimension.height);

        if (inGame) {
            drawLivesAndAlive(g2d);

            // draw the ground
            g.setColor(Color.green);
            g.drawLine(0, Commons.GROUND,
                    Commons.BOARD_WIDTH, Commons.GROUND);

            // draw all our sprites
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

    /**
     * Draws the lives, score and aliens alive text on the screen
     *
     * @param g the graphuics
     */
    private void drawLivesAndAlive(Graphics2D g) {
        String text = "Aliens Alive: " + getAliveCount();
        Font font = new Font("Helvetica", Font.BOLD, 15);
        // Get the FontMetrics
        FontMetrics metrics = g.getFontMetrics(font);
        // Determine the X coordinate for the text
        int x = (Commons.BOARD_WIDTH / 2 - metrics.stringWidth(text)) - 20;
        int y = 20;
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(text, x, y);

        g.setColor(Color.WHITE);
        // draw the lives and score on the top right of the screen
        g.drawString("Lives: " + player.getLives() + " ❤", Commons.BOARD_WIDTH / 2 + 20, 20);
        g.drawString("Score " + player.getScore(), Commons.BOARD_WIDTH - 200, 20);
        g.drawString("Time: " + time, 20, 20);
    }

    /**
     * Draws the game over screen. This utilizes the internal "message" variable
     * in order to display if the player won or lost
     *
     * @param g the graphics
     */
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

    /**
     * Ticks the whole game
     * This checks for collisions, moves all sprites and also
     * makes the aliens drops the bombs. Additionally this checks
     * if the game is over or not
     */
    private void tick() {
        //update time
        time = (int) (System.currentTimeMillis() - startTime) / 1000;

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

            // check if the player's bullet hits an alien
            for (Alien alien : aliens) {
                double alienX = alien.getX();
                double alienY = alien.getY();

                // collision detection
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

            // moves the bullet
            double y = shot.getY();
            y -= 8;

            if (y < 0) {
                shot.die();
            } else {
                shot.setY(y);
            }
        }

        // move aliens
        moveAliens();

        // bombs
        Random generator = new Random();

        // We randomly drop bombs using the chance * the alive count
        // this makes it so we consistently drop bombs randomly
        for (Alien alien : aliens) {
            int shot = generator.nextInt(Math.max(Commons.CHANCE * getAliveCount(), 1));
            Bomb bomb = alien.getBomb();

            // drop a new bomb by resetting the dead one
            if (shot == 1 && alien.isVisible() && bomb.isDestroyed()) {
                bomb.setDestroyed(false);
                bomb.setX(alien.getX());
                bomb.setY(alien.getY());
            }

            double bombX = bomb.getX();
            double bombY = bomb.getY();
            double playerX = player.getX();
            double playerY = player.getY();

            // check for bomb collisions with the player
            if (player.isVisible() && !bomb.isDestroyed()) {
                if (bombX >= (playerX)
                        && bombX <= (playerX + Commons.PLAYER_WIDTH)
                        && bombY >= (playerY)
                        && bombY <= (playerY + Commons.PLAYER_HEIGHT)) {

                    player.damage();
                    bomb.setDestroyed(true);
                }
            }

            // check if the bomb is out of bounds
            if (!bomb.isDestroyed()) {
                bomb.setY(bomb.getY() + 2);

                if (bomb.getY() >= Commons.GROUND - Commons.BOMB_HEIGHT) {
                    bomb.setDestroyed(true);
                }
            }
        }
    }

    /**
     * Move the aliens
     * This also plays the moving sound and speeds up as there is less aliens
     */
    private void moveAliens() {
        // we calculate our move speed
        double moveSpeed = (double) (((Commons.NUMBER_OF_ALIENS_TO_DESTROY - getAliveCount() + 1)) * alienSpeed) / ((double) Commons.ALIEN_WIDTH);

        // calculate when the next move sound will play
        if (System.currentTimeMillis() - lastMove > getAliveCount() * nextMoveInterval) {
            lastMove = System.currentTimeMillis();
            moveSounds[moves++ % 4].play();
        }

        // We check all aliens to see if they are still able to move without going out of the screen
        // Additionally we move the aliens down if one gets out of bounds and change direction
        for (Alien alien : aliens) {
            double x = alien.getX();

            // check right border
            if (x >= Commons.BOARD_WIDTH - Commons.BORDER_RIGHT && alienDirection != -1) {
                alienDirection = -1;

                for (Alien a2 : aliens) {
                    a2.setY(a2.getY() + Commons.GO_DOWN);
                }
            }

            // check left border
            if (x <= Commons.BORDER_LEFT && alienDirection != 1) {
                alienDirection = 1;

                for (Alien a : aliens) {
                    a.setY(a.getY() + Commons.GO_DOWN);
                }
            }
        }

        // Move all the aliens and check if they invaded
        for (Alien alien : aliens) {
            if (alien.isVisible()) {
                double y = alien.getY();

                if (y > Commons.GROUND - Commons.ALIEN_HEIGHT) {
                    inGame = false;
                    message = "Invasion!";
                }

                alien.move((alienDirection * moveSpeed));
            }
        }
    }

    /**
     * Gets how many aliens remain alive
     *
     * @return the alive count
     */
    private int getAliveCount() {
        int alive = 0;
        for (Alien alien : aliens) {
            if (alien.isVisible()) {
                alive++;
            }
        }
        return alive;
    }

    /**
     * Method called by the {@link GameCycle} to tick and draw
     */
    private void doGameCycle() {
        tick();
        repaint();
    }

    /**
     * @return the starting game time
     */
    public int getTime() {
        return time;
    }

    /**
     * Sets the starting game time
     *
     * @param time the game time
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     * @return all the aliens in the game
     */
    public List<Alien> getAliens() {
        return aliens;
    }

    /**
     * Ends the game by setting the inGame variable to false
     */
    public void end() {
        this.inGame = false;
    }

    /**
     * @return the player's shot sprite
     */
    public Shot getPlayerShot() {
        return shot;
    }

    /**
     * Sets the player's shot
     *
     * @param shot the shot
     */
    public void setShot(Shot shot) {
        this.shot = shot;
    }

    /**
     * Gets the game player object
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Class in charge of calling the tick and draw
     * This class is initialized and passed to a {@link java.util.Timer}
     */
    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    /**
     * Our input handler
     */
    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // pass the key presses to the player
            player.keyPressed(e);

            double x = player.getX();
            double y = player.getY();
            int key = e.getKeyCode();

            // shoot on space bar or restart the game if the game ended
            if (key == KeyEvent.VK_SPACE) {
                if (inGame) {
                    if (!shot.isVisible()) {
                        shot = new Shot(x, y);
                    }
                } else {
                    inGame = true;
                    timer.start();
                    gameInit();
                }
            }

            if (inGame) {
                // save and load the game with G and C
                if (key == KeyEvent.VK_G) {
                    gameState.save();
                } else if (key == KeyEvent.VK_C) {
                    gameState.load();
                }
            }
        }
    }
}