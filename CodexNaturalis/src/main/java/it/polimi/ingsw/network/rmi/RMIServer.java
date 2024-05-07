package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.model.PlayerHand;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.CardInfo;
import it.polimi.ingsw.network.maybeUseful.RemoteLock;
import it.polimi.ingsw.network.messages.GameEvent;
import it.polimi.ingsw.network.messages.userMessages.UserInputEvent;
import it.polimi.ingsw.network.messages.userMessages.UserMessage;
import it.polimi.ingsw.network.messages.userMessages.UserMessageWrapper;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class RMIServer extends Thread implements ServerRMIInterface {
    private final Map<Integer, GameHandler> gameHandlerMap;
    private final List<ClientRMIInterface> clients = new ArrayList<>(); //list of all client stubs+

//    queue used to pass data to another thread in a thread safe way
     //final BlockingQueue<EventWrapper> updates = new LinkedBlockingQueue<>();
//    TODO: fix thread to update clients
//    Thread broadcastUpdateThread = new Thread(() -> {
//        System.out.println("Broadcasting thread started");
//        try{
//            while(true){
//                System.out.println("Waiting for updates");
//                EventWrapper update = updates.take();
//                System.out.println("We are after take method :)))))))");
//                System.out.println();
//                synchronized (this.clients) {
//                    for (ClientRMIInterface client : this.clients) {
//                        client.update(update.getType(), update.getMessage());
//                    }
//                }
//            }
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } catch (RemoteException e) {
//            throw new RuntimeException(e);
//        }
//    });


    public RMIServer(Map<Integer, GameHandler> gameHandlerMap) {
        this.gameHandlerMap = gameHandlerMap;
    }

    @Override
    public void connect(ClientRMIInterface client) throws RemoteException {
        System.out.println("Connecting client to the server...");
        synchronized (this.clients) {
            this.clients.add(client);
        }
        System.out.println("Client connected to the server successfully");
    }

    @Override
    public int createGame(ClientRMIInterface client) {
        System.out.println("createGame request received");
        //sequential game id starting from 0
        int id = GameValues.numberOfGames;
        GameValues.numberOfGames++;
        gameHandlerMap.put(id, new GameHandler(id, this));

        try {
            addClientToGameHandler(id, client);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

//        try {
//            System.out.println("line 76) " + updates.size());
//            updates.put(new EventWrapper(GameEvent.GAME_CREATED, gameHandlerMap.get(id).getGame()));
//            System.out.println("line 78) " + updates.size());
//        } catch (Exception e) {
//            System.err.println("Error in adding game to the queue");
//            throw new RuntimeException(e);
//        }

        return id;
    }

    @Override
    public List<Integer> getAvailableGames() throws RemoteException {
        return new ArrayList<>(gameHandlerMap.keySet());
    }

    @Override
    public Game addPlayerToGame(int gameId, String username) throws RemoteException {
        return this.gameHandlerMap.get(gameId).addPlayerToGame(gameId, username);
    }

    @Override
    public int setReady(int gameId, String username) throws RemoteException {
        return this.gameHandlerMap.get(gameId).setReady(gameId, username);

        //TODO: inizializzazione del gioco se tutti i giocatori sono pronti
    }

    @Override
    public void subscribe(ClientRMIInterface client, int gameId) throws RemoteException {
        this.gameHandlerMap.get(gameId).subscribe(client, gameId);
    }

    @Override
    public RemoteLock getWaitingLock(int gameId) {
        return gameHandlerMap.get(gameId).getWaitingLock();
    }

    @Override
    public BlockingQueue<Boolean> getQueue(int gameId) {

        return gameHandlerMap.get(gameId).getQueue();
    }


    public GameHandler getGameHandler(int gameId){
        return gameHandlerMap.get(gameId);
    }

    public void updateClient(ClientRMIInterface client, GameEvent event, Game game) throws RemoteException{
        client.update(event, game);
    }



    private void addClientToGameHandler(Integer gameId, ClientRMIInterface client) throws RemoteException {
        this.gameHandlerMap.get(gameId).addClient(client);
    }

    private void startRMIServer() {
        //creating server stub
        ServerRMIInterface server = new RMIServer(new HashMap<>());

        final String serverName = "Server"; //name of the server used to register itself
        ServerRMIInterface stub = null;
        try {
            stub = (ServerRMIInterface) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.createRegistry(1234);
            registry.rebind(serverName, stub);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Server bound");
    }

    public void update(ClientRMIInterface clientRMIInterface, UserMessageWrapper message) throws RemoteException {
        //RMIClient client = this.getClient(message.getMessage().getUsername());

        UserInputEvent userInputEvent = message.getType();
        UserMessage userMessage = message.getMessage();

    }

//    public RMIClient getClient(String username){
//        return this.clients.stream().filter(c -> c.getUsername().equals(username)).findFirst().orElse(null);
//    }

    public CardInfo getCardInfo(Card card, int gameId) {
        return gameHandlerMap.get(gameId).getController().getCardHandler().getCardInfo(card);
    }

    public void run(){
        startRMIServer();
        //broadcastUpdateThread.start();
    }


}
