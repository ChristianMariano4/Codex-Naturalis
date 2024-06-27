package it.polimi.ingsw.network.client;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.rmi.ServerRMIInterface;
import it.polimi.ingsw.view.GUI.GUI;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class representing a client using RMI.
 * This class extends the Client class and implements methods for creating and joining games, getting available games, setting the client as ready, quitting the game, connecting to the server, setting the username, getting a playable card by its ID, getting the other side of a card, getting information about a card, ending the turn, drawing a card, playing a card, setting a secret objective card, setting a marker, setting the side of a starter card, getting the ready status, and sending a chat message.
 */
public class RMIClient extends Client {
    private ServerRMIInterface serverRMIInterface;

    /**
     * Constructor for the RMIClient class that is using the TUI.
     * @param serverIP the IP address of the server.
     * @throws RemoteException if a network error occurs.
     */
    public RMIClient(String serverIP) throws RemoteException {
        super(serverIP);
        this.isGUI = false;
    }

    /**
     * Constructor for the RMIClient class that is using the GUI.
     * @param gui the GUI of the client.
     * @param serverIP the IP address of the server.
     * @throws RemoteException if a network error occurs.
     */
    public RMIClient(GUI gui, String serverIP) throws RemoteException {
        super(serverIP);
        this.view = gui.getViewGUI();
        this.isGUI = true;
    }

    /**
     * Creates a game with the specified number of players.
     * @param username the username of the player creating the game.
     * @param numberOfPlayers the number of players in the game.
     * @return the created game.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    @Override
    public Game createGame(String username, int numberOfPlayers) throws ServerDisconnectedException {
        try {
            this.gameId = serverRMIInterface.createGame(this, numberOfPlayers);
            serverRMIInterface.subscribe(this, this.gameId);
            //server.initializePlayersHand(this.gameId, this.player);
            return serverRMIInterface.addPlayerToGame(this.gameId, username, this);
        } catch (RemoteException e) {
            throw new ServerDisconnectedException();
        } catch (GameAlreadyStartedException | NotExistingPlayerException | InterruptedException | IOException |
                 GameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the list of available games.
     * @return a list of available games.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    @Override
    public ArrayList<Game> getAvailableGames() throws ServerDisconnectedException {
        try {
            return serverRMIInterface.getAvailableGames();
        } catch (RemoteException e) {
            throw new ServerDisconnectedException();
        }
    }

    /**
     * Joins a game with the specified game ID.
     * @param gameId the ID of the game to join.
     * @param username the username of the player joining the game.
     * @return the joined game.
     * @throws ServerDisconnectedException if the server is disconnected.
     * @throws NotExistingPlayerException if the player does not exist.
     * @throws RemoteException if a network error occurs.
     * @throws GameNotFoundException if the game does not exist.
     */
    @Override
    public Game joinGame(int gameId, String username) throws ServerDisconnectedException, NotExistingPlayerException, RemoteException, GameNotFoundException {
        this.gameId = gameId;
        try {
                serverRMIInterface.subscribe(this, this.gameId);
                return serverRMIInterface.addPlayerToGame(this.gameId, username, this);
            } catch (RemoteException e) {
                throw new ServerDisconnectedException();
            } catch (GameAlreadyStartedException e) {
                return serverRMIInterface.reconnectPlayerToGame(this.gameId, username, this);
            } catch (IOException e) {
                throw new RuntimeException(e);
        }
    }

