package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.exceptions.CardTypeMismatchException;
import it.polimi.ingsw.exceptions.NotExistingPlayerException;
import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import it.polimi.ingsw.exceptions.WrongInputException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.TUI.TUI;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;

import java.io.IOException;
import java.io.WriteAbortedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ViewGUI implements View, Runnable {

    private Game game;
    private Client client;
    private final Scanner scanner = new Scanner(System.in);
    private String playerId;
    GUI gui;

    public ViewGUI(GUI gui) {
        this.gui = gui;
    }

    @FXML
    public void setUsername(String username) throws IOException, InterruptedException, ServerDisconnectedException, WrongInputException {

        if(client.checkUsername(username)) {
            client.setUsername(username);
        }
        else {
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

    }

    @Override
    public void update(Game gameUpdated) {
        this.game = gameUpdated;
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
        client.joinGame(gameId, client.getUsername());
    }

    @Override
    public void run() {
        gui.launchGUI();
    }

    public void createGame() throws ServerDisconnectedException {
        this.game = client.createGame(this.client.getUsername());
    }

    public HashMap<String, Integer> showScoreboard() {
        HashMap<String, Integer> scoreboard = new HashMap<>();
        return scoreboard;
    }
}
