package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

//this is the skeleton (proxy of the server)
public interface ServerRMIInterface extends Remote {
    //method used from client to tell the server how to contact it
    void connect(ClientRMIInterface client) throws RemoteException;

    //expose methods that the client can call, i.e. those of the controller
    int createGame(ClientRMIInterface client) throws RemoteException;

    List<Integer> getAvailableGames() throws RemoteException;
    public void addPlayerToGame(int gameId, String username) throws RemoteException;
    public int setReady(int gameId, String username) throws RemoteException;
    void subscribe(ClientRMIInterface client, int gameId) throws RemoteException;

}
