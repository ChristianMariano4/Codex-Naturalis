package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.enumerations.GUIScene;
import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import it.polimi.ingsw.exceptions.WrongInputException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.IOException;

/**
 * The LoginScreenController class is responsible for handling user interactions in the login screen of the GUI.
 * It extends the GUIController class.
 */
public class LoginScreenController extends GUIController {
    public TextField username; // The TextField for the username input.
    public Label errorMessage; // The Label for displaying error messages.

    //public SoundController soundController; // The SoundController for handling sound effects.

    /**
     * Handles the action of the connect button.
     * It sets the username and switches the scene to the lobby.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the thread is interrupted.
     * @throws ServerDisconnectedException If the server is disconnected.
     */
    @FXML
    public void connectButton() throws IOException, InterruptedException, ServerDisconnectedException {
        try {
            viewGUI.setUsername(username.getText());
            gui.switchScene(GUIScene.LOBBY);
        } catch (WrongInputException e) {
            errorMessage.setDisable(false);
            errorMessage.setVisible(true);
        }

    }

    /**
     * Initializes the scene.
     * It sets an event handler for the username TextField that triggers the connect button action when the enter key is pressed.
     */
    @Override
    public void sceneInitializer() {
        username.setOnKeyPressed(e ->
        {
            if(e.getCode().equals(KeyCode.ENTER)) {
                try {
                    connectButton();
                } catch (IOException | InterruptedException | ServerDisconnectedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}