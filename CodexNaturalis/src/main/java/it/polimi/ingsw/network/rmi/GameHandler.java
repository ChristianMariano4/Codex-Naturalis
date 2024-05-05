package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.EventManager;
import it.polimi.ingsw.network.GameListener;
import it.polimi.ingsw.network.Listener;
import it.polimi.ingsw.network.messages.GameEvent;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class GameHandler {
    private Game game;
    private Controller controller;
    private int readyPlayers = 0;
    private List<ClientRMIInterface> clients; //list of the clients related to this game
    private final EventManager eventManager;
    private final RMIServer server;

    public GameHandler(int gameId, RMIServer server){
        this.server = server;
        this.eventManager = new EventManager();
        this.controller = new Controller(eventManager);
        try {
            this.game = controller.createGame(gameId);
        } catch (InvalidConstructorDataException e) {
            throw new RuntimeException(e);
        } catch (CardNotImportedException e) {
            throw new RuntimeException(e);
        } catch (CardTypeMismatchException e) {
            throw new RuntimeException(e);
        } catch (DeckIsEmptyException e) {
            throw new RuntimeException(e);
        }
        this.clients = new ArrayList<>();

    }

    public void addPlayerToGame(int gameId, String username) throws RemoteException {
        System.out.println("Request to add player to game " + gameId + " received");
        try {
            this.controller.addPlayerToGame(gameId, username);
        } catch (AlreadyExistingPlayerException e) {
            throw new RuntimeException(e);
        } catch (AlreadyFourPlayersException e) {
            //TODO: avvisare il client che non può entrare in questo game
            throw new RuntimeException(e);
        }
        System.out.println("Player " + username + " added to game successfully");
        System.out.println("There are now " + this.controller.getGame().getNumberOfPlayers() + " players in the game");

//        try {
//            updates.put(new EventWrapper(GameEvent.NEW_PLAYER, gameHandlerMap.get(gameId).getGame()));
//            System.out.println("line 107) " + updates.size());
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

    }

    public void addClient(ClientRMIInterface client) {
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

    public int setReady(int gameId, String username) throws RemoteException{
        readyPlayers++;
        if(readyPlayers == controller.getGame().getNumberOfPlayers()){
            try {
                controller.inzializeGame(this.game);
            } catch (CardTypeMismatchException e) {
                throw new RuntimeException(e);
            } catch (InvalidConstructorDataException e) {
                throw new RuntimeException(e);
            } catch (CardNotImportedException e) {
                throw new RuntimeException(e);
            } catch (DeckIsEmptyException e) {
                throw new RuntimeException(e);
            } catch (AlreadyExistingPlayerException e) {
                throw new RuntimeException(e);
            } catch (AlreadyFourPlayersException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (UnlinkedCardException e) {
                throw new RuntimeException(e);
            } catch (AlreadyThreeCardsInHandException e) {
                throw new RuntimeException(e);
            }
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

}
