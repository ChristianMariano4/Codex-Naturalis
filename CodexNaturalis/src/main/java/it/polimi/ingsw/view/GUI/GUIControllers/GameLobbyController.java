package it.polimi.ingsw.view.GUI.GUIControllers;

import com.sun.media.jfxmedia.control.MediaPlayerOverlay;
import it.polimi.ingsw.enumerations.GUIScene;
import it.polimi.ingsw.exceptions.NotEnoughPlayersException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import javax.lang.model.util.TypeKindVisitor14;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private ArrayList<Label> names = new ArrayList<>();
    public Pane p1;
    public Pane p2;
    public Pane p3;
    public Pane p4;
    private ArrayList<Pane> playerPanes = new ArrayList<>();
    public Button nextButton;
    public Button backButton;
    public Label readyPlayers;
    public Button setReadyButton;
    private Integer playerNum = 1;
    public Pane exitPane;

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
    @FXML
    public void nextRulebookSlide() {
        rulebookTabPane.getSelectionModel().select(rulebookTabPane.getSelectionModel().getSelectedIndex()+1);
        nextButton.disableProperty().bind(rulebookTabPane.getSelectionModel().selectedIndexProperty().greaterThanOrEqualTo(10));
    }
    @FXML
    public void previousRulebookSlide() {
        rulebookTabPane.getSelectionModel().select(rulebookTabPane.getSelectionModel().getSelectedIndex()-1);
        backButton.disableProperty().bind(rulebookTabPane.getSelectionModel().selectedIndexProperty().lessThanOrEqualTo(0));
    }

    public void update(Object update) {
        if(!(update instanceof Game))
            return;
        Game game = (Game) update;

            for (int i = 0; i < 4; i++) {
                try {
                        Label label = names.get(i);
                        String username = game.getListOfPlayers().get(i).getUsername();
                        Pane playerPane = playerPanes.get(i);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                label.setText(username);
                                label.setDisable(false);
                                label.setVisible(true);
                                playerPane.setDisable(false);
                                playerPane.setVisible(true);

                            }
                        });//multithreading javafx

                    new Thread() {
                        public void run() {
                            try {
                                while (!viewGUI.getPlaying()) {
                                    Thread.sleep(1);
                                }
                                Platform.runLater(new Runnable() {

                                    public void run() {
                                        gui.switchScene(GUIScene.GAME);
                                    }
                                });
                            } catch (Exception e) {
                                throw new RuntimeException();
                            }
                        }
                    }.start(); //waiting for game to begin
                } catch (IndexOutOfBoundsException e) {
                    synchronized (playerPanes) {
                        names.get(i).setDisable(true);
                        names.get(i).setVisible(false);
                        playerPanes.get(i).setDisable(true);
                        playerPanes.get(i).setVisible(false);
                    }
                }
            }

    }
    @FXML
    public void setReadyButton() {
        try {
            ArrayList<Integer> playersInfo = viewGUI.setReady();
            HashMap<String, Boolean> readyStatus = viewGUI.getReadyStatus();
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    readyPlayers.setText("Ready players: " + readyStatus.values().stream().filter(e -> e).toList().size() + "/" + playersInfo.get(0));
                    readyPlayers.setDisable(false);
                    readyPlayers.setVisible(true);
                    setReadyButton.setDisable(true);
                }
            });
        }
        catch (Exception e)
        {
            System.exit(-1); //server crashed
            //TODO: handle server disconnection
        }

    }

    @FXML
    public void quitGame() {
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
        try {
            viewGUI.quitGame();
            viewGUI.initialize();
            gui.switchScene(GUIScene.LOBBY);
        }catch (Exception e)
        {
            throw new RuntimeException();
        }
    }

    @Override
    public void sceneInitializer() {
        synchronized (playerPanes) {
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
        }
    }
}
