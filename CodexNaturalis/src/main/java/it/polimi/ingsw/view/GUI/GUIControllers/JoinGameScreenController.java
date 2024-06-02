package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.enumerations.GUIScene;
import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import it.polimi.ingsw.model.Game;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class JoinGameScreenController extends GUIController {
    public ChoiceBox<String> gameList;

    public String gameOnTextFlow;
    public TextFlow textFlow;
    public Button joinButton;
    private ArrayList<Integer> availableGames = new ArrayList<>();

    public ListView<String> gameList2;
    ObservableList<String> items = FXCollections.observableArrayList();
    @FXML
    public void back() {
        gui.switchScene(GUIScene.LOBBY);
    }

    @FXML
    public void joinButton() {
        if(gameList2.getSelectionModel().getSelectedItem() != null) {
            joinButton.setDisable(false);
            joinButton.setVisible(true);
        }
    }
    @FXML
    public void join(ActionEvent event)
    {
        int gameId;
        String choice = gameList2.getSelectionModel().getSelectedItem();
        choice = choice.replace("Game id: ", "");
        gameId = Integer.parseInt(choice);
        try
        {
            viewGUI.joinGame(gameId);
            gui.switchScene(GUIScene.GAMELOBBY);
        } catch (ServerDisconnectedException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void sceneInitializer() {



        joinButton.setDisable(true);
        joinButton.setVisible(false);

        gameList2.getItems().clear();
        gameList2.setItems(items);
        try {
            ArrayList<Integer> games = (ArrayList<Integer>) viewGUI.showAvailableGames();
            for(int i: games) {
                items.add("Game id: " + i);
            }
            availableGames.addAll(viewGUI.showAvailableGames());
            Task<Void> checkGames = new Task() {
                @Override
                protected Object call() throws Exception {
                    while (availableGames.containsAll(viewGUI.showAvailableGames()));
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            sceneInitializer();
                        }
                    });

                    return null;
                }
            };
            new Thread(checkGames).start();

        } catch (ServerDisconnectedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
