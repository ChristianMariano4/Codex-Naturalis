package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.enumerations.GUIScene;
import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import it.polimi.ingsw.model.Game;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class JoinGameScreenController extends GUIController {
    public ChoiceBox<String> gameList;

    public String gameOnTextFlow;
    public TextFlow textFlow;
    public Button join;
    @FXML
    public void back() {
        gui.switchScene(GUIScene.LOBBY);
    }

    @FXML
    public void allGames() {
        try {
            gameList.getItems().clear();
            ArrayList<Integer> games = (ArrayList<Integer>) viewGUI.showAvailableGames();
            for(int i : games)
            {
                gameList.getItems().add("Game id: " + i);
            }
            gameList.setDisable(false);
            gameList.getSelectionModel().selectFirst();
            gameList.setVisible(true);
/*            if(gameList.getSelectionModel().getSelectedItem() != null) {
                join.setDisable(false);
                join.setVisible(true);
            }*/


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


    public void gameInformations() {
        int gameId;
        String choice = gameList.getSelectionModel().getSelectedItem();
        if(!choice.equals(gameOnTextFlow)) {
            //print all informations
            gameOnTextFlow = choice;
            System.out.println("GameId:" + choice);
            //TODO: get game by id?
            gameId = Integer.parseInt(choice);
            //Game game = viewGUI.getGameById(gameId);

        }
        choice = choice.replace("Game id: ", "");

        //textFlow
    }
}
