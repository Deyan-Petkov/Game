package game.controllers;

import game.eventHandlers.MyView;
import game.levels.GameLevel;
import game.levels.Level2;
import game.levels.Level3;
import game.levels.Level4;
import game.objects.Turkey;
import org.jbox2d.common.Vec2;
import java.awt.*;
import java.awt.event.*;

/**
 * Handles mouse events.
 * Gives focus to the simulation frame.
 * Navigates the Turkey Walker Object to nettle if mouse is press on one of
 * the nettle objects.
 */
public class MouseHandler extends MouseAdapter {
    private Component target;
    private GameLevel gameLevel;
    private Turkey turkey;
    private MyView view;
    private static final float WALKING_SPEED = 2;//Determines Turkey walking speed
    //hods current mouse position converted from pixels to Vec2(meters)
    private Vec2 mouseVec;
    // holds mouseVec x and y points
    private int mouseX, mouseY;


    /**
     * Constructor
     * @param view View model frame
     * @param gameLevel Level of the game.
     * @param target java.awt.event.MouseAdapter
     */
    public MouseHandler(MyView view, GameLevel gameLevel, Component target) {
        this.target = target;
        this.view = view;
        this.gameLevel = gameLevel;
        mouseVec = new Vec2();
        this.turkey = gameLevel.getTurkey();
        mouseX = 0;
        mouseY = 0;
    }

    /**
     * Gives focus to the frame.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        target.requestFocus();
    }

    /**
     * If mouse is clicked onto one of the nettle objects
     * the Turkey will start walking towards.
     * Turkey sound will be played too.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (gameLevel instanceof Level2 || gameLevel instanceof Level3 || gameLevel instanceof Level4){
        }
        mouseVec = view.viewToWorld(e.getPoint());
        mouseX = (int) mouseVec.x;
        mouseY = (int) mouseVec.y;

//        // If mouse position and nettle position are same makes Turkey walk to the nettle
        for (Vec2 nettlePos : gameLevel.nettlePosition()) {
            //if x and y mouse points match with nettle x and y position
            if (mouseX == (int) nettlePos.x && mouseY == (int) nettlePos.y) {
                //if turkey didn't turn in egg already play the sound
                if (turkey.getTurkeyExists()) {
                    turkey.playTurkeySound();
                }
                // Changes Turkey walk direction depending on which side of hero/origin Turkey stays
                if (nettlePos.x < turkey.getPosition().x) {
                    turkey.startWalking(-WALKING_SPEED);
                } else {
                    turkey.startWalking(WALKING_SPEED);
                }
                break;
            }
        }
    }

    /**
     *  Redefines the given variables according to the new level values.
     * @param view View frame of this simulation
     * @param gameLevel Level of the game
     */
    public void setView(MyView view, GameLevel gameLevel) {
        this.view = view;
        this.gameLevel = gameLevel;
        this.turkey = gameLevel.getTurkey();
    }

}


