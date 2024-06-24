package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.enumerations.GUIScene;
import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

/**
 * The LobbyController class is responsible for handling user interactions in the lobby scene of the GUI.
 * It extends the GUIController class.
 */
public class LobbyController extends GUIController{
    public Button createGame;
    public Button joinGame;
    public Pane numChoice;
    public Pane exitPane;

    /**
     * Handles the action of creating a game.
     */
    @FXML
    public void createGame() {
        createGame.setDisable(true);
        createGame.setVisible(false);

        joinGame.setDisable(true);
        joinGame.setVisible(false);

        numChoice.setDisable(false);
        numChoice.setVisible(true);

    }

    /**
     * Handles the action of going back.
     */
    @FXML
    public void back() {
        createGame.setDisable(false);
        createGame.setVisible(true);

        joinGame.setDisable(false);
        joinGame.setVisible(true);

        numChoice.setDisable(true);
        numChoice.setVisible(false);

    }

    /**
     * Handles the action of creating a game for two players.
     */
    @FXML
    public void twoPlayers() {
        try {
            viewGUI.createGame(2);
        } catch(ServerDisconnectedException e) {
            //TODO: server disconnected GUI screen
        }
        gui.switchScene(GUIScene.GAMELOBBY);
    }

    /**
     * Handles the action of creating a game for three players.
     */
    @FXML
    public void threePlayers() {
        try {
            viewGUI.createGame(3);
        } catch(ServerDisconnectedException e) {
            //TODO: server disconnected GUI screen
        }
        gui.switchScene(GUIScene.GAMELOBBY);
    }

    /**
     * Handles the action of creating a game for four players.
     */
    @FXML
    public void fourPlayers() {
        try {
            viewGUI.createGame(4);
        } catch(ServerDisconnectedException e) {
            //TODO: server disconnected GUI screen
        }
        gui.switchScene(GUIScene.GAMELOBBY);

    }

    /**
     * Handles the action of joining a game.
     */
    @FXML
    public void joinGame()
    {
        gui.switchScene(GUIScene.JOINGAME);
    }

    /**
     * Handles the action of quitting the game.
     */
    @FXML
    public void quit() {
        exitPane.setDisable(false);
        exitPane.setVisible(true);
    }

    /**
     * Handles the action of not exiting the game.
     */
    @FXML
    public void noExitButton() {
        exitPane.setDisable(true);
        exitPane.setVisible(false);
    }

    /**
     * Handles the action of confirming to exit the game.
     */
    @FXML
    public void yesExitButton() {
        Platform.exit();
    }
}
