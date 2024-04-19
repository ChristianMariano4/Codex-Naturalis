package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.exceptions.CardTypeMismatchException;
import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.Game;

import java.util.HashMap;
import java.util.Map;


public class Server {

    private final Map<Integer, Game> games;

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
    private void addGame(Game game)
    {
        games.put(game.getGameId(), game);
    }

    public static void main(String[] args) {
        Server server = new Server();
        Controller controller = new Controller(server);
        try {
            Game game = controller.createGame();
            server.addGame(game);
        } catch (InvalidConstructorDataException | CardTypeMismatchException | CardNotImportedException |
                 DeckIsEmptyException e) {
            throw new RuntimeException(e);
        }
    }

}
