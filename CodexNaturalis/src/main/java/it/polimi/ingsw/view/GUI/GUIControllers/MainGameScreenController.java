package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerHand;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayableCard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class MainGameScreenController extends GUIController{


    public TabPane rulebookTabPane;
    public Pane rulebookPane;
    private boolean isInRulebook = true;
    public Button nextButton;
    public Button backButton;

    public Pane card1;
    public Pane card2;
    public Pane card3;
    public Pane playerHand;
    private ArrayList<Pane> playerHandPanes = new ArrayList<>();

    @FXML
    public void rulebookButton(ActionEvent actionEvent) {
        if(isInRulebook) {
            rulebookPane.setDisable(false);
            rulebookPane.setVisible(true);
            isInRulebook = false;
        } else  {
            rulebookPane.setDisable(true);
            rulebookPane.setVisible(false);
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

    public void update(Object update)
    {

    }
    public void sceneInitializer() {



        playerHandPanes.add(card1);
        playerHandPanes.add(card2);
        playerHandPanes.add(card3);


        try {


            Game game = viewGUI.getGame();
            PlayerHand hand = game.getPlayer(viewGUI.getUsername()).getPlayerHand();
            for(int i = 0; i<3 ; i++)
            {
                PlayableCard card = hand.getCardsInHand().get(i);
                playerHandPanes.get(i).setStyle(getStyle(getCardUrl(card)));
            }
            playerHand.setDisable(false);
            playerHand.setVisible(true);
        }
        catch (Exception e)
        {

        }

    }
    private String  getCardUrl(Card card)
    {
        String url = "images/cardsFront/";
        if(card.getCardId() < 100)
            url = url + "0";
        if(card.getCardId() < 10)
            url = url + "0";
        url = url + card.getCardId() + ".png";
        return url;
    }
    private String getStyle(String url)
    {
        String style = "-fx-background-image: url(";
        style = style + url;
        style = style + ");";
        style = style + " -fx-background-size: cover;";
        return style;
    }


}
