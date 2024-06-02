package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.exceptions.InvalidCardPositionException;
import it.polimi.ingsw.exceptions.NotExistingPlayerException;
import it.polimi.ingsw.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerHand;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.view.GUI.InspectedCardInfo;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;

import static it.polimi.ingsw.model.GameValues.*;

public class MainGameScreenController extends GUIController{

    private boolean inGame = false;
    private InspectedCardInfo inspectedCardInfo;
    private boolean dragging = false;
    private boolean playingCard = false;
    public Pane scalable;

    private PlayableCard cardJustPlayed = null;
    private Player playerUpdated = null;

    public Pane tableTop;

    public Pane resourcesPane;
    public Label fungi;
    public Label animal;
    public Label insect;
    public Label plant;
    public Label quill;
    public Label manuscript;
    public Label inkwell;

    public Label turnLabel;
    public Label requirementsLabel;
    public Label positionLabel;

    public TabPane rulebookTabPane;
    public Pane rulebookPane;
    private boolean isInRulebook = true;
    public Button nextButton;
    public Button backButton;

    private boolean frontSide = true;
    private int cardInHandSelected = 3;
    public Pane cardsInHandInspector;
    public Pane handCardInspector;

    private int sharedObjChoice = 2;
    public Pane objectiveCardsInspector;
    public Pane showObjCard;

    public Pane secretObjCardsInspector;
    public Pane showSObjCard;

    public Pane drawingCardsInspector;
    public Pane drawingCardPane;

    public Pane card1;
    public Pane card2;
    public Pane card3;
    public Pane playerHand;
    private ArrayList<Pane> playerHandPanes = new ArrayList<>();
    public Button playCard;

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

    public Pane drawingField;
    public Pane resourceDeck;
    public Pane goldDeck;
    public Pane topDiscoveredR;
    public Pane bottomDiscoveredR;
    public Pane topDiscoveredG;
    public Pane bottomDiscoveredG;
    public Button drawCard;

    public Pane playerField;
    public Pane starterCard;
    public Pane movingField;
    public Pane fieldBoundsCheck;
    private Rectangle fieldBounds = new Rectangle();
    private Pane[][] fieldPanes = new Pane[DEFAULT_MATRIX_SIZE][DEFAULT_MATRIX_SIZE];

    public Pane m1;
    public Pane m2;
    public Pane m3;
    public Pane m4;
    public ArrayList<Pane> scoreboardMarkerPanes = new ArrayList<>();
    public Pane popupPane;

    public Pane playersMarkerPane;
    public Pane p0;
    public Pane p1;
    public Pane p2;
    public Pane p3;
    public ArrayList<Pane> otherMarkers;
    public Pane firstPlayer;
    public Pane notFirstPlayer;
    public Pane blackMarker;
    public Pane mBackground;

    public Label usernameOne;
    public Label usernameTwo;
    public Label usernameThree;
    public ArrayList<Label> otherUsername;
    public Pane playerHandBackground;
    public Pane drawingFieldBackground;

    public Label twentyPoints;
    public Label finalRound;

