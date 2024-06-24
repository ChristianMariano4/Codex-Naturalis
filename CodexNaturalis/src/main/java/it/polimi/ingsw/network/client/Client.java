package it.polimi.ingsw.network.client;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.rmi.ClientRMIInterface;
import it.polimi.ingsw.view.GUI.ViewGUI;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.TUI.ViewCLI;
import it.polimi.ingsw.network.messages.GameEvent;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Abstract class representing a client in the network.
 * This class is responsible for handling client-side operations and interactions with the server.
 * It provides methods for creating and joining games, setting up the game state, and handling game events.
 */
public abstract class Client extends UnicastRemoteObject implements ClientRMIInterface, Runnable {
    protected String username = null;
    protected boolean isGUI;
    private boolean isRMI = false;

    protected int gameId = -1; //invalid default value
    protected View view;
    protected boolean playing = false;
    protected Thread viewThread;
    protected boolean markerTurn = false;
    protected boolean markerDone = false;
    protected boolean starterCardAssigned = false;
    protected ArrayList<ObjectiveCard> objectiveCardsToChoose = null;
    protected boolean gameBegin = false;
    public long lastHeartbeat = System.currentTimeMillis();
    protected final String serverIP;

    /**
     * Constructor for the Client class.
     * @param isRMI a boolean indicating whether the client is using RMI.
     * @param serverIP the IP address of the server.
     * @throws RemoteException if a network error occurs.
     */
    public Client(boolean isRMI, String serverIP) throws RemoteException {
        super();
        this.isRMI = isRMI;
        this.serverIP = serverIP;
    }

    /**
     * Checks if the client is using RMI.
     * @return true if the client is using RMI, false otherwise.
     */
    public boolean isRMI()
    {
        return this.isRMI;
    }

    /**
     * Creates a new game.
     * @param username the username of the player creating the game.
     * @param numberOfPlayers the number of players in the game.
     * @return the created game.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public abstract Game createGame(String username, int numberOfPlayers) throws ServerDisconnectedException;

    /**
     * Gets the list of available games.
     * @return a list of available games.
     * @throws IOException if an I/O error occurs.
     * @throws InterruptedException if the thread is interrupted.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public abstract ArrayList<Game> getAvailableGames() throws IOException, InterruptedException, ServerDisconnectedException;

    /**
     * Joins a game.
     * @param gameId the ID of the game to join.
     * @param username the username of the player joining the game.
     * @return the joined game.
     * @throws ServerDisconnectedException if the server is disconnected.
     * @throws NotExistingPlayerException if the player does not exist.
     * @throws RemoteException if a network error occurs.
     * @throws GameNotFoundException if the game is not found.
     */
    public abstract Game joinGame(int gameId, String username) throws ServerDisconnectedException, NotExistingPlayerException, RemoteException, GameNotFoundException;

