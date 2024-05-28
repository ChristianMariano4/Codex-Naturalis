package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.enumerations.GUIScene;
import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.ObjectiveCard;
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
        this.game = client.createGame(this.client.getUsername(), 2);
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

}
