package game.eventHandlers;

import game.Game;
import game.levels.GameLevel;
import game.levels.Level2;
import game.levels.Level3;
import game.levels.Level4;
import game.objects.Hero;
import org.jbox2d.common.Vec2;

import java.io.*;
import java.util.ArrayList;

/**
 * Saves the current state of the game for particular user.
 */
public class SaveGame {

    private GameLevel world;
    private Game game;
    private Hero hero;
    private FileWriter fileWriter;
    private FileReader fileReader;
    private BufferedReader bufferReader;
    // holds all users and the related info for the saved game state
    private static File usersFile;
    // used to store the info we want to keep when update userFile
    private static File tempFile;
    // holds the info from the current read line split by ','
    private String[] tokens;
    // holds the current read line
    private String line;
    //holds current user
    private String currentUser;
    // holds the the boolean value showing if turkey is destroyed or not
    // so we know to load it or not when we restore the level
    private String turkeyExists;
    // this values will be drawn on the display when the game is restored
    private int egsCollected, heroLifes, level;
    private float heroPositionX, heroPositionY, cloudPositionX, cloudPositionY;
    //these three values keep track over the updating of the userFile
    private boolean userFileDeleted;
    private boolean renameTempFile;
    private boolean tempFileCreated;
    //will change to true if such a user existed
    //while deleteGameState() and will work as a reference
    //for ControlPanel deleteUserButton
    private boolean userExisted;

    /**
     * SaveGame class constructor.
     * Will create gameState(userFile).txt if it doesnt't exist yet or is deleted.
     *
     * @param game The entry point for the program.
     */
    public SaveGame(Game game) {
        this.game = game;
        userExisted = false;


        if (usersFile == null) {
            try {
                usersFile = new File("data/textFiles/gameState.txt");
                // use userfileCreated and userFile.exist() for debugging purposes.
                boolean userFileCreated = usersFile.createNewFile();
            } catch (IOException e) {
                System.out.println(e);
            }
        }

    }

