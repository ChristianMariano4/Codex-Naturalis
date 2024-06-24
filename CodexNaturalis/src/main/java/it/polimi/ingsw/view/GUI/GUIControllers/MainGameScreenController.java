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
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.HashMap;

import static it.polimi.ingsw.model.GameValues.*;

/**
 * The MainGameScreenController class is responsible for handling user interactions in the main game screen of the GUI.
 * It extends the GUIController class.
 */
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
    private ScrollBar chatScrollBar;

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

    public Pane cardsInHandInspector;
    public Pane handCardInspector;

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
    private final ArrayList<Pane> playerHandPanes = new ArrayList<>();
    public Button playCard;

    public Label waitTurn;
    public Pane markerSelection;
    public Pane marker1;
    public Pane marker2;
    public Pane marker3;
    public Pane marker4;
    public Pane MPane;
    private final ArrayList<Pane> markerPanes = new ArrayList<>();

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
    private final ArrayList<Pane> platPanes = new ArrayList<>();

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
    private final Rectangle fieldBounds = new Rectangle();
    private final Pane[][] fieldPanes = new Pane[DEFAULT_MATRIX_SIZE][DEFAULT_MATRIX_SIZE];
    private final ArrayList<Pane> otherPlayerFields = new ArrayList<>();
    private final ArrayList<Pane> otherPlayerFieldBoundsChecks = new ArrayList<>();
    private final ArrayList<Rectangle> otherPlayerFieldBounds = new ArrayList<>();

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

    /**
     * Initializes the marker pane.
     */
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

    /**
     * Shows the username when the mouse hovers over it.
     * @param event The MouseEvent object.
     */
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

    /**
     * Shows the other player's field when the mouse hovers over it.
     * @param event The MouseEvent object.
     */
    @FXML
    public void showOtherPlayerField(MouseEvent event) {
        Pane pane = (Pane) event.getSource();
        int index = switch (pane.getId()) {
            case "p2" -> 1;
            case "p3" -> 2;
            default -> 0;
        };
        if(inMyField) {
            setOtherField(index);
        }
        else {
            boolean isDisable = otherPlayerFields.get(index).isDisable();
            setMyField();
            if(isDisable)
                setOtherField(index);
        }
    }

    /**
     * Sets the other player's field.
     * @param index The index of the other player.
     */
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

    /**
     * Sets the current player's field.
     */
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

    /**
     * Hides the username when the mouse is not hovering over it.
     * @param event The MouseEvent object.
     */
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

    /**
     * Handles the action of the rulebook button.
     */
    @FXML
    public void rulebookButton() {
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

    /**
     * Goes to the next slide in the rulebook.
     */
    @FXML
    public void nextRulebookSlide() {
        rulebookTabPane.getSelectionModel().select(rulebookTabPane.getSelectionModel().getSelectedIndex()+1);
        nextButton.disableProperty().bind(rulebookTabPane.getSelectionModel().selectedIndexProperty().greaterThanOrEqualTo(10));
    }

    /**
     * Goes to the previous slide in the rulebook.
     */
    @FXML
    public void previousRulebookSlide() {
        rulebookTabPane.getSelectionModel().select(rulebookTabPane.getSelectionModel().getSelectedIndex()-1);
        backButton.disableProperty().bind(rulebookTabPane.getSelectionModel().selectedIndexProperty().lessThanOrEqualTo(0));
    }

    /**
     * Initializes the scene.
     */
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

    /**
     * Gets the URL of a card.
     * @param card The card.
     * @param side The side of the card.
     * @return The URL of the card.
     */
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

    /**
     * Gets the style of a card.
     * @param url The URL of the card.
     * @return The style of the card.
     */
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
        } catch (Exception ignored) {

        }
    };

    /**
     * Starts a new thread that waits for the marker selection phase to begin.
     */
    private void preGame() {
        waitThread = new Thread(markerThread);
        waitThread.start();
    }

    /**
     * Sets the first available marker for the current player and proceeds to the starter card side selection phase.
     */
    @FXML
    private void markerOne() {
        try {

            viewGUI.setMarker(viewGUI.getGame().getAvailableMarkers().getFirst());
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
        markerSelection.setDisable(true);
        markerSelection.setVisible(false);
        chooseStarterCardSide();
    }

    /**
     * Sets the second available marker for the current player and proceeds to the starter card side selection phase.
     */
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

    /**
     * Sets the third available marker for the current player and proceeds to the starter card side selection phase.
     */
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

    /**
     * Sets the fourth available marker for the current player and proceeds to the starter card side selection phase.
     */
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

    /**
     * Prepares the secret objective card selection phase.
     * It retrieves the objective cards to choose from and the shared objective cards from the viewGUI.
     * It then sets the style of the secret and shared objective cards in the GUI.
     * Finally, it enables and makes visible the secret objective card selection pane in the GUI.
     */
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
    /**
     * Handles the action of selecting the first secret objective card.
     * It sets the first secret objective card for the current player in the viewGUI.
     * It then disables and hides the secret objective card selection pane in the GUI.
     */
    @FXML
    public void secretOne()
    {
        try {
            viewGUI.setSecretObjective(viewGUI.getObjectiveCardsToChoose().getFirst());
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
        catch (Exception ignored)
        {
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
            throw new RuntimeException();
        }
    }

    public void initializeChat() {
        chatButtonPane.setDisable(false);
        chatButtonPane.setVisible(true);
        chatList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<String> call(ListView<String> list) {
                final ListCell cell = new ListCell() {
                    @Override
                    public void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty || item == null);
                        if (!isEmpty()) {
                            setPrefWidth(chatList.getPrefWidth() - 5);
                            setTextFill(Color.WHITE);
                            setWrapText(true);
                            assert item != null;
                            setText(item.toString());

                        }
                    }

                };

                return cell;
            }
        });
        chatMessages.clear();
        chatList.setItems(chatMessages);
        chatMessages.add("Write a public message or /[player's username] to send a private message");
            new Thread(() -> { //this thread solved a bug with lookup
                while (chatList.lookup(".scroll-bar:vertical") == null);
                Platform.runLater(() -> {
                    chatScrollBar = (ScrollBar) chatList.lookup(".scroll-bar:vertical");
                    chatScrollBar.visibleAmountProperty().addListener(e -> chatList.scrollTo(chatMessages.getLast()));
                });
            }).start();

            chatField.setOnKeyPressed(e ->
            {
                if(e.getCode().equals(KeyCode.ENTER))
                {
                    sendChatMessage();
                }
            });
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

                Label resourceLabel;
                switch(resource){
                    case FUNGI -> resourceLabel = fungi;
                    case ANIMAL -> resourceLabel = animal;
                    case PLANT -> resourceLabel = plant;
                    case INSECT -> resourceLabel = insect;
                    case QUILL -> resourceLabel = quill;
                    case INKWELL -> resourceLabel = inkwell;
                    case MANUSCRIPT -> resourceLabel = manuscript;
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

    /**
     * This method sets the text of the turnLabel to indicate whose turn it is.
     * It also sets the style of the turnLHbox and enables or disables the playCard and drawCard buttons based on whose turn it is.
     * The turnLabel is then made visible.
     * If an exception occurs, a RuntimeException is thrown.
     */
    private void setTurn()
    {
        try {
            if(viewGUI.getGame().getListOfPlayers().stream().filter(e -> !e.getUsername().equals(viewGUI.getUsername()) && !e.getIsDisconnected()).toList().isEmpty())
            {
                turnLabel.setText("You are the ony player left,\nthe game is paused");
                playCard.setDisable(true);
                playCard.setVisible(false);

                drawCard.setDisable(true);
                drawCard.setVisible(false);

                playingCard = false;
            }
            else {
                if (viewGUI.getIsTurn()) {
                    turnLabel.setText("It is currently your turn,\n select a card from your hand to Play");
                    turnLHbox.setStyle("-fx-text-alignment: center; -fx-background-color: rgba(0,0,0,0.4); -fx-background-radius: 20;");
                    //turnLHbox.setLayoutY(12);
                    setBorderPane(playerHandBackground, false);

                    playCard.setDisable(false);
                    playCard.setVisible(true);

                    setMyField();

                } else {

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
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
    }

    /**
     * This method initializes the player's hand by adding cards to the playerHandPanes list.
     * It then sets the visibility and enabled state of the cards based on the parameter 'enabled'.
     * If an exception occurs, a RuntimeException is thrown.
     */
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

    /**
     * This method is used to show the other side of the card in hand.
     * It checks if the front side of the card is currently being shown, and if so, it sets the side to BACK and calls the showCardInHand method.
     * If the back side of the card is currently being shown, it sets the side to FRONT and calls the showCardInHand method.
     */
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

    /**
     * This method is used to select the first card in the hand for inspection.
     * It sets the cardInHandSelected to 0 and the side to FRONT, then calls the showCardInHand method.
     */
    @FXML
    public void card1HandInspectorButton() {
        inspectedCardInfo.setCardInHandSelected(0);
        inspectedCardInfo.setSide(Side.FRONT);
        //cardInHandSelected = 0;
        showCardInHand();
    }

    /**
     * This method is used to select the second card in the hand for inspection.
     * It sets the cardInHandSelected to 1 and the side to FRONT, then calls the showCardInHand method.
     */
    @FXML
    public void card2HandInspectorButton() {
        inspectedCardInfo.setCardInHandSelected(1);
        inspectedCardInfo.setSide(Side.FRONT);
        //cardInHandSelected = 1;
        showCardInHand();
    }

    /**
     * This method is used to select the third card in the hand for inspection.
     * It sets the cardInHandSelected to 2 and the side to FRONT, then calls the showCardInHand method.
     */
    @FXML
    public void card3HandInspectorButton() {
        inspectedCardInfo.setCardInHandSelected(2);
        inspectedCardInfo.setSide(Side.FRONT);
        //cardInHandSelected = 2;
        showCardInHand();
    }

    /**
     * This method is used to display the selected card from the player's hand in the GUI.
     * It first retrieves the current game state from the viewGUI.
     * It then enables and makes visible the card inspector for the cards in hand, and disables and hides the player's field and the border field pane in the GUI.
     * Finally, it sets the style of the hand card inspector based on the card selected for inspection.
     * If an exception occurs during this process, a RuntimeException is thrown.
     */
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

    /**
     * This method is used to show the card in hand.
     * It sets the visibility and enabled state of the cardsInHandInspector, playerField, and borderFieldPane.
     * It then sets the style of the handCardInspector based on the card selected for inspection.
     * If an exception occurs, a RuntimeException is thrown.
     */
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

    /**
     * This method is used to set the visibility and enabled state of the additional panes.
     * It iterates over the fieldPanes array and sets the visibility and enabled state of each pane based on the parameter 'visible'.
     * If an exception occurs, a RuntimeException is thrown.
     */
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
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * This method is used when the exit hand button is clicked.
     * It sets the visibility and enabled state of the cardsInHandInspector, playerField, and borderFieldPane.
     * It also sets the cardInHandSelected to 3 and the frontSide to true.
     */
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
    }

    /**
     * This method is used to show the other side of the shared objective card.
     * It checks if the front side of the card is currently being shown, and if so, it sets the side to BACK and calls the showSharedObjCard method.
     * If the back side of the card is currently being shown, it sets the side to FRONT and calls the showSharedObjCard method.
     */
    @FXML
    public void showOtherSharedObjCardSide() {
        if(inspectedCardInfo.getFrontSide()) {
            inspectedCardInfo.setSide(Side.BACK);
            showSharedObjCard();
            inspectedCardInfo.setFrontSide(false);
        } else {
            inspectedCardInfo.setSide(Side.FRONT);
            showSharedObjCard();
            inspectedCardInfo.setFrontSide(true);
        }
    }

    /**
     * This method is used to select the first shared objective card for inspection.
     * It sets the sharedObjChoice to 0 and the side to FRONT, then calls the showSharedObjCard method.
     */
    @FXML
    public void sharedObjCardButtonOne() {
        inspectedCardInfo.setSharedObjChoice(0);
        inspectedCardInfo.setSide(Side.FRONT);
        inspectedCardInfo.setFrontSide(true);
        //sharedObjChoice = 0;
        showSharedObjCard();
    }

    /**
     * This method is used to select the second shared objective card for inspection.
     * It sets the sharedObjChoice to 1 and the side to FRONT, then calls the showSharedObjCard method.
     */
    @FXML
    public void sharedObjCardButtonTwo() {
        inspectedCardInfo.setSharedObjChoice(1);
        inspectedCardInfo.setSide(Side.FRONT);
        inspectedCardInfo.setFrontSide(true);
        showSharedObjCard();
    }

    /**
     * This method is used to show the shared objective card.
     * It sets the visibility and enabled state of the objectiveCardsInspector, playerField, and borderFieldPane.
     * It then sets the style of the showObjCard based on the shared objective card selected for inspection.
     * If an exception occurs, a RuntimeException is thrown.
     */
    private void showSharedObjCard() {
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

    /**
     * This method is used when the exit objective button is clicked.
     * It sets the visibility and enabled state of the objectiveCardsInspector, playerField, and borderFieldPane.
     * It also sets the sharedObjChoice to 2 and the frontSide to true.
     */
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

    /**
     * This method is used to show the other side of the secret objective card.
     * It checks if the front side of the card is currently being shown, and if so, it sets the side to BACK and calls the showSecretObjCard method.
     * If the back side of the card is currently being shown, it sets the side to FRONT and calls the showSecretObjCard method.
     */
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

    /**
     * This method is used to select the secret objective card for inspection.
     * It sets the side to FRONT, then calls the showSecretObjCard method.
     */
    @FXML
    public void secretObjCardButton() {
        inspectedCardInfo.setSide(Side.FRONT);
        inspectedCardInfo.setFrontSide(true);
        showSecretObjCard();
    }

    /**
     * This method is used to show the secret objective card.
     * It sets the visibility and enabled state of the secretObjCardsInspector, playerField, and borderFieldPane.
     * It then sets the style of the showSObjCard based on the secret objective card selected for inspection.
     * If an exception occurs, a RuntimeException is thrown.
     */
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

    /**
     * This method is used when the exit secret objective button is clicked.
     * It sets the visibility and enabled state of the secretObjCardsInspector, playerField, and borderFieldPane.
     * It also sets the frontSide to true.
     */
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
    /**
     * This method is used to show the other side of the drawing field card.
     * It checks if the front side of the card is currently being shown, and if so, it sets the side to BACK and calls the showDrawingFieldCard method.
     * If the back side of the card is currently being shown, it sets the side to FRONT and calls the showDrawingFieldCard method.
     */
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

    /**
     * This method is triggered when the left deck button is clicked.
     * It sets the side of the inspected card to BACK, the card type to RESOURCE, and indicates that the card is from the deck.
     * It also sets the draw position to FROMDECK.
     * Finally, it calls the showDrawingFieldCard method to display the card.
     */
    @FXML
    public void leftDeckButton() {
        inspectedCardInfo.setSide(Side.BACK);
        inspectedCardInfo.setCardType(CardType.RESOURCE);
        inspectedCardInfo.setFromDeck(true);
        inspectedCardInfo.setDrawPosition(DrawPosition.FROMDECK);
        showDrawingFieldCard();
    }

    /**
     * This method is triggered when the right deck button is clicked.
     * It sets the side of the inspected card to BACK, the card type to GOLD, and indicates that the card is from the deck.
     * It also sets the draw position to FROMDECK.
     * Finally, it calls the showDrawingFieldCard method to display the card.
     */
    @FXML
    public void rightDeckButton() {
        inspectedCardInfo.setSide(Side.BACK);
        inspectedCardInfo.setCardType(CardType.GOLD);
        inspectedCardInfo.setFromDeck(true);
        inspectedCardInfo.setDrawPosition(DrawPosition.FROMDECK);
        showDrawingFieldCard();
    }

    /**
     * This method is triggered when the right top drawing button is clicked.
     * The implementation details are not provided in the code excerpt.
     */
    @FXML
    public void rightTopDrawingButton() {
        inspectedCardInfo.setSide(Side.FRONT);
        inspectedCardInfo.setCardType(CardType.GOLD);
        inspectedCardInfo.setFromDeck(false);
        inspectedCardInfo.setDrawPosition(DrawPosition.LEFT);
        showDrawingFieldCard();
    }

    /**
     * This method is triggered when the left top drawing button is clicked.
     * It sets the side of the inspected card to FRONT, the card type to RESOURCE, and indicates that the card is not from the deck.
     * It also sets the draw position to LEFT.
     * Finally, it calls the showDrawingFieldCard method to display the card.
     */
    @FXML
    public void leftTopDrawingButton() {
        inspectedCardInfo.setSide(Side.FRONT);
        inspectedCardInfo.setCardType(CardType.RESOURCE);
        inspectedCardInfo.setFromDeck(false);
        inspectedCardInfo.setDrawPosition(DrawPosition.LEFT);
        showDrawingFieldCard();
    }

    /**
     * This method is triggered when the right bottom drawing button is clicked.
     * It sets the side of the inspected card to FRONT, the card type to GOLD, and indicates that the card is not from the deck.
     * It also sets the draw position to RIGHT.
     * Finally, it calls the showDrawingFieldCard method to display the card.
     */
    @FXML
    public void rightBottomDrawingButton() {
        inspectedCardInfo.setSide(Side.FRONT);
        inspectedCardInfo.setCardType(CardType.GOLD);
        inspectedCardInfo.setFromDeck(false);
        inspectedCardInfo.setDrawPosition(DrawPosition.RIGHT);
        showDrawingFieldCard();
    }

    /**
     * This method is triggered when the left bottom drawing button is clicked.
     * It sets the side of the inspected card to FRONT, the card type to RESOURCE, and indicates that the card is not from the deck.
     * It also sets the draw position to RIGHT.
     * Finally, it calls the showDrawingFieldCard method to display the card.
     */
    @FXML
    public void leftBottomDrawingButton() {
        inspectedCardInfo.setSide(Side.FRONT);
        inspectedCardInfo.setCardType(CardType.RESOURCE);
        inspectedCardInfo.setFromDeck(false);
        inspectedCardInfo.setDrawPosition(DrawPosition.RIGHT);
        showDrawingFieldCard();
    }

    /**
     * This method is used to display the selected card from the drawing field in the GUI.
     * It first retrieves the current game state from the viewGUI.
     * It then enables and makes visible the card inspector for the drawing field cards, and disables and hides the player's field and the border field pane in the GUI.
     * Finally, it sets the style of the drawingCardPane based on the card selected for inspection.
     * If an exception occurs during this process, a RuntimeException is thrown.
     */
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

    /**
     * This method is used when the exit drawing button is clicked.
     * It sets the visibility and enabled state of the drawingCardsInspector, playerField, and borderFieldPane.
     * It also sets the fromDeck to false, the cardType to null, and the drawPosition to null.
     */
    @FXML
    public void exitDrawingFieldButton() {
        drawingCardsInspector.setDisable(true);
        drawingCardsInspector.setVisible(false);
        playerField.setDisable(false);
        playerField.setVisible(true);
        borderFieldPane.setDisable(false);
        borderFieldPane.setVisible(true);
    }

    /**
     * This method initializes the scoreboard by creating new Panes for each marker and adding them to the scoreboardMarkerPanes list.
     * It also adds all the platPanes to the platPanes list and sets their visibility and enabled state to false.
     * Finally, it sets the preferred height and width of each pane in the scoreboardMarkerPanes list and calls the markerPositionInScoreboard method.
     */
    private void initializeScoreboard() {
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

    /**
     * This method sets the layoutY property of each marker in the children of the given pane.
     * It starts with a transformation of 0 and decreases it by 2.5 for each marker.
     * @param pane The pane whose children's layoutY property is to be set.
     */
    private void setScoreboardChildren(Pane pane) {
        double transformation = 0;
        for(Node marker: pane.getChildren()) {
            Pane markerPane = (Pane) marker;
            markerPane.setLayoutY(transformation);
            //pane.getChildren().add(markerPane);
            transformation = transformation - 2.5;
        }
    }

    /**
     * This method positions the markers in the scoreboard.
     * It first retrieves the current game from the viewGUI and resets the scoreboard.
     * It then iterates over the list of players in the game, retrieves the points of each player, and adds the player's marker to the corresponding platPane.
     * Finally, it sets the visibility and enabled state of each marker and platPane to true.
     */
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

    /**
     * This method resets the scoreboard by setting the visibility and enabled state of each platPane to false and clearing its children.
     */
    private void resetScoreboard() {
        for(Pane pane: platPanes) {
            pane.setDisable(true);
            pane.setVisible(false);
            pane.getChildren().clear();
        }
    }

    /**
     * This method initializes the objective cards by setting the style of each shared objective card and the secret objective card based on their URLs.
     * It then sets the visibility and enabled state of the objectiveCards to true.
     * If an exception occurs, a RuntimeException is thrown.
     */
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

    /**
     * This method initializes the drawing field by calling the emptyDeckHandler method for each deck and discovered card.
     * It then sets the visibility and enabled state of the drawingField to true.
     * If an exception occurs, a RuntimeException is thrown.
     */
    private void initializeDrawingField()
    {
        try
        {
            emptyDeckHandler(resourceDeck, Side.BACK);
            emptyDeckHandler(goldDeck, Side.BACK);

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

    /**
     * This method handles empty decks by setting the style of the given pane based on the URL of the top card of the corresponding deck.
     * If an exception occurs, it sets the visibility and enabled state of the pane to false.
     * @param pane The pane whose style is to be set.
     * @param side The side of the card to be used to get the URL.
     */
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

    /**
     * This method is used to initialize the player's field.
     * It first sets the visibility and enabled state of the borderFieldPane.
     * It then creates a new Rectangle for the fieldCut and sets its layoutX, layoutY, width, height, and style.
     * Finally, it creates new Panes for each field and boundsCheck, and sets their properties accordingly.
     */
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

        try {
            PlayableCard card = viewGUI.getPlayerField().getMatrixField()[DEFAULT_MATRIX_SIZE/2][DEFAULT_MATRIX_SIZE/2];
            starterCard.setStyle(getStyle(getCardUrl(card ,card.getCurrentSide())));
            fieldPanes[DEFAULT_MATRIX_SIZE/2][DEFAULT_MATRIX_SIZE/2] = starterCard;
            surround(starterCard);
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }

    }

    /**
     * Sets mouse actions for the given field, boundsCheck and bounds.
     * This includes actions for mouse press, drag and scroll events.
     * @param field The Pane object representing the field.
     * @param boundsCheck The Pane object used for bounds checking.
     * @param bounds The Rectangle object representing the bounds.
     */
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

    /**
     * Sets the default configuration for the given field and boundsCheck.
     * @param field The Pane object representing the field.
     * @param boundsCheck The Pane object used for bounds checking.
     */
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

    /**
     * Surrounds the given pane with other panes.
     * @param pane The Pane object to be surrounded.
     */
    public void surround(Pane pane)
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

    /**
     * Sets up a new pane in relation to a given pane and orientation.
     * @param pane The Pane object to be used as reference.
     * @param newPane The new Pane object to be set up.
     * @param orientation The AngleOrientation object representing the orientation.
     */
    public void setupCardPane(Pane pane, Pane newPane, AngleOrientation orientation) {
        double oldX = pane.getLayoutX();
        double oldY = pane.getLayoutY();

        double newX = 0;
        double newY = 0;
        switch (orientation) {
            case TOPRIGHT,
                 BOTTOMRIGHT -> // 20 and 24 are the width and height of the angle: 90 - 20 = 70, 60 - 24 = 36
                    newX = oldX + (CARD_WIDTH - ANGLE_WIDTH);
            case TOPLEFT, BOTTOMLEFT -> newX = oldX - (CARD_WIDTH - ANGLE_WIDTH);
        }
        switch (orientation) {
            case TOPRIGHT, TOPLEFT -> newY = oldY - (CARD_HEIGHT - ANGLE_HEIGHT);
            case BOTTOMRIGHT, BOTTOMLEFT -> newY = oldY + (CARD_HEIGHT - ANGLE_HEIGHT);
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

                        //checks if the position where I want to place the new pane is a valid playable position
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

                        newPane.setOnMouseDragged(e -> dragging = true);

                        newPane.setOnMouseClicked(this::clickedOnFieldPane);



                        return;

                    }
                }
            }
            throw new RuntimeException();

        } catch (IndexOutOfBoundsException ignored)
        {
        }
        catch (Exception e) {
            throw new RuntimeException();
        }

    }

    /**
     * Finds the external boundaries of the given field.
     * @param field The Pane object representing the field.
     * @return A HashMap object mapping Direction to Integer representing the external boundaries.
     */
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

    /**
     * Updates the bounds of the given field.
     * @param field The Pane object representing the field.
     * @param bounds The Rectangle object representing the bounds.
     */
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

    /**
     * Handles the mouse click event on the field pane.
     * @param event The MouseEvent object representing the mouse event.
     */
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
                                                surround(source);


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
                        if(viewGUI.getGame().getTableTop().getDrawingField().getNoCardsLeft())
                            viewGUI.endTurn();
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
                    throw new RuntimeException();
                }

            }
        }
    }

    /**
     * Sets the border style of the given pane.
     * @param pane The Pane object to be styled.
     * @param toRemove A boolean value indicating whether to remove the border or not.
     */
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

    /**
     * Handles the action for the exit popup button.
     */
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

    /**
     * Removes a card from the hand.
     */
    private void removeCardFromHand() {
        playerHandPanes.get(inspectedCardInfo.getCardInHandSelected()).setDisable(true);
        playerHandPanes.get(inspectedCardInfo.getCardInHandSelected()).setVisible(false);
    }

    /**
     * Handles the action for the draw card button.
     */
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
            //can't happen because the only cards you see are the ones you can draw
            throw new RuntimeException();

        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
    }

    /**
     * Initializes the fields of other players.
     */
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

    /**
     * Rebuilds the field of the current player.
     */
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
                        surround(fieldPanes[i][j]);
                }
            }
            updateBounds(movingField, fieldBounds);
        }
        catch (Exception e)
        {
            throw new RuntimeException();
        }
    }

    /**
     * Builds a field from the played cards.
     * @param index The index of the player.
     * @param playedCards The list of CardPosition objects representing the played cards.
     */
    private void buildFieldFromPlayedCards(int index, ArrayList<CardPosition> playedCards)
    {

        for (CardPosition cardToAdd : playedCards) {
            int layoutX = cardToAdd.getPositionY() - DEFAULT_MATRIX_SIZE / 2;
            int layoutY = cardToAdd.getPositionX() - DEFAULT_MATRIX_SIZE / 2;
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

    /**
     * Handles the event when a player reaches twenty points.
     * @param username The username of the player who reached twenty points.
     */
    @Override
    public void twentyPoints(String username) {
        Platform.runLater(() -> {
            if(username == null)
                twentyPoints.setText("Both decks are empty!");
            else
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
        });
    }

    /**
     * Handles the event for the final round.
     */
    @Override
    public void finalRound() {
        Platform.runLater(() -> {
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
        });
    }

    /**
     * Handles the action for the quit game button.
     */
    @FXML
    public void quitGame() {
        exitPane.setDisable(false);
        exitPane.setVisible(true);
    }

    /**
     * Handles the action for the yes exit button.
     */
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

    /**
     * Handles the action for the no exit button.
     */
    @FXML
    public void noExitButton() {
        exitPane.setDisable(true);
        exitPane.setVisible(false);
    }

    /**
     * Handles the action for the show chat button.
     */
    @FXML
    public void showChat()
    {
        newMessage.setDisable(true);
        newMessage.setVisible(false);
        chatPane.setDisable(!chatPane.isDisable());
        chatPane.setVisible(!chatPane.isVisible());
    }

    /**
     * Handles the event when a chat message is received.
     * @param message The received chat message.
     */
    @Override
    public void chatMessage(String message)
    {
        Platform.runLater(() -> {
            if(!chatPane.isVisible()) {
                newMessage.setDisable(false);
                newMessage.setVisible(true);
            }
            chatMessages.add(message);
        });
    }

    /**
     * Handles the action for the send chat message button.
     */
    @FXML
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

    /**
     * Updates the game state based on the given update object.
     * @param update The object representing the update.
     */
    public void update(Object update)
    {
        if(drawingCard)
            return;
        if(inGame) {
            Platform.runLater(() -> {
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

            });
        }
    }
}