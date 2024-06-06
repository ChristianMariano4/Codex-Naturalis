package it.polimi.ingsw.view.GUI.GUIControllers;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.exceptions.InvalidCardPositionException;
import it.polimi.ingsw.exceptions.NotExistingPlayerException;
import it.polimi.ingsw.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.view.GUI.InspectedCardInfo;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.HashMap;

import static it.polimi.ingsw.model.GameValues.*;

public class MainGameScreenController extends GUIController{

    private boolean inGame = false;
    private InspectedCardInfo inspectedCardInfo;
    private boolean dragging = false;
    private boolean playingCard = false;
    private boolean drawingCard = false;
    public Pane scalable;
    private boolean twentyPointsReached = false;

    public Pane chatPane;
    public Pane chatButtonPane;
    public ListView<String> chatList;
    public TextField chatField;
    ObservableList<String> chatMessages = FXCollections.observableArrayList();
    public Circle newMessage;

    private Thread waitThread;

    private PlayableCard cardJustPlayed = null;
    private Player playerUpdated = null;

    public Pane tableTop;
    public HBox turnLHbox;

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
    private ArrayList<Pane> otherPlayerFields = new ArrayList<>();
    private ArrayList<Pane> otherPlayerFieldBoundsChecks = new ArrayList<>();
    private ArrayList<Rectangle> otherPlayerFieldBounds = new ArrayList<>();

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

    public Label usernameZero;
    public Label usernameOne;
    public Label usernameTwo;
    public Label usernameThree;
    public ArrayList<Label> otherUsername;
    public Pane playerHandBackground;
    public Pane drawingFieldBackground;

