/**
 * Walking to the left pres "A"
 * Walking to the right press "D"
 * Jump press "W"
 * Quick exit press "1"
 * Egg gives you 3 lifes
 * If Hero collide with the nettle will loose a life and you'll hear a crow squawk
 * Only when the Turkey is "fed" you can take an egg
 * If you click with the mouse on any nettle plant(preferably at bottom:) )and Turkey will start moving towards.
 * Everything the Cloud throws will take you a life
 * Use the blank text space in the bottom side of the menu to register.
 * Only registered user can save game state
 * "Trial" user can be used to play but can not save game
 *
 */



package game;

import city.cs.engine.DebugViewer;
import game.controllers.ControlPanel;
import game.controllers.Controller;
import game.controllers.MouseHandler;
import game.eventHandlers.MyView;
import game.eventHandlers.Tracker;
import game.levels.*;
import game.objects.Hero;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Base class for the game.
 * All important parts of the simulation are instantiated in here.
 */
public class Game {
    //current level of the game
    private GameLevel world;
    //handle mouse related events
    private MouseHandler mouseHandler;
    //creates the view
    private MyView view;
    //variable holding current level
    private int level;
    //control all keyboard events
    // and some events triggered form key press
    private Controller controller;
    //events related to the move of the player
    private Tracker tracker;
    //user GUI
    private ControlPanel buttons;
    //holds all levels of the game
    private ArrayList<GameLevel> gameLevels;

    /**
     * Game constructor.
     * Initialize and pack all critical game components.
     */
    public Game() {


        // load/instantiate world components
        gameLevels = new ArrayList<>();
        //load the array with all levels
        buildGameLevelsArray();
        level = 0;
        //assign world to the current game level for future references
        world = gameLevels.get(level);
        //ControlPanel has to be instantiated before populate() because
        //populate() is using a sound related values from it
        buttons = new ControlPanel(this);
        //loads the current level with objects shared by all levels
        world.populate(this);


        // make a view
        view = new MyView(world, world.getHero(), 720, 660);
        //handles events re;ated to player move
        tracker = new Tracker(view, world);
        //keyboard events and other related to
        controller = new Controller(world.getHero(), this);

        // display the view in a frame
        final JFrame frame = new JFrame("Multilevel Game");
        //Creates user interface
        frame.add(buttons.getMainPanel(), BorderLayout.WEST);

        //loads the users in combobox1 menu
        try {
            buttons.setCombobox();
        }catch (IOException e){
            System.out.println(e);
        }

        // quit the application when the game window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Sets where to appear the window next time when is made visible
        frame.setLocationByPlatform(true);
        //given window position
        frame.setLocation(100, 10);

        // display the world in the window
        frame.add(view);
        // don't let the game window be resized
        frame.setResizable(false);

        // make the window visible
        frame.setVisible(true);
        //get keyboard focus
        frame.requestFocus();
        frame.pack();
        //handles mouse events
        mouseHandler = new MouseHandler(view, world, frame);

        //gets attention when mouse is over the frame
        view.addMouseListener(mouseHandler);
        //assign tracker to current world
        world.addStepListener(tracker);

        // uncomment this to make a debugging view
//        JFrame debugView = new DebugViewer(world, 500, 500);

        // adds keyboard control to current world
        frame.addKeyListener(controller);
        //start simulation
        world.start();

    }

    /**
     * Advance to the next level of the game.
     */
    public void goNextLevel() {
        //Holds current Hero lifes
        int heroLifesNow = getHero().getLifes();

        //keeps the count of collected egs
        int collectedEggs = getHero().getEggs();
        //stop simulation
        world.stop();
        //quit  the game or load next level
        if (level == 3) {
            //change the background when last level is finished
            view.gameCompletedGreeting();
        } else {
            level++;
            //assign world to the next level
            world = gameLevels.get(level);

            // fill the new world with bodies
            world.populate(this);

            // show the new world in the view
            view.setWorld(world);
            //updates the components in MyView class according to the new world
            view.setLevelView(world, world.getHero(), world.getBackgroundImg());
            //pass the data to the new level
            getHero().setLifes(heroLifesNow);
            getHero().setEggs(collectedEggs);

            //updates the components in Tracker class according to the new world
            tracker.setTracker(view, world);

            // switch the keyboard control to the new player
            controller.setBody(world.getHero());

            //updates the components in MouseHandler class according to the new world
            mouseHandler.setView(view, world);
            //updates mouse respond to the current level
            view.addMouseListener(mouseHandler);
            //adds control over the new world
            world.addStepListener(tracker);
            //Sets gong sound to false do is available when new level gate is reached
            tracker.setGongSoundPlayed(false);

            //starts the simulation again
            world.start();
        }
    }

    /**
     * Gives access to all player methods.
     * @return Curent game player.
     */
    public Hero getHero() {
        return world.getHero();
    }

    /**
     * Gives access to all GameLevel methods.
     * @return Current level.
     */
    public GameLevel getGameLevel() {
        return world;
    }

    /**
     * Setter for the variable keeping track of which level simulation is in at the moment.
     * @param level Holds numeric value of the current level.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     *
     * @return Current Level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Stops the game simulation.
     */
    public void pauseGame() {
        world.stop();
    }

    /**
     * Starts the game simulation.
     */
    public void startGame() {
        world.start();
    }

    /**
     * Erases the array holding all levels
     * and sets the variable responseable for keeping track
     * of the current level numeric value to the correct value.
     */
    public void resetGameLevels() {
        level = -1;
        gameLevels = new ArrayList<>();
    }

    /**
     * Holds all levels.
     */
    public void buildGameLevelsArray() {
        gameLevels.add(new Level1());
        gameLevels.add(new Level2());
        gameLevels.add(new Level3());
        gameLevels.add(new Level4());
    }

    /**
     * Checks if the background sound is set on or off.
     * @return True - background sound is stopped. False - background sound is on.
     */
    public boolean checkIfMute(){
        return buttons.checkIfMute();
    }


    /**
     * Run the program.
     */
    public static void main(String[] args) {
        new Game();

    }
}
