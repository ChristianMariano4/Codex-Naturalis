package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;

//this is the skeleton (proxy of the server)
public interface ServerRMIInterface extends Remote {
    //method used from client to tell the server how to contact it
    void connect(ClientRMIInterface client) throws RemoteException;

    //expose methods that the client can call, i.e. those of the controller
    int createGame() throws RemoteException;

    void addPlayerToGame(int gameId, String username) throws RemoteException;

    void inzializeGame(Game game) throws RemoteException;
    void inizializeMarker(Player player, Marker marker) throws RemoteException;
    void inizializePlayerCards(Player player) throws RemoteException;
    void inizializePlayerMatrix(Player player, Side side) throws RemoteException;
    void inizializePlayerHand(Player player) throws RemoteException;

    void addClientToGameHandler(Integer gameId, ClientRMIInterface client) throws RemoteException;
}
