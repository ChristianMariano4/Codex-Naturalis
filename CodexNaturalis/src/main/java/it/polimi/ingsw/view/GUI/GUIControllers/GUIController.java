package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.ViewGUI;

import javafx.stage.Stage;

/**
 * Initializes the scene by adding all player names and panes to their respective lists.
 * It then retrieves the current game from the viewGUI and updates the game lobby.
 * Finally, it starts a new thread that waits for the game to start and then changes the scene to the game.
 */
public abstract class GUIController {

    protected ViewGUI viewGUI;
    protected GUI gui;
    protected Stage primaryStage;

    /**
     * Sets the ViewGUI instance for this controller.
     * @param view The ViewGUI instance to be set.
     */
    public void setView(ViewGUI view) {
        this.viewGUI = view;
    }

    /**
     * Sets the primary stage for this controller.
     * @param stage The Stage instance to be set.
     */
    public void setStage(Stage stage)
    {
        this.primaryStage = stage;
    }

    /**
     * Sets the GUI instance for this controller.
     * @param gui The GUI instance to be set.
     */
    public void setGUI(GUI gui) {
        this.gui = gui;
    }

    /**
     * This method is called to initialize the scene.
     * It can be overridden by subclasses to provide custom scene initialization.
     */
    public void sceneInitializer() { }

    /**
     * This method is called when the final round of the game starts.
     * It can be overridden by subclasses to provide custom behavior.
     */
    public void finalRound(){ }

    /**
     * This method is called to update the game state.
     * It can be overridden by subclasses to provide custom update behavior.
     * @param update The object representing the update.
     */
    public void update(Object update){ }

    /**
     * This method is called when a player reaches twenty points.
     * It can be overridden by subclasses to provide custom behavior.
     * @param username The username of the player who reached twenty points.
     */
    public void twentyPoints(String username) { }

    /**
     * This method is called when a chat message is received.
     * It can be overridden by subclasses to provide custom behavior.
     * @param message The received chat message.
     */
    public void chatMessage(String message) { }
}