    /**
     * Gets the username of the client.
     * @return the username of the client.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets the client as ready to play.
     * @return a list of game IDs that the client is ready to play.
     * @throws NotEnoughPlayersException if there are not enough players to start the game.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public abstract ArrayList<Integer> setReady() throws NotEnoughPlayersException, IOException, ServerDisconnectedException;

    /**
     * Quits the game.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public abstract void quitGame() throws IOException, ServerDisconnectedException;

    /**
     * Updates the client's state based on a game event.
     * @param event the game event.
     * @param gameUpdate the game update associated with the event.
     * @throws RemoteException if a network error occurs.
     */
    @Override
    @SuppressWarnings("unchecked")
    public synchronized void update(GameEvent event, Object gameUpdate) throws RemoteException{

            switch (event) {
                case NEW_PLAYER -> {
                    view.update((Game) gameUpdate);
                    view.newPlayer((Game) gameUpdate);
                }
                case GAME_INITIALIZED -> {
                    view.update((Game) gameUpdate);
                    this.playing = true;

                }
                case GAME_BEGIN -> {
                    view.update((Game) gameUpdate);
                    this.gameBegin = true;
                }
                case TURN_EVENT -> {
                    view.update((Game) gameUpdate);
                    if(this.viewThread!=null)
                        this.viewThread.interrupt();
                }
                case SECRET_OBJECTIVE_CHOICE_REQUEST ->
                {
                    this.objectiveCardsToChoose = (ArrayList<ObjectiveCard>) gameUpdate;
                }
                case MARKER_EVENT ->
                {
                    Game game = (Game) gameUpdate;
                    view.update(game);
                    try {
                        if (!this.markerTurn && game.getPlayer(username).getMarker() == null) {
                            String currentMarkerChoice = game.getListOfPlayers().get(4 - game.getAvailableMarkers().size()).getUsername(); //how many markers have already been chosen
                            if (username.equals(currentMarkerChoice))
                                this.markerTurn = true;
                        }
                    }
                    catch (NotExistingPlayerException e)
                    {
                        throw new RuntimeException();
                    }
                }
                case MARKER_DONE ->
                {
                    view.update((Game) gameUpdate);
                    this.markerDone = true;
                }
                case ASSIGNED_STARTER_CARDS ->
                {
                    view.update((Game) gameUpdate);
                    this.starterCardAssigned = true;
                }
                case STARTER_CARD_SIDE_CHOSEN ->
                {
                    view.update((Game) gameUpdate);
                }
                case TWENTY_POINTS -> {
                    view.twentyPoints((String) gameUpdate);
                    if(viewThread!=null)
                        this.viewThread.interrupt();
                }
                case FINAL_ROUND ->
                {
                    view.finalRound();
                    if(viewThread!=null)
                        this.viewThread.interrupt();
                }
                case GAME_END ->
                {
                    view.update((Game) gameUpdate);
                    view.gameEnd();
                    if (this.viewThread != null) {
                        this.viewThread.interrupt();
                    }
                }
                case PLAYER_DISCONNECTED ->
                {
                    view.update((Game) gameUpdate);
                    if (this.viewThread != null) {
                        this.viewThread.interrupt();
                    }
                }
                case PLAYER_RECONNECTED ->
                {
                    view.update((Game) gameUpdate);
                    if (this.viewThread != null) {
                        this.viewThread.interrupt();
                    }
                }
                case CHAT_MESSAGE ->
                {
                    view.chatMessage((String) gameUpdate);
                }
            }
    }

    /**
     * Handles the operations that need to be performed before the game starts.
     * @param viewCLI the command line interface view.
     * @return a boolean indicating whether the pre-game start operations were successful.
     * @throws InterruptedException if the thread is interrupted.
     * @throws NotExistingPlayerException if the player does not exist.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    boolean preGameStart(ViewCLI viewCLI) throws InterruptedException, NotExistingPlayerException, IOException, ServerDisconnectedException {

        int choice = viewCLI.setChoiceGame();
        if (choice == 0) {
            viewCLI.gameQuit();
            System.exit(0);
        }
        if (choice == 2)
            return true;
        if(!viewCLI.setReady()) {
            this.playing = false;
            return true;
        }
        while (!this.playing) {
            Thread.sleep(10);
        }
        this.markerTurn = viewCLI.waitingForMarkerTurn();
        while (!this.markerTurn) {
            Thread.sleep(10);
        }
        try {
            viewCLI.markerSelection();
            viewCLI.chooseStarterCardSide();
        } catch (GameNotFoundException e) {
            return true;
        }
        while (this.objectiveCardsToChoose == null) {
            Thread.sleep(10);
        }
        viewCLI.showSharedObjectiveCards();
        try {
            viewCLI.chooseObjectiveCard(this.objectiveCardsToChoose);
        } catch (GameNotFoundException e) {
            return true;
        }
        viewCLI.waitingForGameBegin();
        while (!this.gameBegin) {
            Thread.sleep(10);
        }
        return true;
    }

    /**
     * Resets the client's state to the default values.
     */
    public void resetClient()
    {
         this.gameId = -1;
         this.playing = false;
         this.viewThread = null;
         this.markerTurn = false;
         this.markerDone = false;
         this.starterCardAssigned = false;
         this.objectiveCardsToChoose = null;
         this.gameBegin = false;
    }

