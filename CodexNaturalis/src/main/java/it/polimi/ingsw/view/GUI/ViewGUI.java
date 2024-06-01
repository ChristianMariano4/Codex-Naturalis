package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.DrawingField;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerField;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.TUI.TUI;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;

import java.io.IOException;
import java.io.WriteAbortedException;
import java.util.*;

public class ViewGUI implements View, Runnable {

    private Game game;
    private Client client;
    private final Scanner scanner = new Scanner(System.in);
    private String playerId;
    GUI gui;

    public ViewGUI(GUI gui) {
        this.gui = gui;
    }
    public String getUsername()
    {
        return client.getUsername();
    }

    @FXML
    public void setUsername(String username) throws IOException, InterruptedException, ServerDisconnectedException, WrongInputException {

        try{
            client.setUsername(username);
        }
        catch(InvalidUsernameException e) {
            throw new WrongInputException();

        }

    }


    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public void boardUpdate(Game gameUpdated) {

    }

    @Override
    public void newPlayer(Game gameUpdated) {
        gui.update(gameUpdated);
    }

    @Override
    public void update(Game gameUpdated) {
        this.game = gameUpdated;
        gui.update(gameUpdated);
    }



    @Override
    public void twentyPoints(String username) {

    }

    @Override
    public void chooseObjectiveCard(ArrayList<ObjectiveCard> objectiveCardsToChoose) {

    }

    @Override
    public void finalRound() {

    }

    @Override
    public void gameEndDisconnection() {
        //TODO: implement
    }

    public List<Integer> showAvailableGames() throws ServerDisconnectedException, IOException, InterruptedException {
        return client.getAvailableGames();
    }

    public void joinGame(int gameId) throws ServerDisconnectedException {
        this.game = client.joinGame(gameId, client.getUsername());
    }

    @Override
    public void run() {
        gui.launchGUI();
    }

    public void createGame(int numberOfPlayers) throws ServerDisconnectedException {
        this.game = client.createGame(this.client.getUsername(), numberOfPlayers);
    }

    public HashMap<String, Integer> showScoreboard() {
        LinkedHashMap<String, Integer> playersPlacement = new LinkedHashMap<>();
        ArrayList<Player> sortedPlayers = new ArrayList<>();
        List<Integer> points = game.getListOfPlayers().stream().map(e -> e.getPoints()).toList();
        Collections.sort(points);
        Collections.reverse(points);
        for(Integer point: points)
        {
            sortedPlayers.addAll(game.getListOfPlayers().stream().filter(e -> (e.getPoints() == point)).toList());
        }
        for(Player p : sortedPlayers) {
            playersPlacement.put(p.getUsername(), p.getPoints());
        }
        return playersPlacement;
    }
    public Game getGame()
    {
        return this.game;
    }
    public ArrayList<Integer> setReady() throws ServerDisconnectedException, IOException, NotEnoughPlayersException {
        return client.setReady();
    }
    public void changeScene(GUIScene scene)
    {
        gui.switchScene(scene);
    }
    public boolean getPlaying()
    {
        return client.getPlaying();
    }
    public boolean getMarkerTurn()
    {
        if(game.getCurrentPlayer().getUsername().equals(client.getUsername()))
            client.serMarkerTurn(true);
        return client.getMarkerTurn();
    }
    public void setMarker(Marker marker) throws NotExistingPlayerException, ServerDisconnectedException, IOException, NotAvailableMarkerException {
        client.setMarker(game.getPlayer(client.getUsername()), game.getGameId(), marker);

    }
    public ArrayList<ObjectiveCard> getObjectiveCardsToChoose()
    {
        return client.getObjectiveCardsToChoose();
    }
    public void setSecretObjective(ObjectiveCard card) throws NotExistingPlayerException, ServerDisconnectedException, IOException {
        client.setSecretObjectiveCard(game.getGameId(), game.getPlayer(client.getUsername()), card);
    }

    public StarterCard getStarterCard() throws NotExistingPlayerException, ServerDisconnectedException, IOException {
        StarterCard starterCard = game.getPlayer(client.getUsername()).getStarterCard();
        return starterCard;
    }
    public void setStarterCardSide(StarterCard card, Side side) throws NotExistingPlayerException, ServerDisconnectedException, IOException {
        client.setStarterCardSide(game.getGameId(), game.getPlayer(client.getUsername()), card, side);
    }
    public ArrayList<ObjectiveCard> getSharedObjectiveCards()
    {
        return game.getTableTop().getSharedObjectiveCards();
    }
    public boolean getGameBegin()
    {
        return client.getGameBegin();
    }
    public ObjectiveCard getSecretObjectiveCard() throws NotExistingPlayerException {
        return game.getPlayer(client.getUsername()).getSecretObjective();
    }
    public ResourceCard getTopResourceCard() throws DeckIsEmptyException {
        return game.getTableTop().getDrawingField().seeTopResourceCard();
    }
    public GoldCard getTopGoldCard() throws DeckIsEmptyException {
        return game.getTableTop().getDrawingField().seeTopGoldCard();
    }
    public HashMap<DrawPosition,ResourceCard> getDiscoveredResourceCards()
    {
        return game.getTableTop().getDrawingField().getDiscoveredResourceCards();
    }
    public HashMap<DrawPosition,GoldCard> getDiscoveredGoldCards()
    {
        return game.getTableTop().getDrawingField().getDiscoveredGoldCards();
    }
    public PlayerField getPlayerField() throws NotExistingPlayerException {
        return game.getPlayer(client.getUsername()).getPlayerField();
    }
    public boolean getIsTurn() throws NotExistingPlayerException {
        return game.getPlayer(client.getUsername()).getIsTurn();
    }
    public Player playCard(PlayableCard cardOnBoard, PlayableCard card, AngleOrientation orientation) throws InvalidCardPositionException, NotExistingPlayerException, NotTurnException, RequirementsNotMetException, CardTypeMismatchException, ServerDisconnectedException, IOException, AngleAlreadyLinkedException {
        return client.playCard(game.getGameId(), client.getUsername(), cardOnBoard, card, orientation);
    }
    public PlayableCard getOtherSideCard(PlayableCard card) throws ServerDisconnectedException, IOException {
        return client.getOtherSideCard(game.getGameId(), card);
    }
    public void drawCard(CardType cardType, DrawPosition drawPosition) throws NotExistingPlayerException, NotTurnException, ServerDisconnectedException, IOException, AlreadyThreeCardsInHandException, DeckIsEmptyException {
        client.drawCard(game.getGameId(), client.getUsername(), cardType, drawPosition);
    }
    public void endTurn() throws NotExistingPlayerException, IOException, CardTypeMismatchException, ServerDisconnectedException {
        client.endTurn(game.getGameId(), client.getUsername());
    }

}
