package it.polimi.ingsw.network.client;

import it.polimi.ingsw.exceptions.NotExistingPlayerException;
import it.polimi.ingsw.network.messages.GameEvent;

import java.io.IOException;
import java.rmi.Remote;

/**
 * Interface for handling client-side operations.
 * This interface defines methods for updating the client's state and getting the client's username.
 */
public interface ClientHandlerInterface extends Remote {

    /**
     * Updates the client's state based on a game event.
     * @param event the game event.
     * @param gameUpdate the game update associated with the event.
     * @throws IOException if a network error occurs.
     * @throws NotExistingPlayerException if the player does not exist.
     */
    void update(GameEvent event, Object gameUpdate) throws IOException,  NotExistingPlayerException;

    /**
     * Gets the username of the client.
     * @return the username of the client.
     * @throws IOException if a network error occurs.
     */
    String getUsername() throws IOException;
}