    public Label twentyPoints;
    public Label finalRound;
    private boolean finalTurn = false;
    private boolean inMyField = true;
    public Pane exitPane;
    public Label gameIdLabel;
    public Pane borderFieldPane;
    public Pane chatBackground;

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
                else
                {
                    usernameZero.setText(viewGUI.getUsername());
                    usernameZero.setStyle("-fx-text-alignment: center; -fx-background-color: rgba(0,0,0,0.4); -fx-background-radius: 20;");
                    usernameZero.setAlignment(Pos.CENTER);
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
            case "p0":
                usernameZero.setDisable(false);
                usernameZero.setVisible(true);
                break;
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
    public void showOtherPlayerField(MouseEvent event) {
        Pane pane = (Pane) event.getSource();
        int index = 0;
        switch (pane.getId()) {
            case "p1":
                index = 0;
                break;
            case "p2":
                index = 1;
                break;
            case "p3":
                index = 2;
                break;
        }
        if(inMyField) {
            setOtherField(index);
        }
        else {
            boolean isDisable = false;
            if(otherPlayerFields.get(index).isDisable())
                isDisable = true;
            setMyField();
            if(isDisable)
                setOtherField(index);
        }
    }

    private void setOtherField(int index)
    {
        try {
            movingField.setDisable(true);
            movingField.setVisible(false);

            fieldBoundsCheck.setDisable(true);
            fieldBounds.setDisable(true);

            otherPlayerFields.get(index).setDisable(false);
            otherPlayerFields.get(index).setVisible(true);

            otherPlayerFieldBounds.get(index).setDisable(false);

            otherPlayerFieldBoundsChecks.get(index).setDisable(false);
            inMyField = false;
            initializePlayerHand(viewGUI.getGame().getPlayer(otherPlayerFields.get(index).getId()).getPlayerHand(), Side.BACK, false);
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }

    }
    @FXML
    public void setMyField()
    {
        try {
            for (int i = 0; i < otherPlayerFields.size(); i++) {
                otherPlayerFields.get(i).setVisible(false);
                otherPlayerFields.get(i).setDisable(true);

                otherPlayerFieldBounds.get(i).setDisable(true);

                otherPlayerFieldBoundsChecks.get(i).setDisable(true);

            }
            movingField.setDisable(false);
            movingField.setVisible(true);
            fieldBoundsCheck.setDisable(false);
            fieldBounds.setDisable(false);
            initializePlayerHand(viewGUI.getGame().getPlayer(viewGUI.getUsername()).getPlayerHand(), Side.FRONT, true);
            inMyField = true;
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
    }
    @FXML
    public void hideUsername(MouseEvent event) {
        Pane pane = (Pane)event.getSource();
        switch (pane.getId()) {
            case "p0":
                usernameZero.setDisable(true);
                usernameZero.setVisible(false);
                break;
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

        try {
            gameIdLabel.setText("GameId: " + viewGUI.getGame().getGameId());
            gameIdLabel.setStyle("-fx-text-alignment: center; -fx-background-color: rgba(0,0,0,0.4); -fx-background-radius: 20;");
            inspectedCardInfo = new InspectedCardInfo();
            if (viewGUI.getGame().getGameStatus().getStatusNumber() >= GameStatus.GAME_STARTED.getStatusNumber()) { //game already started
                tabletopSetup();
                rebuildMyField();

            } else if (viewGUI.getGame().getPlayer(viewGUI.getUsername()).getIsReconnecting()) //reconnection before game begins, we do not allow any choice
            {
                waitForGameBegin();
            }
            else { //joining normally
                markerPanes.add(marker1);
                markerPanes.add(marker2);
                markerPanes.add(marker3);
                markerPanes.add(marker4);
                preGame();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
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
                if(Thread.interrupted())
                    return;
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
            return;
        }
    };
    private void preGame()
    {
        waitThread = new Thread(markerThread);
        waitThread.start();
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
                if(Thread.interrupted())
                    return;
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
            return;
        }
    };

    public void waitForGameBegin()
    {
        markerSelection.setDisable(true);
        markerSelection.setVisible(false);
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
        try {
            markerSelection.setDisable(true);
            markerSelection.setVisible(false);
            initializePlayerHand(viewGUI.getGame().getPlayer(viewGUI.getUsername()).getPlayerHand(), Side.FRONT, true);
            initializeScoreboard();
            initializeObjectiveCards();
            initializeDrawingField();
            initializePlayerField();
            initializeOtherPlayerFields();
            markerPaneInitializer();
            resourceInitializer();
            setTurn();
            initializeChat();
            inGame = true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    public void initializeChat() {
        chatButtonPane.setDisable(false);
        chatButtonPane.setVisible(true);
        chatList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                final ListCell cell = new ListCell() {
                    private Text text;

                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty || item == null);
                        if (!isEmpty()) {
                            setPrefWidth(chatList.getPrefWidth() - 5);
                            setTextFill(Color.WHITE);
                            setWrapText(true);
                            setText(item.toString());

                        }
                    }

                };

                return cell;
            }
        });
        chatList.setItems(chatMessages);
        chatMessages.add("Write a public message or /[player's username] to send a private message");
        Node node = chatList.lookup(".scroll-bar:vertical");
        if (node instanceof ScrollBar) {
            scrollBar = (ScrollBar) node;
        }
        scrollBar.visibleAmountProperty().addListener(e ->
                chatList.scrollTo(chatMessages.getLast()));
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
                turnLabel.setText("It is currently your turn,\n select a card from your hand to Play");
                turnLHbox.setStyle("-fx-text-alignment: center; -fx-background-color: rgba(0,0,0,0.4); -fx-background-radius: 20;");
                //turnLHbox.setLayoutY(12);
                setBorderPane(playerHandBackground, false);

                playCard.setDisable(false);
                playCard.setVisible(true);

                setMyField();

            }
            else {

                turnLabel.setText("It is currently " + viewGUI.getGame().getCurrentPlayer().getUsername() + "'s turn");
                turnLHbox.setStyle("-fx-text-alignment: center; -fx-background-color: rgba(0,0,0,0.4); -fx-background-radius: 20;");
                //turnLHbox.setLayoutY(19);
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

    private void initializePlayerHand(PlayerHand hand, Side side, boolean enabled)
    {
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
            for(int i = 0; i< 3 ; i++)
            {
                try {
                    PlayableCard card = hand.getCardsInHand().get(i);
                    playerHandPanes.get(i).setStyle(getStyle(getCardUrl(card, side)));
                }
                catch (IndexOutOfBoundsException e)
                {
                    playerHandPanes.get(i).setDisable(true);
                    playerHandPanes.get(i).setVisible(false);

                }
            }
            playerHand.setDisable(!enabled);
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
        borderFieldPane.setDisable(true);
        borderFieldPane.setVisible(false);

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
        borderFieldPane.setDisable(false);
        borderFieldPane.setVisible(true);
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
        borderFieldPane.setDisable(false);
        borderFieldPane.setVisible(true);
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
        inspectedCardInfo.setFrontSide(true);
        //sharedObjChoice = 0;
        showSharedObjCard();
    }
    @FXML
    public void sharedObjCardButtonTwo() {
        inspectedCardInfo.setSharedObjChoice(1);
        inspectedCardInfo.setSide(Side.FRONT);
        inspectedCardInfo.setFrontSide(true);
        showSharedObjCard();
    }
    private void showSharedObjCard() {
        Game game = viewGUI.getGame();

        objectiveCardsInspector.setDisable(false);
        objectiveCardsInspector.setVisible(true);
        playerField.setDisable(true);
        playerField.setVisible(false);
        borderFieldPane.setDisable(true);
        borderFieldPane.setVisible(false);

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
        borderFieldPane.setDisable(false);
        borderFieldPane.setVisible(true);
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
        inspectedCardInfo.setSide(Side.FRONT);
        inspectedCardInfo.setFrontSide(true);
        showSecretObjCard();
    }
    private void showSecretObjCard() {
        secretObjCardsInspector.setDisable(false);
        secretObjCardsInspector.setVisible(true);
        playerField.setDisable(true);
        playerField.setVisible(false);
        borderFieldPane.setDisable(true);
        borderFieldPane.setVisible(false);

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
        borderFieldPane.setDisable(false);
        borderFieldPane.setVisible(true);
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
        borderFieldPane.setDisable(true);
        borderFieldPane.setVisible(false);

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
        borderFieldPane.setDisable(false);
        borderFieldPane.setVisible(true);
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
            int points = player.getPoints();
            if(points >= 29) //capping scoreboard to 29
                points = 29;
            scoreboardMarkerPanes.get(i).setStyle(getStyle(player.getMarker().getPath()));
            platPanes.get(points).getChildren().add(scoreboardMarkerPanes.get(i));

            setScoreboardChildren(platPanes.get(points));

            scoreboardMarkerPanes.get(i).setDisable(false);
            scoreboardMarkerPanes.get(i).setVisible(true);
            platPanes.get(points).setDisable(false);
            platPanes.get(points).setVisible(true);
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
        Pane currentPane;

        try
        {
            emptyDeckHandler(resourceDeck, Side.BACK);
            emptyDeckHandler(goldDeck, Side.BACK);

            HashMap<DrawPosition,ResourceCard> discoveredResourceCards = viewGUI.getDiscoveredResourceCards();
            HashMap<DrawPosition,GoldCard> discoveredGoldCards = viewGUI.getDiscoveredGoldCards();

            //Empty decks handler
            emptyDeckHandler(topDiscoveredR, Side.FRONT);
            emptyDeckHandler(bottomDiscoveredR, Side.FRONT);
            emptyDeckHandler(topDiscoveredG, Side.FRONT);
            emptyDeckHandler(bottomDiscoveredG, Side.FRONT);

        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
        drawingField.setDisable(false);
        drawingField.setVisible(true);
    }
    private void emptyDeckHandler(Pane pane, Side side) {
        try {
            switch (pane.getId()) {
                case "resourceDeck":
                    pane.setStyle(getStyle(getCardUrl(viewGUI.getTopResourceCard(),side)));
                    break;
                case "goldDeck":
                    pane.setStyle(getStyle(getCardUrl(viewGUI.getTopGoldCard(),side)));
                    break;
                case "topDiscoveredR":
                    pane.setStyle(getStyle(getCardUrl(viewGUI.getDiscoveredResourceCards().get(DrawPosition.LEFT),side)));
                    break;
                case "bottomDiscoveredR":
                    pane.setStyle(getStyle(getCardUrl(viewGUI.getDiscoveredResourceCards().get(DrawPosition.RIGHT),side)));
                    break;
                case "topDiscoveredG":
                    pane.setStyle(getStyle(getCardUrl(viewGUI.getDiscoveredGoldCards().get(DrawPosition.LEFT),side)));
                    break;
                case "bottomDiscoveredG":
                    pane.setStyle(getStyle(getCardUrl(viewGUI.getDiscoveredGoldCards().get(DrawPosition.RIGHT),side)));
                    break;
            }
        } catch (Exception e) {
            pane.setDisable(true);
            pane.setVisible(false);
        }
    }
    private double startDragX;
    private double startDragY;

    private void initializePlayerField()
    {
        borderFieldPane.setDisable(false);
        borderFieldPane.setVisible(true);

        Rectangle fieldCut = new Rectangle();
        fieldCut.setLayoutX(288.0f); //298.0f
        fieldCut.setLayoutY(65.0f);
        fieldCut.setWidth(350.0f);
        fieldCut.setHeight(350.0f);
        fieldCut.setStyle("-fx-border-radius: 20px");


        Pane field1 = new Pane();
        Pane boundsCheck1 = new Pane();

        Pane field2 = new Pane();
        Pane boundsCheck2 = new Pane();

        Pane field3 = new Pane();
        Pane boundsCheck3 = new Pane();

        setDefaultFieldConfiguration(field1, boundsCheck1);
        setDefaultFieldConfiguration(field2, boundsCheck2);
        setDefaultFieldConfiguration(field3, boundsCheck3);

        otherPlayerFields.add(field1);
        otherPlayerFields.add(field2);
        otherPlayerFields.add(field3);

        otherPlayerFieldBoundsChecks.add(boundsCheck1);
        otherPlayerFieldBoundsChecks.add(boundsCheck2);
        otherPlayerFieldBoundsChecks.add(boundsCheck3);

        otherPlayerFieldBounds.add(new Rectangle());
        otherPlayerFieldBounds.add(new Rectangle());
        otherPlayerFieldBounds.add(new Rectangle());

        for(Rectangle bounds : otherPlayerFieldBounds)
        {
            scalable.getChildren().add(bounds);
            bounds.setVisible(false);
            bounds.setDisable(true);
            bounds.setMouseTransparent(true);
        }


        scalable.getChildren().add(fieldBounds);
        fieldBounds.setVisible(false);


        setMouseActions(movingField, fieldBoundsCheck, fieldBounds);

        setMouseActions(field1, boundsCheck1, otherPlayerFieldBounds.get(0));
        setMouseActions(field2, boundsCheck2, otherPlayerFieldBounds.get(1));
        setMouseActions(field3, boundsCheck3, otherPlayerFieldBounds.get(2));


        playerField.setClip(fieldCut);
        playerField.setStyle(getStyle("images/Backgrounds/yellow.jpg"));
        playerField.setDisable(false);
        playerField.setVisible(true);
        borderFieldPane.setDisable(false);
        borderFieldPane.setVisible(true);
        //scalable.setStyle(getStyle("images/Backgounds/woodCut.png"));

        try {
            PlayableCard card = viewGUI.getPlayerField().getMatrixField()[DEFAULT_MATRIX_SIZE/2][DEFAULT_MATRIX_SIZE/2];
            starterCard.setStyle(getStyle(getCardUrl(card ,card.getCurrentSide())));
            fieldPanes[DEFAULT_MATRIX_SIZE/2][DEFAULT_MATRIX_SIZE/2] = starterCard;
            sorround(starterCard);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    private void setMouseActions(Pane field, Pane boundsCheck, Rectangle bounds)
    {
        field.setOnMousePressed(e -> {


            startDragX =  e.getSceneX()/(primaryStage.getWidth() / WINDOW_WIDTH ) - field.getTranslateX();
            startDragY = e.getSceneY()/(primaryStage.getHeight() / WINDOW_HEIGHT ) - field.getTranslateY();
        });

        field.setOnMouseDragged(e -> {
            double translateX = e.getSceneX()/(primaryStage.getWidth() / WINDOW_WIDTH ) - startDragX;
            double translateY = e.getSceneY()/(primaryStage.getHeight() / WINDOW_HEIGHT ) - startDragY;

            boundsCheck.setTranslateX(translateX);
            if(boundsCheck.getBoundsInParent().getMaxX() > bounds.getBoundsInParent().getMaxX() && boundsCheck.getBoundsInParent().getMinX() < bounds.getBoundsInParent().getMinX())
            {
                field.setTranslateX(translateX);
            }
            else
            {
                boundsCheck.setTranslateX(-translateX);
            }
            boundsCheck.setTranslateY(translateY);
            if(boundsCheck.getBoundsInParent().getMaxY() > bounds.getBoundsInParent().getMaxY() && boundsCheck.getBoundsInParent().getMinY() < bounds.getBoundsInParent().getMinY())
            {
                field.setTranslateY(translateY);
            }
            else
            {
                boundsCheck.setTranslateY(-translateY);
            }

        });

        field.setOnScroll(e -> {
            double scaleFactor = e.getDeltaY() > 0 ? GameValues.DEFAULT_SCALE_FACTOR : 1/GameValues.DEFAULT_SCALE_FACTOR;
            if((field.getScaleX() <= 2 && e.getDeltaY() > 0) || (field.getScaleX()>= 0.75  && e.getDeltaY() < 0))
            {
                field.setScaleX(field.getScaleX() * scaleFactor);
                field.setScaleY(field.getScaleY() * scaleFactor);

                boundsCheck.setScaleX(boundsCheck.getScaleX() * scaleFactor);
                boundsCheck.setScaleY(boundsCheck.getScaleY() * scaleFactor);

                bounds.setScaleX(bounds.getScaleX() * scaleFactor);
                bounds.setScaleY(bounds.getScaleY() * scaleFactor);

            }
        });

    }

    private void setDefaultFieldConfiguration(Pane field, Pane boundsCheck)
    {
        playerField.getChildren().add(field);
        field.setLayoutX(movingField.getLayoutX());
        field.setLayoutY(movingField.getLayoutY());
        field.setPrefWidth(movingField.getPrefWidth());
        field.setPrefHeight(movingField.getPrefHeight());
        field.setStyle(movingField.getStyle());
        field.setDisable(true);
        field.setVisible(false);


        playerField.getChildren().add(boundsCheck);
        boundsCheck.setLayoutX(fieldBoundsCheck.getLayoutX());
        boundsCheck.setLayoutY(fieldBoundsCheck.getLayoutY());
        boundsCheck.setPrefWidth(fieldBoundsCheck.getPrefWidth());
        boundsCheck.setPrefHeight(fieldBoundsCheck.getPrefHeight());
        boundsCheck.setStyle(fieldBoundsCheck.getStyle());
        boundsCheck.setDisable(true);
        boundsCheck.setVisible(false);


    }

    public void sorround(Pane pane)
    {
        //Check if the angle is playable and not covered
        pane.setId("full");
        pane.getStyleClass().clear();
        Pane topRight = new Pane();
        Pane bottomRight = new Pane();
        Pane topLeft = new Pane();
        Pane bottomLeft = new Pane();
        setupCardPane(pane, bottomRight, AngleOrientation.BOTTOMRIGHT);
        setupCardPane(pane, topLeft, AngleOrientation.TOPLEFT);
        setupCardPane(pane, topRight, AngleOrientation.TOPRIGHT);
        setupCardPane(pane, bottomLeft, AngleOrientation.BOTTOMLEFT);
        updateBounds(movingField, fieldBounds);

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
                        newPane.getStyleClass().add("clickableObject");
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

        } catch (IndexOutOfBoundsException e)
        {
            return;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    private HashMap<Direction, Integer> findExternals(Pane field)
    {
        int firstX = DEFAULT_MATRIX_SIZE;
        int lastX = -1;
        int firstY = DEFAULT_MATRIX_SIZE;
        int lastY = -1;
        HashMap<Direction, Integer> externals = new HashMap<>();
        Object[][] matrix = null;
        int toAdd = 0; //making player fields of other players a bit more comfortable to navigate
        if(field.equals(movingField))
            matrix = fieldPanes;
        else {
            toAdd = 1;
            for(Player player: viewGUI.getGame().getListOfPlayers())
            {
                if(player.getUsername().equals(field.getId()))
                {
                    matrix = player.getPlayerField().getMatrixField();
                    break;
                }
            }
            if(matrix == null)
                throw new RuntimeException();
        }
        for(int i = 0; i < DEFAULT_MATRIX_SIZE; i++)
        {
            for (int j = 0; j < DEFAULT_MATRIX_SIZE; j++) {

                if(matrix[i][j] != null)
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
        externals.put(Direction.TOP, DEFAULT_MATRIX_SIZE/2 - firstX + toAdd);
        externals.put(Direction.BOTTOM, lastX - DEFAULT_MATRIX_SIZE/2 + toAdd);
        externals.put(Direction.LEFT, DEFAULT_MATRIX_SIZE/2 - firstY + toAdd);
        externals.put(Direction.RIGHT, lastY - DEFAULT_MATRIX_SIZE/2 + toAdd);

        return externals;
    }

    private void updateBounds(Pane field, Rectangle bounds)
    {
        try
        {
            HashMap<Direction, Integer> externals = findExternals(field);
            bounds.setLayoutX(field.getLayoutX() + (externals.get(Direction.LEFT) + 1)*(CARD_WIDTH - ANGLE_WIDTH));
            bounds.setLayoutY(field.getLayoutY() + (externals.get(Direction.TOP) + 1)* (CARD_HEIGHT - ANGLE_HEIGHT));
            bounds.setWidth(field.getPrefWidth() - (CARD_WIDTH - ANGLE_WIDTH) * (2 + externals.get(Direction.LEFT) + externals.get(Direction.RIGHT)));
            bounds.setHeight(field.getPrefHeight()- (CARD_HEIGHT - ANGLE_HEIGHT) * (2 + externals.get(Direction.TOP) + externals.get(Direction.BOTTOM)));


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
                        drawingCard = true;

                        playCard.setDisable(true);
                        playCard.setVisible(false);

                        drawCard.setDisable(false);
                        drawCard.setVisible(true);

                        setAdditionalPanesVisibility(false);

                        turnLabel.setText("Draw a card to end your turn");
                        turnLHbox.setStyle("-fx-text-alignment: center; -fx-background-color: rgba(0,0,0,0.4); -fx-background-radius: 20;");
                        //turnLHbox.setLayoutY(8);
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
        borderFieldPane.setDisable(false);
        borderFieldPane.setVisible(true);
        try {

            DrawPosition drawPosition = inspectedCardInfo.getDrawPosition();
            CardType cardType = inspectedCardInfo.getCardType();
            viewGUI.drawCard(cardType, drawPosition);
            drawingCard = false;
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

    private void initializeOtherPlayerFields()
    {
        ArrayList<Player> otherPlayers = new ArrayList<>();
        for(Player player: viewGUI.getGame().getListOfPlayers())
        {
            if(player.getUsername().equals(viewGUI.getUsername()))
                continue;
            otherPlayers.add(player);
        }
        for(int i = 0; i < otherPlayers.size(); i++)
        {
            otherPlayerFields.get(i).setId(otherPlayers.get(i).getUsername());
            buildFieldFromPlayedCards(i, otherPlayers.get(i).getPlayerField().getPlayedCards());
        }
    }

    private void rebuildMyField()
    {
        try {
            ArrayList<CardPosition> playedCards = viewGUI.getGame().getPlayer(viewGUI.getUsername()).getPlayerField().getPlayedCards();

            for(int i = 1; i < playedCards.size() ; i++)
            {
                CardPosition cardToAdd = playedCards.get(i);
                int layoutX = cardToAdd.getPositionY() - DEFAULT_MATRIX_SIZE/2;
                int layoutY = cardToAdd.getPositionX() - DEFAULT_MATRIX_SIZE/2;
                Pane cardPane = new Pane();
                cardPane.setLayoutX(starterCard.getLayoutX() + layoutX * (CARD_WIDTH - ANGLE_WIDTH));
                cardPane.setLayoutY(starterCard.getLayoutY() + layoutY * (CARD_HEIGHT - ANGLE_HEIGHT));
                cardPane.setPrefWidth(CARD_WIDTH);
                cardPane.setPrefHeight(CARD_HEIGHT);
                cardPane.setDisable(false);
                cardPane.setVisible(true);
                cardPane.setOpacity(1);
                cardPane.setId("full");
                cardPane.setStyle(getStyle(getCardUrl(cardToAdd.getCard(), cardToAdd.getCard().getCurrentSide())));
                fieldPanes[cardToAdd.getPositionX()][cardToAdd.getPositionY()] = cardPane;
                movingField.getChildren().add(cardPane);
            }
            for(int i = 0; i< DEFAULT_MATRIX_SIZE; i++)
            {
                for (int j = 0; j< DEFAULT_MATRIX_SIZE; j++)
                {
                    if(fieldPanes[i][j] != null && fieldPanes[i][j].getId().equals("full"))
                        sorround(fieldPanes[i][j]);
                }
            }
            updateBounds(movingField, fieldBounds);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }


    private void buildFieldFromPlayedCards(int index, ArrayList<CardPosition> playedCards)
    {

        for(int i = 0; i < playedCards.size() ; i++)
        {
            CardPosition cardToAdd = playedCards.get(i);
            int layoutX = cardToAdd.getPositionY() - DEFAULT_MATRIX_SIZE/2;
            int layoutY = cardToAdd.getPositionX() - DEFAULT_MATRIX_SIZE/2;
            Pane cardPane = new Pane();
            cardPane.setLayoutX(starterCard.getLayoutX() + layoutX * (CARD_WIDTH - ANGLE_WIDTH));
            cardPane.setLayoutY(starterCard.getLayoutY() + layoutY * (CARD_HEIGHT - ANGLE_HEIGHT));
            cardPane.setPrefWidth(CARD_WIDTH);
            cardPane.setPrefHeight(CARD_HEIGHT);
            cardPane.setDisable(false);
            cardPane.setVisible(true);
            cardPane.setStyle(getStyle(getCardUrl(cardToAdd.getCard(), cardToAdd.getCard().getCurrentSide())));
            otherPlayerFields.get(index).getChildren().add(cardPane);
        }
        updateBounds(otherPlayerFields.get(index), otherPlayerFieldBounds.get(index));
    }


    @Override
    public void twentyPoints(String username) {
        Platform.runLater(new Runnable() {
            public void run() {
                twentyPoints.setText("Player " + username + "\n has reached 20 points!");
                popupPane.setDisable(false);
                popupPane.setVisible(true);
                twentyPoints.setDisable(false);
                twentyPoints.setVisible(true);
                twentyPointsReached = true;
                if(finalTurn)
                {
                    finalRound();
                }
            }
        });
    }

    @Override
    public void finalRound() {
        Platform.runLater(new Runnable() {
            public void run() {
                if(!twentyPointsReached)
                {
                    finalTurn = true;
                    return;
                }
                popupPane.setDisable(false);
                popupPane.setVisible(true);
                twentyPoints.setDisable(true);
                twentyPoints.setVisible(false);
                finalRound.setDisable(false);
                finalRound.setVisible(true);
            }
        });
    }
    @FXML
    public void quitGame() {
        exitPane.setDisable(false);
        exitPane.setVisible(true);
    }
    @FXML
    public void yesExitButton() {
        try
        {
            if(waitThread != null && waitThread.isAlive())
                waitThread.interrupt();
            viewGUI.quitGame();
            viewGUI.initialize();
            gui.switchScene(GUIScene.LOBBY);
        }catch (Exception e)
        {
            throw new RuntimeException();
        }
    }
    @FXML
    public void noExitButton() {
        exitPane.setDisable(true);
        exitPane.setVisible(false);
    }
    @FXML
    public void showChat()
    {

        newMessage.setDisable(true);
        newMessage.setVisible(false);
        chatPane.setDisable(!chatPane.isDisable());
        chatPane.setVisible(!chatPane.isVisible());
    }
    private ScrollBar scrollBar;

    @Override
    public void chatMessage(String message)
    {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(!chatPane.isVisible()) {
                    newMessage.setDisable(false);
                    newMessage.setVisible(true);
                }
                chatMessages.add(message);
            }
        });
    }

    public void sendChatMessage()
    {
        try {
            String message = chatField.getText();
            if(message.isBlank())
                return;
            viewGUI.sendChatMessage(message);
            chatField.clear();
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
    }


    public void update(Object update)
    {

        if(drawingCard)
            return;
        if(inGame) {
            Platform.runLater(new Runnable() {
                public void run() {
                    try {
                        playerUpdated = null;
                        cardJustPlayed = null;
                        initializeDrawingField();
                        initializePlayerHand(viewGUI.getGame().getPlayer(viewGUI.getUsername()).getPlayerHand(), Side.FRONT, true);
                        setBorderPane(playerHandBackground, true);
                        setBorderPane(drawingFieldBackground, true);
                        setTurn();
                        resourceInitializer();
                        markerPositionInScoreboard();
                        initializeOtherPlayerFields();
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException();
                    }

                }
            });
        }

    }




}
