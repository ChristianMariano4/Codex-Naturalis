package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.client.AbstractClientHandler;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

//this is the skeleton (proxy of the server)
public interface ServerRMIInterface extends Remote {
    //method used from client to tell the server how to contact it
    void connect(AbstractClientHandler client) throws RemoteException;
    //expose methods that the client can call, i.e. those of the controller
    int createGame(ClientRMIInterface client) throws RemoteException;
    List<Integer> getAvailableGames() throws RemoteException;
    Game addPlayerToGame(int gameId, String username, ClientRMIInterface client) throws RemoteException;
    int setReady(int gameId) throws RemoteException, DeckIsEmptyException, NotExistingPlayerException, InterruptedException, NotEnoughPlayersException;
    void subscribe(ClientRMIInterface client, int gameId) throws RemoteException;
    BlockingQueue<Boolean> getQueue(int gameId) throws RemoteException;
    CardInfo getCardInfo(Card card, int gameId) throws RemoteException;
    PlayableCard getPlayableCardById(int gameId, int cardId) throws RemoteException;
    Player getPlayer(int gameId, String username) throws RemoteException, NotExistingPlayerException;
    void setMarker(Player player, int gameId, Marker marker) throws RemoteException, NotAvailableMarkerException, NotExistingPlayerException;
    void setStarterCardSide(int gameId, Player player,StarterCard starterCard, Side side) throws RemoteException, NotExistingPlayerException;
    public void playCard(int gameId, String username, PlayableCard card, PlayableCard otherCard, AngleOrientation orientation) throws RemoteException, NotExistingPlayerException, InvalidCardPositionException, RequirementsNotMetException, CardTypeMismatchException, AngleAlreadyLinkedException, NotTurnException;
    void setSecretObjectiveCard(int gameId, Player player, ObjectiveCard objectiveCard) throws RemoteException, NotExistingPlayerException;
    public void drawCard(int gameId, String username, CardType cardType, DrawPosition drawPosition) throws RemoteException, NotTurnException, NotExistingPlayerException, AlreadyThreeCardsInHandException, DeckIsEmptyException;
    public void endTurn(int gameId, String username) throws RemoteException, NotExistingPlayerException, CardTypeMismatchException;
    public boolean checkUsername(String username) throws RemoteException;

    public PlayableCard getOtherSideCard(int gameId, PlayableCard card) throws RemoteException;
    public GoldCard getOtherSideCard(int gameId, GoldCard card) throws RemoteException;

    public ResourceCard getOtherSideCard(int gameId, ResourceCard card) throws RemoteException;

    public StarterCard getOtherSideCard(int gameId, StarterCard card) throws RemoteException;

    public ObjectiveCard getOtherSideCard(int gameId, ObjectiveCard card) throws RemoteException;


    }

//TODO: game setup
//1 - create game
//2 - initialize game
//3 - each player receives a random starter card and choose the side to play
//4 - each player draws 2 resources card and 1 gold card --> DONE
//5 - discover 2 shared objective cards
//6 - each player chooses 1 secret objective card among 2
//7 - the game starts and the player order is shown -> black marker is assigned to first player

//TODO: game begins -> rounds
