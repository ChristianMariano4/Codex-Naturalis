package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerField;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

/**
 * This class represents the GUI view of the game.
 * It implements the View interface and the Runnable interface.
 */
public class ViewGUI implements View, Runnable {

    private Game game;
    private Client client;
    GUI gui;

    /**
     * Constructor for the ViewGUI class.
     * @param gui The GUI instance associated with this view.
     */
    public ViewGUI(GUI gui) {
        this.gui = gui;
    }

    /**
     * Getter for the username of the client.
     * @return The username of the client.
     */
    public String getUsername()
    {
        return client.getUsername();
    }

    /**
     * Setter for the username of the client.
     * @param username The username to be set.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the thread is interrupted.
     * @throws ServerDisconnectedException If the server is disconnected.
     * @throws WrongInputException If the input is wrong.
     */
    @FXML
    public void setUsername(String username) throws IOException, InterruptedException, ServerDisconnectedException, WrongInputException {
        try{
            client.setUsername(username);
        } catch(InvalidUsernameException e) {
            throw new WrongInputException();
        }
    }

    /**
     * Setter for the client.
     * @param client The client to be set.
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Method to update the view when a new player joins the game.
     * @param gameUpdated The updated game.
     */
    @Override
    public void newPlayer(Game gameUpdated) {
        gui.update(gameUpdated);
    }

    /**
     * Method to update the view when the game is updated.
     * @param gameUpdated The updated game.
     */
    @Override
    public void update(Game gameUpdated) {
        this.game = gameUpdated;
        gui.update(gameUpdated);
    }

    /**
     * Method to update the view when a player reaches twenty points.
     * @param username The username of the player who reached twenty points.
     */
    @Override
    public void twentyPoints(String username) {
        gui.twentyPoints(username);
    }

    /**
     * Method to update the view when the player has to choose an objective card.
     * @param objectiveCardsToChoose The objective cards to choose from.
     */
    @Override
    public void chooseObjectiveCard(ArrayList<ObjectiveCard> objectiveCardsToChoose) { }

    /**
     * Method to update the view when the final round of the game starts.
     */
    @Override
    public void finalRound() {
        gui.finalRound();
    }

    /**
     * Method to update the view when the game ends.
     */
    @Override
    public void gameEnd() {
        gui.gameEnd();
    }

    /**
     * Method to update the view when a chat message is received.
     * @param message The received chat message.
     */
    @Override
    public void chatMessage(String message) {
        gui.chatMessage(message);
    }

    /**
     * Method to show the available games.
     * @return The available games.
     * @throws ServerDisconnectedException If the server is disconnected.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the thread is interrupted.
     */
    public ArrayList<Game> showAvailableGames() throws ServerDisconnectedException, IOException, InterruptedException {
        return client.getAvailableGames();
    }

    /**
     * Method to join a game.
     * @param gameId The ID of the game to join.
     * @throws ServerDisconnectedException If the server is disconnected.
     * @throws NotExistingPlayerException If the player does not exist.
     * @throws RemoteException If a remote method invocation error occurs.
     * @throws GameNotFoundException If the game is not found.
     */
    public void joinGame(int gameId) throws ServerDisconnectedException, NotExistingPlayerException, RemoteException, GameNotFoundException {
        this.game = client.joinGame(gameId, client.getUsername());
    }

    /**
     * Method to launch the GUI.
     */
    @Override
    public void run() {
        GUI.launchGUI();
    }

    /**
     * Method to create a game.
     * @param numberOfPlayers The number of players in the game.
     * @throws ServerDisconnectedException If the server is disconnected.
     */
    public void createGame(int numberOfPlayers) throws ServerDisconnectedException {
        this.game = client.createGame(this.client.getUsername(), numberOfPlayers);
    }

    /**
     * Method to get the ready status of the players.
     * @return The ready status of the players.
     * @throws ServerDisconnectedException If the server is disconnected.
     */
    public HashMap<String, Boolean> getReadyStatus() throws ServerDisconnectedException {
        try {
            return client.getReadyStatus();
        } catch (IOException e){
            throw new ServerDisconnectedException();
        }
    }

    /**
     * Method to get the game
     * @return this.game
     */
    public Game getGame()
    {
        return this.game;
    }

