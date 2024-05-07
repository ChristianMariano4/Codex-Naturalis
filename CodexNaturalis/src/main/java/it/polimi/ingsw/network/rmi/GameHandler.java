package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerHand;
import it.polimi.ingsw.network.EventManager;
import it.polimi.ingsw.network.GameListener;
import it.polimi.ingsw.network.Listener;
import it.polimi.ingsw.network.maybeUseful.RemoteLock;
import it.polimi.ingsw.network.messages.GameEvent;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameHandler {
    //TODO: check se il client ha i permessi giusti
    private Game game;
    private Controller controller;
    private int readyPlayers = 0;
    private List<ClientRMIInterface> clients; //list of the clients related to this game
    private final EventManager eventManager;
    private final RMIServer server;
    private final RemoteLock waitingLock = new RemoteLock();
    private final BlockingQueue<Boolean> threadUpdates = new LinkedBlockingQueue<>();


    public GameHandler(int gameId, RMIServer server){
        this.server = server;
        this.eventManager = new EventManager();
        this.controller = new Controller(eventManager);
        try {
            this.game = controller.createGame(gameId);
        } catch (InvalidConstructorDataException | CardNotImportedException | CardTypeMismatchException |
                 DeckIsEmptyException e) {
            throw new RuntimeException(e);
        }
        this.clients = new ArrayList<>();

    }

    public Game addPlayerToGame(int gameId, String username) throws RemoteException {
        System.out.println("Request to add player to game " + gameId + " received");
        Game game;
        try {
            game = this.controller.addPlayerToGame(gameId, username);
        } catch (AlreadyExistingPlayerException e) {
            throw new RuntimeException(e);
        } catch (AlreadyFourPlayersException e) {
            //TODO: avvisare il client che non può entrare in questo game
            throw new RuntimeException(e);
        }
        eventManager.notify(GameEvent.NEW_PLAYER, this.game);
        return game;
    }

    public void addClient(ClientRMIInterface client) throws RemoteException {
        clients.add(client);
    }

    public void notifyUpdate(GameEvent event, Game game){
        for(ClientRMIInterface client : clients){
            try {
                client.update(event, game);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int setReady(int gameId, String username) throws RemoteException {
        readyPlayers++;
        if(readyPlayers == controller.getGame().getNumberOfPlayers()){
            try {
                controller.inzializeGame(this.game);
            } catch (CardTypeMismatchException | InvalidConstructorDataException | CardNotImportedException |
                     DeckIsEmptyException | AlreadyExistingPlayerException | AlreadyFourPlayersException | IOException |
                     UnlinkedCardException | AlreadyThreeCardsInHandException e) {
                throw new RuntimeException(e);
            }
        }

        if(readyPlayers >= controller.getGame().getNumberOfPlayers()){
            this.threadUpdates.add(true);
            eventManager.notify(GameEvent.GAME_BEGIN, controller.getGame());
        }
        return readyPlayers;
    }

    public void subscribe(ClientRMIInterface client, int gameId) throws RemoteException {
        eventManager.subscribe(GameEvent.class, new GameListener(client, server));
    }


    public List<ClientRMIInterface> getClients() {
        return clients;
    }

    public void addReadyPlayer(){
        readyPlayers++;
    }

    public int getReadyPlayers(){
        return readyPlayers;
    }


    public void updateClient(ClientRMIInterface client, GameEvent event, Game game){
        try {
            server.updateClient(client, event, game);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void run(){
        //TODO: start listening for user input and then invoke a method in the controller
        //each instance of gameHandler is a listener, i.e. is subscribed in the Listeners HashMap in EventManager
    }

    public RemoteLock getWaitingLock()
    {
        return waitingLock;
    }

    public BlockingQueue<Boolean> getQueue()
    {
        return threadUpdates;
    }

}
