package spaceinvaders.sound;

public enum Sound {

    EXPLOSION("explosion"),
    INVADER1("fastinvader1"),
    INVADER2("fastinvader2"),
    INVADER3("fastinvader3"),
    INVADER4("fastinvader4"),
    SHOOT("shoot"),
    DEATH("invaderkilled"),
    ufo_high("ufo_highpitch"),
    UFGO_LOW("ufo_lowpitch");

    private SoundClip soundClip;

    Sound(String soundName) {
        soundClip = new SoundClip();
        soundClip.load("/sounds/" + soundName + ".wav");
    }

    public void play() {
        soundClip.setLooping(false);
        soundClip.play();
    }

    public void playLoop() {
        soundClip.setLooping(true);
        soundClip.play();
    }

    public void stop() {
        soundClip.setLooping(false);
        soundClip.stop();
    }
}
