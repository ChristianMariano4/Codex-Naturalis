package it.polimi.ingsw.network.client;

import it.polimi.ingsw.exceptions.NotExistingPlayerException;
import it.polimi.ingsw.network.messages.GameEvent;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientHandlerInterface extends Remote {
    void update(GameEvent event, Object gameUpdate) throws IOException, InterruptedException, NotExistingPlayerException;
    String getUsername() throws IOException;
}
