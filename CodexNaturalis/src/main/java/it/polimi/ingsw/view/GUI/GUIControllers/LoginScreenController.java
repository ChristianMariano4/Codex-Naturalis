package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.enumerations.GUIScene;
import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import it.polimi.ingsw.exceptions.WrongInputException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.IOException;

public class LoginScreenController extends GUIController {
    public TextField username;
    public Label errorMessage;

    public SoundController soundController;


    @FXML
    public void connectButton() throws IOException, InterruptedException, ServerDisconnectedException {
        try
        {
           // soundController = new SoundController();
            //soundController.buttonClick();
            viewGUI.setUsername(username.getText());
            gui.switchScene(GUIScene.LOBBY);
        }
        catch (WrongInputException e)
        {
            errorMessage.setDisable(false);
            errorMessage.setVisible(true);
        }

    }

    @Override
    public void sceneInitializer() {
        username.setOnKeyPressed(e ->
        {
            if(e.getCode().equals(KeyCode.ENTER))
            {
                try {
                    connectButton();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } catch (ServerDisconnectedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
