package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

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
    }
    @FXML
    public void quit(ActionEvent event)
    {}
}
