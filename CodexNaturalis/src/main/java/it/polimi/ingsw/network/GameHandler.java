package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.exceptions.CardTypeMismatchException;
import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.messages.GameEvent;
import it.polimi.ingsw.network.rmi.ClientRMIInterface;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class GameHandler extends Thread{
    private Game game;
    private Controller controller;

    private List<ClientRMIInterface> clients;
    public GameHandler(Game game) throws CardTypeMismatchException, InvalidConstructorDataException, CardNotImportedException, DeckIsEmptyException {
        this.controller = new Controller();
        this.game = game;
        this.clients = new ArrayList<>();
    }

    //constructor used when the game isn't created yet
    public GameHandler(int gameId){
        this.controller = new Controller();
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

    public GameHandler(){
        this.controller = new Controller();
        this.game = null;
        this.clients = new ArrayList<>();
    }

    public Game getGame() {
        return game;
    }
    public void addClient(ClientRMIInterface client){
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
        this.game = game;
    }

    public Controller getController(){
        return controller;
    }

    public List<ClientRMIInterface> getClients() {
        return clients;
    }

    public void update(){
        //TODO
        //controller.update();
    }

    public void run(){
        //TODO: start listening for user input and then invoke a method in the controller
        //each instance of gameHandler is a listener, i.e. is subscribed in the Listeners HashMap in EventManager
    }

}
