package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Game;

import java.util.HashMap;
import java.util.Map;


public class Server {

    private Map<Integer, Game> games;

    public Server()
    {
        this.games = new HashMap<>();
    }

    /**
     * Returns the game with the specified gameId
     * @param gameId the id of the game
     * @return the game with the specified gameId
     */
    public Game getGameById(int gameId)
    {
        return games.get(gameId);
    }

    /**
     * Adds a game to the server
     * @param game the game to be added
     */
    public void addGame(Game game)
    {
        games.put(game.getGameId(), game);
    }

}
