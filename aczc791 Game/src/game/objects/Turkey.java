//create Turkey body
package game.objects;

import city.cs.engine.*;
import game.eventHandlers.Collideable;
import game.levels.GameLevel;
import game.eventHandlers.InTouch;
import org.jbox2d.common.Vec2;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

public class Turkey extends Walker implements Collideable {

    private static SoundClip turkeySound;

    static {
        try {
            turkeySound = new SoundClip("data/sounds/kalkun.wav");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e);
        }
    }

    private World world;
    private GameLevel gameLevel;
    //play turkeySound if turkey still exist
    private boolean turkeyExists;
    //set the borders of the shape
    private static final Shape turkeyShape = new PolygonShape(
            0.29f, 1.43f, 1.08f, 0.28f, 1.08f, -0.32f,
            0.49f, -1.45f, -0.54f, -1.49f, -1.05f, -0.36f, -1.04f, 0.57f, -0.2f, 1.41f);
    //attach image to the shape
    private static BodyImage image = new BodyImage("data/images/objects/turkey.png", 3f);

    //Constructor
    public Turkey(World world, GameLevel gameLevel) {
        super(world, turkeyShape);
        this.world = world;
        this.gameLevel = gameLevel;
        addImage(image);
        turkeyExists = true;

    }

    @Override//takes control in term of collusion event
    public void collisionResponse(Body b) {
        if (b instanceof Nettle && turkeyExists) {
            System.out.println("Yummy :P");
            //Creates new egg
            Egg egg = new Egg(world, gameLevel.getHero());
            // set egg position
            egg.setPosition(new Vec2(this.getPosition()));
            //add Collusion to check if Hero collide with the Egg
            egg.addCollisionListener(new InTouch(gameLevel));
            //If they collide eggs get destroyed
            destroy();
            turkeyExists = false;
        } else if (b instanceof Mushroom) {
            b.destroy();
            System.out.println(" Yummy :P ");
        }

    }

    // turkey still exist and is not transformed true/false
    public boolean getTurkeyExists() {
        return turkeyExists;
    }

    public void playTurkeySound() {
        System.out.println("Loading turkey sound.");
        turkeySound.play();

    }
}
