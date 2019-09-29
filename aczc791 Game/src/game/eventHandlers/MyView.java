package game.eventHandlers;

import city.cs.engine.*;
import game.levels.GameLevel;
import game.objects.Hero;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Draws all objects in the current simulation.
 * It's important to keep all of them update because other ways will'not be visible.
 */
public class MyView extends UserView {
    // background image , variable to hold the egg image and another variable to hold the new scaled egg image
    private static Image backgroundImg, oldEgg, egg;
    private Hero hero;
    private Font style;
    private GameLevel world;
    private int takenEggs;
    //holds numeric representation of the distance to the gong(next level)...it's calculated in Tracker class
    private int levelProgress;
    //changes background image X position making the background to move
    private int imageOffset;
    private static SoundClip gameCompleted;
    static {
        try {
            gameCompleted = new SoundClip("data/sounds/gameCompleted.wav");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println(e);
        }
    }

    /**
     * Constructor for the View
     * @param world Level of the game
     * @param hero Player
     * @param width View window size
     * @param height View window height
     */
    public MyView(GameLevel world, Hero hero, int width, int height) {
        super(world, width, height);
        this.world = world;
        this.hero = hero;
        backgroundImg = world.getBackgroundImg();
        oldEgg = new ImageIcon("data/images/objects/egg.png").getImage();
        //creates new scaled instance of teh original egg to be displayed.
        egg = oldEgg.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
        style = new Font("Serif", Font.ITALIC, 17);
        imageOffset = 0;
        takenEggs = 0;

    }

    /**#
     * Setter for the imageOffset.Used to translate Vec2 to screen coordinates. Having that attached to the background simulates background move.
     * @param off Represent translation from meters to pixels.
     */
    public void setImageOffset(int off) {
        imageOffset = off;
    }

    /**
     * Draws the background and because the imageOffset variable which is constantly changed according to the move of the player in Tracker class the background moves in opposite direction as the rest of the objects.
     */
    @Override
    protected void paintBackground(Graphics2D g) {

        // iterate over and draw the given repeatImg value for the given level
        for (int i = 0; i <= world.repeatImg(); i++) {
            g.drawImage(backgroundImg, imageOffset + backgroundImg.getWidth(this) * i, 27, this);
        }
    }

    /**
     * Displays player lifes, collected egs and distance to the end of the level.
     */
    @Override
    protected void paintForeground(Graphics2D g) {

        //Writes on the display
        g.setFont(style);
        g.setColor(Color.red);
        //display as many eggs as collected

        //draws as many eggs as hero has collected
        for (int i = 0; i < hero.getEggs(); i++) {
            g.drawImage(egg, i * 40, 100, this);
        }

        g.drawString(world.currentLevel(), 10, 50);           //display current level
        g.drawString("Lifes: " + hero.getLifes(), 10, 70); //display remaining lifes
        g.drawString(+levelProgress + " m Left", 10, 90); //display meters to the door


    }

    /**
     * Hero getter.
     * @return Hero.
     */
    //for setting hero lifes in game
    public Hero getHero() {
        return hero;
    }

    /**
     * Sets the parameters according to the new level.
     * @param world Level of the game
     * @param hero Hero
     * @param backgroundImg Image draw as a background
     */
    public void setLevelView(GameLevel world, Hero hero, Image backgroundImg) {
        this.world = world;
        this.hero = hero;
        this.backgroundImg = backgroundImg;
    }

    /**
     * Provides access to the variable holding the value of distance left to the end of the level.
     * This value is updated constantly via Tracker class.
     * @param levelProgress  Distance left to the end of the level.
     */
    public void setLevelProgress(int levelProgress) {
        this.levelProgress = levelProgress;
    }

    /**
     * Change the background and the sound once the game is fully completed.
     */
    public void gameCompletedGreeting() {
        backgroundImg = new ImageIcon("data/images/backgrounds/gameComplete.gif").getImage();
        world.getLevelSound().stop();
        gameCompleted.play();
    }

}
