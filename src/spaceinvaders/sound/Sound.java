package spaceinvaders.sound;

/**
 * The enum containing all the different sounds in the game
 */
public enum Sound {

    /**
     * The sound when a player explodes
     */
    EXPLOSION("explosion"),

    /**
     * The invader sounds when they move, they are all similar
     * just different tones. They are cycled
     */
    INVADER1("fastinvader1"),
    INVADER2("fastinvader2"),
    INVADER3("fastinvader3"),
    INVADER4("fastinvader4"),

    /**
     * Sound when a player shoots
     */
    SHOOT("shoot"),

    /**
     * The alien death sound
     */
    DEATH("retro_beep_04");

    private SoundClip soundClip;

    /**
     * Sound constructor, takes a sound name and looks for it in the /sounds/ folder
     * <strong>Only accepts .wav</strong>
     *
     * @param soundFileName the name of the sound file
     */
    Sound(String soundFileName) {
        soundClip = new SoundClip();
        soundClip.load("/sounds/" + soundFileName + ".wav");
    }

    /**
     * Plays the sound without looping
     */
    public void play() {
        soundClip.setLooping(false);
        soundClip.play();
    }

    /**
     * Plays the sound and loops it
     */
    public void playLoop() {
        soundClip.setLooping(true);
        soundClip.play();
    }

    /**
     * Softly stops the sound if it's looping, doesn't abruptly cut it
     */
    public void stop() {
        soundClip.setLooping(false);
        soundClip.stop();
    }
}
