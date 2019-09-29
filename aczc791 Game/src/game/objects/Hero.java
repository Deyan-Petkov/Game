//Create Hero Body
package game.objects;

import city.cs.engine.*;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;


public class Hero extends Walker {

    private int lifes, eggs;
    private static SoundClip lostLifeSound;

    static {
        try {
            lostLifeSound = new SoundClip("data/sounds/lostLifeSound.wav");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e);
        }
    }

    //declare Hero shape dimensions
    private static final Shape heroShape = new PolygonShape(0.55f, 1.77f, 1.11f, -0.19f, 0.71f, -1.85f,
            -0.75f, -1.82f, -1.13f, -0.42f, -0.83f, 1.38f, -0.24f, 1.79f);
    //attach image to Hero shape
    private static final BodyImage image = new BodyImage("data/images/objects/hero.png", 4f);

    //Hero constructor
    public Hero(World world) {
        super(world, heroShape);
        addImage(image);
        lifes = 10;
        eggs = 0;
    }//Hero life getter

    public int getLifes() {
        return lifes;
    }

    //used to transfer hero lifes between levels
    public void setLifes(int lifes) {
        this.lifes = lifes;
    }


    //Hero life setter
    public void incrementLifes() {
        this.lifes += 3;
        eggs++;
    }

    public void decrementLifes() {
        lostLifeSound.play();
        System.out.println("Loading Lost Life Sound.");
        this.lifes -= 1;
    }

    public int getEggs() {
        return eggs;
    }

    public void setEggs(int eggs) {
        this.eggs = eggs;
    }

}
