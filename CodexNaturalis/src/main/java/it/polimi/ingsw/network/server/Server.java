package it.polimi.ingsw.network.server;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.client.ClientHandlerInterface;
import it.polimi.ingsw.network.client.ClientInfo;
import it.polimi.ingsw.network.messages.GameEvent;
import it.polimi.ingsw.network.messages.clientMessages.UserInputEvent;
import it.polimi.ingsw.network.messages.clientMessages.UserMessage;
import it.polimi.ingsw.network.messages.clientMessages.UserMessageWrapper;
import it.polimi.ingsw.network.rmi.ServerRMIInterface;
import it.polimi.ingsw.network.socket.SocketConnectionHandler;

import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.BlockingQueue;

public class Server extends Thread implements ServerRMIInterface {
    private final Map<Integer, GameHandler> gameHandlerMap;
    private final Map<ClientHandlerInterface, ClientInfo> clients = new HashMap(); //list of all client stubs with their last heartbeat and gameId
    private final GameSerializer gameSerializer;
    private final int recoverGames;


    public Server(Map<Integer, GameHandler> gameHandlerMap) {
        this.gameHandlerMap = gameHandlerMap;
        this.gameSerializer = new GameSerializer();
        this.recoverGames = 0;
    }

    @Override
    public void connect(ClientHandlerInterface client) throws IOException {
        System.out.println("Connecting client to the server...");
        synchronized (this.clients) {
            this.clients.put(client, new ClientInfo(System.currentTimeMillis()));
        }
        System.out.println("Client connected to the server successfully");

        new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(GameValues.HEARTBEAT_INTERVAL);
                    if(System.currentTimeMillis() - clients.get(client).getLastHeartbeat() > GameValues.HEARTBEAT_TIMEOUT) {
                        disconnect(client);
                        System.out.println("Client disconnected successfully");
                        break;
                    }
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public void disconnect(ClientHandlerInterface client) throws IOException {
        if(clients.get(client).getGameId() != -1) { //if the client is in a game
            gameHandlerMap.get(clients.get(client).getGameId()).setPlayerDisconnected(clients.get(client).getUsername());
            gameHandlerMap.get(clients.get(client).getGameId()).unsubscribe(clients.get(client).getUsername());
        }
        synchronized (this.clients) {
            gameHandlerMap.get(clients.get(client).getGameId()).removeClient(client);
            this.clients.remove(client);
        }
        System.out.println("Client disconnected. Clients size: "+ clients.size()); //debugging

    }

    @Override
    public void sendHeartbeat(long time, ClientHandlerInterface client) throws RemoteException {
        synchronized (this.clients){
            clients.get(client).setLastHeartbeat(time);
        }
    }

    @Override
    public int createGame(ClientHandlerInterface client, int numberOfPlayers) {
        System.out.println("createGame request received");
        //sequential game id starting from 0
        int id = GameValues.numberOfGames;
        GameValues.numberOfGames++;
        gameHandlerMap.put(id, new GameHandler(id, this, numberOfPlayers));
        //subscribe(gameSerializer, id); //subscribe game serializer to game events to handle game state saving
        return id;
    }

    @Override
    public List<Integer> getAvailableGames() throws RemoteException {
        return new ArrayList<>(gameHandlerMap.keySet().stream().filter(g -> gameHandlerMap.get(g).getIsOpen()).toList());
    }


    @Override
    public Game addPlayerToGame(int gameId, String username, ClientHandlerInterface client) throws RemoteException, GameAlreadyStartedException {
        try {
            addClientToGameHandler(gameId, client);
            clients.get(client).setGameId(gameId);
            clients.get(client).setUsername(username);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return this.gameHandlerMap.get(gameId).addPlayerToGame(gameId, username);
    }

    @Override
    public ArrayList<Integer> setReady(int gameId) throws IOException, DeckIsEmptyException, NotExistingPlayerException, InterruptedException, NotEnoughPlayersException {
        return this.gameHandlerMap.get(gameId).setReady();
    }

    @Override
    public void subscribe(ClientHandlerInterface client, int gameId) throws IOException, GameAlreadyStartedException {
        this.gameHandlerMap.get(gameId).subscribe(client, gameId);
    }

    private void subscribe(GameSerializer gameSerializer, int gameId) {
        this.gameHandlerMap.get(gameId).subscribe(gameSerializer);
    }


    @Override
    public BlockingQueue<Boolean> getQueue(int gameId) {

        return gameHandlerMap.get(gameId).getQueue();
    }


    public GameHandler getGameHandler(int gameId){
        return gameHandlerMap.get(gameId);
    }

    public void updateClient(ClientHandlerInterface client, GameEvent event, Object gameUpdate) throws IOException, InterruptedException, NotExistingPlayerException {
        Runnable updaterThread = () -> //this is needed for socket synchronization
                {
                    try {
                        client.update(event, gameUpdate);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } catch (NotExistingPlayerException e) {
                        throw new RuntimeException(e);
                    }
                };
        new Thread(updaterThread).start();

    }
    public void setUsername(String username) throws IOException, InvalidUsernameException {
        if(checkUsername(username))
            return;
        throw new InvalidUsernameException();

    }
    public boolean checkUsername(String username) throws IOException {
        if (username.equals(""))
            return false;
        for(ClientHandlerInterface client : clients.keySet())
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




    private void addClientToGameHandler(Integer gameId, ClientHandlerInterface client) throws RemoteException {
        this.gameHandlerMap.get(gameId).addClient(client);
    }

    private void startRMIServer() {
        //creating server stub
        //ServerRMIInterface server = this;

        final String serverName = "Server"; //name of the server used to register itself
        ServerRMIInterface stub = null;
        try {
            stub = (ServerRMIInterface) UnicastRemoteObject.exportObject(this, 0);
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

    public void update(ClientHandlerInterface ClientHandlerInterface, UserMessageWrapper message) throws RemoteException {
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
        try {
            System.out.println("Server started on " + InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
//        if(recoverGames == 1){
//            List<Game> games = gameSerializer.loadGamesState();
//            for(Game game : games)
//            {
//                gameHandlerMap.put(game.getGameId(), new GameHandler(game.getGameId(), this, game.getListOfPlayers().size()));
//
//            }
//        }

    /*    while(true)
        {
            try {
                Thread.sleep(1);
            }
            catch(Exception e)
            {

            }
        }*/

        //broadcastUpdateThread.start();
    }


}