    /**
     * Delete particular user record
     * when new record is desired or user is deleted.
     *
     * @param currentUser Holds the current user name.
     * @throws IOException
     */
    public boolean deleteGameState(String currentUser) throws IOException {
        //Create new file which is gonna replace the existing gameState file
        tempFile = new File("data/textFiles/tempFile.txt");
        tempFileCreated = tempFile.createNewFile();

        fileReader = new FileReader(usersFile);
        fileWriter = new FileWriter(tempFile, true);
        bufferReader = new BufferedReader(fileReader);

        try {
            // keep reading lines until reach empty one(end of the doc)
            while ((line = bufferReader.readLine()) != null) {
                //split the doc where is comma.
                tokens = line.split(",");
                //pos 0 is user name - if equals current user- skip it
                if (tokens[0].equals(currentUser)) {
                    userExisted = true;
                } else {// if names doesn't match write this line in the new document
                    fileWriter.write(line + System.getProperty("line.separator"));
                }

            }
        } finally {// close all open streams
            if (fileWriter != null) {
                fileWriter.close();
            }
            if (bufferReader != null) {
                bufferReader.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
            //delete old file and rename the new one to old ones name
            // userFileDeleted and renemeTempFile could be used for debugging purposes
            //giving feedback for the completion of the process
            userFileDeleted = usersFile.delete();
            renameTempFile = tempFile.renameTo(usersFile);
        }
        return userExisted;
    }

    /**
     * Save the current game state in file.
     *
     * @param currentUser Holds the current user name.
     * @throws IOException
     */
    public void getGameState(String currentUser)
            throws IOException {
        //update these fields as current user might
        //be in different level since SaveLevel was instantiated
        world = game.getGameLevel();
        hero = game.getHero();

        try {//find user in the file and delete it
            // so we can update the info further down this method
            deleteGameState(currentUser);
            //crate new fileWriter to add the new state of the currentUser game play
            fileWriter = new FileWriter(usersFile, true);

            // if we at levels with Cloud being instantiated take its values too
            if (world instanceof Level2 || world instanceof Level3 || world instanceof Level4) {
                fileWriter.write(currentUser + "," + "cloudExist" + "," + hero.getPosition().x + "," + hero.getPosition().y + "," + hero.getEggs() + "," + hero.getLifes() + "," + game.getLevel() + "," + world.getTurkey().getTurkeyExists() + "," + world.getCloud().getPosition().x + "," + world.getCloud().getPosition().y + "\n");

            } else {// else don't look for cloud - there is no one
                fileWriter.write(currentUser + "," + "cloudDoesntExist" + "," + hero.getPosition().x + "," + hero.getPosition().y + "," + hero.getEggs() + "," + hero.getLifes() + "," + game.getLevel() + "," + world.getTurkey().getTurkeyExists() + "," + "\n");
            }
        } finally {
            if (fileWriter != null) {
                fileWriter.close();
            }
        }

    }

    /**
     * Set the game according to the last saved state for this user.
     *
     * @param currentUser Holds the current user name.
     * @return Return 0 if current user exist in the records and 1 if not.
     * @throws IOException
     */
    public int setGameState(String currentUser) throws IOException {

        this.currentUser = currentUser;

        try {
            fileReader = new FileReader(usersFile);
            bufferReader = new BufferedReader(fileReader);
            // keep reading lines until reach empty one(end of the doc)
            while ((line = bufferReader.readLine()) != null) {
                tokens = line.split(",");
                // if find the current user and he/she has saved game state
                if (tokens[0].equals(currentUser) && tokens.length > 1) {
                    //set these variables to the values in the file
                    heroPositionX = Float.parseFloat(tokens[2]);
                    heroPositionY = Float.parseFloat(tokens[3]);
                    egsCollected = Integer.parseInt(tokens[4]);
                    heroLifes = Integer.parseInt(tokens[5]);
                    level = Integer.parseInt(tokens[6]);
                    turkeyExists = (tokens[7]);
                    //if the the saving has records for Cloud load them too
                    if (tokens[1].equals("cloudExist")) {
                        cloudPositionX = Float.parseFloat(tokens[8]);
                        cloudPositionY = Float.parseFloat((tokens[9]));
                        System.out.println("inside setGameState inside");
                    }// all done
                    return 0;
                }
            }
        } finally {
            if (bufferReader != null) {
                bufferReader.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
        }//something went wrong/this user doesn't exist or it has no game play savings
        return 1;
    }

    /**
     * Set the variables in the current game
     * according to those in the last saved game state.
     */
    public void continueGame() {
        //as goNextLevel increase level by one, decrease level by one,
        // but only if level is grater or equal to zero (as we load the levels from ArrayList)
        if (level >= 0) {
            level -= 1;
            game.setLevel(level);
            game.goNextLevel();
        }
        //update variables after goNextLevel according to the new level we are in now
        world = game.getGameLevel();
        hero = game.getHero();
        //set the variables from the the current game to those in the file records
        hero.setPosition(new Vec2(heroPositionX, heroPositionY));
        hero.setEggs(egsCollected);
        hero.setLifes(heroLifes);
        //If turkey doesn't exist in the file records
        // destroy it after being created from goNextLevel
        if (turkeyExists.equals("false")) {
            world.getTurkey().destroy();
        }// if saving is from levels with Cloud - set cloud position accordingly
        if (world instanceof Level2 || world instanceof Level3 || world instanceof Level4) {
            world.getCloud().setPosition(new Vec2(cloudPositionX, cloudPositionY));
        }
    }

    /**
     *  If the given user existed when deleteGameState() is trying to delete it, userExisted will be changed
     *  to true and after that set again to false in deleteUserButton in ControlPanel after confirmation
     *  for the deletion is shown to the user.
     * @param userExisted {boolean} If the query in text panel exist in th file with users.
     */
    public void setUserExisted(boolean userExisted){
        this.userExisted = userExisted;
    }
}
