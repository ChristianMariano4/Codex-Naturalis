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
import it.polimi.ingsw.network.rmi.ServerRMIInterface;
import it.polimi.ingsw.network.socket.SocketConnectionHandler;

import java.io.IOException;
import java.net.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * The Server class is responsible for managing the game server.
 * It extends Thread and implements the ServerRMIInterface.
 */
public class Server extends Thread implements ServerRMIInterface {
    private final Map<Integer, GameHandler> gameHandlerMap;
    private final Map<ClientHandlerInterface, ClientInfo> clients = new HashMap<>(); //list of all client stubs with their last heartbeat and gameId

    /**
     * Constructor for the Server class.
     * @param gameHandlerMap The map of game handlers.
     */
    public Server(Map<Integer, GameHandler> gameHandlerMap) {
        this.gameHandlerMap = gameHandlerMap;
    }

    /**
     * Connects a client to the server.
     * @param client The client to be connected.
     * @throws IOException If an I/O error occurs.
     */
    @Override
    public void connect(ClientHandlerInterface client) throws IOException {
        synchronized (this.clients) {
            this.clients.put(client, new ClientInfo(System.currentTimeMillis()));
        }
        System.out.println("Client connected to the server successfully. Clients size: "+ clients.size());

        new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(GameValues.HEARTBEAT_INTERVAL);
                    if(System.currentTimeMillis() - clients.get(client).getLastHeartbeat() > GameValues.HEARTBEAT_TIMEOUT) {
                        disconnect(client);
                        System.out.println("Client disconnected. Clients size: "+ clients.size());
                        break;
                    }
                } catch (InterruptedException | IOException | NotExistingPlayerException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**
     * Disconnects a client from the server.
     * @param client The client to be disconnected.
     * @throws IOException If an I/O error occurs.
     * @throws NotExistingPlayerException If the player does not exist.
     */
    public void disconnect(ClientHandlerInterface client) throws IOException, NotExistingPlayerException {
        try {
            if (clients.get(client).getGameId() != -1) {
                if (gameHandlerMap.get(clients.get(client).getGameId()).getGame().getGameStatus().getStatusNumber() < GameStatus.GAME_STARTED.getStatusNumber() &&
                        gameHandlerMap.get(clients.get(client).getGameId()).getGame().getGameStatus().getStatusNumber() >= GameStatus.ALL_PLAYERS_READY.getStatusNumber()) {
                    gameHandlerMap.get(clients.get(client).getGameId()).setRandomInitialization(clients.get(client).getUsername());
                }
                gameHandlerMap.get(clients.get(client).getGameId()).setPlayerDisconnected(clients.get(client).getUsername());
                gameHandlerMap.get(clients.get(client).getGameId()).unsubscribe(clients.get(client).getUsername());
                gameHandlerMap.get(clients.get(client).getGameId()).removeClient(client);
                clients.get(client).reset();
            }
        }catch (NullPointerException ignored) {}
        synchronized (this.clients) {
            this.clients.remove(client);
        }

    }

    /**
     * Removes a game from the server.
     * @param game The game to be removed.
     */
    public void removeGame(GameHandler game)
    {
        for(ClientHandlerInterface client: clients.keySet().stream().filter(e-> game.getClients().contains(e)).toList())
        {
            clients.get(client).reset();
        }
        gameHandlerMap.remove(game.getGame().getGameId(), game);
    }

    /**
     * Sends a heartbeat signal to the client.
     * @param time The time of the heartbeat.
     * @param client The client's remote object.
     * @throws RemoteException If a remote error occurs.
     */
    @Override
    public void sendHeartbeat(long time, ClientHandlerInterface client) throws RemoteException {
        synchronized (this.clients){
            clients.get(client).setLastHeartbeat(time);
        }
    }

    /**
     * Creates a new game on the server.
     * @param client The client creating the game.
     * @param numberOfPlayers The number of players in the game.
     * @return The ID of the created game.
     * @throws NotExistingPlayerException If the player does not exist.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the thread is interrupted.
     */
    @Override
    public int createGame(ClientHandlerInterface client, int numberOfPlayers) throws NotExistingPlayerException, IOException, InterruptedException {
        System.out.println("createGame request received");
        int id;
        Random rand = new Random();
        do {
            int number = rand.nextInt(10000,100000);
            if(gameHandlerMap.keySet().stream().filter( e-> e == number).toList().isEmpty()) {
                id = number;
                break;
            }
        }while (true);
        GameValues.numberOfGames++;
        gameHandlerMap.put(id, new GameHandler(id, this, numberOfPlayers));
        return id;
    }

    /**
     * Retrieves a list of all available games on the server.
     * @return A list of all available games.
     * @throws RemoteException If a remote error occurs.
     */
    @Override
    public ArrayList<Game> getAvailableGames() throws RemoteException {
        return new ArrayList<>(gameHandlerMap.values().stream().map(GameHandler::getGame).toList());
    }

    /**
     * Reconnects a player to a game.
     * @param gameId The ID of the game.
     * @param username The username of the player.
     * @param client The client's remote object.
     * @return The game to which the player was reconnected.
     * @throws RemoteException If a remote error occurs.
     * @throws NotExistingPlayerException If the player does not exist.
     */
    @Override
    public Game reconnectPlayerToGame(int gameId, String username, ClientHandlerInterface client) throws RemoteException, NotExistingPlayerException {
        try {
            gameHandlerMap.get(gameId).reconnectPlayerToGame(username);
            clients.get(client).setGameId(gameId);
        } catch (NotExistingPlayerException e) {
            throw new NotExistingPlayerException();
        }
        return this.gameHandlerMap.get(gameId).getGame();
    }

    /**
     * Allows a player to quit a game.
     * @param gameId The ID of the game.
     * @param client The client's remote object.
     * @throws RemoteException If a remote error occurs.
     * @throws NotExistingPlayerException If the player does not exist.
     */
    @Override
    public void quitGame(int gameId, ClientHandlerInterface client) throws RemoteException, NotExistingPlayerException {
        try {

            if (gameHandlerMap.get(clients.get(client).getGameId()).getGame().getGameStatus().getStatusNumber() < GameStatus.GAME_STARTED.getStatusNumber() &&
                    gameHandlerMap.get(clients.get(client).getGameId()).getGame().getGameStatus().getStatusNumber() >= GameStatus.ALL_PLAYERS_READY.getStatusNumber()) {
                gameHandlerMap.get(clients.get(client).getGameId()).setRandomInitialization(clients.get(client).getUsername());
            }
            gameHandlerMap.get(gameId).setPlayerDisconnected(client.getUsername());
            gameHandlerMap.get(gameId).unsubscribe(client.getUsername());
            gameHandlerMap.get(gameId).removeClient(client);
            clients.get(client).reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException ignored) {
            //nothing, this is when the last player quits the game
        }
    }

    /**
     * Adds a player to a game.
     * @param gameId The ID of the game.
     * @param username The username of the player.
     * @param client The client's remote object.
     * @return The game to which the player was added.
     * @throws RemoteException If a remote error occurs.
     * @throws GameAlreadyStartedException If the game has already started.
     */
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

    /**
     * Sets a player as ready for the game.
     * @param gameId The ID of the game.
     * @param username The username of the player.
     * @return A list of player IDs who are ready.
     * @throws IOException If an I/O error occurs.
     * @throws DeckIsEmptyException If the deck is empty.
     * @throws NotExistingPlayerException If the player does not exist.
     * @throws InterruptedException If the thread is interrupted.
     */
    @Override
    public ArrayList<Integer> setReady(int gameId, String username) throws IOException, DeckIsEmptyException, NotExistingPlayerException, InterruptedException {
        return this.gameHandlerMap.get(gameId).setReady(username);
    }

    /**
     * Returns the ready status of all players in the game.
     * @param gameId The ID of the game.
     * @return A map of usernames to their ready status.
     * @throws RemoteException If a remote error occurs.
     */
    public HashMap<String, Boolean> getReadyStatus(int gameId) throws RemoteException
    {
        return this.gameHandlerMap.get(gameId).getReadyStatus();
    }

    /**
     * Subscribes the client to a game.
     * @param client The client's remote object.
     * @param gameId The ID of the game.
     * @throws IOException If an I/O error occurs.
     * @throws GameAlreadyStartedException If the game has already started.
     * @throws GameNotFoundException If the game does not exist.
     */
    @Override
    public void subscribe(ClientHandlerInterface client, int gameId) throws IOException, GameAlreadyStartedException, GameNotFoundException {
        try {
            this.gameHandlerMap.get(gameId).subscribe(client);
        } catch (NullPointerException e) {
            throw new GameNotFoundException();
        }
    }

    /**
     * Updates a client with a game event.
     * @param client The client to be updated.
     * @param event The game event.
     * @param gameUpdate The game update.
     * @throws IOException If an I/O error occurs.
     * @throws InterruptedException If the thread is interrupted.
     * @throws NotExistingPlayerException If the player does not exist.
     */
    public void updateClient(ClientHandlerInterface client, GameEvent event, Object gameUpdate) throws IOException, InterruptedException, NotExistingPlayerException {
        Runnable updaterThread = () -> //this is needed for socket synchronization
                {
                    synchronized (this) {
                        try {
                            client.update(event, gameUpdate);
                        } catch (IOException e) {
                            System.err.println("Failed to update client: " + e.getMessage());
                        } catch (NotExistingPlayerException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
        new Thread(updaterThread).start();

    }

    /**
     * Sets the username of a player.
     * @param username The username of the player.
     * @throws IOException If an I/O error occurs.
     * @throws InvalidUsernameException If the username is invalid.
     */
    public void setUsername(String username) throws IOException, InvalidUsernameException {
        if(checkUsername(username)) {
            return;
        }
        throw new InvalidUsernameException();

    }

    /**
     * Checks if a username is valid.
     * @param username The username to be checked.
     * @return True if the username is valid, false otherwise.
     * @throws IOException If an I/O error occurs.
     */
    public boolean checkUsername(String username) throws IOException {
        if (username.isEmpty() || username.equals("exit")) //exit is banned as a username so that TUI can quit chat with the /exit command
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

    /**
     * Sets the secret objective card for a player.
     * @param gameId The ID of the game.
     * @param player The player.
     * @param objectiveCard The objective card.
     * @throws NotExistingPlayerException If the player does not exist.
     */
    public void setSecretObjectiveCard(int gameId, Player player, ObjectiveCard objectiveCard) throws NotExistingPlayerException {
        GameHandler game = gameHandlerMap.get(gameId);
        game.setSecretObjectiveCard(player, objectiveCard);
    }

    /**
     * Adds a client to a game handler.
     * @param gameId The ID of the game.
     * @param client The client.
     * @throws RemoteException If a remote error occurs.
     */
    private void addClientToGameHandler(Integer gameId, ClientHandlerInterface client) throws RemoteException {
        this.gameHandlerMap.get(gameId).addClient(client);
    }

    /**
     * Starts the RMI server.
     */
    private void startRMIServer() {
        ServerRMIInterface stub;
        try {
            stub = (ServerRMIInterface) UnicastRemoteObject.exportObject(this, 0);
            Registry registry = LocateRegistry.createRegistry(1234);
            registry.rebind(GameValues.SERVER_NAME, stub);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.out.println("RMI Server ready");
    }

    /**
     * Starts the socket server.
     */
    private void startSocketServer() {
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

    /**
     * Retrieves a player from a game.
     * @param gameId The ID of the game.
     * @param username The username of the player.
     * @return The player.
     * @throws RemoteException If a remote error occurs.
     * @throws NotExistingPlayerException If the player does not exist.
     */
    public Player getPlayer(int gameId, String username) throws RemoteException, NotExistingPlayerException {
        return this.gameHandlerMap.get(gameId).getPlayer(username);
    }

    /**
     * Retrieves information about a card.
     * @param card The card.
     * @param gameId The ID of the game.
     * @return The information about the card.
     */
    public CardInfo getCardInfo(Card card, int gameId) {
        return gameHandlerMap.get(gameId).getController().getCardHandler().getCardInfo(card);
    }

    /**
     * Sets a marker for a player.
     * @param player The player.
     * @param gameId The ID of the game.
     * @param marker The marker.
     * @throws NotAvailableMarkerException If the marker is not available.
     * @throws NotExistingPlayerException If the player does not exist.
     */
    public void setMarker(Player player, int gameId, Marker marker) throws NotAvailableMarkerException, NotExistingPlayerException {
        gameHandlerMap.get(gameId).setMarker(player, marker);
    }

    /**
     * Sets the side of a starter card.
     * @param gameId The ID of the game.
     * @param player The player.
     * @param starterCard The starter card.
     * @param side The side of the card.
     * @throws RemoteException If a remote error occurs.
     * @throws NotExistingPlayerException If the player does not exist.
     */
    public void setStarterCardSide(int gameId, Player player, StarterCard starterCard, Side side) throws RemoteException, NotExistingPlayerException {
        gameHandlerMap.get(gameId).setStarterCardSide(player, starterCard, side);
    }

    /**
     * Plays a card.
     * @param gameId The ID of the game.
     * @param username The username of the player.
     * @param card The card to be played.
     * @param otherCard The other card.
     * @param orientation The orientation of the card.
     * @return The player.
     * @throws RemoteException If a remote error occurs.
     * @throws NotExistingPlayerException If the player does not exist.
     * @throws InvalidCardPositionException If the card position is invalid.
     * @throws RequirementsNotMetException If the requirements are not met.
     * @throws CardTypeMismatchException If the card type is mismatched.
     * @throws AngleAlreadyLinkedException If the angle is already linked.
     * @throws NotTurnException If it is not the player's turn.
     */
    public Player playCard(int gameId, String username, PlayableCard card, PlayableCard otherCard, AngleOrientation orientation) throws RemoteException, NotExistingPlayerException, InvalidCardPositionException, RequirementsNotMetException, CardTypeMismatchException, AngleAlreadyLinkedException, NotTurnException {
        GameHandler game = gameHandlerMap.get(gameId);
        if(game.getGame().getCurrentPlayer().equals(game.getPlayer(username)))
        {
            return game.playCard(game.getPlayer(username), card, otherCard, orientation);

        }
        throw new NotTurnException();
    }

    /**
     * Draws a card.
     * @param gameId The ID of the game.
     * @param username The username of the player.
     * @param cardType The type of the card.
     * @param drawPosition The position of the draw.
     * @throws RemoteException If a remote error occurs.
     * @throws NotTurnException If it is not the player's turn.
     * @throws NotExistingPlayerException If the player does not exist.
     * @throws AlreadyThreeCardsInHandException If there are already three cards in hand.
     * @throws DeckIsEmptyException If the deck is empty.
     */
    public void drawCard(int gameId, String username, CardType cardType, DrawPosition drawPosition) throws RemoteException, NotTurnException, NotExistingPlayerException, AlreadyThreeCardsInHandException, DeckIsEmptyException {
        GameHandler game = gameHandlerMap.get(gameId);
        if(game.getGame().getCurrentPlayer().equals(game.getPlayer(username)))
        {
           game.drawCard(username, cardType, drawPosition);
           return;
        }
        throw new NotTurnException();
    }

    /**
     * Ends a player's turn.
     * @param gameId The ID of the game.
     * @param username The username of the player.
     * @throws RemoteException If a remote error occurs.
     * @throws NotExistingPlayerException If the player does not exist.
     */
    public void endTurn(int gameId, String username) throws RemoteException, NotExistingPlayerException {
        GameHandler game = gameHandlerMap.get(gameId);
        game.turnEvent(username);
    }

    /**
     * Retrieves a playable card by its ID.
     * @param gameId The ID of the game.
     * @param cardId The ID of the card.
     * @return The playable card.
     * @throws RemoteException If a remote error occurs.
     */
    public PlayableCard getPlayableCardById(int gameId, int cardId) throws RemoteException
    {
        return gameHandlerMap.get(gameId).getController().getCardHandler().getPlayableCardById(cardId);
    }

    /**
     * Retrieves the other side of the selected PlayableCard
     * @param card is the selected PlayableCard
     * @return the other side of the selected PlayableCard
     * @throws RemoteException if the operation fails
     */
    public PlayableCard getOtherSideCard(int gameId, PlayableCard card) throws RemoteException {
        return gameHandlerMap.get(gameId).getController().getCardHandler().getOtherSideCard(card);
    }

    /**
     * Retrieves the other side of the selected GoldCard
     * @param card is the selected GoldCard
     * @return the other side of the selected GoldCard
     */
    public GoldCard getOtherSideCard(int gameId, GoldCard card) throws RemoteException
    {
        return gameHandlerMap.get(gameId).getController().getCardHandler().getOtherSideCard(card);

    }

    /**
     * Retrieves the other side of the selected ResourceCard
     * @param card is the selected ResourceCard
     * @return the other side of the selected ResourceCard
     */
    public ResourceCard getOtherSideCard(int gameId, ResourceCard card) throws RemoteException
    {
        return gameHandlerMap.get(gameId).getController().getCardHandler().getOtherSideCard(card);

    }

    /**
     * Retrieves the other side of the selected StarterCard
     * @param card is the selected StarterCard
     * @return the other side of the selected StarterCard
     */
    public StarterCard getOtherSideCard(int gameId, StarterCard card) throws RemoteException
    {
        return gameHandlerMap.get(gameId).getController().getCardHandler().getOtherSideCard(card);

    }

    /**
     * Retrieves the other side of the selected ObjectiveCard
     * @param card is the selected ObjectiveCard
     * @return the other side of the selected ObjectiveCard
     */
    public ObjectiveCard getOtherSideCard(int gameId, ObjectiveCard card) throws RemoteException
    {
        return gameHandlerMap.get(gameId).getController().getCardHandler().getOtherSideCard(card);

    }

    /**
     * Sends a chat message.
     * @param gameId The ID of the game.
     * @param message The message.
     * @param sender The sender of the message.
     * @throws RemoteException If a remote error occurs.
     */
    public void sendChatMessage(int gameId, String message, String sender) throws RemoteException
    {
        gameHandlerMap.get(gameId).sendChatMessage(message, sender);
    }

    /**
     * Sends a private chat message.
     * @param sender The sender of the message.
     * @param receiver The receiver of the message.
     * @param chatMessage The chat message.
     */
    public void sendPrivateChatMessage(String sender, String receiver, String chatMessage)
    {
        ClientHandlerInterface receiverClient = clients.keySet().stream().filter(e -> clients.get(e).getUsername() != null && clients.get(e).getUsername().equals(receiver)).findFirst().orElse(null);
        ClientHandlerInterface senderClient = clients.keySet().stream().filter(e -> clients.get(e).getUsername() != null && clients.get(e).getUsername().equals(sender)).findFirst().orElse(null);

        if(receiverClient == null || senderClient == null)
            return;
        try {
            updateClient(receiverClient, GameEvent.CHAT_MESSAGE, chatMessage);
            updateClient(senderClient, GameEvent.CHAT_MESSAGE, chatMessage);
        } catch (IOException | InterruptedException | NotExistingPlayerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The main method of the server.
     */
    public void run(){
        startSocketServer();
        startRMIServer();
        try {
            System.out.println("Server started on " + InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
