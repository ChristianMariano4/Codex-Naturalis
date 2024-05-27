package it.polimi.ingsw.view.GUI.GUIControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class GameLobbyController extends GUIController {
    public TabPane rulebookTabPane;
    public Pane rulebookPane;
    public Pane playersPane;
    public Pane playersPane1;
    private boolean isInRulebook = true;
    public Text p1name;
    public Text p2name;
    public Text p3name;
    public Text p4name;
    public Pane p1;
    public Pane p2;
    public Pane p3;
    public Pane p4;
    public Button nextButton;
    public Button backButton;
    private Integer playerNum = 1;

    public void rulebookButton() {
        if(isInRulebook) {
            rulebookPane.setDisable(false);
            rulebookPane.setVisible(true);

            playersPane.setDisable(true);
            playersPane.setVisible(false);
            playersPane1.setDisable(true);
            playersPane1.setVisible(false);

            isInRulebook = false;
        } else  {
            rulebookPane.setDisable(true);
            rulebookPane.setVisible(false);

            playersPane.setDisable(false);
            playersPane.setVisible(true);
            playersPane1.setDisable(false);
            playersPane1.setVisible(true);

            isInRulebook = true;
        }
    }
    @FXML
    public void nextRulebookSlide() {
        rulebookTabPane.getSelectionModel().select(rulebookTabPane.getSelectionModel().getSelectedIndex()+1);
        nextButton.disableProperty().bind(rulebookTabPane.getSelectionModel().selectedIndexProperty().greaterThanOrEqualTo(10));
    }
    @FXML
    public void previousRulebookSlide() {
        rulebookTabPane.getSelectionModel().select(rulebookTabPane.getSelectionModel().getSelectedIndex()-1);
        backButton.disableProperty().bind(rulebookTabPane.getSelectionModel().selectedIndexProperty().lessThanOrEqualTo(0));
    }
    public void showPlayerPane() {
        switch (playerNum) {
            case 1:
                p1.setDisable(false);
                p1.setVisible(true);
                p1name.setText("fdhsfhskdj");
                break;
            case 2:
                p2.setDisable(false);
                p2.setVisible(true);
                p2name.setText("fdhsfhskdj");
                break;
            case 3:
                p3.setDisable(false);
                p3.setVisible(true);
                p3name.setText("fdhsfhskdj");
                break;
            case 4:
                p4.setDisable(false);
                p4.setVisible(true);
                p4name.setText("fdhsfhskdj");
                break;
        }
        playerNum++;
    }
    @FXML
    public void setReadyButton() {

    }

}
