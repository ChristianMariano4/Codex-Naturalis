package it.polimi.ingsw.network;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.client.AbstractClientHandler;
import it.polimi.ingsw.network.messages.GameEvent;
import it.polimi.ingsw.network.messages.userMessages.UserInputEvent;
import it.polimi.ingsw.network.messages.userMessages.UserMessage;
import it.polimi.ingsw.network.messages.userMessages.UserMessageWrapper;
import it.polimi.ingsw.network.rmi.ClientRMIInterface;
import it.polimi.ingsw.network.rmi.GameHandler;
import it.polimi.ingsw.network.rmi.ServerRMIInterface;
import it.polimi.ingsw.network.socket.SocketConnectionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class Server extends Thread implements ServerRMIInterface {
    private final Map<Integer, GameHandler> gameHandlerMap;
    private final List<AbstractClientHandler> clients = new ArrayList<>(); //list of all client stubs+

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


    public Server(Map<Integer, GameHandler> gameHandlerMap) {
        this.gameHandlerMap = gameHandlerMap;
    }

    @Override
    public void connect(AbstractClientHandler client) throws RemoteException {
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
    public Game addPlayerToGame(int gameId, String username, ClientRMIInterface client) throws RemoteException {
        try {
            addClientToGameHandler(gameId, client);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return this.gameHandlerMap.get(gameId).addPlayerToGame(gameId, username);
    }

    @Override
    public int setReady(int gameId) throws RemoteException, DeckIsEmptyException, NotExistingPlayerException, InterruptedException, NotEnoughPlayersException {
        return this.gameHandlerMap.get(gameId).setReady();
    }

    @Override
    public void subscribe(ClientRMIInterface client, int gameId) throws RemoteException {
        this.gameHandlerMap.get(gameId).subscribe(client, gameId);
    }


    @Override
    public BlockingQueue<Boolean> getQueue(int gameId) {

        return gameHandlerMap.get(gameId).getQueue();
    }


    public GameHandler getGameHandler(int gameId){
        return gameHandlerMap.get(gameId);
    }

    public void updateClient(ClientRMIInterface client, GameEvent event, Object gameUpdate) throws RemoteException, InterruptedException, NotExistingPlayerException {
        client.update(event, gameUpdate);
    }
    public boolean checkUsername(String username) throws RemoteException{
        for(AbstractClientHandler client : clients)
        {
            if(client.getUsername() == null)
                continue;
            if(client.getUsername().equals(username))
                return false;
        }
        return true;
    }

    public void setSecretObjectiveCard(int gameId, Player player, ObjectiveCard objectiveCard) throws NotExistingPlayerException {
        GameHandler game = gameHandlerMap.get(gameId);
        game.setSecretObjectiveCard(player, objectiveCard);
    }




    private void addClientToGameHandler(Integer gameId, ClientRMIInterface client) throws RemoteException {
        this.gameHandlerMap.get(gameId).addClient(client);
    }

    private void startRMIServer() {
        //creating server stub
        ServerRMIInterface server = new Server(new HashMap<>());

        final String serverName = "Server"; //name of the server used to register itself
        ServerRMIInterface stub = null;
        try {
            stub = (ServerRMIInterface) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.createRegistry(1234);
            registry.rebind(serverName, stub);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println("RMI Server ready");
    }
    private void startSocketServer()
    {

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(GameValues.SOCKET_SERVER_PORT);
        } catch (IOException e) {
            System.err.println(e.getMessage()); // porta non disponibile
            return;
        }
        System.out.println("Socket server ready");

        SocketConnectionHandler socketConnectionHandler = new SocketConnectionHandler(serverSocket,this);
        Thread socketConnectionHandlerThread = new Thread(socketConnectionHandler);
        socketConnectionHandlerThread.start();

    }

    public void update(ClientRMIInterface clientRMIInterface, UserMessageWrapper message) throws RemoteException {
        //Client client = this.getClient(message.getMessage().getUsername());

        UserInputEvent userInputEvent = message.getType();
        UserMessage userMessage = message.getMessage();

    }

    public Player getPlayer(int gameId, String username) throws RemoteException, NotExistingPlayerException {
        return this.gameHandlerMap.get(gameId).getPlayer(username);
    }

    public CardInfo getCardInfo(Card card, int gameId) {
        return gameHandlerMap.get(gameId).getController().getCardHandler().getCardInfo(card);
    }

    public void setMarker(Player player, int gameId, Marker marker) throws NotAvailableMarkerException, NotExistingPlayerException {
        gameHandlerMap.get(gameId).setMarker(player, marker);
    }


    public void setStarterCardSide(int gameId, Player player, StarterCard starterCard, Side side) throws RemoteException, NotExistingPlayerException {
        gameHandlerMap.get(gameId).setStarterCardSide(player, starterCard, side);
    }

    public void playCard(int gameId, String username, PlayableCard card, PlayableCard otherCard, AngleOrientation orientation) throws RemoteException, NotExistingPlayerException, InvalidCardPositionException, RequirementsNotMetException, CardTypeMismatchException, AngleAlreadyLinkedException, NotTurnException {
        GameHandler game = gameHandlerMap.get(gameId);
        if(game.getGame().getCurrentPlayer().equals(game.getPlayer(username)))
        {
            game.playCard(game.getPlayer(username), card, otherCard, orientation);
            return;
        }
        throw new NotTurnException();
    }
    public void drawCard(int gameId, String username, CardType cardType, DrawPosition drawPosition) throws RemoteException, NotTurnException, NotExistingPlayerException, AlreadyThreeCardsInHandException, DeckIsEmptyException {
        GameHandler game = gameHandlerMap.get(gameId);
        if(game.getGame().getCurrentPlayer().equals(game.getPlayer(username)))
        {
           game.getController().drawCard(game.getPlayer(username), cardType, drawPosition);
           return;
        }
        throw new NotTurnException();
    }
    public void endTurn(int gameId, String username) throws RemoteException, NotExistingPlayerException, CardTypeMismatchException {
        GameHandler game = gameHandlerMap.get(gameId);
        game.turnEvent(username);
    }


    public PlayableCard getPlayableCardById(int gameId, int cardId) throws RemoteException
    {
        return gameHandlerMap.get(gameId).getController().getCardHandler().getPlayableCardById(cardId);
    }

    /**
     * Get the other side of the selected PlayableCard
     * @param card is the selected PlayableCard
     * @return the other side of the selected PlayableCard
     * @throws CardNotFoundException if the card selected doesn't exist
     */

    public PlayableCard getOtherSideCard(int gameId, PlayableCard card) throws RemoteException {
        return gameHandlerMap.get(gameId).getController().getCardHandler().getOtherSideCard(card);
    }

    /**
     * Get the other side of the selected GoldCard
     * @param card is the selected GoldCard
     * @return the other side of the selected GoldCard
     */
    public GoldCard getOtherSideCard(int gameId, GoldCard card) throws RemoteException
    {
        return gameHandlerMap.get(gameId).getController().getCardHandler().getOtherSideCard(card);

    }

    /**
     * Get the other side of the selected ResourceCard
     * @param card is the selected ResourceCard
     * @return the other side of the selected ResourceCard
     */
    public ResourceCard getOtherSideCard(int gameId, ResourceCard card) throws RemoteException
    {
        return gameHandlerMap.get(gameId).getController().getCardHandler().getOtherSideCard(card);

    }

    /**
     * Get the other side of the selected StarterCard
     * @param card is the selected StarterCard
     * @return the other side of the selected StarterCard
     */
    public StarterCard getOtherSideCard(int gameId, StarterCard card) throws RemoteException
    {
        return gameHandlerMap.get(gameId).getController().getCardHandler().getOtherSideCard(card);

    }

    /**
     * Get the other side of the selected ObjectiveCard
     * @param card is the selected ObjectiveCard
     * @return the other side of the selected ObjectiveCard
     */
    public ObjectiveCard getOtherSideCard(int gameId, ObjectiveCard card) throws RemoteException
    {
        return gameHandlerMap.get(gameId).getController().getCardHandler().getOtherSideCard(card);

    }

    public void run(){
        startSocketServer();
        startRMIServer();

        //broadcastUpdateThread.start();
    }


}
