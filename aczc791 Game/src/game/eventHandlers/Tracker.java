package game.eventHandlers;

import city.cs.engine.*;
import game.levels.GameLevel;
import game.levels.Level4;
import game.objects.Hero;
import org.jbox2d.common.Vec2;

/**
 * This class implements StepListener which gives control to the
 * code inside the methods body at each step execution.
 * This class is responsible for background move in opposite direction according to
 * the player.
 * Because takes control periodically this class also implements big part of Cloud object behaviour for all levels.
 * Detects when player approach the final point of the level and plays the gongSound.
 */
public class Tracker implements StepListener {

    private MyView view;
    private Hero hero;
    private GameLevel world;
    //holds the calculated difference between hero and the gong(door)
    private int levelProgress;
    private boolean gongSoundPlayed;

    /**
     * @param view  View of the simulation.
     * @param world Level of the game.
     */
    public Tracker(MyView view, GameLevel world) {
        this.view = view;
        this.world = world;
        this.hero = world.getHero();
        gongSoundPlayed = false;
    }

    /**
     * Centers the view according to hero position at x axis
     * (for level 4 to y axis too so player can simulate moving upwards)
     * and y axis is set to the View(constant).
     *Convert from world coordinates to screen coordinates in the current view.
     */
    public void postStep(StepEvent e) {

        if (world instanceof Level4) {//centers the background at Hero in 4th level
            view.setCentre(new Vec2(hero.getPosition().x, hero.getPosition().y));
            //Calculate distance to the door for each level to be displayed
            levelProgress = (int) (world.doorPosition().y - world.getHero().getPosition().y);
        } else {
            //Sets Hero position X as center of the world and gets Y position from View so when Hero jumps
            // it stays still but when he walks everything moves in opposite direction.
            view.setCentre(new Vec2(hero.getPosition().x, view.getY() - 2.7f));
            //translate the world(Vec2) dimension to  view(pixels) which
            // allows the view to move in opposite direction according Hero too.
            view.setImageOffset((int) (view.worldToView(new Vec2(0, 0)).x - 450));
            //calculates the distance between Hero and the gong(door)
            levelProgress = (int) (world.doorPosition().x - world.getHero().getPosition().x);
        }

        //implements the cloud behavior
        world.cloudNavigation();

        //sends to myView walking distance left to the end of the level
        view.setLevelProgress(levelProgress);


    }


    /**
     * Before each step check the position of the player and if 20m away - play gongSound to announce it .
     */
    public void preStep(StepEvent e) {
        // Controls the sound when approaching the gong to be played only once
        if (!gongSoundPlayed && !(world instanceof Level4)) {
            if ((int) hero.getPosition().x >= world.getDoorPos() - 20) {
                world.getLevelSound().stop();
                world.getDoor().playGongSound();
                gongSoundPlayed = true;
            }
        }

    }

    /**
     * Redefines the given variables for each level according to the new level values
     *
     * @param view  View of the simulation
     * @param world Level of the game.
     */
    public void setTracker(MyView view, GameLevel world) {
        this.view = view;
        this.world = world;
        this.hero = world.getHero();
    }

    /**
     * Accessor to control sound being played only once as it stars when the Player is near by the gong.
     *
     * @param gongSoundPlayed Boolean variable holding the state of the sound stream.
     */
    public void setGongSoundPlayed(boolean gongSoundPlayed) {
        this.gongSoundPlayed = gongSoundPlayed;
    }

}