    /**
     * Method to set the ready status of the players
     * @return The ready status of the players.
     * @throws ServerDisconnectedException If the server is disconnected.
     */
    public ArrayList<Integer> setReady() throws ServerDisconnectedException, IOException, NotEnoughPlayersException {
        return client.setReady();
    }

    /**
     * Method to change the scene
     * @param scene The scene to be changed to
     */
    public void changeScene(GUIScene scene)
    {
        gui.switchScene(scene);
    }
    /**
     * Method to get the playing status
     * @return The playing status
     */
    public boolean getPlaying()
    {
        return client.getPlaying();
    }

    /**
     * Method to get the marker turn
     * @return The marker turn
     */
    public boolean getMarkerTurn() {
        if(game.getCurrentPlayer().getUsername().equals(client.getUsername()))
            client.serMarkerTurn(true);
        return client.getMarkerTurn();
    }

    /**
     * Method to set the marker
     * @param marker The marker to be set
     * @throws NotExistingPlayerException If the player does not exist.
     * @throws ServerDisconnectedException If the server is disconnected.
     * @throws IOException If an I/O error occurs.
     * @throws NotAvailableMarkerException If the marker is not available.
     */
    public void setMarker(Marker marker) throws NotExistingPlayerException, ServerDisconnectedException, IOException, NotAvailableMarkerException {
        client.setMarker(game.getPlayer(client.getUsername()), game.getGameId(), marker);

    }

    /**
     * Method to get the objective cards to choose
     * @return The objective cards to choose
     */
    public ArrayList<ObjectiveCard> getObjectiveCardsToChoose()
    {
        return client.getObjectiveCardsToChoose();
    }

    /**
     * Method to set the secret objective
     * @param card The card to be set as the secret objective
     * @throws NotExistingPlayerException If the player does not exist.
     * @throws ServerDisconnectedException If the server is disconnected.
     * @throws IOException If an I/O error occurs.
     */
    public void setSecretObjective(ObjectiveCard card) throws NotExistingPlayerException, ServerDisconnectedException, IOException {
        client.setSecretObjectiveCard(game.getGameId(), game.getPlayer(client.getUsername()), card);
    }

    /**
     * Method to get the starter card
     * @return The starter card
     * @throws NotExistingPlayerException If the player does not exist.
     */
    public StarterCard getStarterCard() throws NotExistingPlayerException {
        return game.getPlayer(client.getUsername()).getStarterCard();
    }

    /**
     * Method to set the starter card side
     * @param card The card to set the side for
     * @param side The side to be set
     * @throws NotExistingPlayerException If the player does not exist.
     * @throws ServerDisconnectedException If the server is disconnected.
     * @throws IOException If an I/O error occurs.
     */
    public void setStarterCardSide(StarterCard card, Side side) throws NotExistingPlayerException, ServerDisconnectedException, IOException {
        client.setStarterCardSide(game.getGameId(), game.getPlayer(client.getUsername()), card, side);
    }

    /**
     * Method to get the shared objective cards
     * @return The shared objective cards
     */
    public ArrayList<ObjectiveCard> getSharedObjectiveCards()
    {
        return game.getTableTop().getSharedObjectiveCards();
    }

    /**
     * Method to get the game begin status
     * @return The game begin status
     */
    public boolean getGameBegin()
    {
        return client.getGameBegin();
    }

    /**
     * Method to get the secret objective card
     * @return The secret objective card
     * @throws NotExistingPlayerException If the player does not exist.
     */
    public ObjectiveCard getSecretObjectiveCard() throws NotExistingPlayerException {
        return game.getPlayer(client.getUsername()).getSecretObjective();
    }

    /**
     * Method to get the top resource card
     * @return The top resource card
     * @throws DeckIsEmptyException If the deck is empty.
     */
    public ResourceCard getTopResourceCard() throws DeckIsEmptyException {
        return game.getTableTop().getDrawingField().seeTopResourceCard();
    }

    /**
     * Method to get the top gold card
     * @return The top gold card
     * @throws DeckIsEmptyException If the deck is empty.
     */
    public GoldCard getTopGoldCard() throws DeckIsEmptyException {
        return game.getTableTop().getDrawingField().seeTopGoldCard();
    }

    /**
     * Method to get the discovered resource cards
     * @return The discovered resource cards
     */
    public HashMap<DrawPosition,ResourceCard> getDiscoveredResourceCards() {
        return game.getTableTop().getDrawingField().getDiscoveredResourceCards();
    }

