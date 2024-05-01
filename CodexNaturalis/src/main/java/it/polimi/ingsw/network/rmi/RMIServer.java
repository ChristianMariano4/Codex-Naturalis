package it.polimi.ingsw.network.rmi;

//import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.GameHandler;
import it.polimi.ingsw.network.messages.EventWrapper;
import it.polimi.ingsw.network.socket.SocketClientHandler;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RMIServer extends Thread implements ServerRMIInterface {
    private final Map<Integer, GameHandler> gameHandlerMap;
    private final List<ClientRMIInterface> newClients;

    //final List<ClientRMIInterface> clients = new ArrayList<>(); //list of the client stubs
    //per passare dati a un altro thread in modo thread safe
    // final BlockingQueue<EventWrapper> updates = new LinkedBlockingQueue<>();
    //TODO: fix thread to update clients
//    Thread broadcastUpdateThread = new Thread(() -> {
//        System.out.println("Broadcasting thread started");
//        try{
//            while(true){
//                System.out.println("Waiting for updates");
//                EventWrapper update = updates.take();
//                synchronized (this.clients) {
//                    for (ClientRMIInterface client : this.clients) {
//                        client.showUpdate(update.getMessage());
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
        this.newClients = new ArrayList<>();
    }

    @Override
    public void connect(ClientRMIInterface client) throws RemoteException {
        System.out.println("Connecting client to the server...");
        synchronized (this.newClients) {
            this.newClients.add(client);
        }
        System.out.println("Client connected to the server successfully");
    }

    @Override
    public int createGame() {
        System.out.println("createGame request received");
        //sequential game id starting from 0
        int id = GameValues.numberOfGames;
        GameValues.numberOfGames++;
        gameHandlerMap.put(id, new GameHandler(id));

        System.out.println("Game created with id: " + gameHandlerMap.get(id).getGame().getGameId());
        return id;

//        synchronized (this.clients) {
//            for(ClientRMIInterface client : this.clients) {
//                try {
//                    client.update(currGame, GameEvent.GAME_CREATED);
//                } catch (RemoteException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }

//        try {
//            updates.put(new EventWrapper(currGame, GameEvent.GAME_CREATED));
//            System.out.println("1) " + updates.size());
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }

    }

    @Override
    public void addPlayerToGame(int gameId, String username) throws RemoteException {
        System.out.println("Request to add player to game " + gameId + " received");
        Game game = this.gameHandlerMap.get(gameId).getGame();
        try {

            Player player = new Player(username, game);
            game.addPlayer(player);
        } catch (InvalidConstructorDataException e) {
            throw new RuntimeException(e);
        } catch (AlreadyExistingPlayerException e) {
            throw new RuntimeException(e);
        } catch (AlreadyFourPlayersException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Player " + username + "added to game successfully");
        System.out.println("There are now " + game.getListOfPlayers().size() + " players in the game");
    }

    @Override
    public void inzializeGame(Game game) throws RemoteException {

    }

    @Override
    public void inizializeMarker(Player player, Marker marker) throws RemoteException {

    }

    @Override
    public void inizializePlayerCards(Player player) throws RemoteException {

    }

    @Override
    public void inizializePlayerMatrix(Player player, Side side) throws RemoteException {

    }

    @Override
    public void inizializePlayerHand(Player player) throws RemoteException {

    }

    public void addClientToGameHandler(Integer gameId, ClientRMIInterface client) throws RemoteException {
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

    private void startSocketServer(){
        try {
            ExecutorService executor = Executors.newCachedThreadPool();
            ServerSocket serverSocket = new ServerSocket(GameValues.SERVER_PORT+1);
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    SocketClientHandler clientHandler = new SocketClientHandler(new GameHandler(), new ObjectInputStream(clientSocket.getInputStream()), new ObjectOutputStream(clientSocket.getOutputStream()));
                    newClients.add(clientHandler);
                    executor.submit(clientHandler);
                } catch (Exception e) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Socket error");
            return;
        }
    }

    public void broadcastUpdate(EventWrapper ew){
        //TODO: usare un thread per fare questo per non rallentare
        for(ClientRMIInterface client: gameHandlerMap.get(ew.getMessage().getGameId()).getClients()){
            try {
                client.update(ew.getType(), ew.getMessage());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void run(){
        startRMIServer();
        startSocketServer();
    }

    public static void main(String[] args) {
        RMIServer server = new RMIServer(new HashMap<>());
        server.start();


        //register in registry
    }
}
