package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.enumerations.GUIScene;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.*;

/**
 * This class is responsible for controlling the scoreboard in the GUI.
 * It extends the GUIController class.
 */
public class ScoreboardController extends GUIController{
    public Pane p0;
    public Pane p1;
    public Pane p2;
    public Pane p3;
    private final ArrayList<Pane> panes = new ArrayList<>();
    public Label p0Label;
    public Label p1Label;
    public Label p2Label;
    public Label p3Label;
    private final ArrayList<Label> labels = new ArrayList<>();

    /**
     * Initializes the scene by adding all panes and labels to their respective lists.
     * It then retrieves the current game from the viewGUI and sorts the players based on their points.
     * Finally, it sets the visibility and enabled state of each pane and label based on the sorted players.
     */
    @Override
    public void sceneInitializer() {
        panes.add(p0);
        panes.add(p1);
        panes.add(p2);
        panes.add(p3);
        labels.add(p0Label);
        labels.add(p1Label);
        labels.add(p2Label);
        labels.add(p3Label);

        Game game = viewGUI.getGame();
        LinkedHashMap<String, Integer> playersPlacement = new LinkedHashMap<>();
        ArrayList<Player> sortedPlayers = new ArrayList<>();
        List<Integer> points = new ArrayList<>(game.getListOfPlayers().stream().filter(e -> !e.getIsDisconnected()).map(Player::getPoints).toList());
        Collections.sort(points);
        Collections.reverse(points);
        for(Integer point: points)
        {
            sortedPlayers.addAll(game.getListOfPlayers().stream().filter(e -> (e.getPoints() == point  && !e.getIsDisconnected())).toList());
        }
        for(Player p : sortedPlayers) {
            playersPlacement.put(p.getUsername(), p.getPoints());
        }
        int pane = 0;
        for(String player : playersPlacement.keySet())
        {
            panes.get(pane).setDisable(false);
            panes.get(pane).setVisible(true);
            labels.get(pane).setText(player + ": " + playersPlacement.get(player));
            pane++;
        }
    }

    /**
     * Handles the action for the return to lobby button.
     * It initializes the viewGUI and changes the scene to the lobby.
     */
    @FXML
    public void returnToLobby() {
        viewGUI.initialize();
        viewGUI.changeScene(GUIScene.LOBBY);
    }
}
