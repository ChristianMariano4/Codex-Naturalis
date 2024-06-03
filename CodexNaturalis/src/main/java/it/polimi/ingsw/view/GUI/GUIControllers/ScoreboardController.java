package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.enumerations.GUIScene;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class ScoreboardController extends GUIController{

    public TextArea playerPoints;
    public Pane p0;
    public Pane p1;
    public Pane p2;
    public Pane p3;
    private ArrayList<Pane> panes;
    public Label p0Label;
    public Label p1Label;
    public Label p2Label;
    public Label p3Label;
    private ArrayList<Label> labels;


/*
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
    }*/
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
        ArrayList<Player> playersList = game.getListOfPlayers();
        ArrayList<Integer> pPoints = new ArrayList<>();
        for(Player player: playersList) {
            if(!pPoints.contains(player.getPoints())) {
                pPoints.add(player.getPoints());
            }
        }
        pPoints.sort(Collections.reverseOrder());
        for(int i = 0; i< viewGUI.getGame().getListOfPlayers().size(); i++) {
            int p = pPoints.get(i);
            for(Player player: playersList) {
                if(player.getPoints() == p) {
                    panes.get(i).setDisable(false);
                    panes.get(i).setVisible(true);
                    labels.get(i).setText(player.getUsername() + ": " + player.getPoints());
                }
            }
        }

    }
    @FXML
    public void returnToLobby() {
        viewGUI.initialize();
        viewGUI.changeScene(GUIScene.LOBBY);
    }
}
