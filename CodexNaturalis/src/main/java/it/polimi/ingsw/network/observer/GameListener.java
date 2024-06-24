package it.polimi.ingsw.network.observer;

import it.polimi.ingsw.exceptions.NotExistingPlayerException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.client.ClientHandlerInterface;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.messages.GameEvent;

import java.io.IOException;
import java.rmi.RemoteException;
/**
 * The GameListener class implements the Listener interface for GameEvent.
 * It is responsible for handling game events and updating the client accordingly.
 * The class includes a reference to the client, the server, and the username of the client.
 * It also includes methods for updating the client based on game events and retrieving the client and username.
 */
public class GameListener implements Listener<GameEvent> {

    private final ClientHandlerInterface client;
    private final String username;
    private final Server server;

    /**
     * Constructs a new GameListener with the specified client and server.
     * @param client The client that this listener is associated with.
     * @param server The server that this listener is associated with.
     * @throws IOException If an I/O error occurs.
     */
    public GameListener(ClientHandlerInterface client, Server server) throws IOException {
        this.client = client;
        this.server = server;
        this.username = client.getUsername();
    }

    /**
     * Updates the client based on the received game event.
     * This method is called when a game event occurs. It updates the client by calling the server's updateClient method.
     * The updateClient method is called with the client, the game event, and the first argument from the args array.
     * If an InterruptedException, NotExistingPlayerException, or IOException occurs during the update, it throws a RuntimeException.
     * @param event The game event that occurred.
     * @param args Additional arguments that may be needed for the update.
     * @throws RuntimeException if an InterruptedException, NotExistingPlayerException, or IOException occurs during the update.
     */
    @Override
    public void update(GameEvent event, Object... args) {
        try {
            server.updateClient(client, event, args[0]);
        } catch (InterruptedException | NotExistingPlayerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the client associated with this GameListener.
     */
    public ClientHandlerInterface getClient() {
        return client;
    }

    /**
     * Returns the username of the client associated with this GameListener.
     */
    public String getUsername() {
        return username;
    }
}
