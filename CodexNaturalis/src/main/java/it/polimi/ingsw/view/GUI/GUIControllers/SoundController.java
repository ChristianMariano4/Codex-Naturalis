package it.polimi.ingsw.view.GUI.GUIControllers;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URISyntaxException;

public class SoundController extends GUIController {

    private MediaView mediaView;

    public void buttonClick() {
        try {
            String fileName = getClass().getResource("/sounds/buttonClick.wav").toURI().toString();
            Media media = new Media(fileName);
            MediaPlayer player = new MediaPlayer(media);
            mediaView = new MediaView();
            mediaView.setMediaPlayer(player);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mediaView.getMediaPlayer().play();
    }
}