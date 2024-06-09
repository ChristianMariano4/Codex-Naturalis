package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.client.ClientHandlerInterface;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

//this is the skeleton (proxy of the server)
public interface ServerRMIInterface extends Remote {
    //method used from client to tell the server how to contact it
    void connect(ClientHandlerInterface client) throws IOException;
    //expose methods that the client can call, i.e. those of the controller
    int createGame(ClientHandlerInterface client, int numberOfPlayers) throws IOException, NotExistingPlayerException, InterruptedException;
    ArrayList<Game> getAvailableGames() throws RemoteException;
    Game addPlayerToGame(int gameId, String username, ClientHandlerInterface client) throws RemoteException, GameAlreadyStartedException;

    ArrayList<Integer> setReady(int gameId, String username) throws IOException, DeckIsEmptyException, NotExistingPlayerException, InterruptedException, NotEnoughPlayersException;
    HashMap<String, Boolean> getReadyStatus(int gameId) throws RemoteException;
    void subscribe(ClientHandlerInterface client, int gameId) throws IOException, GameAlreadyStartedException, GameNotFoundException;
    CardInfo getCardInfo(Card card, int gameId) throws RemoteException;
    PlayableCard getPlayableCardById(int gameId, int cardId) throws RemoteException;
    Player getPlayer(int gameId, String username) throws RemoteException, NotExistingPlayerException;
    void setMarker(Player player, int gameId, Marker marker) throws RemoteException, NotAvailableMarkerException, NotExistingPlayerException;
    void setStarterCardSide(int gameId, Player player,StarterCard starterCard, Side side) throws RemoteException, NotExistingPlayerException;
    Player playCard(int gameId, String username, PlayableCard card, PlayableCard otherCard, AngleOrientation orientation) throws RemoteException, NotExistingPlayerException, InvalidCardPositionException, RequirementsNotMetException, CardTypeMismatchException, AngleAlreadyLinkedException, NotTurnException;
    void setSecretObjectiveCard(int gameId, Player player, ObjectiveCard objectiveCard) throws RemoteException, NotExistingPlayerException;
    void drawCard(int gameId, String username, CardType cardType, DrawPosition drawPosition) throws RemoteException, NotTurnException, NotExistingPlayerException, AlreadyThreeCardsInHandException, DeckIsEmptyException;
    void endTurn(int gameId, String username) throws RemoteException, NotExistingPlayerException, CardTypeMismatchException;
    void setUsername(String username) throws IOException, InvalidUsernameException;

    PlayableCard getOtherSideCard(int gameId, PlayableCard card) throws RemoteException;
    GoldCard getOtherSideCard(int gameId, GoldCard card) throws RemoteException;

    ResourceCard getOtherSideCard(int gameId, ResourceCard card) throws RemoteException;

    StarterCard getOtherSideCard(int gameId, StarterCard card) throws RemoteException;

    ObjectiveCard getOtherSideCard(int gameId, ObjectiveCard card) throws RemoteException;

    void sendHeartbeat(long time, ClientHandlerInterface client) throws RemoteException;


    Game reconnectPlayerToGame(int gameId, String username, ClientHandlerInterface client) throws RemoteException, NotExistingPlayerException;

    void quitGame(int gameId, ClientHandlerInterface client) throws RemoteException, NotExistingPlayerException;

    void sendChatMessage(int gameId, String message, String username) throws RemoteException;
}
