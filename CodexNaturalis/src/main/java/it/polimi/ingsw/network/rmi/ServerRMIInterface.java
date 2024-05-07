package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.NotAvailableMarkerException;
import it.polimi.ingsw.exceptions.NotExistingPlayerException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerHand;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardInfo;
import it.polimi.ingsw.network.maybeUseful.RemoteLock;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;
import java.util.List;
import java.util.concurrent.BlockingQueue;

//this is the skeleton (proxy of the server)
public interface ServerRMIInterface extends Remote {
    //method used from client to tell the server how to contact it
    void connect(ClientRMIInterface client) throws RemoteException;
    //expose methods that the client can call, i.e. those of the controller
    int createGame(ClientRMIInterface client) throws RemoteException;
    List<Integer> getAvailableGames() throws RemoteException;
    Game addPlayerToGame(int gameId, String username) throws RemoteException;
    int setReady(int gameId) throws RemoteException;
    void subscribe(ClientRMIInterface client, int gameId) throws RemoteException;
    RemoteLock getWaitingLock(int gameId) throws RemoteException;
    BlockingQueue<Boolean> getQueue(int gameId) throws RemoteException;
    CardInfo getCardInfo(Card card, int gameId) throws RemoteException;
    Player getPlayer(int gameId, String username) throws RemoteException, NotExistingPlayerException;
    void setMarker(Player player, int gameId, Marker marker) throws RemoteException, NotAvailableMarkerException;
}
