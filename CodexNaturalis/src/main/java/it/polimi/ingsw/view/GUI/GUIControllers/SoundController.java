package it.polimi.ingsw.view.GUI.GUIControllers;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URISyntaxException;

public class SoundController extends GUIController {

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