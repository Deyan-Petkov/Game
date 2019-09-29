package game.objects;

import city.cs.engine.*;
import game.eventHandlers.Collideable;
import game.Game;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * Doors in a game. When the actor collides with a door, if
 * the current level is complete the game is advanced to the
 * next level.
 */
public class Door extends StaticBody implements Collideable {
    private Game game;
    private static SoundClip gongHit;

    static {
        try {
            gongHit = new SoundClip("data/sounds/gongHit.wav");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e);
        }
    }

    /**
     * Initialise a new door.
     *
     * @param world The world.
     */

    private static final Shape gongShape = new PolygonShape(1.21f, 1.45f, 1.4f, -1.4f, -1.42f, -1.4f, -1.22f, 1.46f);
    private static BodyImage image = new BodyImage("data/images/objects/gong.png", 3f);


    public Door(World world, Game game) {
        super(world, gongShape);
        addImage(image);
        this.game = game;
    }

    @Override
    public void collisionResponse(Body b) {
        if (b == game.getHero()) {
            System.out.println("Going to next level...");
            game.goNextLevel();
        }
    }

    public void playGongSound() {
        gongHit.play();
        System.out.println("Loading gongHit sound.");

    }

}
