package game.levels;


import city.cs.engine.*;
import city.cs.engine.Shape;
import city.cs.engine.StaticBody;
import game.Game;
import org.jbox2d.common.Vec2;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Level 1 of the game
 */
public class Level1 extends GameLevel {

    private Game game;
    private static Image backgroundImg;
    //How many times to concatenate image for the view
    private int repeatImg;
    private static SoundClip level1Sound;
    static {
        try {
            level1Sound = new SoundClip("data/sounds/level1Sound.wav");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e);
        }
    }

    /**
     * Populate the world.
     */
    @Override
    public void populate(Game game) {
        super.populate(game);
        this.game = game;

        //Make ground
        Shape groundShape = new BoxShape(100, 0.001f);
        Body ground = new StaticBody(this, groundShape);
        ground.setPosition(new Vec2(0, -11f));

        Shape platformShape = new BoxShape(0.8f, 0.001f);
        Body turkeyPlatform = new StaticBody(this, platformShape);
        turkeyPlatform.setPosition(new Vec2(31, -3));

        backgroundImg = new ImageIcon("data/images/backgrounds/background1.png").getImage();

        repeatImg = 2;
    //        level1Sound.loop();
    }

    @Override
    public Vec2 startPosition() {
        return new Vec2(-3, -10);
    }

    @Override
    public Vec2 doorPosition() {
        return new Vec2(70f, -9.6f);
    }

    @Override
    public void cloudNavigation() {}

    @Override
    public Vec2 turkeyPosition() {
        return new Vec2(31, 3.1f);
    }

    @Override
    public ArrayList<Vec2> nettlePosition() {
        ArrayList<Vec2> arr = new ArrayList<>();
        arr.add(new Vec2(1, -10.5f));
        arr.add(new Vec2(6, -10.5f));
        arr.add(new Vec2(15, -10.5f));
        arr.add(new Vec2(20, -10.5f));
        arr.add(new Vec2(25, -10.5f));
        arr.add(new Vec2(40, -10.5f));
        arr.add(new Vec2(45, -10.5f));
        arr.add(new Vec2(60, -10.5f));
        arr.add(new Vec2(65, -10.5f));


        return arr;
    }

    @Override
    public String currentLevel() {
        return "Level 1";
    }

    @Override
    public Image getBackgroundImg() {
        return backgroundImg;
    }

    @Override
    public int repeatImg() {
        return repeatImg;
    }

    @Override
    public SoundClip getLevelSound(){return level1Sound;}

}
