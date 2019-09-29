package game.levels;

import city.cs.engine.*;
import city.cs.engine.Shape;
import game.Game;
import game.eventHandlers.InTouch;
import game.objects.Cloud;
import game.objects.Mushroom;
import org.jbox2d.common.Vec2;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class Level3 extends GameLevel implements ActionListener {

    private Cloud cloud;
    private Mushroom mushroom;
    //according this variable Cloud will be dropping Fireballs
    private static Image backgroundImg;
    //How many times background to be drawn nex to itself
    private int repeatImg;
    private Timer timer;
    private static SoundClip level3Sound;
    static {
        try {
            level3Sound = new SoundClip("data/sounds/level3Sound.wav");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e);
        }
    }


    @Override
    public void populate(Game game) {
        super.populate(game);

        //Creates the ground
        Shape groundShape = new BoxShape(200, 0.001f);
        Body ground = new StaticBody(this, groundShape);
        ground.setPosition(new Vec2(0, -11f));
        //Creates platform Turkey sits on
        Shape platformShape = new BoxShape(1, 0.1f);
        Body turkeyPlatform = new StaticBody(this, platformShape);
        turkeyPlatform.setPosition(new Vec2(45, 10));

        Image image = new ImageIcon("data/images/backgrounds/background3.png").getImage();
        backgroundImg = image.getScaledInstance(1280, 640, Image.SCALE_SMOOTH);
        repeatImg = 2;

        cloud = new Cloud(this);
        cloud.setPosition(new Vec2(-15, 6));
        cloud.setGravityScale(0);
        cloud.startWalking(5f);
        timer = new Timer(3000,this);
        timer.start();

//        level3Sound.loop();

    }


    @Override   //Hero position
    public Vec2 startPosition() {
        return new Vec2(-3, -10.7f);
    }

    @Override
    public Vec2 doorPosition() {
        return new Vec2(70, -9.6f);
    }


    private void dropMushroom() {
        mushroom = new Mushroom(getHero().getWorld(), getHero(),this);
        mushroom.addCollisionListener(new InTouch(this));
        mushroom.setPosition(cloud.getPosition());
    }

    @Override
    public void cloudNavigation() { }

    @Override
    public void actionPerformed(ActionEvent e) {

            dropMushroom();
            //if mushroom position is greater than hero position, cloud start moving backwards
            if (((int) mushroom.getPosition().x) > ((int) getHero().getPosition().x)) {
                mushroom.startWalking(-4.5f);
            }
            // mushroom start moving right if hero is on the right side
            else if (((int) mushroom.getPosition().x) < ((int) getHero().getPosition().x)) {
                mushroom.startWalking(4.5f);
            }
            mushroom.setPosition(cloud.getPosition());



    }
    @Override
    public Vec2 turkeyPosition() {
        return new Vec2(45, 10.1f);
    }

    @Override
    public ArrayList<Vec2> nettlePosition() {
        ArrayList<Vec2> arr = new ArrayList<>();
        arr.add(new Vec2(0, -10.5f));
        arr.add(new Vec2(5, -10.5f));
        arr.add(new Vec2(10, -10.5f));
        arr.add(new Vec2(15, -10.5f));
        arr.add(new Vec2(25, -10.5f));
        arr.add(new Vec2(30, -10.5f));
        arr.add(new Vec2(35, -10.5f));
        arr.add(new Vec2(50, -10.5f));
        arr.add(new Vec2(55, -10.5f));
        arr.add(new Vec2(60, -10.5f));

        return arr;
    }

    @Override
    public String currentLevel() {
        return "Level 3";
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
    public Mushroom getMushroom(){
        return mushroom;
    }

    @Override
    public SoundClip getLevelSound() {
        return level3Sound;
    }
}
