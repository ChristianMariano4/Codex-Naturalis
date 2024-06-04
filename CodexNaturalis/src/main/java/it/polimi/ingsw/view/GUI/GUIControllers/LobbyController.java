package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.enumerations.GUIScene;
import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class LobbyController extends GUIController{
    public Button createGame;
    public Button joinGame;
    public Pane numChoice;
    public Pane exitPane;


    @FXML
    public void createGame(ActionEvent event)
    {
        createGame.setDisable(true);
        createGame.setVisible(false);

        joinGame.setDisable(true);
        joinGame.setVisible(false);

        numChoice.setDisable(false);
        numChoice.setVisible(true);

    }

    @FXML
    public void back(ActionEvent event)
    {
        createGame.setDisable(false);
        createGame.setVisible(true);

        joinGame.setDisable(false);
        joinGame.setVisible(true);

        numChoice.setDisable(true);
        numChoice.setVisible(false);

    }
    @FXML
    public void twoPlayers(ActionEvent event)
    {
        try {
            viewGUI.createGame(2);
        }
        catch(ServerDisconnectedException e)
        {
            //TODO: server disconnected GUI screen
        }
        gui.switchScene(GUIScene.GAMELOBBY);
    }

    @FXML
    public void threePlayers(ActionEvent event)
    {
        try {
            viewGUI.createGame(3);
        }
        catch(ServerDisconnectedException e)
        {
            //TODO: server disconnected GUI screen
        }
        gui.switchScene(GUIScene.GAMELOBBY);


    }

    @FXML
    public void fourPlayers(ActionEvent event)
    {
        try {
            viewGUI.createGame(4);
        }
        catch(ServerDisconnectedException e)
        {
            //TODO: server disconnected GUI screen
        }
        gui.switchScene(GUIScene.GAMELOBBY);

    }
    @FXML
    public void joinGame(ActionEvent event)
    {
        gui.switchScene(GUIScene.JOINGAME);
    }
    @FXML
    public void quit(ActionEvent event) {
        exitPane.setDisable(false);
        exitPane.setVisible(true);

    }
    @FXML
    public void noExitButton(ActionEvent event) {
        exitPane.setDisable(true);
        exitPane.setVisible(false);
    }
    @FXML
    public void yesExitButton(ActionEvent event) {
        Platform.exit();
    }
}