    private void markerPaneInitializer() {

        playersMarkerPane.setDisable(false);
        playersMarkerPane.setVisible(true);

        otherMarkers = new ArrayList<>();
        otherMarkers.add(p1);
        otherMarkers.add(p2);
        otherMarkers.add(p3);

        otherUsername = new ArrayList<>();
        otherUsername.add(usernameOne);
        otherUsername.add(usernameTwo);
        otherUsername.add(usernameThree);

        try {
            Player player = viewGUI.getGame().getPlayer(viewGUI.getUsername());
            p0.setStyle(getStyle(player.getMarker().getPath()));
            if(player.getIsFirst()) {
                firstPlayer.setDisable(false);
                firstPlayer.setVisible(true);
                notFirstPlayer.setDisable(true);
                notFirstPlayer.setVisible(false);
                blackMarker.setStyle(getStyle(Marker.BLACK.getPath()));
            } else {
                notFirstPlayer.setDisable(false);
                notFirstPlayer.setVisible(true);
                firstPlayer.setDisable(true);
                firstPlayer.setVisible(false);
                blackMarker.setDisable(true);
                blackMarker.setVisible(false);
            }
            double width = 3.75;
            int markerPosition = 0;
            for(int i = 0; i < viewGUI.getGame().getListOfPlayers().size(); i++) {
                if(!viewGUI.getGame().getListOfPlayers().get(i).getUsername().equals(player.getUsername())) {
                    otherMarkers.get(markerPosition).setStyle(getStyle(viewGUI.getGame().getListOfPlayers().get(i).getMarker().getPath()));
                    otherMarkers.get(markerPosition).setDisable(false);
                    otherMarkers.get(markerPosition).setVisible(true);
                    otherUsername.get(markerPosition).setText(viewGUI.getGame().getListOfPlayers().get(i).getUsername());
                    otherUsername.get(markerPosition).setStyle("-fx-text-alignment: center; -fx-background-color: rgba(0,0,0,0.4); -fx-background-radius: 20;");
                    otherUsername.get(markerPosition).setAlignment(Pos.CENTER);
                    width = width + 53.75;
                    markerPosition++;
                }
            }
            if(markerPosition == 3) {
                width = width + 3.75;
            }
            mBackground.setPrefWidth(width);

        } catch (NotExistingPlayerException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void showUsername(MouseEvent event) {
        Pane pane = (Pane)event.getSource();
        switch (pane.getId()) {
            case "p1":
                usernameOne.setDisable(false);
                usernameOne.setVisible(true);
                break;
            case "p2":
                usernameTwo.setDisable(false);
                usernameTwo.setVisible(true);
                break;
            case "p3":
                usernameThree.setDisable(false);
                usernameThree.setVisible(true);
                break;
        }
    }
    @FXML
    public void hydeUsername(MouseEvent event) {
        Pane pane = (Pane)event.getSource();
        switch (pane.getId()) {
            case "p1":
                usernameOne.setDisable(true);
                usernameOne.setVisible(false);
                break;
            case "p2":
                usernameTwo.setDisable(true);
                usernameTwo.setVisible(false);
                break;
            case "p3":
                usernameThree.setDisable(true);
                usernameThree.setVisible(false);
                break;
        }
    }

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

    public void sceneInitializer() {

        inspectedCardInfo = new InspectedCardInfo();
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
       initializeDrawingField();
       initializePlayerField();
       markerPaneInitializer();
       resourceInitializer();
       setTurn();
       inGame = true;
    }

    private void resourceInitializer()
    {
        try {
            resourcesPane.setVisible(true);
            Player player;
            if(playerUpdated!=null)
            {
                player = playerUpdated;
            }
            else {
                player = viewGUI.getGame().getPlayer(viewGUI.getUsername());
            }
            for (Resource resource : Resource.values()) {
                if (resource.equals(Resource.NONE))
                    continue;

                Label resourceLabel = null;
                switch(resource){
                    case FUNGI -> {

                        resourceLabel = fungi;
                    }
                    case ANIMAL -> {
                        resourceLabel = animal;
                    }
                    case PLANT -> {
                        resourceLabel = plant;
                    }
                    case INSECT -> {
                        resourceLabel = insect;
                    }
                    case QUILL -> {
                        resourceLabel = quill;
                    }
                    case INKWELL -> {
                        resourceLabel = inkwell;
                    }
                    case MANUSCRIPT -> {
                        resourceLabel = manuscript;

                    }
                    default -> throw new RuntimeException();
                }
                resourceLabel.setText(Integer.toString(player.getResourceAmount(resource)));
            }
        }
        catch (Exception e)
        {

            throw new RuntimeException();
        }
    }

    private void setTurn()
    {
        try {
            if (viewGUI.getIsTurn()) {
                turnLabel.setText("It is currently your turn, select a card from your hand to Play");
                turnLabel.setStyle("-fx-text-alignment: center; -fx-background-color: rgba(0,0,0,0.4); -fx-background-radius: 20;");
                setBorderPane(playerHandBackground, false);

                playCard.setDisable(false);
                playCard.setVisible(true);

            }
            else {

                turnLabel.setText("It is currently " + viewGUI.getGame().getCurrentPlayer().getUsername() + "'s turn");
                turnLabel.setStyle("-fx-text-alignment: center; -fx-background-color: rgba(0,0,0,0.4); -fx-background-radius: 20;");

                playCard.setDisable(true);
                playCard.setVisible(false);

                drawCard.setDisable(true);
                drawCard.setVisible(false);
            }
            turnLabel.setDisable(false);
            turnLabel.setVisible(true);
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
    }

    private void initializePlayerHand()
    {
        Game game = viewGUI.getGame();
        playerHandPanes.add(card1);
        playerHandPanes.add(card2);
        playerHandPanes.add(card3);

        card1.setDisable(false);
        card1.setVisible(true);

        card2.setDisable(false);
        card2.setVisible(true);

        card3.setDisable(false);
        card3.setVisible(true);
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
    @FXML
    public void showOtherCardInHandSide() {
        if(inspectedCardInfo.getFrontSide()) {
            inspectedCardInfo.setSide(Side.BACK);
            showCardInHand();
            inspectedCardInfo.setFrontSide(false);
        } else {
            inspectedCardInfo.setSide(Side.FRONT);
            showCardInHand();
            inspectedCardInfo.setFrontSide(true);
        }
    }
    @FXML
    public void card1HandInspectorButton() {
        inspectedCardInfo.setCardInHandSelected(0);
        inspectedCardInfo.setSide(Side.FRONT);
        //cardInHandSelected = 0;
        showCardInHand();
    }
    @FXML
    public void card2HandInspectorButton() {
        inspectedCardInfo.setCardInHandSelected(1);
        inspectedCardInfo.setSide(Side.FRONT);
        //cardInHandSelected = 1;
        showCardInHand();
    }
    @FXML
    public void card3HandInspectorButton() {
        inspectedCardInfo.setCardInHandSelected(2);
        inspectedCardInfo.setSide(Side.FRONT);
        //cardInHandSelected = 2;
        showCardInHand();
    }
    private void showCardInHand() {
        Game game = viewGUI.getGame();

        cardsInHandInspector.setDisable(false);
        cardsInHandInspector.setVisible(true);
        playerField.setDisable(true);
        playerField.setVisible(false);

        try {
            handCardInspector.setStyle(getStyle(getCardUrl(game.getPlayer(viewGUI.getUsername()).getPlayerHand().getCardsInHand().get(inspectedCardInfo.getCardInHandSelected()), inspectedCardInfo.getSide())));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
    @FXML
    public void playCardButton() {
        cardsInHandInspector.setDisable(true);
        cardsInHandInspector.setVisible(false);
        playerField.setDisable(false);
        playerField.setVisible(true);
        playingCard = true;

        setAdditionalPanesVisibility(true);


    }

    private void setAdditionalPanesVisibility(boolean visible)
    {
        try {
            for (int i = 0; i < DEFAULT_MATRIX_SIZE; i++) {
                for (int j = 0; j < DEFAULT_MATRIX_SIZE; j++) {
                    if (fieldPanes[i][j] != null && fieldPanes[i][j].getId().equals("empty")) {
                        fieldPanes[i][j].setDisable(!visible);
                        fieldPanes[i][j].setVisible(visible);
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
    }

    @FXML
    public void exitHandButton() {
        cardsInHandInspector.setDisable(true);
        cardsInHandInspector.setVisible(false);
        playerField.setDisable(false);
        playerField.setVisible(true);
        inspectedCardInfo.setCardInHandSelected(3);
        inspectedCardInfo.setFrontSide(true);
        //inspectedCardInfo reset to default values
    }
    //SHOW SHARED OBJECTIVE CARDS
    @FXML
    public void showOtherSharedObjCardSide() {
        if(inspectedCardInfo.getFrontSide()) {
            inspectedCardInfo.setSide(Side.BACK);
            showSharedObjCard();
            inspectedCardInfo.setFrontSide(false);
            //frontSide = false;
        } else {
            inspectedCardInfo.setSide(Side.FRONT);
            showSharedObjCard();
            inspectedCardInfo.setFrontSide(true);
        }
    }
    @FXML
    public void sharedObjCardButtonOne() {
        inspectedCardInfo.setSharedObjChoice(0);
        inspectedCardInfo.setSide(Side.FRONT);
        //sharedObjChoice = 0;
        showSharedObjCard();
    }
    @FXML
    public void sharedObjCardButtonTwo() {
        inspectedCardInfo.setSharedObjChoice(1);
        inspectedCardInfo.setSide(Side.FRONT);
        showSharedObjCard();
    }
    private void showSharedObjCard() {
        Game game = viewGUI.getGame();

        objectiveCardsInspector.setDisable(false);
        objectiveCardsInspector.setVisible(true);
        playerField.setDisable(true);
        playerField.setVisible(false);

        try {
            ArrayList<ObjectiveCard> sharedObjectiveCards = viewGUI.getSharedObjectiveCards();
            showObjCard.setStyle(getStyle(getCardUrl(sharedObjectiveCards.get(inspectedCardInfo.getSharedObjChoice()), inspectedCardInfo.getSide())));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
    @FXML
    public void exitObjectiveButton() {
        objectiveCardsInspector.setDisable(true);
        objectiveCardsInspector.setVisible(false);
        playerField.setDisable(false);
        playerField.setVisible(true);
        inspectedCardInfo.setSharedObjChoice(2);
        inspectedCardInfo.setFrontSide(true);
    }
    //SHOW SECRET OBJECTIVE CARD
    @FXML
    public void showOtherSecretObjCardSide() {
        if(inspectedCardInfo.getFrontSide()) {
            inspectedCardInfo.setSide(Side.BACK);
            showSecretObjCard();
            inspectedCardInfo.setFrontSide(false);
        } else {
            inspectedCardInfo.setSide(Side.FRONT);
            showSecretObjCard();
            inspectedCardInfo.setFrontSide(true);
        }
    }
    @FXML
    public void secretObjCardButton() {
        showSecretObjCard();
    }
    private void showSecretObjCard() {
        secretObjCardsInspector.setDisable(false);
        secretObjCardsInspector.setVisible(true);
        playerField.setDisable(true);
        playerField.setVisible(false);

        try {
            showSObjCard.setStyle(getStyle(getCardUrl(viewGUI.getSecretObjectiveCard(), inspectedCardInfo.getSide())));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
    @FXML
    public void exitObjectiveSecretButton() {
        secretObjCardsInspector.setDisable(true);
        secretObjCardsInspector.setVisible(false);
        playerField.setDisable(false);
        playerField.setVisible(true);
        inspectedCardInfo.setFrontSide(true);
    }
    //SHOW DRAWING FIELD CARD
    @FXML
    public void showOtherDrawingCardSide() {
        if(!inspectedCardInfo.getFromDeck()) {
            if(inspectedCardInfo.getFrontSide()) {
                inspectedCardInfo.setSide(Side.BACK);
                showDrawingFieldCard();
                inspectedCardInfo.setFrontSide(false);
            } else {
                inspectedCardInfo.setSide(Side.FRONT);
                showDrawingFieldCard();
                inspectedCardInfo.setFrontSide(true);
            }
        }
    }
    @FXML
    public void leftDeckButton() {
        inspectedCardInfo.setSide(Side.BACK);
        inspectedCardInfo.setCardType(CardType.RESOURCE);
        inspectedCardInfo.setFromDeck(true);
        inspectedCardInfo.setDrawPosition(DrawPosition.FROMDECK);
        showDrawingFieldCard();
    }
    @FXML
    public void rightDeckButton() {
        inspectedCardInfo.setSide(Side.BACK);
        inspectedCardInfo.setCardType(CardType.GOLD);
        inspectedCardInfo.setFromDeck(true);
        inspectedCardInfo.setDrawPosition(DrawPosition.FROMDECK);
        showDrawingFieldCard();
    }
    @FXML
    public void rightTopDrawingButton() {
        inspectedCardInfo.setSide(Side.FRONT);
        inspectedCardInfo.setCardType(CardType.GOLD);
        inspectedCardInfo.setFromDeck(false);
        inspectedCardInfo.setDrawPosition(DrawPosition.LEFT);
        showDrawingFieldCard();
    }
    @FXML
    public void leftTopDrawingButton() {
        inspectedCardInfo.setSide(Side.FRONT);
        inspectedCardInfo.setCardType(CardType.RESOURCE);
        inspectedCardInfo.setFromDeck(false);
        inspectedCardInfo.setDrawPosition(DrawPosition.LEFT);
        showDrawingFieldCard();
    }
    @FXML
    public void rightBottomDrawingButton() {
        inspectedCardInfo.setSide(Side.FRONT);
        inspectedCardInfo.setCardType(CardType.GOLD);
        inspectedCardInfo.setFromDeck(false);
        inspectedCardInfo.setDrawPosition(DrawPosition.RIGHT);
        showDrawingFieldCard();
    }
    @FXML
    public void leftBottomDrawingButton() {
        inspectedCardInfo.setSide(Side.FRONT);
        inspectedCardInfo.setCardType(CardType.RESOURCE);
        inspectedCardInfo.setFromDeck(false);
        inspectedCardInfo.setDrawPosition(DrawPosition.RIGHT);
        showDrawingFieldCard();
    }

    private void showDrawingFieldCard() {
        drawingCardsInspector.setDisable(false);
        drawingCardsInspector.setVisible(true);
        playerField.setDisable(true);
        playerField.setVisible(false);

        try {
            if(inspectedCardInfo.getFromDeck()) {
                if(inspectedCardInfo.getCardType().equals(CardType.GOLD)) {
                    drawingCardPane.setStyle(getStyle(getCardUrl(viewGUI.getTopGoldCard(),Side.BACK)));
                } else if(inspectedCardInfo.getCardType().equals(CardType.RESOURCE)) {
                    drawingCardPane.setStyle(getStyle(getCardUrl(viewGUI.getTopResourceCard(),Side.BACK)));
                }
            } else {
                if(inspectedCardInfo.getCardType().equals(CardType.GOLD)) {
                    HashMap<DrawPosition,GoldCard> discoveredGoldCards = viewGUI.getDiscoveredGoldCards();
                    if(inspectedCardInfo.getDrawPosition().equals(DrawPosition.RIGHT)) {
                       drawingCardPane.setStyle(getStyle(getCardUrl(discoveredGoldCards.get(DrawPosition.RIGHT), inspectedCardInfo.getSide())));
                    } else {
                        drawingCardPane.setStyle(getStyle(getCardUrl(discoveredGoldCards.get(DrawPosition.LEFT),inspectedCardInfo.getSide())));
                    }
                } else if(inspectedCardInfo.getCardType().equals(CardType.RESOURCE)) {
                    HashMap<DrawPosition,ResourceCard> discoveredResourceCards = viewGUI.getDiscoveredResourceCards();
                    if(inspectedCardInfo.getDrawPosition().equals(DrawPosition.RIGHT)) {
                        drawingCardPane.setStyle(getStyle(getCardUrl(discoveredResourceCards.get(DrawPosition.RIGHT),inspectedCardInfo.getSide())));
                    } else {
                        drawingCardPane.setStyle(getStyle(getCardUrl(discoveredResourceCards.get(DrawPosition.LEFT),inspectedCardInfo.getSide())));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
    @FXML
    public void exitDrawingFieldButton() {
        drawingCardsInspector.setDisable(true);
        drawingCardsInspector.setVisible(false);
        playerField.setDisable(false);
        playerField.setVisible(true);
        frontSide = true;
    }

    private void initializeScoreboard()
    {
        m1 = new Pane();
        m2 = new Pane();
        m3 = new Pane();
        m4 = new Pane();

        scoreboardMarkerPanes.add(m1);
        scoreboardMarkerPanes.add(m2);
        scoreboardMarkerPanes.add(m3);
        scoreboardMarkerPanes.add(m4);

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
        for(Pane pane: scoreboardMarkerPanes) {
            pane.setPrefHeight(30);
            pane.setPrefWidth(30);
        }
        scoreBoard.setDisable(false);
        scoreBoard.setVisible(true);
        markerPositionInScoreboard();
    }

    private void setScoreboardChildren(Pane pane) {
        double transformation = 0;
        for(Node marker: pane.getChildren()) {
            Pane markerPane = (Pane) marker;
            markerPane.setLayoutY(transformation);
            //pane.getChildren().add(markerPane);
            transformation = transformation - 2.5;
        }
    }

    private void markerPositionInScoreboard() {
        Game game = viewGUI.getGame();
        resetScoreboard();

        for(int i = 0; i < game.getListOfPlayers().size(); i++) {
            Player player;
            if(game.getListOfPlayers().get(i).getUsername().equals(viewGUI.getUsername()) && playerUpdated != null)
            {
                player = playerUpdated;
            }
            else
            {
                player = game.getListOfPlayers().get(i);
            }
            scoreboardMarkerPanes.get(i).setStyle(getStyle(player.getMarker().getPath()));
            platPanes.get(player.getPoints()).getChildren().add(scoreboardMarkerPanes.get(i));

            setScoreboardChildren(platPanes.get(player.getPoints()));

            scoreboardMarkerPanes.get(i).setDisable(false);
            scoreboardMarkerPanes.get(i).setVisible(true);
            platPanes.get(player.getPoints()).setDisable(false);
            platPanes.get(player.getPoints()).setVisible(true);
        }
    }

    private void resetScoreboard() {
        for(Pane pane: platPanes) {
            pane.setDisable(true);
            pane.setVisible(false);
            pane.getChildren().clear();
        }
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

    private void initializeDrawingField()
    {

        try
        {
            resourceDeck.setStyle(getStyle(getCardUrl(viewGUI.getTopResourceCard(),Side.BACK)));
            goldDeck.setStyle(getStyle(getCardUrl(viewGUI.getTopGoldCard(),Side.BACK)));

            HashMap<DrawPosition,ResourceCard> discoveredResourceCards = viewGUI.getDiscoveredResourceCards();
            HashMap<DrawPosition,GoldCard> discoveredGoldCards = viewGUI.getDiscoveredGoldCards();

            topDiscoveredR.setStyle(getStyle(getCardUrl(discoveredResourceCards.get(DrawPosition.LEFT),Side.FRONT)));
            bottomDiscoveredR.setStyle(getStyle(getCardUrl(discoveredResourceCards.get(DrawPosition.RIGHT),Side.FRONT)));

            topDiscoveredG.setStyle(getStyle(getCardUrl(discoveredGoldCards.get(DrawPosition.LEFT),Side.FRONT)));
            bottomDiscoveredG.setStyle(getStyle(getCardUrl(discoveredGoldCards.get(DrawPosition.RIGHT),Side.FRONT)));

        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
        drawingField.setDisable(false);
        drawingField.setVisible(true);
    }
    private double startDragX;
    private double startDragY;
    public void initializePlayerField()
    {
        Rectangle fieldCut = new Rectangle();
        fieldCut.setLayoutX(288.0f); //298.0f
        fieldCut.setLayoutY(60.0f); //70.0f
        fieldCut.setWidth(350.0f);
        fieldCut.setHeight(350.0f);
        fieldCut.setStyle("-fx-border-radius: 20px"); //TODO: rounded border doesn't works
        movingField.setOnMousePressed(e -> {

            startDragX = e.getSceneX() - movingField.getTranslateX();
            startDragY = e.getSceneY() - movingField.getTranslateY();
        });



        scalable.getChildren().add(fieldBounds);
        fieldBounds.setVisible(false);
        fieldBounds.setMouseTransparent(true);


        movingField.setOnMouseDragged(e -> {
            double translateX = e.getSceneX() - startDragX;
            double translateY = e.getSceneY() - startDragY;


            fieldBoundsCheck.setTranslateX(translateX);
            if(fieldBoundsCheck.getBoundsInParent().getMaxX() > fieldBounds.getBoundsInParent().getMaxX() && fieldBoundsCheck.getBoundsInParent().getMinX() < fieldBounds.getBoundsInParent().getMinX())
            {
                movingField.setTranslateX(translateX);
            }
            else
            {
                fieldBoundsCheck.setTranslateX(-translateX);
            }
            fieldBoundsCheck.setTranslateY(translateY);
            if(fieldBoundsCheck.getBoundsInParent().getMaxY() > fieldBounds.getBoundsInParent().getMaxY() && fieldBoundsCheck.getBoundsInParent().getMinY() < fieldBounds.getBoundsInParent().getMinY())
            {
                movingField.setTranslateY(translateY);
            }
            else
            {
                fieldBoundsCheck.setTranslateY(-translateY);
            }

        });

        movingField.setOnScroll(e -> {
            double scaleFactor = e.getDeltaY() > 0 ? 1.1 : 1/1.1;
            if((movingField.getScaleX() <= 2 && e.getDeltaY() > 0) || (movingField.getScaleX()>= 0.75  && e.getDeltaY() < 0))
            {
                movingField.setScaleX(movingField.getScaleX() * scaleFactor);
                movingField.setScaleY(movingField.getScaleY() * scaleFactor);

                fieldBoundsCheck.setScaleX(fieldBoundsCheck.getScaleX() * scaleFactor);
                fieldBoundsCheck.setScaleY(fieldBoundsCheck.getScaleY() * scaleFactor);

                fieldBounds.setScaleX(fieldBounds.getScaleX() * scaleFactor);
                fieldBounds.setScaleY(fieldBounds.getScaleY() * scaleFactor);

            }
        });


        playerField.setClip(fieldCut);
        playerField.setStyle(getStyle("images/Backgrounds/yellow.jpg"));
        playerField.setDisable(false);
        playerField.setVisible(true);
        //scalable.setStyle(getStyle("images/Backgounds/woodCut.png"));

        try {
            PlayableCard card = viewGUI.getPlayerField().getMatrixField()[DEFAULT_MATRIX_SIZE/2][DEFAULT_MATRIX_SIZE/2];
            starterCard.setStyle(getStyle(getCardUrl(card ,card.getCurrentSide())));
            fieldPanes[DEFAULT_MATRIX_SIZE/2][DEFAULT_MATRIX_SIZE/2] = starterCard;
            sorround(starterCard);
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }

    }

    public void sorround(Pane pane)
    {
        //Check if the angle is playable and not covered
        pane.setId("full");
        Pane topRight = new Pane();
        Pane bottomRight = new Pane();
        Pane topLeft = new Pane();
        Pane bottomLeft = new Pane();
        setupCardPane(pane, bottomRight, AngleOrientation.BOTTOMRIGHT);
        setupCardPane(pane, topLeft, AngleOrientation.TOPLEFT);
        setupCardPane(pane, topRight, AngleOrientation.TOPRIGHT);
        setupCardPane(pane, bottomLeft, AngleOrientation.BOTTOMLEFT);
        updateBounds();

    }
    public void setupCardPane(Pane pane, Pane newPane, AngleOrientation orientation) {
        double oldX = pane.getLayoutX();
        double oldY = pane.getLayoutY();

        double newX = 0;
        double newY = 0;
        switch (orientation) {
            case TOPRIGHT,
                 BOTTOMRIGHT -> { // 20 and 24 are the width and height of the angle: 90 - 20 = 70, 60 - 24 = 36
                newX = oldX + (CARD_WIDTH - ANGLE_WIDTH);
            }
            case TOPLEFT, BOTTOMLEFT -> {
                newX = oldX - (CARD_WIDTH - ANGLE_WIDTH);
            }
        }
        switch (orientation) {
            case TOPRIGHT, TOPLEFT -> {
                newY = oldY - (CARD_HEIGHT - ANGLE_HEIGHT);
            }
            case BOTTOMRIGHT, BOTTOMLEFT -> {
                newY = oldY + (CARD_HEIGHT - ANGLE_HEIGHT);
            }
        }
        try {
            for (int i = 0; i < DEFAULT_MATRIX_SIZE; i++) {
                for (int j = 0; j < DEFAULT_MATRIX_SIZE; j++) {
                    if (fieldPanes[i][j] != null && fieldPanes[i][j].equals(pane)) {
                        int xPane = i + orientation.mapEnumToX();
                        int yPane = j + orientation.mapEnumToY();

                        PlayableCard[][] matrixField = viewGUI.getPlayerField().getMatrixField();

                        if (matrixField[xPane][yPane] != null) {
                            return;
                        }
                        //delete old Pane
                        Pane oldPane = fieldPanes[xPane][yPane];
                        if (oldPane != null) {
                            oldPane.setDisable(true);
                            oldPane.setVisible(false);
                            movingField.getChildren().remove(oldPane);
                        }

                        //checks if the position where i want to place the new pane is a valid playable position
                        if (cardJustPlayed != null && !cardJustPlayed.getAngle(orientation).isPlayable())
                            return;

                        for (AngleOrientation angleOrientation : AngleOrientation.values()) {
                            if (angleOrientation.equals(AngleOrientation.NONE))
                                continue;

                            int checkX = xPane + angleOrientation.mapEnumToX();
                            int checkY = yPane + angleOrientation.mapEnumToY();
                            if (matrixField[checkX][checkY] != null && !matrixField[checkX][checkY].getAngle(angleOrientation.getOpposite()).isPlayable()) {
                                return;
                            }
                        }
                        newPane.setId("empty");
                        fieldPanes[xPane][yPane] = newPane;

                        movingField.getChildren().add(newPane);



                        newPane.setLayoutX(newX);
                        newPane.setLayoutY(newY);
                        newPane.setPrefWidth(CARD_WIDTH);
                        newPane.setPrefHeight(CARD_HEIGHT);

                        newPane.setVisible(false);
                        newPane.setDisable(true);
                        newPane.setOpacity(0.2);
                        newPane.setStyle("-fx-background-color: white");

                        newPane.setOnMouseDragged(e ->
                        {
                            dragging = true;
                        });

                        newPane.setOnMouseClicked(e -> {
                            clickedOnFieldPane(e);
                        });



                        return;

                    }
                }
            }
            throw new RuntimeException();
        } catch (Exception e) {
            throw new RuntimeException();
        }

    }
    private HashMap<Direction, Integer> findExternals()
    {
        int firstX = DEFAULT_MATRIX_SIZE;
        int lastX = -1;
        int firstY = DEFAULT_MATRIX_SIZE;
        int lastY = -1;
        HashMap<Direction, Integer> externals = new HashMap<>();
        for(int i = 0; i < DEFAULT_MATRIX_SIZE; i++)
        {
            for (int j = 0; j < DEFAULT_MATRIX_SIZE; j++) {

                if(fieldPanes[i][j] != null)
                {
                    if(i < firstX)
                        firstX = i;
                    if(i > lastX)
                        lastX = i;
                    if(j < firstY)
                        firstY = j;
                    if(j > lastY)
                       lastY = j;
                }
            }
        }
        externals.put(Direction.TOP, DEFAULT_MATRIX_SIZE/2 - firstX);
        externals.put(Direction.BOTTOM, lastX - DEFAULT_MATRIX_SIZE/2 );
        externals.put(Direction.LEFT, DEFAULT_MATRIX_SIZE/2 - firstY);
        externals.put(Direction.RIGHT, lastY - DEFAULT_MATRIX_SIZE/2);

        return externals;

    }

    private void updateBounds()
    {
        try
        {
            HashMap<Direction, Integer> externals = findExternals();
            fieldBounds.setLayoutX(movingField.getLayoutX() + (externals.get(Direction.LEFT) + 1)*(CARD_WIDTH - ANGLE_WIDTH));
            fieldBounds.setLayoutY(movingField.getLayoutY() + (externals.get(Direction.TOP) + 1)* (CARD_HEIGHT - ANGLE_HEIGHT));
            fieldBounds.setWidth(movingField.getWidth() - (CARD_WIDTH - ANGLE_WIDTH) * (2 + externals.get(Direction.LEFT) + externals.get(Direction.RIGHT)));
            fieldBounds.setHeight(movingField.getHeight()- (CARD_HEIGHT - ANGLE_HEIGHT) * (2 + externals.get(Direction.TOP) + externals.get(Direction.BOTTOM)));

        }catch (Exception e)
        {
            throw new RuntimeException();
        }
    }

    @FXML
    public void clickedOnFieldPane(MouseEvent event) {
        if(event.getEventType() == MouseEvent.MOUSE_CLICKED) {
            if(dragging)
            {
                dragging = false;
                return;
            }
            if (playingCard) {
                try {
                    if (viewGUI.getIsTurn()) {
                        Pane source = (Pane) event.getSource();
                        if(source.getId().equals("full"))
                        {
                            return;
                        }
                        PlayableCard card = viewGUI.getGame().getPlayer(viewGUI.getUsername()).getPlayerHand().getCardsInHand().get(inspectedCardInfo.getCardInHandSelected());
                        if(inspectedCardInfo.getSide().equals(Side.BACK))
                        {
                            card = viewGUI.getOtherSideCard(card);
                        }

                        for (int i = 0; i < DEFAULT_MATRIX_SIZE; i++) {
                            for (int j = 0; j < DEFAULT_MATRIX_SIZE; j++) {
                                if (fieldPanes[i][j] != null && fieldPanes[i][j].equals(source)) {
                                    PlayableCard[][] matrixField = viewGUI.getGame().getPlayer(viewGUI.getUsername()).getPlayerField().getMatrixField();

                                    for (AngleOrientation orientation : AngleOrientation.values()) {
                                        if (!orientation.equals(AngleOrientation.NONE)) {
                                            int xPane = i + orientation.mapEnumToX();
                                            int yPane = j + orientation.mapEnumToY();

                                            if (matrixField[xPane][yPane] != null) {
                                                playerUpdated = viewGUI.playCard(matrixField[xPane][yPane], card, orientation.getOpposite());
                                                cardJustPlayed = card;
                                                markerPositionInScoreboard();
                                                resourceInitializer();
                                                source.setStyle(getStyle(getCardUrl(card, inspectedCardInfo.getSide())));
                                                source.setVisible(true);
                                                source.setDisable(false);
                                                source.setOpacity(1);
                                                sorround(source);


                                                removeCardFromHand();

                                                positionLabel.setDisable(true);
                                                positionLabel.setVisible(false);

                                                requirementsLabel.setDisable(true);
                                                requirementsLabel.setVisible(false);

                                                inspectedCardInfo.setFrontSide(true);
                                                inspectedCardInfo.setSide(Side.FRONT);

                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        playingCard = false;

                        playCard.setDisable(true);
                        playCard.setVisible(false);

                        drawCard.setDisable(false);
                        drawCard.setVisible(true);

                        setAdditionalPanesVisibility(false);

                        turnLabel.setText("Draw a card to end your turn");
                        turnLabel.setStyle("-fx-text-alignment: center; -fx-background-color: rgba(0,0,0,0.4); -fx-background-radius: 20;");
                        setBorderPane(drawingFieldBackground, false);
                        setBorderPane(playerHandBackground, true);
                    }
                }catch (InvalidCardPositionException e)
                {
                    popupPane.setDisable(false);
                    popupPane.setVisible(true);
                    positionLabel.setDisable(false);
                    positionLabel.setVisible(true);
                    requirementsLabel.setDisable(true);
                    requirementsLabel.setVisible(false);
                    inspectedCardInfo.setFrontSide(true);
                    inspectedCardInfo.setSide(Side.FRONT);
                }
                catch (RequirementsNotMetException e)
                {
                    popupPane.setDisable(false);
                    popupPane.setVisible(true);
                    requirementsLabel.setDisable(false);
                    requirementsLabel.setVisible(true);
                    positionLabel.setDisable(true);
                    positionLabel.setVisible(false);
                    inspectedCardInfo.setFrontSide(true);
                    inspectedCardInfo.setSide(Side.FRONT);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException();
                }

            }
        }
    }

    private void setBorderPane(Pane pane, boolean toRemove) {
        if(!toRemove) {
            if(pane.getId().equals("playerHandBackground")) {
                pane.setStyle("-fx-border-color: red; -fx-border-width: 3px; -fx-border-radius: 9; -fx-background-color: black; -fx-background-radius: 10;");
            } else if(pane.getId().equals("drawingFieldBackground")) {
                pane.setStyle("-fx-border-color: red; -fx-border-width: 3px; -fx-border-radius: 19; -fx-background-color: black; -fx-background-radius: 20;");
            }
        } else {
            if(pane.getId().equals("playerHandBackground")) {
                pane.setStyle("-fx-border-color: none; -fx-background-color: black; -fx-background-radius: 10;");
            } else if(pane.getId().equals("drawingFieldBackground")) {
                pane.setStyle("-fx-border-color: none; -fx-background-color: black; -fx-background-radius: 20;");
            }
        }
    }

    @FXML
    public void exitPopupButton() {
        popupPane.setDisable(true);
        popupPane.setVisible(false);

        twentyPoints.setDisable(true);
        twentyPoints.setVisible(false);
        finalRound.setDisable(true);
        finalRound.setVisible(false);
        requirementsLabel.setDisable(true);
        requirementsLabel.setVisible(false);
        positionLabel.setDisable(true);
        positionLabel.setVisible(false);
    }


    private void removeCardFromHand() {
        playerHandPanes.get(inspectedCardInfo.getCardInHandSelected()).setDisable(true);
        playerHandPanes.get(inspectedCardInfo.getCardInHandSelected()).setVisible(false);
    }

    @FXML
    public void drawCardButton()
    {
        drawingCardsInspector.setDisable(true);
        drawingCardsInspector.setVisible(false);

        playerField.setDisable(false);
        playerField.setVisible(true);
        try {

            DrawPosition drawPosition = inspectedCardInfo.getDrawPosition();
            CardType cardType = inspectedCardInfo.getCardType();
            viewGUI.drawCard(cardType, drawPosition);
            viewGUI.endTurn();
        }
        catch (DeckIsEmptyException e)
        {
            //TODO: handle empty deck
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
    @Override
    public void twentyPoints(String username) {
        Platform.runLater(new Runnable() {
            public void run() {
                twentyPoints.setText("Player " + username + " has reached 20 points!");
                popupPane.setDisable(false);
                popupPane.setVisible(true);
                twentyPoints.setDisable(false);
                twentyPoints.setVisible(true);
            }
        });
    }

    @Override
    public void finalRound() {
        Platform.runLater(new Runnable() {
            public void run() {
                popupPane.setDisable(false);
                popupPane.setVisible(true);
                twentyPoints.setDisable(true);
                twentyPoints.setVisible(false);
                finalRound.setDisable(false);
                finalRound.setVisible(true);
            }
        });
    }



    public void update(Object update)
    {
        if(inGame) {
            Platform.runLater(new Runnable() {
                public void run() {
                    playerUpdated = null;
                    cardJustPlayed = null;
                    initializeDrawingField();
                    initializePlayerHand();
                    setBorderPane(playerHandBackground, true);
                    setBorderPane(drawingFieldBackground, true);
                    setTurn();
                    resourceInitializer();
                    markerPositionInScoreboard();
                    //Scoreboard

                }
            });
        }

    }

}
