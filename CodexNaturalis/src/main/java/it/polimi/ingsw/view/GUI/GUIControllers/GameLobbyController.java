package it.polimi.ingsw.view.GUI.GUIControllers;

import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class GameLobbyController extends GUIController {
    public TabPane rulebookTabPane;
    public Pane playersPane;
    public Pane playersPane1;
    private boolean isInRulebook = true;
    public Text p1name;
    public Text p2name;
    public Text p3name;
    public Text p4name;

    public void rulebookButton() {
        if(isInRulebook) {
            rulebookTabPane.setDisable(false);
            rulebookTabPane.setVisible(true);

            playersPane.setDisable(true);
            playersPane.setVisible(false);
            playersPane1.setDisable(true);
            playersPane1.setVisible(false);

            isInRulebook = false;
        } else  {
            rulebookTabPane.setDisable(true);
            rulebookTabPane.setVisible(false);

            playersPane.setDisable(false);
            playersPane.setVisible(true);
            playersPane1.setDisable(false);
            playersPane1.setVisible(true);

            isInRulebook = true;
        }

    }
}
