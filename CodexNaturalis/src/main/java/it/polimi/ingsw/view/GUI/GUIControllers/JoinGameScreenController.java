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

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * This class is responsible for controlling the join game screen in the GUI.
 * It extends the GUIController class.
 */
public class JoinGameScreenController extends GUIController {
    public ChoiceBox<String> gameList;

    public String gameOnTextFlow;
    public TextFlow textFlow;
    public Button joinButton;
    private final ArrayList<Integer> availableGames = new ArrayList<>();
    Task<Void> checkGames;

    public ListView<String> gameList2;
    ObservableList<String> items = FXCollections.observableArrayList();
    public Pane errorPane;
    private ScheduledService<Void> service;
    public Pane exitPane;

    /**
     * Handles the action for the back button.
     * It switches the scene to the lobby.
     */
    @FXML
    public void back() {
        gui.switchScene(GUIScene.LOBBY);
    }

    /**
     * Handles the action for the join button.
     * It enables and makes visible the join button if a game is selected from the list.
     */
    @FXML
    public void joinButton() {
        if (gameList2.getSelectionModel().getSelectedItem() != null) {
            joinButton.setDisable(false);
            joinButton.setVisible(true);
        }
    }

    /**
     * Handles the action for the join button.
     * It joins the selected game and switches the scene to the game lobby or the game depending on the game status.
     * If an exception occurs during this process, it displays the error pane.
     */
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

    /**
     * Handles the action for the exit from error button.
     * It hides the error pane.
     */
    @FXML
    public void exitFromErrorButton() {
        errorPane.setDisable(true);
        errorPane.setVisible(false);
    }

    /**
     * Initializes the scene by refreshing the game list.
     */
    @Override
    public void sceneInitializer() {
        refresh();
    }

    /**
     * Handles the action for the refresh button.
     * It disables and hides the join button, clears the game list, and then retrieves and displays the available games.
     */
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

    /**
     * Handles the action for the quit button.
     * It displays the exit pane.
     */
    @FXML
    public void quitButton() {
        exitPane.setDisable(false);
        exitPane.setVisible(true);
    }

    /**
     * Handles the action for the no exit button.
     * It hides the exit pane.
     */
    @FXML
    public void noExitButton(ActionEvent event) {
        exitPane.setDisable(true);
        exitPane.setVisible(false);
    }

    /**
     * Handles the action for the yes exit button.
     * It exits the platform.
     */
    @FXML
    public void yesExitButton(ActionEvent event) {
        Platform.exit();
    }
}
