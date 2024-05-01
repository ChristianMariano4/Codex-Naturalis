package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.messages.GameEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

//used from the server to communicate with the client
//this interface is implemented by the client remote object
//this is the stub (proxy of the client)
public interface ClientRMIInterface extends Remote {
    void update(GameEvent event, Game game) throws RemoteException;
}
