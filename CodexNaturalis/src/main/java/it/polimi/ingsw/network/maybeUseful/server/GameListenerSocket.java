//package it.polimi.ingsw.network.server;
//
//import it.polimi.ingsw.model.Game;
//import it.polimi.ingsw.network.client.Client;
//import it.polimi.ingsw.network.messages.EventWrapper;
//import it.polimi.ingsw.network.messages.GameEvent;
//
//import java.net.Socket;
//
///**
// * This class represents the listener for changes occurring in the model
// */
//public class GameListenerSocket {
//
//    private final Server server;
//    private final Client client;
//    private final Game listenedGame;
//    private GameEvent lastEvent;
//
//    public GameListenerSocket(Server server, Client client, Game listenedGame){
//        this.server = server;
//        this.client = client;
//        this.listenedGame = listenedGame;
//    }
//
//    public void update(GameEvent event, Object... args){
//        EventWrapper ew = new EventWrapper(event, listenedGame);
//        Socket clientSocket = client.getClientSocket();
//        server.sendTo(clientSocket, ew);
//    }
//
//    private boolean isMyGameChanged(GameEvent event, Object changedObject){
//        //TODO return true if almost one thing in the game has changed, false otherwise
//        return true;
//    }
//
//    public Client getClient(){
//        return this.client;
//    }
//
//}
