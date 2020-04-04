/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spaceinvaders;

import spaceinvaders.sprites.Alien;
import spaceinvaders.sprites.Shot;
import spaceinvaders.sprites.Sprite;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Saves and load a game state
 * The structure of the file is:
 *
 * lives
 * playerX:playerY
 * shotX:shotY:alive
 * for i = 1 to alienCount
 *     alienX:alienY:alive
 *     bombX:bombY:alive
 *
 * @author Pato
 */
public class GameState {
    
    private final Board game;
    private final File saveFile;

    public GameState(Board game) {
        this.game = game;
        this.saveFile = new File("save.dat");
    }  
    
    /**
     * Loads a game from the save-file
     */
    public void load() {
        if (!saveFile.exists()) {
            return;
        }
        
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(saveFile))) {           
            String line = bufferedReader.readLine();

            // read lives, score, and time
            if (line != null) {
                int lives = Integer.parseInt(line);
                int score = Integer.parseInt(bufferedReader.readLine());
                long time = Long.parseLong(bufferedReader.readLine());
                game.getPlayer().setLives(lives);
                game.getPlayer().setScore(score);
                game.setStartTime(System.currentTimeMillis() - time);
            } else {
                return;
            }

            // read the player position
            line = bufferedReader.readLine();
            double playerX, playerY;
            String[] split = line.split(":");
            playerX = Double.parseDouble(split[0]);
            playerY = Double.parseDouble(split[1]);
            game.getPlayer().setX(playerX);
            game.getPlayer().setY(playerY);

            // read the player bullet position
            line = bufferedReader.readLine();
            double shotX, shotY;
            String[] shotSplit = line.split(":");
            if (Boolean.parseBoolean(shotSplit[2])) {
                shotX = Double.parseDouble(shotSplit[0]);
                shotY = Double.parseDouble(shotSplit[1]);
                game.setShot(new Shot(shotX, shotY));
            } else {
                game.getPlayerShot().die();
            }

            
            // we read all the aliens and bombs
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 6; j++) {
                    line = bufferedReader.readLine();

                    // read alien position and state
                    String[] alienSplit = line.split(":");
                    double alienX = Double.parseDouble(alienSplit[0]);
                    double alienY = Double.parseDouble(alienSplit[1]);
                    boolean alive = Boolean.parseBoolean(alienSplit[2]);

                    Alien alien = game.getAliens().get(i * 6 + j);
                    alien.setX(alienX);
                    alien.setY(alienY);
                    alien.setVisible(alive);
                    alien.setDying(!alive);

                    // read bomb position adn state
                    line = bufferedReader.readLine();
                    String[] bombSplit = line.split(":");
                    double bombX = Double.parseDouble(bombSplit[0]);
                    double bombY = Double.parseDouble(bombSplit[1]);
                    alive = Boolean.parseBoolean(bombSplit[2]);

                    alien.getBomb().setX(bombX);
                    alien.getBomb().setY(bombY);
                    alien.getBomb().setVisible(alive);
                    alien.getBomb().setDying(!alive);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Saves the game state into a file
     */
    public void save() {
        StringBuilder saveString = new StringBuilder();
        
        // save lives, score and time
        saveString.append(game.getPlayer().getLives()).append('\n');
        saveString.append(game.getPlayer().getScore()).append('\n');
        saveString.append(System.currentTimeMillis() - game.getStartTime()).append('\n');

        // save player and shot
        saveString.append(serializeObject(game.getPlayer()));
        saveString.append(serializeObject(game.getPlayerShot()));
        
        // save all aliens and bombs
        for (Alien alien : game.getAliens()) {
            saveString.append(serializeObject(alien));
            saveString.append(serializeObject(alien.getBomb()));
        }
        
        // we save it into the file 
        try (FileWriter fileWriter = new FileWriter(saveFile, false)) {
            fileWriter.write(saveString.toString());
            fileWriter.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }   
    
    /**
     * Serializes a game sprite into a readable format by the game
     * The format for each serializable object is
     * x:y:alive
     * 
     * @param item the item to serialize
     * @return the serializable string
     */
    private String serializeObject(Sprite item) {
        return item.getX() + ":" + item.getY() + ":" + item.isVisible() + "\n";
    }
}
