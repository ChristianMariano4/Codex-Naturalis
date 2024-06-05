package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.enumerations.GUIScene;
import it.polimi.ingsw.enumerations.GameStatus;
import it.polimi.ingsw.exceptions.GameNotFoundException;
import it.polimi.ingsw.exceptions.NotExistingPlayerException;
import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import it.polimi.ingsw.model.Game;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextFlow;
import javafx.concurrent.ScheduledService;
import javafx.util.Duration;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

import static org.fusesource.jansi.Ansi.ansi;

public class JoinGameScreenController extends GUIController {
    public ChoiceBox<String> gameList;

    public String gameOnTextFlow;
    public TextFlow textFlow;
    public Button joinButton;
    private ArrayList<Integer> availableGames = new ArrayList<>();
    Task<Void> checkGames;

    public ListView<String> gameList2;
    ObservableList<String> items = FXCollections.observableArrayList();
    public Pane errorPane;
    private ScheduledService<Void> service;
    public Pane exitPane;

    @FXML
    public void back() {
        gui.switchScene(GUIScene.LOBBY);
    }

    @FXML
    public void joinButton() {
        if (gameList2.getSelectionModel().getSelectedItem() != null) {
            joinButton.setDisable(false);
            joinButton.setVisible(true);
        }
    }

    @FXML
    public void join(ActionEvent event) {
        int gameId;
        String choice = gameList2.getSelectionModel().getSelectedItem();
        choice = choice.replaceAll("\\D+", "");
        gameId = Integer.parseInt(choice);
        try {
            viewGUI.initialize();
            viewGUI.joinGame(gameId);
            if (viewGUI.getGame().getGameStatus().getStatusNumber() < GameStatus.ALL_PLAYERS_READY.getStatusNumber()) {
                gui.switchScene(GUIScene.GAMELOBBY);
            } else {
                gui.switchScene(GUIScene.GAME);
            }
        } catch (ServerDisconnectedException e) {
            throw new RuntimeException(e);
        } catch (NotExistingPlayerException e) {
            errorPane.setDisable(false);
            errorPane.setVisible(true);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (GameNotFoundException e) {
            errorPane.setDisable(false);
            errorPane.setVisible(true);
        }
    }

    @FXML
    public void exitFromErrorButton() {
        errorPane.setDisable(true);
        errorPane.setVisible(false);
    }

    @Override
    public void sceneInitializer() {
        refresh();
    }

    @FXML
    public void refresh() {
        joinButton.setDisable(true);
        joinButton.setVisible(false);

        gameList2.getItems().clear();
        gameList2.setItems(items);
        try {
            ArrayList<Game> games = viewGUI.showAvailableGames();
            for (Game game : games) {
                if (game.getGameStatus().getStatusNumber() < GameStatus.ALL_PLAYERS_JOINED.getStatusNumber()) {
                    items.add("GameID: " + game.getGameId());
                } else if (game.getGameStatus().getStatusNumber() == GameStatus.ALL_PLAYERS_JOINED.getStatusNumber()) {
                    items.add("GameID: " + game.getGameId() + " - FULL");
                } else if (game.getGameStatus().getStatusNumber() >= GameStatus.ALL_PLAYERS_READY.getStatusNumber()) {
                    items.add("GameID: " + game.getGameId() + " - STARTED");
                }
            }
            availableGames.clear();
            availableGames.addAll(viewGUI.showAvailableGames().stream().map(Game::getGameId).toList());
        } catch (ServerDisconnectedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void quitButton() {
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
