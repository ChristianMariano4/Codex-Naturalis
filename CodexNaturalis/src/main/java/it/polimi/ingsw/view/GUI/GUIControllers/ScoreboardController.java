package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.enumerations.GUIScene;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class ScoreboardController extends GUIController{

    public TextArea playerPoints;

    @Override
    public void sceneInitializer()
    {
        StringBuilder points = new StringBuilder();
        Game game = viewGUI.getGame();
        ArrayList<Player> playersList = game.getListOfPlayers();
        ArrayList<Integer> pPoints = new ArrayList<>();
        for(Player player: playersList) {
            if(!pPoints.contains(player.getPoints())) {
                pPoints.add(player.getPoints());
            }
        }
        pPoints.sort(Collections.reverseOrder());
        for(Integer p: pPoints) {
            for(Player player: playersList) {
                if(p.equals(player.getPoints())) {
                    points.append(player.getUsername()).append(": ").append(player.getPoints()).append("\n");
                }
            }
        }
        playerPoints.setText(points.toString());
    }
    @FXML
    public void returnToLobby() {
        viewGUI.initialize();
        viewGUI.changeScene(GUIScene.LOBBY);
    }
}
