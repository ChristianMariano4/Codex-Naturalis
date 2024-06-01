package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import javafx.scene.control.TextArea;


public class ScoreboardController extends GUIController{

    public TextArea playerPoints;

    @Override
    public void sceneInitializer()
    {
        StringBuilder points = new StringBuilder();
        Game game = viewGUI.getGame();
        for(Player player : game.getListOfPlayers())
        {
            points.append(player.getUsername()).append(": ").append(player.getPoints()).append("\n");
        }
        playerPoints.setText(points.toString());

    }
}
