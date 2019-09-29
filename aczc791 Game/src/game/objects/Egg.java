//Creates Egg shape
package game.objects;

import city.cs.engine.*;
import game.eventHandlers.Collideable;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Egg extends Walker implements Collideable {
    private Hero hero;
    private static SoundClip eggCollectedSound;
    static {
        try {
            eggCollectedSound = new SoundClip("data/sounds/chuckle.wav");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e);
        }
    }
    // physical borders of the shape
    private static final Shape eggShape = new PolygonShape(0.078f, 0.456f, 0.288f, 0.134f, 0.302f,
            -0.202f, 0.156f, -0.398f, -0.132f, -0.418f, -0.286f, -0.23f, -0.3f, 0.126f, -0.132f, 0.442f);
    //adds image to the shape
    private static BodyImage eggImage = new BodyImage("data/images/objects/egg.png");

    //Constructor
    public Egg(World world, Hero hero) {
        super(world, eggShape);
        addImage(eggImage);
        this.hero = hero;
    }

    @Override
    public void collisionResponse(Body b) {
        if (b == hero) {
            eggCollectedSound.play();
            hero.incrementLifes();
            //Message report
            System.out.println("+3 lifes ...I fell better \nCurrent lives: " + hero.getLifes());
            //Destroys the egg after is being taken
            destroy();
        }
    }
}
