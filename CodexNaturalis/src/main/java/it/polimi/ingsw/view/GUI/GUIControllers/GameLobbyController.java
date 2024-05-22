package it.polimi.ingsw.view.GUI.GUIControllers;

import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class GameLobbyController extends GUIController {
    public TabPane rulebookTabPane;
    private boolean isInRulebook = true;
    public Button rulebookButton;

    public void rulebookButton() {
        if(isInRulebook) {
            rulebookTabPane.setDisable(false);
            rulebookTabPane.setVisible(true);
            isInRulebook = false;
        } else  {
            rulebookTabPane.setDisable(true);
            rulebookTabPane.setVisible(false);
            isInRulebook = true;
        }

    }
}
