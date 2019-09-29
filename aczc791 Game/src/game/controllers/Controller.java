

package game.controllers;

import city.cs.engine.SoundClip;
import city.cs.engine.Walker;
import game.Game;
import game.levels.Level4;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.event.*;
import java.io.IOException;

/**
 * Keyboard commands controller.
 * Some of the sounds related to key input are attached to too.
 */
public class Controller extends KeyAdapter {
    //Sets walking and jumping speed
    private static final float JUMPING_SPEED = 6;
    private static final float WALKING_SPEED = 5f;
    private static SoundClip walkingSound,jumpingSound;
    // controls walking sound to be played in loop
    // but loop to be started only once
    private boolean isWalking;
    static {
        try {
            walkingSound = new SoundClip("data/sounds/walking.wav");
            jumpingSound = new SoundClip("data/sounds/jump.wav");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e);
        }
    }
    private Game game;
    private Walker body;

    /**
     * Constructor for Controller.
     * @param body The body which has to be controlled by keyboard input.
     * @param game The entry point of the program.
     */
    public Controller(Walker body, Game game) {
        this.body = body;
        this.game = game;
        isWalking = false;

    }

    /**
     * Takes attention when key is pressed and executes the code inside its body.
     * If "w" - player jumps
     * if "A" - player walks to the left
     * if "D" - player walks to the right
     * Sounds for walking and jumping are executed accordingly.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        //store key value
        int code = e.getKeyCode();
        //checks if the pressed key is "1"
        //if true game window is closed, we escape the game
        if (code == KeyEvent.VK_1) System.exit(0);
            //same check for jump control - "W"
        else if (code == KeyEvent.VK_W) {
            //if we are not in 4th level jump as normal
            if(!(game.getGameLevel() instanceof Level4)){
                jumpingSound.play();
                body.jump(JUMPING_SPEED);
            }else {//else jump higher
                body.jump(12);

            }
        }
        //move to left is "A" is pressed
        else if (code == KeyEvent.VK_A) {
            body.startWalking(-WALKING_SPEED);
            //if the sound is not looping then play it
            if(!isWalking){
                walkingSound.loop();
                //sound is looping now... dont load it again
                isWalking = true;
            }

        }
//        move to right is "D" is pressed
        else if (code == KeyEvent.VK_D) {
            body.startWalking(WALKING_SPEED);
            //if walking sound is not looping then play it
            if(!isWalking){
                walkingSound.loop();
                //sound is looping now...dont load it again
                isWalking = true;
            }

        }
    }

    /**
     *If keys for walking are released body stops move
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_A || code == KeyEvent.VK_D) body.stopWalking();
        walkingSound.stop();
        isWalking = false;
    }

    /**
     * Setter for the body field.
     * @param body Body which is going to be controlled by the keyboard input.
     */
    public void setBody(Walker body) {
        this.body = body;
    }

}
