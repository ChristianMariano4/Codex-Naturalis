package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.view.GUI.ViewGUI;
import it.polimi.ingsw.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LoginScreenController extends GUIController {
    @FXML
    public void actionEnter(ActionEvent event) {
        viewGUI.setUsername();
    }
}
