package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import it.polimi.ingsw.exceptions.WrongInputException;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.ViewGUI;
import it.polimi.ingsw.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginScreenController extends GUIController {
    public TextField username;


    @FXML
    public void actionEnter(ActionEvent event) throws IOException, InterruptedException, ServerDisconnectedException {
        try
        {
            viewGUI.setUsername(username.getText());
        }
        catch (WrongInputException e)
        {

            gui.displayErrorMessage(); //send feedback to GUI by calling gui method
        }

    }
}
