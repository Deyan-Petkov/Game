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
import java.io.IOException;
import java.util.ArrayList;

public class Level4 extends GameLevel {

    private Cloud cloud;
    private Mushroom mushroom;
    private static Image backgroundImg;
    //How many times background to be drawn net to itself
    private int repeatImg;
    //reduce the instantiation of Mushrooms
    private int count;
    // keeps the nettle positions so MouseHandler knows them and can determin if mouse was pressed onto them
    private ArrayList<Vec2> arrNettlePos;
   //left and right wall/ need it for mushrooms collusion detection(change walking direction)
    private Body lWall,rWall;
    private static SoundClip level4Sound;
    static {
        try {
            level4Sound = new SoundClip("data/sounds/level4Sound.wav");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e);
        }
    }


    @Override
    public void populate(Game game) {

        Image image = new ImageIcon("data/images/backgrounds/background4.png").getImage();
        backgroundImg = image.getScaledInstance(1280, 640, Image.SCALE_SMOOTH);
        repeatImg = 1;
        //holds nettle positions needed for mouseHandler class
        arrNettlePos = new ArrayList<>();

        //Creates platforms
        for (int i = 0; i <= 6; i++) {
            Shape groundShape1 = new BoxShape(20, 0.5f);
            Body ground1 = new StaticBody(this, groundShape1);
            if (i % 2 == 0) {
                ground1.setPosition(new Vec2(4, i * 7));
                arrNettlePos.add(new Vec2(i + 4, ground1.getPosition().y + 1f));
                arrNettlePos.add(new Vec2(i + 9, ground1.getPosition().y + 1f));
            } else {
                ground1.setPosition(new Vec2(-4, i * 7));
                arrNettlePos.add(new Vec2(i - 4, ground1.getPosition().y + 1f));
                arrNettlePos.add(new Vec2(i - 9, ground1.getPosition().y + 1f));
            }
        }
        super.populate(game);

        //create gound
        Shape groundShape = new BoxShape(30, 10f);
        Body ground = new StaticBody(this, groundShape);
        ground.setPosition(new Vec2(0, -15f));
        //left wall
        Shape wallShape = new BoxShape(0.5f, 100f);
        lWall = new StaticBody(this, wallShape);
        lWall.setPosition(new Vec2(-23.5f, -9f));
        //right wall
        Shape wallShapeR = new BoxShape(0.5f, 100f);
        rWall = new StaticBody(this, wallShapeR);
        rWall.setPosition(new Vec2(24.5f, -9f));

//            getLevelSound().loop();

        //Changes hero jumping speed according to requirements of level4
//        game.setJumpingSpeed(12);

        cloud = new Cloud(this);
        cloud.setGravityScale(0);
        cloud.startWalking(4);
        count = 0;
    }


    @Override   //Hero start position
    public Vec2 startPosition() {
        return new Vec2(-3, -8.9f);
    }

    @Override
    public Vec2 doorPosition() {
        return new Vec2(3, 44f);
    }

    //Method controlling mushroom behaviour
    public void dropMushroom() {
        if (count <= 1 && count >= 0) {
            mushroom = new Mushroom(getHero().getWorld(), getHero(),this);
            System.out.println("Dropping mushroom");
            mushroom.addCollisionListener(new InTouch(this));
            mushroom.setPosition(cloud.getPosition());
            if(getHero().getPosition().x> cloud.getPosition().x){
            mushroom.startWalking(-3);
            }else{mushroom.startWalking(3);}
        }
        count++;
    }


    @Override
    public void cloudNavigation() {
        // Determines Cloud position according to Hero
        cloud.setPosition(new Vec2(cloud.getPosition().x, getHero().getPosition().y + 8));
        //Turns back the Cloud
        if (cloud.getPosition().x > 23) {
            if (count == 1) {
                count--;
            }
            cloud.startWalking(-5);
        } else if (cloud.getPosition().x < -22) {
            if (count == 1) {
                count--;
            }
            cloud.startWalking(5);
        }//if Hero and Cloud are aligned drops a mushroom
        if (((int) getHero().getPosition().x) == ((int) cloud.getPosition().x)) {
            //create fireball but only one to control the flow
            if (count < 1) {
                dropMushroom();
            }
        }

    }

    @Override
    public Vec2 turkeyPosition() {
        return new Vec2(-22, 29.8f);
    }

    @Override
    public ArrayList<Vec2> nettlePosition() {
        return arrNettlePos;
    }

    @Override
    public String currentLevel() {
        return "Level 4";
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
    public Body getlWall(){
        return lWall;
    }
    @Override
    public Body getrWall(){
        return rWall;
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
        return level4Sound;
    }
}
