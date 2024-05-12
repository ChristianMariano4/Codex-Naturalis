//package it.polimi.ingsw.network.socket;
//
//import it.polimi.ingsw.enumerations.Marker;
//import it.polimi.ingsw.enumerations.Side;
//import it.polimi.ingsw.exceptions.AlreadyExistingPlayerException;
//import it.polimi.ingsw.exceptions.AlreadyFourPlayersException;
//import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
//import it.polimi.ingsw.model.Game;
//import it.polimi.ingsw.model.GameValues;
//import it.polimi.ingsw.model.Player;
//import it.polimi.ingsw.network.rmi.GameHandler;
//import it.polimi.ingsw.network.messages.EventWrapper;
//import it.polimi.ingsw.network.messages.GameEvent;
//import it.polimi.ingsw.network.rmi.ClientRMIInterface;
//import it.polimi.ingsw.network.Server;
//import it.polimi.ingsw.network.rmi.ServerRMIInterface;
//
//import java.io.ObjectInputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.rmi.RemoteException;
//import java.rmi.registry.LocateRegistry;
//import java.rmi.registry.Registry;
//import java.rmi.server.UnicastRemoteObject;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.LinkedBlockingQueue;
//
//public class SocketServer extends Thread{
//    private final Map<Integer, GameHandler> gameHandlerMap;
//    private final List<ClientRMIInterface> newClients;
//
//    final List<ClientRMIInterface> clients = new ArrayList<>(); //list of the client stubs
//    //    per passare dati a un altro thread in modo thread safe
//    final BlockingQueue<EventWrapper> updates = new LinkedBlockingQueue<>();
//    //    TODO: fix thread to update clients
//    Thread broadcastUpdateThread = new Thread(() -> {
//        System.out.println("Broadcasting thread started");
//        try{
//            while(true){
//                System.out.println("Waiting for updates");
//                EventWrapper update = updates.take();
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
//
//
//    public SocketServer(Map<Integer, GameHandler> gameHandlerMap) {
//        this.gameHandlerMap = gameHandlerMap;
//        this.newClients = new ArrayList<>();
//    }
//
//    @Override
//    public void connect(ClientRMIInterface client) {
//        System.out.println("Connecting client to the server...");
//        synchronized (this.newClients) {
//            this.newClients.add(client);
//        }
//        System.out.println("Client connected to the server successfully");
//    }
//
//    @Override
//    public int createGame() {
//        //TODO
//
//        per aggiornare tutti i client
//        synchronized (this.clients) {
//            for(ClientRMIInterface client : this.clients) {
//                try {
//                    client.update(currGame, GameEvent.GAME_CREATED);
//                } catch (RemoteException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }
//        return 1; //momentaneo per non avere errore
//    }
//
//    @Override
//    public void addPlayerToGame(int gameId, String username) throws RemoteException {
//        //TODO
//    }
//
//    @Override
//    public void inzializeGame(Game game) throws RemoteException {
//
//    }
//
//    @Override
//    public void inizializeMarker(Player player, Marker marker) throws RemoteException {
//
//    }
//
//    @Override
//    public void inizializePlayerCards(Player player) throws RemoteException {
//
//    }
//
//    @Override
//    public void inizializePlayerMatrix(Player player, Side side) throws RemoteException {
//
//    }
//
//    @Override
//    public void inizializePlayerHand(Player player) throws RemoteException {
//
//    }
//
//    public void addClientToGameHandler(Integer gameId, ClientRMIInterface client) throws RemoteException {
//
//    }
//
//
//    private void startSocketServer(){
//        try {
//            ExecutorService executor = Executors.newCachedThreadPool();
//            ServerSocket serverSocket = new ServerSocket(GameValues.SERVER_PORT+1);
//            while (true) {
//                try {
//                    Socket clientSocket = serverSocket.accept();
//                    SocketClientHandler clientHandler = new SocketClientHandler(new GameHandler(), new ObjectInputStream(clientSocket.getInputStream()), new ObjectOutputStream(clientSocket.getOutputStream()));
//                    newClients.add(clientHandler);
//                    executor.submit(clientHandler);
//                } catch (Exception e) {
//                    break;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("Socket error");
//            return;
//        }
//    }
//
////    public void broadcastUpdate(EventWrapper ew){
////        //TODO: usare un thread per fare questo per non rallentare
////        for(ClientRMIInterface client: gameHandlerMap.get(ew.getMessage().getGameId()).getClients()){
////            try {
////                client.update(ew.getType(), ew.getMessage());
////            } catch (RemoteException e) {
////                throw new RuntimeException(e);
////            }
////        }
////    }
//
//    public void run(){
//        startSocketServer();
//        broadcastUpdateThread.start();
//    }
//
//    public static void main(String[] args) {
//        SocketServer server = new SocketServer(new HashMap<>());
//        server.start();
//
//        //register in registry
//    }
//}
