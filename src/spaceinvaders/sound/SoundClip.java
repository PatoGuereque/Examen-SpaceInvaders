
package spaceinvaders.sound;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import java.io.IOException;
import java.net.URL;


/**
 * @author antoniomejorado
 */
public class SoundClip {

    private AudioInputStream sample;
    private Clip clip;
    private boolean looping = false;

    /**
     * Creates a SoundClip without loading anything.
     * After creating the object using this constructor, one
     * must call {@link SoundClip#load(String)} to load a sound
     */
    public SoundClip() {
        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            System.out.println("Error en " + e.toString());
        }
    }

    /**
     * Creates a SoundClip and loads the
     * sound from the provided path
     *
     * @param filePath the sound file path
     */
    public SoundClip(String filePath) {
        this();
        load(filePath);
    }

    /**
     * Sets whether or not the clip will be
     * played continuously or just once
     *
     * @param looping if the clip should loop
     */
    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    /**
     * Gets the resource url from our classloader
     *
     * @param filename the sound file name
     * @return the file url
     */
    private URL getURL(String filename) {
        URL url = null;
        try {
            url = this.getClass().getResource(filename);
        } catch (Exception e) {

            System.out.println("Error en " + e.toString());
        }
        return url;
    }

    /**
     * Gets if the clip is loaded
     * @return true if the clip is loaded
     */
    public boolean isLoaded() {
        return sample != null;
    }

    /**
     * Loads the audio clip from the specified file
     * @param audiofile the audio file path
     * @return true if it successfully loaded the sound
     */
    public boolean load(String audiofile) {
        try {
            sample = AudioSystem.getAudioInputStream(getURL(audiofile));
            clip.open(sample);
            return true;
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            System.out.println("Error en " + e.toString());
            return false;
        }
    }

    /**
     * Plays the sound.
     * <p>
     * If {@link SoundClip#setLooping(boolean)} was set to true, it will
     * play it continuously.
     */
    public void play() {
        if (!isLoaded())
            return;

        if (!clip.isRunning()) {
            clip.setFramePosition(0);
        }

        if (looping)
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        else
            clip.loop(0);
    }

    /**
     * Stops the loop if there is any
     */
    public void stop() {
        clip.loop(0);
    }
}
