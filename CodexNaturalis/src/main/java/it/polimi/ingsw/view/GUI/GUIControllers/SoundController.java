package it.polimi.ingsw.view.GUI.GUIControllers;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URISyntaxException;

/**
 * This class is responsible for controlling the sound effects in the GUI.
 * It extends the GUIController class.
 */
public class SoundController extends GUIController {

    /**
     * This method is used to play a button click sound effect.
     * It first retrieves the URI of the sound file, creates a new Media object with this URI, and then creates a new MediaPlayer object with this Media.
     * It then sets the volume of the MediaPlayer to 0.5 and plays the sound.
     * If an exception occurs during this process, a RuntimeException is thrown.
     */
    public void buttonClick() {
        try {
            String fileName = getClass().getResource("/sounds/buttonClick.wav").toURI().toString();
            Media media = new Media(fileName);
            MediaPlayer player = new MediaPlayer(media);
            player.setVolume(0.5);
            player.play();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}