    /**
     * Method to get the discovered gold cards
     * @return The discovered gold cards
     */
    public HashMap<DrawPosition,GoldCard> getDiscoveredGoldCards() {
        return game.getTableTop().getDrawingField().getDiscoveredGoldCards();
    }

    /**
     * Method to get the player field
     * @return The player field
     * @throws NotExistingPlayerException If the player does not exist.
     */
    public PlayerField getPlayerField() throws NotExistingPlayerException {
        return game.getPlayer(client.getUsername()).getPlayerField();
    }

    /**
     * Method to get the is turn status
     * @return The is turn status
     * @throws NotExistingPlayerException If the player does not exist.
     */
    public boolean getIsTurn() throws NotExistingPlayerException {
        return game.getPlayer(client.getUsername()).getIsTurn();
    }

    /**
     * Method to play a card
     * @param cardOnBoard The card on board
     * @param card The card to be played
     * @param orientation The orientation of the card
     * @return The player after the card is played
     * @throws InvalidCardPositionException If the card position is invalid.
     * @throws NotExistingPlayerException If the player does not exist.
     * @throws NotTurnException If it is not the player's turn.
     * @throws RequirementsNotMetException If the requirements are not met.
     * @throws CardTypeMismatchException If the card type is mismatched.
     * @throws ServerDisconnectedException If the server is disconnected.
     * @throws IOException If an I/O error occurs.
     * @throws AngleAlreadyLinkedException If the angle is already linked.
     */
    public Player playCard(PlayableCard cardOnBoard, PlayableCard card, AngleOrientation orientation) throws InvalidCardPositionException, NotExistingPlayerException, NotTurnException, RequirementsNotMetException, CardTypeMismatchException, ServerDisconnectedException, IOException, AngleAlreadyLinkedException {
        return client.playCard(game.getGameId(), client.getUsername(), cardOnBoard, card, orientation);
    }

    /**
     * Method to get the other side card
     * @param card The card to get the other side for
     * @return The other side card
     * @throws ServerDisconnectedException If the server is disconnected.
     * @throws IOException If an I/O error occurs.
     */
    public PlayableCard getOtherSideCard(PlayableCard card) throws ServerDisconnectedException, IOException {
        return client.getOtherSideCard(game.getGameId(), card);
    }

    /**
     * Method to draw a card
     * @param cardType The type of the card to be drawn
     * @param drawPosition The position to draw the card from
     * @throws NotExistingPlayerException If the player does not exist.
     * @throws NotTurnException If it is not the player's turn.
     * @throws ServerDisconnectedException If the server is disconnected.
     * @throws IOException If an I/O error occurs.
     * @throws AlreadyThreeCardsInHandException If there are already three cards in hand.
     * @throws DeckIsEmptyException If the deck is empty.
     */
    public void drawCard(CardType cardType, DrawPosition drawPosition) throws NotExistingPlayerException, NotTurnException, ServerDisconnectedException, IOException, AlreadyThreeCardsInHandException, DeckIsEmptyException {
        client.drawCard(game.getGameId(), client.getUsername(), cardType, drawPosition);
    }

    /**
     * Method to end the turn
     * @throws NotExistingPlayerException If the player does not exist.
     * @throws IOException If an I/O error occurs.
     * @throws CardTypeMismatchException If the card type is mismatched.
     * @throws ServerDisconnectedException If the server is disconnected.
     */
    public void endTurn() throws NotExistingPlayerException, IOException, CardTypeMismatchException, ServerDisconnectedException {
        client.endTurn(game.getGameId(), client.getUsername());
    }

    /**
     * Method to initialize the game
     */
    public void initialize() {
        this.game = null;
        client.resetClient();
    }

    /**
     * Method to quit the game
     * @throws ServerDisconnectedException If the server is disconnected.
     * @throws IOException If an I/O error occurs.
     */
    public void quitGame() throws ServerDisconnectedException, IOException {

        client.quitGame();
    }

/**
 * Method to send a chat message
 * @param message The message to be sent
 * @throws ServerDisconnectedException If the server is disconnected.
 * @throws IOException If an I/O error occurs.
 */
    public void sendChatMessage(String message) throws ServerDisconnectedException, IOException {
        client.sendChatMessage(game.getGameId(), message);
    }
}
