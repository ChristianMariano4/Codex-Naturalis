package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.enumerations.GUIScene;
import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class LobbyController extends GUIController{
    @FXML
    public void createGame(ActionEvent event)
    {
        try {
            viewGUI.createGame();
        }
        catch(ServerDisconnectedException e)
        {
            //TODO: server disconnected GUI screen
        }
    }
    @FXML
    public void joinGame(ActionEvent event)
    {
        gui.switchScene(GUIScene.JOINGAME);
    }
    @FXML
    public void quit(ActionEvent event) {

    }
}
