package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.enumerations.GUIScene;
import it.polimi.ingsw.model.Game;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is responsible for controlling the game lobby in the GUI.
 * It extends the GUIController class.
 */
public class GameLobbyController extends GUIController {
    public TabPane rulebookTabPane;
    public Pane rulebookPane;
    public Pane playersPane;
    public Pane playersPane1;
    private boolean isInRulebook = true;
    public Label p1name;
    public Label p2name;
    public Label p3name;
    public Label p4name;
    private final ArrayList<Label> names = new ArrayList<>();
    public Pane p1;
    public Pane p2;
    public Pane p3;
    public Pane p4;
    private final ArrayList<Pane> playerPanes = new ArrayList<>();
    public Button nextButton;
    public Button backButton;
    public Label readyPlayers;
    public Button setReadyButton;
    public Pane exitPane;
    public Label gameIdLabel;
    public Thread waitThread;

    /**
     * Handles the action for the rulebook button.
     * It toggles the visibility and enabled state of the rulebook and players panes.
     */
    @FXML
    public void rulebookButton() {
        synchronized (playerPanes) {
            if (isInRulebook) {
                rulebookPane.setDisable(false);
                rulebookPane.setVisible(true);

                playersPane.setDisable(true);
                playersPane.setVisible(false);
                playersPane1.setDisable(true);
                playersPane1.setVisible(false);

                isInRulebook = false;
            } else {
                rulebookPane.setDisable(true);
                rulebookPane.setVisible(false);

                playersPane.setDisable(false);
                playersPane.setVisible(true);
                playersPane1.setDisable(false);
                playersPane1.setVisible(true);

                isInRulebook = true;
            }
        }
    }

    /**
     * Handles the action for the next rulebook slide button.
     * It selects the next slide in the rulebook tab pane and disables the next button when the last slide is reached.
     */
    @FXML
    public void nextRulebookSlide() {
        rulebookTabPane.getSelectionModel().select(rulebookTabPane.getSelectionModel().getSelectedIndex()+1);
        nextButton.disableProperty().bind(rulebookTabPane.getSelectionModel().selectedIndexProperty().greaterThanOrEqualTo(10));
    }

    /**
     * Handles the action for the previous rulebook slide button.
     * It selects the previous slide in the rulebook tab pane and disables the back button when the first slide is reached.
     */
    @FXML
    public void previousRulebookSlide() {
        rulebookTabPane.getSelectionModel().select(rulebookTabPane.getSelectionModel().getSelectedIndex()-1);
        backButton.disableProperty().bind(rulebookTabPane.getSelectionModel().selectedIndexProperty().lessThanOrEqualTo(0));
    }

    /**
     * Updates the game lobby based on the given update object.
     * It updates the visibility and enabled state of each player pane and label based on the players in the game.
     * @param update The object representing the update.
     */
    public void update(Object update) {
        if(!(update instanceof Game game))
            return;
        Platform.runLater(() -> {
            for (int i = 0; i < 4; i++) {
                try {
                    Label label = names.get(i);
                    String username = game.getListOfPlayers().get(i).getUsername();
                    Pane playerPane = playerPanes.get(i);
                    label.setText(username);
                    label.setDisable(false);
                    label.setVisible(true);
                    playerPane.setDisable(false);
                    playerPane.setVisible(true);

                } catch (IndexOutOfBoundsException e) {
                    names.get(i).setDisable(true);
                    names.get(i).setVisible(false);
                    playerPanes.get(i).setDisable(true);
                    playerPanes.get(i).setVisible(false);
                }
            }
        });
    }

    /**
     * Handles the action for the set ready button.
     * It sets the player as ready and updates the ready players label.
     */
    @FXML
    public void setReadyButton() {
        try {
            ArrayList<Integer> playersInfo = viewGUI.setReady();
            HashMap<String, Boolean> readyStatus = viewGUI.getReadyStatus();
            Platform.runLater(() -> {
                readyPlayers.setText("Ready players: " + readyStatus.values().stream().filter(e -> e).toList().size() + "/" + playersInfo.getFirst());
                readyPlayers.setDisable(false);
                readyPlayers.setVisible(true);
                setReadyButton.setDisable(true);
            });
        }
        catch (Exception e)
        {
            System.exit(-1); //server crashed
        }

    }

    /**
     * Handles the action for the quit game button.
     * It displays the exit pane.
     */
    @FXML
    public void quitGame() {
        exitPane.setDisable(false);
        exitPane.setVisible(true);
    }

    /**
     * Handles the action for the no exit button.
     * It hides the exit pane.
     */
    @FXML
    public void noExitButton() {
        exitPane.setDisable(true);
        exitPane.setVisible(false);
    }

    /**
     * Handles the action for the yes exit button.
     * It quits the game, initializes the viewGUI, interrupts the wait thread, and changes the scene to the lobby.
     */
    @FXML
    public void yesExitButton() {
        try {
            viewGUI.quitGame();
            viewGUI.initialize();
            if(waitThread!=null)
                waitThread.interrupt();
            gui.switchScene(GUIScene.LOBBY);
        }catch (Exception e)
        {
            throw new RuntimeException();
        }
    }

    /**
     * Initializes the scene by adding all player names and panes to their respective lists.
     * It then retrieves the current game from the viewGUI and updates the game lobby.
     * Finally, it starts a new thread that waits for the game to start and then changes the scene to the game.
     */
    @Override
    public void sceneInitializer() {
        gameIdLabel.setText("GameId: " + viewGUI.getGame().getGameId());
        gameIdLabel.setStyle("-fx-text-alignment: center; -fx-background-color: rgba(0,0,0,0.4); -fx-background-radius: 20;");

            this.names.add(p1name);
            this.names.add(p2name);
            this.names.add(p3name);
            this.names.add(p4name);

            this.playerPanes.add(p1);
            this.playerPanes.add(p2);
            this.playerPanes.add(p3);
            this.playerPanes.add(p4);

            Game game = viewGUI.getGame();
            update(game);
            waitThread = new Thread(()-> {
                    try {
                        while (true) {
                            if(Thread.interrupted())
                                return;
                            if(viewGUI.getPlaying())
                                break;
                            Thread.sleep(1);
                        }
                        Platform.runLater(() -> gui.switchScene(GUIScene.GAME));
                    } catch (InterruptedException e)
                    {
                        //end
                    }
            });
            waitThread.start();
    }
}