    /**
     * Runs the Text User Interface (TUI) for the client.
     * @throws ServerDisconnectedException if the server is disconnected.
     * @throws IOException if an I/O error occurs.
     * @throws InterruptedException if the thread is interrupted.
     * @throws NotExistingPlayerException if the player does not exist.
     */
    protected void runTUI() throws ServerDisconnectedException, IOException, InterruptedException, NotExistingPlayerException {
        view = new ViewCLI(this);
        ViewCLI viewCLI = (ViewCLI) view;
        viewCLI.setUsername();
        while (preGameStart(viewCLI))
        {
            if(playing) {
                this.viewThread = new Thread(viewCLI); //game loop actually begins here
                this.viewThread.start();
                this.viewThread.join();
            }
            resetClient(); //resetting the client after end of game
            if(viewCLI.getAsyncReader().isAlive()){
                viewCLI.getAsyncReader().interrupt();
            }
        }
    }

    /**
     * Sets the marker turn for the client.
     * @param turn a boolean indicating whether it's the client's turn to choose a marker.
     */
    public void serMarkerTurn(boolean turn)
    {
        this.markerTurn = turn;
    }

    /**
     * Gets the marker turn for the client.
     * @return a boolean indicating whether it's the client's turn to choose a marker.
     */
    public boolean getMarkerTurn() {
        return this.markerTurn;
    }

    /**
     * Gets the playing state of the client.
     * @return a boolean indicating whether the client is currently playing a game.
     */
    public boolean getPlaying()
    {
        return this.playing;
    }

    /**
     * Gets the game begin state of the client.
     * @return a boolean indicating whether the game has begun.
     */
    public boolean getGameBegin()
    {
        return this.gameBegin;
    }

    /**
     * Runs the Graphical User Interface (GUI) for the client.
     */
    protected void runGUI() {
            ViewGUI viewGUI = (ViewGUI) view;
            viewGUI.setClient(this);
    }

    /**
     * Gets the objective cards for the client to choose from.
     * @return a list of objective cards for the client to choose from.
     */
    public ArrayList<ObjectiveCard> getObjectiveCardsToChoose()
    {
        return this.objectiveCardsToChoose;
    }

    /**
     * Connects the client to the server.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public abstract void connectToServer() throws ServerDisconnectedException;

    /**
     * Sets the username for the client.
     * @param username the username to be set.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     * @throws InvalidUsernameException if the username is invalid.
     */
    public abstract void setUsername(String username) throws IOException, ServerDisconnectedException, InvalidUsernameException;

