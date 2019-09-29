package game.controllers;

import game.Game;
import game.eventHandlers.SaveGame;
import game.levels.Level2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Contains buttons and text fields for the GUI
 */
public class ControlPanel extends Container {
    private JPanel mainPanel;
    private JButton pauseButton;
    private JButton quitGameButton;
    private JButton restartGameButton;
    private JButton continueButton;
    private JButton level1Button;
    private JButton level2Button;
    private JButton level3Button;
    private JButton level4Button;
    private JButton saveAndQuitButton;
    private JButton resumeGameButton;
    private JComboBox comboBox1;
    private JButton addUserButton;
    private JTextPane textPane1;
    private JLabel Users;
    private JButton deleteUserButton;
    private JButton muteUnmuteButton;
    private SaveGame saveGame;
    private FileReader fileReader;
    private BufferedReader bufferedReader;
    private FileWriter fileWriter;
    //used whe reed line form file
    private String line;
    private String currentUser;
    //contain the elements of split by "," lines
    private String[] tokens;
    //keeps track if background sound is on or off
    private boolean isMute;
//    private Game game;

    /**
     * Constructor for GUI
     *
     * @param game The entry point for the program.
     */
    public ControlPanel(Game game) {

        saveGame = new SaveGame(game);
        isMute = false;

        /**
         * Stop the simulation
         */
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.pauseGame();
            }
        });
        /**
         * Run the simulation
         */
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.startGame();
            }
        });

        /**
         * Controls background sound
         */
        muteUnmuteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isMute) {//if music is on stop it when click
                    //if is off play it on click according to level specifications
                    game.getGameLevel().getLevelSound().stop();
                    isMute = true;
                } else {
                    if (game.getGameLevel() instanceof Level2) {
                        game.getGameLevel().getLevelSound().play();
                    } else {
                        game.getGameLevel().getLevelSound().loop();
                        isMute = false;
                    }
                }
            }
        });

        /**
         * Start the game form level 1
         */
        restartGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadLevel(game, -1);
            }
        });
        /**
         * Quit the game
         */
        quitGameButton.addActionListener(new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent e) {
                System.exit(0);

            }
        });
        /**
         * Reset and go to Level 1
         */
        level1Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadLevel(game, -1);
            }
        });
        /**
         * Reset and go ot level 2
         */
        level2Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadLevel(game, 0);
            }
        });
        /**
         * Reset and go ot level 3
         */
        level3Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadLevel(game, 1);
            }
        });
        /**
         * Reset and go ot level 4
         */
        level4Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadLevel(game, 2);
            }
        });
        /**
         * If player is registered saves the game at current state and position
         */
        saveAndQuitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {//if user doesn't play with Trial profile save the game to file
                    if (!comboBox1.getSelectedItem().equals("Trial")) {
                        saveGame.getGameState(currentUser);
                        System.exit(0);

                    } else {
                        warningMessage("Please register.");
                    }
                } catch (IOException ex) {
                    System.out.println(ex);
                }

            }
        });
        /**
         * In case of existing saving according to the user
         * particular Level and state will be loaded
         */
        resumeGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {//if user has saving then load it and continue game
                    if (saveGame.setGameState(currentUser) == 0) {
                        game.getGameLevel().getLevelSound().stop();
                        saveGame.setGameState(currentUser);
                        game.resetGameLevels();
                        game.buildGameLevelsArray();
                        saveGame.continueGame();
                    } else {
                        defaultMessage("There is no available records for this profile!");
                    }
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        });

        /**
         * If user doesn't exist - add it to the file with users
         */
        addUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int flag = 0;
                //dont add empty text to the ComboBox
                if (textPane1.getText().trim().length() != 0) {
                    //Check If the given name exist in the list
                    for (int i = 0; i < comboBox1.getItemCount(); i++) {
                        if (comboBox1.getItemAt(i).equals(textPane1.getText())) {
                            flag++;//if this profile exist-flag = 1
                            defaultMessage("Username '" + textPane1.getText() + "' already exist!\n Please Choose different name.");
                        }
                    }
                    // if given name doesnt exist in the list  then add it to it
                    if (flag == 0) {
                        comboBox1.addItem(textPane1.getText());
                        defaultMessage("Welcome " + textPane1.getText());
                        try {
                            fileWriter = new FileWriter("data/textFiles/gameState.txt", true);
                            {
                                fileWriter.write(textPane1.getText() + ",\n");
                                fileWriter.close();
                            }
                        } catch (IOException ex) {
                            System.out.println(ex);
                        } finally {
                            try {
                                if (fileWriter != null) {
                                    fileWriter.close();
                                }
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        //clean the textPane
                        textPane1.setText("");
                    }
                }
            }
        });
        /**
         * If user exist and is different than "Trial" - delete it
         */
        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {//if desired user is not "Trial"
                    if (!textPane1.getText().equals("Trial")) {
                        //if this user existed and it was deleted
                        if(saveGame.deleteGameState(textPane1.getText())){
                            //Send message that user is deleted
                            defaultMessage(textPane1.getText() + " is deleted.");
                            //set the boolean userExisted back to false so next time deleteGameState
                            //will change it to true if user exists
                            saveGame.setUserExisted(false);
                            //reset combobox according to the new state
                            setCombobox();
                        }else{
                            warningMessage("There is not such a profile\n in the records");
                        }

                    } else {//if somebody try to delete "Trial" sent this message
                        warningMessage("User Trial Can Not Be Deleted.");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }//clear writing area
                textPane1.setText("");
            }
        });
        /**
         * TextPanel area.
         * Listener is needed in order to control input text
         */
        textPane1.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
            }
        });
        /**
         * Drop down list displaying all available users recorded in the file
         */
        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //get selected item from the list and assigne currentUser to it
                currentUser = (String) comboBox1.getSelectedItem();
                try {
                    saveGame.setGameState(currentUser);
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        });
    }

    /**
     * Set combobox according to the records in the file holding all users
     *
     * @throws IOException
     */
    public void setCombobox() throws IOException {
        try {
            fileReader = new FileReader("data/textFiles/gameState.txt");
            bufferedReader = new BufferedReader(fileReader);
            //remove all items and reload the existing in the file
            comboBox1.removeAllItems();
            //always add "Trial" as user
            comboBox1.addItem("Trial");
            //read the file till the end is reached
            while ((line = bufferedReader.readLine()) != null) {
                tokens = line.split(",");
                //add all names from the file(names are at first position)
                comboBox1.addItem(tokens[0]);
            }
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (fileReader != null) {
                fileReader.close();
            }
        }
    }

    /**
     * Create a warning window message with relative information displayed
     *
     * @param message Holds the desired message
     */
    public void warningMessage(String message) {
        JOptionPane.showMessageDialog(new JFrame(), message, "WARNING",
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Creates Informational window message to the user.
     *
     * @param message Holds the desired message.
     */
    public void defaultMessage(String message) {
        JOptionPane.showMessageDialog(new JPanel(), message);
    }

    /**
     * Getter for the main JPanel
     *
     * @return Jpanel with all the components in it.
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    /**
     * The routines to create new level and set it up
     * according to desired level
     *
     * @param game  The entry point for the program.
     * @param level Represent level of the game as numeric value.
     */
    private void loadLevel(Game game, int level) {
        //stop any ongoing sounds
        game.getGameLevel().getLevelSound().stop();
        //creates new arrList
        game.resetGameLevels();
        //Loads levels in the arrList
        game.buildGameLevelsArray();
        //set to desired level
        game.setLevel(level);
        //load all necessary components for the game
        // and updates the existing one.
        game.goNextLevel();
        // sets the player lifes and collected eggs
        game.getHero().setLifes(10);
        game.getHero().setEggs(0);
    }

    public boolean checkIfMute() {
        return isMute;
    }

    public void setIsMute(boolean isMute) {
        this.isMute = isMute;
    }


}