    /**
     * Sets the client as ready.
     * @return a list of integers.
     * @throws NotEnoughPlayersException if there are not enough players.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    @Override
    public ArrayList<Integer> setReady() throws NotEnoughPlayersException, ServerDisconnectedException {
        try
        {
            return serverRMIInterface.setReady(this.gameId, this.username);
        } catch (NotExistingPlayerException | DeckIsEmptyException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new ServerDisconnectedException();
        }
    }

    /**
     * Gets the ready status of all players in the game.
     * The ready status is represented as a map where the keys are the usernames of the players and the values are booleans indicating whether each player is ready.
     * @return a map of usernames to their ready status.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public HashMap<String, Boolean> getReadyStatus() throws ServerDisconnectedException
    {
        try {
            return serverRMIInterface.getReadyStatus(this.gameId);
        } catch (RemoteException e) {
            throw new ServerDisconnectedException();
        }
    }

    /**
     * Quits the game.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    @Override
    public void quitGame() throws ServerDisconnectedException {
        try {
            serverRMIInterface.quitGame(this.gameId, this);
        } catch (RemoteException e) {
            throw new ServerDisconnectedException();
        } catch (NotExistingPlayerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Connects to the server.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    @Override
    public void connectToServer() throws ServerDisconnectedException {
        try {
            Registry registry = LocateRegistry.getRegistry(this.serverIP,  GameValues.RMI_SERVER_PORT);
            this.serverRMIInterface = (ServerRMIInterface) registry.lookup(GameValues.SERVER_NAME);
            Thread clientThread = new Thread(this);
            clientThread.start();
            clientThread.join();
        } catch (RemoteException e) {
            System.err.println("Couldn't connect to server");
            throw new ServerDisconnectedException();
        } catch (NotBoundException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The main execution method for the RMIClient.
     * This method connects the client to the server, sends regular heartbeats to the server, and starts the GUI or TUI based on the client's configuration.
     * The heartbeat is sent at a fixed rate specified by the GameValues.HEARTBEAT_INTERVAL.
     * If the client is disconnected from the server, it attempts to recontact the server.
     * If the client is unable to recontact the server, it exits the program.
     * If the client is using the GUI, it runs the GUI. Otherwise, it runs the TUI.
     * @throws RuntimeException if an InterruptedException, NotExistingPlayerException, or IOException occurs.
     */
    public void run() {
        try {
            this.serverRMIInterface.connect(this);
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    serverRMIInterface.sendHeartbeat(System.currentTimeMillis(), this);
                } catch (RemoteException e) {
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
        } catch (InterruptedException | NotExistingPlayerException | IOException e) {
            throw new RuntimeException(e);
        } catch (ServerDisconnectedException e) {
            System.err.println("Disconnected from server");
            System.exit(-1);
        }
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
        try {
            serverRMIInterface.setUsername(username);
            this.username = username;
        } catch (IOException e) {
            throw new ServerDisconnectedException();
        }
    }

