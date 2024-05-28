package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.enumerations.GUIScene;
import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerHand;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.StarterCard;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    public Label waitTurn;
    public Pane markerSelection;
    public Pane marker1;
    public Pane marker2;
    public Pane marker3;
    public Pane marker4;
    public Pane MPane;
    private ArrayList<Pane> markerPanes = new ArrayList<>();

    public Pane starterCardSide;
    public Pane starterFront;
    public Pane starterBack;

    public Pane secretObjective;
    public Pane secret1;
    public Pane secret2;
    public Pane shared1;
    public Pane shared2;

    public Pane waitForBegin;

    public Pane scoreBoard;
    public Pane plat0;
    public Pane plat1;
    public Pane plat2;
    public Pane plat3;
    public Pane plat4;
    public Pane plat5;
    public Pane plat6;
    public Pane plat7;
    public Pane plat8;
    public Pane plat9;
    public Pane plat10;
    public Pane plat11;
    public Pane plat12;
    public Pane plat13;
    public Pane plat14;
    public Pane plat15;
    public Pane plat16;
    public Pane plat17;
    public Pane plat18;
    public Pane plat19;
    public Pane plat20;
    public Pane plat21;
    public Pane plat22;
    public Pane plat23;
    public Pane plat24;
    public Pane plat25;
    public Pane plat26;
    public Pane plat27;
    public Pane plat28;
    public Pane plat29;
    private ArrayList<Pane> platPanes = new ArrayList<>();

    public Pane objectiveCards;
    public Pane sharedLeft;
    public Pane sharedRight;
    public Pane secret;



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

        markerPanes.add(marker1);
        markerPanes.add(marker2);
        markerPanes.add(marker3);
        markerPanes.add(marker4);
        preGame();

    }
    private String  getCardUrl(Card card, Side side)
    {
        String url;
        if(side.equals(Side.FRONT))
            url = "images/cardsFront/";
        else
            url = "images/cardsBack/";
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
    Runnable markerThread = () -> {
        try {
            while (!viewGUI.getMarkerTurn()) {
                Thread.sleep(1);
            }
            Platform.runLater(new Runnable() {

                public void run() {
                    waitTurn.setDisable(true);
                    waitTurn.setVisible(false);
                    MPane.setDisable(false);
                    MPane.setVisible(true);

                    int i = 0;
                    for(Marker marker: viewGUI.getGame().getAvailableMarkers())
                    {

                        markerPanes.get(i).setStyle(getStyle(marker.getPath()));
                        markerPanes.get(i).setDisable(false);
                        markerPanes.get(i).setVisible(true);
                        i++;
                    }

                }
            });
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
    };
    private void preGame()
    {
        new Thread(markerThread).start();
    }
    @FXML
    private void markerOne()
    {
        try {

            viewGUI.setMarker(viewGUI.getGame().getAvailableMarkers().get(0));
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
        markerSelection.setDisable(true);
        markerSelection.setVisible(false);
        chooseStarterCardSide();
    }
    @FXML
    private void markerTwo()
    {
        try {

            viewGUI.setMarker(viewGUI.getGame().getAvailableMarkers().get(1));
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
        markerSelection.setDisable(true);
        markerSelection.setVisible(false);
        chooseStarterCardSide();
    }
    @FXML
    private void markerThree()
    {
        try {

            viewGUI.setMarker(viewGUI.getGame().getAvailableMarkers().get(2));
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
        markerSelection.setDisable(true);
        markerSelection.setVisible(false);
        chooseStarterCardSide();
    }
    @FXML
    private void markerFour()
    {
        try {

            viewGUI.setMarker(viewGUI.getGame().getAvailableMarkers().get(3));
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
        markerSelection.setDisable(true);
        markerSelection.setVisible(false);
        chooseStarterCardSide();
    }

    private void chooseSecretObjective()
    {
        ArrayList<ObjectiveCard> cards = viewGUI.getObjectiveCardsToChoose();
        secret1.setStyle(getStyle(getCardUrl(cards.get(0), Side.FRONT)));
        secret2.setStyle(getStyle(getCardUrl(cards.get(1), Side.FRONT)));

        ArrayList<ObjectiveCard> sharedObjectiveCards = viewGUI.getSharedObjectiveCards();
        shared1.setStyle(getStyle(getCardUrl(sharedObjectiveCards.get(0), Side.FRONT)));
        shared2.setStyle(getStyle(getCardUrl(sharedObjectiveCards.get(1), Side.FRONT)));

        secretObjective.setDisable(false);
        secretObjective.setVisible(true);

    }
    @FXML
    public void secretOne()
    {
        try {
            viewGUI.setSecretObjective(viewGUI.getObjectiveCardsToChoose().get(0));
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
        secretObjective.setDisable(true);
        secretObjective.setVisible(false);
        waitForGameBegin();
    }
    @FXML
    public void secretTwo()
    {
        try {
            viewGUI.setSecretObjective(viewGUI.getObjectiveCardsToChoose().get(1));
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
        secretObjective.setDisable(true);
        secretObjective.setVisible(false);
        waitForGameBegin();
    }
    Runnable waitGameBeginThread = () -> {
        try {
            while (!viewGUI.getGameBegin()) {
                Thread.sleep(1);
            }
            Platform.runLater(new Runnable() {

                public void run() {
                    waitForBegin.setDisable(true);
                    waitForBegin.setVisible(false);
                    tabletopSetup();

                }
            });
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
    };

    public void waitForGameBegin()
    {
        waitForBegin.setDisable(false);
        waitForBegin.setVisible(true);
        new Thread(waitGameBeginThread).start();

    }


    private void chooseStarterCardSide()
    {
        try {
            StarterCard starterCard = viewGUI.getStarterCard();
            starterFront.setStyle(getStyle(getCardUrl(starterCard, Side.FRONT)));
            starterBack.setStyle(getStyle(getCardUrl(starterCard, Side.BACK)));
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
        starterCardSide.setDisable(false);
        starterCardSide.setVisible(true);


    }
    @FXML
    public void starterFrontButton()
    {
        try {
            viewGUI.setStarterCardSide(viewGUI.getStarterCard(), Side.FRONT);
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
        starterCardSide.setDisable(true);
        starterCardSide.setVisible(false);
        chooseSecretObjective();
    }

    @FXML
    public void starterBackButton()
    {
        try {
            viewGUI.setStarterCardSide(viewGUI.getStarterCard(), Side.BACK);
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
        starterCardSide.setDisable(true);
        starterCardSide.setVisible(false);
        chooseSecretObjective();
    }

    public void tabletopSetup()
    {
       initializePlayerHand();
       initializeScoreboard();
       initializeObjectiveCards();


    }

    private void initializePlayerHand()
    {
        Game game = viewGUI.getGame();
        playerHandPanes.add(card1);
        playerHandPanes.add(card2);
        playerHandPanes.add(card3);
        try {
            PlayerHand hand = game.getPlayer(viewGUI.getUsername()).getPlayerHand();
            for(int i = 0; i<3 ; i++)
            {
                PlayableCard card = hand.getCardsInHand().get(i);
                playerHandPanes.get(i).setStyle(getStyle(getCardUrl(card, Side.FRONT)));
            }
            playerHand.setDisable(false);
            playerHand.setVisible(true);
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
    }
    private void initializeScoreboard()
    {
        platPanes.add(plat0);
        platPanes.add(plat1);
        platPanes.add(plat2);
        platPanes.add(plat3);
        platPanes.add(plat4);
        platPanes.add(plat5);
        platPanes.add(plat6);
        platPanes.add(plat7);
        platPanes.add(plat8);
        platPanes.add(plat9);
        platPanes.add(plat10);
        platPanes.add(plat11);
        platPanes.add(plat12);
        platPanes.add(plat13);
        platPanes.add(plat14);
        platPanes.add(plat15);
        platPanes.add(plat16);
        platPanes.add(plat17);
        platPanes.add(plat18);
        platPanes.add(plat19);
        platPanes.add(plat20);
        platPanes.add(plat21);
        platPanes.add(plat22);
        platPanes.add(plat23);
        platPanes.add(plat24);
        platPanes.add(plat25);
        platPanes.add(plat26);
        platPanes.add(plat27);
        platPanes.add(plat28);
        platPanes.add(plat29);
        for(Pane pane : platPanes)
        {
            pane.setDisable(true);
            pane.setVisible(false);
        }
        scoreBoard.setDisable(false);
        scoreBoard.setVisible(true);
    }

    private void initializeObjectiveCards()
    {
        try{
            ArrayList<ObjectiveCard> sharedObjectiveCards = viewGUI.getSharedObjectiveCards();
            sharedLeft.setStyle(getStyle(getCardUrl(sharedObjectiveCards.get(0),Side.FRONT)));
            sharedRight.setStyle(getStyle(getCardUrl(sharedObjectiveCards.get(1),Side.FRONT)));

            secret.setStyle(getStyle(getCardUrl(viewGUI.getSecretObjectiveCard(),Side.FRONT)));

        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
        objectiveCards.setDisable(false);
        objectiveCards.setVisible(true);
    }
}
