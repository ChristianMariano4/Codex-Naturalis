package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.alreadyExistingPlayer;
import it.polimi.ingsw.exceptions.alreadyFourPlayers;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final int gameId;
    private int numberOfPlayers;
    private final List<Player> listOfPlayers;
    private Table table;

    public Game(int gameId) {
        this.gameId = gameId;
        listOfPlayers = new ArrayList<Player>();
        numberOfPlayers = 0;
        table = new Table();
    }

    public int getGameId() {
        return this.gameId;
    }
    public void addPlayer(Player player) throws alreadyExistingPlayer, alreadyFourPlayers {
        if(!(listOfPlayers.contains(player))) {
            if(!(numberOfPlayers >= 4)) {
                this.listOfPlayers.add(player);
                numberOfPlayers++;
            } else {
                throw new alreadyFourPlayers();
            }
        } else {
            throw new alreadyExistingPlayer();
        }
    }
}
