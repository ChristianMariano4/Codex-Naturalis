package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.enumerations.GUIScene;
import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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
     * @param event The ActionEvent object.
     */
    @FXML
    public void createGame(ActionEvent event) {
        createGame.setDisable(true);
        createGame.setVisible(false);

        joinGame.setDisable(true);
        joinGame.setVisible(false);

        numChoice.setDisable(false);
        numChoice.setVisible(true);

    }

    /**
     * Handles the action of going back.
     * @param event The ActionEvent object.
     */
    @FXML
    public void back(ActionEvent event) {
        createGame.setDisable(false);
        createGame.setVisible(true);

        joinGame.setDisable(false);
        joinGame.setVisible(true);

        numChoice.setDisable(true);
        numChoice.setVisible(false);

    }

    /**
     * Handles the action of creating a game for two players.
     * @param event The ActionEvent object.
     */
    @FXML
    public void twoPlayers(ActionEvent event) {
        try {
            viewGUI.createGame(2);
        } catch(ServerDisconnectedException e) {
            //TODO: server disconnected GUI screen
        }
        gui.switchScene(GUIScene.GAMELOBBY);
    }

    /**
     * Handles the action of creating a game for three players.
     * @param event The ActionEvent object.
     */
    @FXML
    public void threePlayers(ActionEvent event) {
        try {
            viewGUI.createGame(3);
        } catch(ServerDisconnectedException e) {
            //TODO: server disconnected GUI screen
        }
        gui.switchScene(GUIScene.GAMELOBBY);
    }

    /**
     * Handles the action of creating a game for four players.
     * @param event The ActionEvent object.
     */
    @FXML
    public void fourPlayers(ActionEvent event) {
        try {
            viewGUI.createGame(4);
        } catch(ServerDisconnectedException e) {
            //TODO: server disconnected GUI screen
        }
        gui.switchScene(GUIScene.GAMELOBBY);

    }

    /**
     * Handles the action of joining a game.
     * @param event The ActionEvent object.
     */
    @FXML
    public void joinGame(ActionEvent event)
    {
        gui.switchScene(GUIScene.JOINGAME);
    }

    /**
     * Handles the action of quitting the game.
     * @param event The ActionEvent object.
     */
    @FXML
    public void quit(ActionEvent event) {
        exitPane.setDisable(false);
        exitPane.setVisible(true);
    }

    /**
     * Handles the action of not exiting the game.
     * @param event The ActionEvent object.
     */
    @FXML
    public void noExitButton(ActionEvent event) {
        exitPane.setDisable(true);
        exitPane.setVisible(false);
    }

    /**
     * Handles the action of confirming to exit the game.
     * @param event The ActionEvent object.
     */
    @FXML
    public void yesExitButton(ActionEvent event) {
        Platform.exit();
    }
}