    /**
     * Retrieves a playable card by its ID.
     * @param gameId the ID of the game.
     * @param cardId the ID of the card.
     * @return the playable card with the specified ID.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public abstract PlayableCard getPlayableCardById(int gameId, int cardId) throws IOException, ServerDisconnectedException;

    /**
     * Retrieves the other side of a playable card.
     * @param gameId the ID of the game.
     * @param playableCard the playable card.
     * @return the other side of the playable card.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public abstract PlayableCard getOtherSideCard(int gameId, PlayableCard playableCard) throws IOException, ServerDisconnectedException;

    /**
     * Retrieves the other side of a starter card.
     * @param gameId the ID of the game.
     * @param starterCard the starter card.
     * @return the other side of the starter card.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public abstract StarterCard getOtherSideCard(int gameId , StarterCard starterCard) throws IOException, ServerDisconnectedException;

    /**
     * Retrieves information about a card.
     * @param card the card.
     * @param gameId the ID of the game.
     * @return information about the card.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public abstract CardInfo getCardInfo(Card card, int gameId) throws IOException, ServerDisconnectedException;

    /**
     * Ends the turn for the client.
     * @param gameId the ID of the game.
     * @param username the username of the client.
     * @throws NotExistingPlayerException if the player does not exist.
     * @throws CardTypeMismatchException if the card type does not match.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public abstract void endTurn(int gameId, String username) throws NotExistingPlayerException, CardTypeMismatchException, IOException, ServerDisconnectedException;

    /**
     * Draws a card for the client.
     * @param gameId the ID of the game.
     * @param username the username of the client.
     * @param cardType the type of the card to be drawn.
     * @param drawPosition the position from where the card should be drawn.
     * @throws NotExistingPlayerException if the player does not exist.
     * @throws NotTurnException if it's not the client's turn.
     * @throws IOException if an I/O error occurs.
     * @throws AlreadyThreeCardsInHandException if the client already has three cards in hand.
     * @throws DeckIsEmptyException if the deck is empty.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public abstract void drawCard(int gameId, String username, CardType cardType, DrawPosition drawPosition) throws NotExistingPlayerException, NotTurnException, IOException, AlreadyThreeCardsInHandException, DeckIsEmptyException, ServerDisconnectedException;

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
     * @throws IOException if an I/O error occurs.
     * @throws AngleAlreadyLinkedException if the angle is already linked.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public abstract Player playCard(int gameId, String username, PlayableCard cardOnBoard, PlayableCard card , AngleOrientation orientation) throws InvalidCardPositionException, NotExistingPlayerException, NotTurnException, RequirementsNotMetException, CardTypeMismatchException, IOException, AngleAlreadyLinkedException, ServerDisconnectedException;

    /**
     * Sets a secret objective card for the client.
     * @param gameId the ID of the game.
     * @param player the player.
     * @param chosenObjectiveCard the chosen objective card.
     * @throws NotExistingPlayerException if the player does not exist.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public abstract void setSecretObjectiveCard(int gameId, Player player, ObjectiveCard chosenObjectiveCard) throws NotExistingPlayerException, IOException, ServerDisconnectedException;

    /**
     * Sets a marker for the client.
     * @param player the player.
     * @param gameId the ID of the game.
     * @param chosenMarker the chosen marker.
     * @throws NotExistingPlayerException if the player does not exist.
     * @throws IOException if an I/O error occurs.
     * @throws NotAvailableMarkerException if the marker is not available.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public abstract void setMarker(Player player, int gameId, Marker chosenMarker) throws NotExistingPlayerException, IOException, NotAvailableMarkerException, ServerDisconnectedException;

    /**
     * Sets the side of a starter card for the client.
     * @param gameId the ID of the game.
     * @param player the player.
     * @param cardFront the front of the card.
     * @param side the side of the card.
     * @throws NotExistingPlayerException if the player does not exist.
     * @throws IOException if an I/O error occurs.
     * @throws ServerDisconnectedException if the server is disconnected.
     */
    public abstract void setStarterCardSide(int gameId, Player player, StarterCard cardFront, Side side) throws NotExistingPlayerException, IOException, ServerDisconnectedException;

    /**
     * Gets the ready status of the client.
     * @return a map of usernames to their ready status.
     * @throws ServerDisconnectedException if the server is disconnected.
     * @throws IOException if an I/O error occurs.
     */
    public abstract HashMap<String, Boolean> getReadyStatus() throws ServerDisconnectedException, IOException;

    /**
     * Sends a chat message.
     * @param gameId the ID of the game.
     * @param message the message to be sent.
     * @throws ServerDisconnectedException if the server is disconnected.
     * @throws IOException if an I/O error occurs.
     */
    public abstract void sendChatMessage(int gameId, String message) throws ServerDisconnectedException, IOException;
}
