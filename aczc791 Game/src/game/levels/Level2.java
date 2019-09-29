package game.levels;

import city.cs.engine.*;
import city.cs.engine.Shape;
import game.Game;
import game.eventHandlers.InTouch;
import game.objects.Cloud;
import game.objects.Fireball;
import org.jbox2d.common.Vec2;


import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Level 2 of the game
 */
public class Level2 extends GameLevel {
    private Game game;
    private Cloud cloud;
    private Fireball fireball;
    private int count;
    private static Image backgroundImg;
    private int repeatImg;
    private static SoundClip level2Sound;
    static {
        try {
            level2Sound = new SoundClip("data/sounds/level2Sound.wav");
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
        ground.setPosition(new Vec2(0, -13.5f));

        Shape platformShape = new BoxShape(0.8f, 0.001f);
        Body turkeyPlatform = new StaticBody(this, platformShape);
        turkeyPlatform.setPosition(new Vec2(47, -6));

        count = 0;
        cloud = new Cloud(this);
        cloud.setPosition(new Vec2(-2, 6));
        cloud.setGravityScale(0);
        Image image = new ImageIcon("data/images/backgrounds/background2.jpg").getImage();
        backgroundImg = image.getScaledInstance(1280, 640, Image.SCALE_SMOOTH);
        repeatImg = 2;

//        level2Sound.play();

    }

    @Override
    public Vec2 startPosition() {
        return new Vec2(2, -12.7f);
    }

    @Override
    public Vec2 doorPosition() {
        return new Vec2(70f, -11.7f);
    }

    @Override
    public Vec2 turkeyPosition() {
        return new Vec2(47, 6.1f);
    }

    public void getFireball() {
        if (count <= 1 && count >= 0) {
            fireball = new Fireball(getHero().getWorld(), getHero());
            fireball.addCollisionListener(new InTouch(this));
            fireball.setPosition(cloud.getPosition());
            count++;
        }
    }

    @Override
    public void cloudNavigation() {
        if (((int) getHero().getPosition().x) == ((int) cloud.getPosition().x)) {
            //create fireball but only one to control the flow
            if (count < 1) {
                getFireball();//Sets the starting position of the fireball at same spot where cloud is in the moment
                if (getHero().getLinearVelocity().x > 0) {
                    fireball.startWalking(5);
                } else if (getHero().getLinearVelocity().x < 0) {
                    fireball.startWalking(-5);
                }
            }
        }
        //if cloud position is greater than hero position, cloud start moving backwards
        else if (((int) cloud.getPosition().x) > ((int) getHero().getPosition().x)) {
            cloud.startWalking(-5.5f);
            //decrease fireball count only if count is 1 to control do ball fall
            if (count == 1) {
                count--;
            }
            // cloud start moving right if hero is on the right side
        } else if (((int) cloud.getPosition().x) < ((int) getHero().getPosition().x)) {
            cloud.startWalking(5.5f);
        }
    }


    @Override// Keeps nettle positions
    public ArrayList<Vec2> nettlePosition() {
        ArrayList<Vec2> arr = new ArrayList<>();
        arr.add(new Vec2(5, -12.8f));
        arr.add(new Vec2(10, -12.8f));
        arr.add(new Vec2(15f, -12.8f));
        arr.add(new Vec2(25, -12.8f));
        arr.add(new Vec2(30, -12.8f));
        arr.add(new Vec2(35, -12.8f));
        arr.add(new Vec2(50, -12.8f));
        arr.add(new Vec2(55, -12.8f));
        arr.add(new Vec2(60, -12.8f));
        return arr;
    }

    @Override
    public String currentLevel() {
        return "Level 2";
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
    public Cloud getCloud(){
        return cloud;
    }

    @Override
    public SoundClip getLevelSound() {
        return level2Sound;
    }
}