    /**
     * Gets a playable card by its ID.
     * @param gameId the ID of the game.
     * @param cardId the ID of the card.
     * @return the playable card with the specified ID.
     * @throws RemoteException if a network error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public PlayableCard getPlayableCardById(int gameId, int cardId) throws RemoteException, ServerDisconnectedException {
        try {
            return serverRMIInterface.getPlayableCardById(gameId, cardId);
        } catch (RemoteException e) {
            throw new ServerDisconnectedException();
        }
    }

    /**
     * Gets the other side of a playable card.
     * @param gameId the ID of the game.
     * @param playableCard the playable card.
     * @return the other side of the playable card.
     * @throws RemoteException if a network error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public PlayableCard getOtherSideCard(int gameId, PlayableCard playableCard) throws RemoteException, ServerDisconnectedException {
        try {
            return serverRMIInterface.getOtherSideCard(gameId, playableCard);
        } catch (RemoteException e) {
            throw new ServerDisconnectedException();
        }
    }

    /**
     * Gets the other side of a starter card.
     * @param gameId the ID of the game.
     * @param starterCard the starter card.
     * @return the other side of the starter card.
     * @throws RemoteException if a network error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public StarterCard getOtherSideCard(int gameId , StarterCard starterCard) throws RemoteException, ServerDisconnectedException {
        try {
            return serverRMIInterface.getOtherSideCard(gameId, starterCard);
        } catch (RemoteException e) {
            throw new ServerDisconnectedException();
        }
    }

    /**
     * Gets information about a card.
     * @param card the card.
     * @param gameId the ID of the game.
     * @return information about the card.
     * @throws RemoteException if a network error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public CardInfo getCardInfo(Card card, int gameId) throws RemoteException, ServerDisconnectedException {
        try {
            return serverRMIInterface.getCardInfo(card, gameId);
        } catch (RemoteException e) {
            throw new ServerDisconnectedException();
        }
    }

    /**
     * Ends the turn for the client.
     * @param gameId the ID of the game.
     * @param username the username of the client.
     * @throws NotExistingPlayerException if the player does not exist.
     * @throws CardTypeMismatchException if the card type does not match.
     * @throws RemoteException if a network error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public void endTurn(int gameId, String username) throws NotExistingPlayerException, CardTypeMismatchException, RemoteException, ServerDisconnectedException {
        try {
            serverRMIInterface.endTurn(gameId, username);
        } catch (RemoteException e) {
            throw new ServerDisconnectedException();
        }
    }

    /**
     * Draws a card for the client.
     * @param gameId the ID of the game.
     * @param username the username of the client.
     * @param cardType the type of the card to be drawn.
     * @param drawPosition the position from where the card should be drawn.
     * @throws NotExistingPlayerException if the player does not exist.
     * @throws NotTurnException if it's not the client's turn.
     * @throws RemoteException if a network error occurs.
     * @throws AlreadyThreeCardsInHandException if the client already has three cards in hand.
     * @throws DeckIsEmptyException if the deck is empty.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public void drawCard(int gameId, String username, CardType cardType, DrawPosition drawPosition) throws NotExistingPlayerException, NotTurnException, RemoteException, AlreadyThreeCardsInHandException, DeckIsEmptyException, ServerDisconnectedException {
        try {
            serverRMIInterface.drawCard(gameId,username,cardType,drawPosition);
        } catch (RemoteException e) {
            throw new ServerDisconnectedException();
        }
    }

    /**
     * Plays a card for the client.
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
     * @throws RemoteException if a network error occurs.
     * @throws AngleAlreadyLinkedException if the angle is already linked.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public Player playCard(int gameId, String username, PlayableCard cardOnBoard, PlayableCard card , AngleOrientation orientation) throws InvalidCardPositionException, NotExistingPlayerException, NotTurnException, RequirementsNotMetException, CardTypeMismatchException, RemoteException, AngleAlreadyLinkedException, ServerDisconnectedException {
        try {
            return serverRMIInterface.playCard(gameId,username,cardOnBoard, card, orientation);
        } catch (RemoteException e) {
            throw new ServerDisconnectedException();
        }
    }

    /**
     * Sets a secret objective card for the client.
     * @param gameId the ID of the game.
     * @param player the player.
     * @param chosenObjectiveCard the chosen objective card.
     * @throws NotExistingPlayerException if the player does not exist.
     * @throws RemoteException if a network error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public void setSecretObjectiveCard(int gameId, Player player, ObjectiveCard chosenObjectiveCard) throws NotExistingPlayerException, RemoteException, ServerDisconnectedException {
        try {
            serverRMIInterface.setSecretObjectiveCard(gameId,player,chosenObjectiveCard);
        } catch (RemoteException e) {
            throw new ServerDisconnectedException();
        }
    }

    /**
     * Sets a marker for the client.
     * @param player the player.
     * @param gameId the ID of the game.
     * @param chosenMarker the chosen marker.
     * @throws NotExistingPlayerException if the player does not exist.
     * @throws RemoteException if a network error occurs.
     * @throws NotAvailableMarkerException if the marker is not available.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public void setMarker(Player player, int gameId, Marker chosenMarker) throws NotExistingPlayerException, RemoteException, NotAvailableMarkerException, ServerDisconnectedException {
        try {
            serverRMIInterface.setMarker(player, gameId, chosenMarker);
        } catch (RemoteException e) {
            throw new ServerDisconnectedException();
        }
    }

    /**
     * Sets the side of a starter card for the client.
     * @param gameId the ID of the game.
     * @param player the player.
     * @param cardFront the front of the card.
     * @param side the side of the card.
     * @throws NotExistingPlayerException if the player does not exist.
     * @throws RemoteException if a network error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public void setStarterCardSide(int gameId, Player player, StarterCard cardFront, Side side) throws NotExistingPlayerException, RemoteException, ServerDisconnectedException {
        try {
            serverRMIInterface.setStarterCardSide(gameId, player, cardFront, side);
        } catch (RemoteException e) {
            throw new ServerDisconnectedException();
        }
    }

    /**
     * Sends a chat message.
     * @param gameId the ID of the game.
     * @param message the message to be sent.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public void sendChatMessage(int gameId, String message) throws ServerDisconnectedException {
        try {
            serverRMIInterface.sendChatMessage(gameId,  message,  username);
        } catch (RemoteException e) {
            throw new ServerDisconnectedException();
        }
    }
}
