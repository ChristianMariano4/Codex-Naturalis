package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyExistingPlayer;
import it.polimi.ingsw.exceptions.AlreadyFourPlayers;


import java.util.ArrayList;

import static it.polimi.ingsw.model.GameValues.MAX_PLAYER_NUMBER;

public class Game {
    private final int gameId;
    private int numberOfPlayers;
    private final ArrayList<Player> listOfPlayers;
    private final TableTop tableTop;


    //controller has to create the drawing field before creating the game to create drawingField
    public Game(int gameId, DrawingField drawingField) {
        this.gameId = gameId;
        this.listOfPlayers = new ArrayList<Player>();
        this.numberOfPlayers = 0;
        this.tableTop = new TableTop(drawingField);
    }

    public int getGameId() {
        return this.gameId;
    }
    public void addPlayer(Player player) throws AlreadyExistingPlayer, AlreadyFourPlayers {
        if(!(listOfPlayers.contains(player))) {
            if(!(numberOfPlayers >= MAX_PLAYER_NUMBER)) {
                this.listOfPlayers.add(player);
                numberOfPlayers++;
                tableTop.addPlayerField(player);
            } else {
                throw new AlreadyFourPlayers();
            }
        } else {
            throw new AlreadyExistingPlayer();
        }
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public ArrayList<Player> getListOfPlayers()
    {
        return new ArrayList<Player>(listOfPlayers);
    }

    public TableTop getTableTop() {
        return tableTop;
    }
}
