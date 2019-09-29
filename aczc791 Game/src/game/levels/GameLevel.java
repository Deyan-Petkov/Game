package game.levels;

import city.cs.engine.*;
import city.cs.engine.Shape;
import game.Game;
import game.eventHandlers.InTouch;
import game.objects.*;
import org.jbox2d.common.Vec2;

import java.awt.*;
import java.util.ArrayList;

/**
 * The base level of the game.
 * Superclass for the rest of the levels.
 */
public abstract class GameLevel extends World {
    private Hero hero;
    private Turkey turkey;
    private Nettle nettle;
    private Door door;
    private Body wall;


    /**
     * Sets the common behavior for all levels.
     *
     * @param game The entry point for the program.
     */
    public void populate(Game game) {
        // Vertical line provoke the player to walk in right direction
        Shape wallShape = new BoxShape(0.001f, 1.5f);
        wall = new StaticBody(this, wallShape);
        wall.setPosition(new Vec2(-5f, -9f));

        //crating new instance of Hero and setting it up
        hero = new Hero(this);
        hero.setPosition(startPosition());
        hero.addCollisionListener(new InTouch(game.getGameLevel()));

        //crating new instance of Door and setting it up
        Door door = new Door(this, game);
        door.setPosition(doorPosition());
        door.addCollisionListener(new InTouch(this));
        this.door = door;

        //crating new instance of Turkey and setting it up
        turkey = new Turkey(this, this);
        turkey.setPosition(turkeyPosition());
        turkey.addCollisionListener(new InTouch(this));

        //Sets Nettle positions according to the given values from the overridden nettlePosition() in the Levels
        for (int i = 0; i < nettlePosition().size(); i++) {
            nettle = new Nettle(game.getHero().getWorld(), game.getHero());
            nettle.setPosition(nettlePosition().get(i));
            nettle.addCollisionListener(new InTouch(this));
        }
        //Gets the boolean value from ControlPanel and sets the background music according it
        if (!game.checkIfMute()) {
            if (game.getGameLevel() instanceof Level2) {
                getLevelSound().play();
            } else {
                getLevelSound().loop();
            }
        }

    }

    /**
     * Sets the position of the player when the game begins.
     * @return Player position
     */
    public abstract Vec2 startPosition();

    /**
     * Sets the position of the exit point form the level.
     * @return Door position in the current level.
     */
    public abstract Vec2 doorPosition();

    /**
     * Controls Cloud behaviour.
     */
    public abstract void cloudNavigation();

    /**
     * Defines turkey position for each level
     * @return Turkey position in the current level.
     */
    public abstract Vec2 turkeyPosition();

    /**
     * Holds the nettle positions for this level.
     * @return List with positions for all nettle objects
     * instantiated in the current level.
     */
    public abstract ArrayList<Vec2> nettlePosition();

    /**
     * Accessor for the background image in the current level.
     * @return The background image from the current level.
     */
    public abstract Image getBackgroundImg();

    /**
     * How many times the background picture is drawn in horizontal direction.
     * @return The number of times background was drawn.
     */
    public abstract int repeatImg();

    /**
     * Updates display value for the current level.
     * @return In which level the player is now.
     */
    public abstract String currentLevel();

    /**
     * Accessor for Turkey object.
     * @return Turkey object.
     */
    public Turkey getTurkey() {
        return turkey;
    }

    /**
     * Accessor for Hero object.
     * @return Hero object.
     */
    public Hero getHero() {
        return hero;
    }

    /**
     * Accessor for Door object.
     * @return Door object.
     */
    public Door getDoor() {
        return door;
    }

    /**
     * Holds the 'x' axis position for the door.
     * @return 'X' axis for Door object.
     */
    public int getDoorPos() {
        return (int) doorPosition().x;
    }

    /**
     * Provides access to the left wall in level4.
     * Used to control mushrooms walking direction.
     * @return Left wall in level4.
     * Must be overridden.
     */
    public Body getlWall() {
        return null;
    }

    /**
     * Provides access to the right wall in level4.
     * Used to control mushrooms walking direction.
     * @return Right wall in level4.
     * Must be overridden.
     */
    public Body getrWall() {
        return null;
    }

    /**
     * Accessor for Cloud object.
     * @return Cloud object.
     * Must be overridden.
     */
    public Cloud getCloud() {
        return null;
    }

    /**
     * Accessor for Mushroom object.
     * @return Mushroom object.
     * Must be overridden.
     */
    public Mushroom getMushroom() {
        return null;
    }

    /**
     * Accessor for the background music in the specific level.
     * @return The background melody for this level.
     */
    public abstract SoundClip getLevelSound();

}
