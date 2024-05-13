package it.polimi.ingsw.network;

import it.polimi.ingsw.exceptions.NotExistingPlayerException;
import it.polimi.ingsw.network.messages.GameEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AbstractClientHandler extends Remote {
    void update(GameEvent event, Object gameUpdate) throws RemoteException, InterruptedException, NotExistingPlayerException;
    String getUsername() throws RemoteException;
}
