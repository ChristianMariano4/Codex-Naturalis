package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class LobbyController extends GUIController{

    public ChoiceBox<String> gameList;
    public Button join;

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
        try {
            ArrayList<Integer> games = (ArrayList<Integer>) viewGUI.showAvailableGames();
            for(int i : games)
            {
                gameList.getItems().add("Game id: " + i);
            }
            gameList.setDisable(false);
            gameList.getSelectionModel().selectFirst();
            gameList.setVisible(true);
            if(gameList.getSelectionModel().getSelectedItem() != null) {
                join.setDisable(false);
                join.setVisible(true);
            }


        } catch (ServerDisconnectedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void join(ActionEvent event)
    {
        int gameId;
        String choice = gameList.getSelectionModel().getSelectedItem();
        choice = choice.replace("Game id: ", "");
        gameId = Integer.parseInt(choice);
        try
        {
            viewGUI.joinGame(gameId);
        } catch (ServerDisconnectedException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    public void quit(ActionEvent event)
    {}
}
