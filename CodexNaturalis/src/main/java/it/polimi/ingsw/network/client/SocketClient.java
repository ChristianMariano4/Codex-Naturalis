package it.polimi.ingsw.network.client;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.socket.ErrorAwareQueue;
import it.polimi.ingsw.network.socket.SocketClientMessageHandler;
import it.polimi.ingsw.view.GUI.GUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;
/**
 * The SocketClient class extends the Client class and provides the implementation for a client that communicates with the server using sockets.
 * It includes methods for setting up the connection, sending and receiving messages, and handling various game-related actions.
 */
public class SocketClient extends Client {
    private Socket serverSocket;
    SocketClientMessageHandler messageHandler;
    Thread messageHandlerThread;
    ErrorAwareQueue messageHandlerQueue = new ErrorAwareQueue(new LinkedBlockingQueue<>());

    public SocketClient(String serverIP) throws RemoteException {
        super(serverIP);
    }

    public SocketClient(GUI gui, String serverIP) throws RemoteException {
        super(serverIP);
        this.view = gui.getViewGUI();
        this.isGUI = true;
    }

    /**
     * Sets the username for the client.
     * @param username the username to be set.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     * @throws InvalidUsernameException if the username is invalid.
     */
    @Override
    public void setUsername(String username) throws IOException, ServerDisconnectedException, InvalidUsernameException {
        try
        {
            messageHandler.sendMessage(ClientMessageType.SET_USERNAME, username);
            messageHandlerQueue.take();
        }
        catch (InvalidUsernameException | ServerDisconnectedException e)
        {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.username = username;
    }

    /**
     * Creates a new game on the server with the specified number of players.
     * @param username the username of the client.
     * @param numberOfPlayers the number of players in the game.
     * @return the created game.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    @Override
    public Game createGame(String username, int numberOfPlayers) throws ServerDisconnectedException {
        try
        {
            messageHandler.sendMessage(ClientMessageType.CREATE_GAME, numberOfPlayers);

            this.gameId = (int) messageHandlerQueue.take();

            messageHandler.sendMessage(ClientMessageType.SUBSCRIBE, this.gameId);
            messageHandlerQueue.take(); //success message
            messageHandler.sendMessage(ClientMessageType.ADD_PLAYER, this.gameId, this.username);
            return (Game) messageHandlerQueue.take();
        }
        catch (ServerDisconnectedException e)
        {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the available games from the server.
     * @return a list of available games.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<Game> getAvailableGames() throws IOException, ServerDisconnectedException {
        messageHandler.sendMessage(ClientMessageType.AVAILABLE_GAMES_REQUEST, (Object) null);
        try {
            return (ArrayList<Game>) messageHandlerQueue.take();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Joins the game with the specified ID.
     * @param gameId the ID of the game to join.
     * @param username the username of the client.
     * @return the joined game.
     * @throws ServerDisconnectedException if the server is disconnected.
     * @throws NotExistingPlayerException if the player does not exist.
     * @throws GameNotFoundException if the game is not found.
     */
    @Override
    public Game joinGame(int gameId, String username) throws ServerDisconnectedException, NotExistingPlayerException, GameNotFoundException {
        this.gameId = gameId;
        try {
            messageHandler.sendMessage(ClientMessageType.SUBSCRIBE, this.gameId);
            if(messageHandlerQueue.take().equals(true)){
                messageHandler.sendMessage(ClientMessageType.ADD_PLAYER, this.gameId, this.username);
            }
            return (Game) messageHandlerQueue.take();
        } catch (ServerDisconnectedException | GameNotFoundException e) {
            throw e;
        } catch (GameAlreadyStartedException e) {
            try {
                messageHandler.sendMessage(ClientMessageType.RECONNECT_PLAYER, this.gameId, this.username);
                return (Game) messageHandlerQueue.take();
            }
            catch (IOException ex) {
                throw new ServerDisconnectedException();
            }
            catch (NotExistingPlayerException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * Sets the ready status for the client.
     * @return a list of player IDs who are ready.
     * @throws NotEnoughPlayersException if there are not enough players.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    @Override
    @SuppressWarnings("unchecked")
    public ArrayList<Integer> setReady() throws NotEnoughPlayersException, IOException, ServerDisconnectedException {
        try {
            messageHandler.sendMessage(ClientMessageType.SET_READY, this.gameId);
            return (ArrayList<Integer>) messageHandlerQueue.take();
        } catch(ServerDisconnectedException se) {
            throw se;
        } catch(NotEnoughPlayersException e) {
            throw new NotEnoughPlayersException();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * Quits the game.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    @Override
    public void quitGame() throws IOException, ServerDisconnectedException {
        messageHandler.sendMessage(ClientMessageType.QUIT_GAME, gameId);
        try {
            messageHandlerQueue.take();
        } catch (NotExistingPlayerException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * Connects to the server.
     */
    @Override
    public void connectToServer() {
        try {
            this.serverSocket = new Socket(this.serverIP, GameValues.SOCKET_SERVER_PORT);
            System.out.println("Connected to sever successfully");
            Thread clientThread = new Thread(this);
            clientThread.start();
            clientThread.join();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets a playable card by its ID.
     * @param gameId the ID of the game.
     * @param cardId the ID of the card.
     * @return the playable card.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    @Override
    public PlayableCard getPlayableCardById(int gameId, int cardId) throws IOException, ServerDisconnectedException {
        messageHandler.sendMessage(ClientMessageType.GET_CARD_BY_ID,gameId, cardId);
        try {
            return (PlayableCard) messageHandlerQueue.take();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * Gets the other side of a playable card.
     * @param gameId the ID of the game.
     * @param playableCard the playable card.
     * @return the other side of the playable card.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    @Override
    public PlayableCard getOtherSideCard(int gameId, PlayableCard playableCard) throws IOException, ServerDisconnectedException {
        messageHandler.sendMessage(ClientMessageType.OTHER_SIDE_PLAYABLE_CARD_REQUEST,gameId, playableCard);
        try {
            return (PlayableCard) messageHandlerQueue.take();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * Gets the other side of a starter card.
     * @param gameId the ID of the game.
     * @param starterCard the starter card.
     * @return the other side of the starter card.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    @Override
    public StarterCard getOtherSideCard(int gameId, StarterCard starterCard) throws IOException, ServerDisconnectedException {
        messageHandler.sendMessage(ClientMessageType.OTHER_SIDE_STARTER_CARD_REQUEST,gameId, starterCard);
        try {
            return (StarterCard) messageHandlerQueue.take();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * Gets the card info.
     * @param card the card.
     * @param gameId the ID of the game.
     * @return the card info.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    @Override
    public CardInfo getCardInfo(Card card, int gameId) throws IOException, ServerDisconnectedException {
        messageHandler.sendMessage(ClientMessageType.CARD_INFO_REQUEST, card, gameId);
        try {
            return (CardInfo) messageHandlerQueue.take();
        } catch(Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * Ends the turn.
     * @param gameId the ID of the game.
     * @param username the username of the client.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    @Override
    public void endTurn(int gameId, String username) throws IOException, ServerDisconnectedException {
            messageHandler.sendMessage(ClientMessageType.END_TURN, gameId, username);
            try {
                messageHandlerQueue.take();
            } catch (Exception e) {
                throw new RuntimeException();
            }
    }

    /**
     * Draws a card.
     * @param gameId the ID of the game.
     * @param username the username of the client.
     * @param cardType the type of the card.
     * @param drawPosition the draw position.
     * @throws NotExistingPlayerException if the player does not exist.
     * @throws NotTurnException if it's not the client's turn.
     * @throws IOException if an I/O error occurs.
     * @throws AlreadyThreeCardsInHandException if the client already has three cards in hand.
     * @throws DeckIsEmptyException if the deck is empty.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    @Override
    public void drawCard(int gameId, String username, CardType cardType, DrawPosition drawPosition) throws NotExistingPlayerException, NotTurnException, IOException, AlreadyThreeCardsInHandException, DeckIsEmptyException, ServerDisconnectedException {
        messageHandler.sendMessage(ClientMessageType.DRAW_CARD, gameId, username, cardType, drawPosition);
        try {
            messageHandlerQueue.take();
        } catch(Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * Plays a card.
     * @param gameId the ID of the game.
     * @param username the username of the client.
     * @param cardOnBoard the card on the board.
     * @param card the card to be played.
     * @param orientation the orientation of the card to be played.
     * @return the player who played the card.
     * @throws InvalidCardPositionException if the card position is invalid.
     * @throws NotExistingPlayerException if the player does not exist.
     * @throws NotTurnException if it's not the client's turn.
     * @throws RequirementsNotMetException if the requirements are not met.
     * @throws CardTypeMismatchException if the card type does not match.
     * @throws IOException if an I/O error occurs.
     * @throws AngleAlreadyLinkedException if the angle is already linked.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    @Override
    public Player playCard(int gameId, String username, PlayableCard cardOnBoard, PlayableCard card, AngleOrientation orientation) throws InvalidCardPositionException, NotExistingPlayerException, NotTurnException, RequirementsNotMetException, CardTypeMismatchException, IOException, AngleAlreadyLinkedException, ServerDisconnectedException {
        messageHandler.sendMessage(ClientMessageType.PLAY_CARD,gameId, username, cardOnBoard, card, orientation);
        try {
            return (Player) messageHandlerQueue.take();
        } catch (InvalidCardPositionException | RequirementsNotMetException e) {
            throw e;
        } catch(Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * Sets a secret objective card.
     * @param gameId the ID of the game.
     * @param player the player.
     * @param chosenObjectiveCard the chosen objective card.
     * @throws NotExistingPlayerException if the player does not exist.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    @Override
    public void setSecretObjectiveCard(int gameId, Player player, ObjectiveCard chosenObjectiveCard) throws NotExistingPlayerException, IOException, ServerDisconnectedException {
        messageHandler.sendMessage(ClientMessageType.SET_SECRET_OBJECTIVE_CARD, gameId, player, chosenObjectiveCard);
        try {
            messageHandlerQueue.take();
        } catch(Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * Sets a marker.
     * @param player the player.
     * @param gameId the ID of the game.
     * @param chosenMarker the chosen marker.
     * @throws NotExistingPlayerException if the player does not exist.
     * @throws IOException if an I/O error occurs.
     * @throws NotAvailableMarkerException if the marker is not available.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    @Override
    public void setMarker(Player player, int gameId, Marker chosenMarker) throws NotExistingPlayerException, IOException, NotAvailableMarkerException, ServerDisconnectedException {
        messageHandler.sendMessage(ClientMessageType.SET_MARKER, player, gameId, chosenMarker);
        try {
            messageHandlerQueue.take();
        } catch(Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * Sets the side of a starter card.
     * @param gameId the ID of the game.
     * @param player the player.
     * @param cardFront the front of the card.
     * @param side the side of the card.
     * @throws NotExistingPlayerException if the player does not exist.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    @Override
    public void setStarterCardSide(int gameId, Player player, StarterCard cardFront, Side side) throws NotExistingPlayerException, IOException, ServerDisconnectedException {
        messageHandler.sendMessage(ClientMessageType.SET_STARTER_CARD_SIDE ,gameId, player, cardFront, side);
        try {
            messageHandlerQueue.take();
        } catch(Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * Gets the ready status of all players.
     * @return a map of usernames to their ready status.
     * @throws ServerDisconnectedException if the server is disconnected.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    @SuppressWarnings("unchecked")
    public HashMap<String, Boolean> getReadyStatus() throws ServerDisconnectedException, IOException {
        messageHandler.sendMessage(ClientMessageType.GET_READY_STATUS, gameId);
        try {
            return (HashMap<String, Boolean>) messageHandlerQueue.take();
        } catch(Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * Sends a chat message.
     * @param gameId the ID of the game.
     * @param message the message to be sent.
     * @throws ServerDisconnectedException if the server is disconnected.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public void sendChatMessage(int gameId, String message) throws ServerDisconnectedException, IOException {
        messageHandler.sendMessage(ClientMessageType.SEND_CHAT_MESSAGE, gameId, message);
        try {
            messageHandlerQueue.take();
        } catch(Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * The main execution method for the SocketClient.
     * This method sets up the connection, starts the message handler thread, and sends regular heartbeats to the server.
     */
    public void run()
    {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(serverSocket.getOutputStream()); //OutputStream must be created before InputStream otherwise it doesn't work
            ObjectInputStream inputStream = new ObjectInputStream(serverSocket.getInputStream());
            SocketClientMessageHandler messageHandler = new SocketClientMessageHandler(this, inputStream, outputStream, messageHandlerQueue);
            this.messageHandler = messageHandler;
            Thread messageHandlerThread = new Thread(messageHandler);
            this.messageHandlerThread = messageHandlerThread;
            messageHandlerThread.start();

            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    messageHandler.sendMessage(ClientMessageType.HEARTBEAT, System.currentTimeMillis());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ServerDisconnectedException e) {
                    System.err.println("Disconnected from server");
                    System.exit(-1);
                    throw new RuntimeException(e);
                }
            }, 0, GameValues.HEARTBEAT_INTERVAL, TimeUnit.MILLISECONDS);
            if(isGUI) {
                runGUI();
            } else {
                runTUI();
            }
        } catch (IOException | NotExistingPlayerException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ServerDisconnectedException e) {
            System.err.println("Disconnected from server");
        }



    }
}
