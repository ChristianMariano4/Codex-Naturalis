package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.client.ClientHandlerInterface;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The ServerRMIInterface is used by the client to communicate with the server.
 * This interface is implemented by the server's remote object.
 * It serves as the skeleton (proxy of the server) in the RMI communication.
 * It extends the Remote interface, which is required for RMI communication.
 * It exposes methods that the client can call, i.e. those of the controller.
 */
public interface ServerRMIInterface extends Remote {

    /**
     * Method used by the client to tell the server how to contact it.
     * @param client The client's remote object.
     * @throws IOException If an I/O error occurs.
     */
    void connect(ClientHandlerInterface client) throws IOException;

    /**
     * Creates a new game with the specified number of players and adds the client to it.
     * @param client The client's remote object.
     * @param numberOfPlayers The number of players in the game.
     * @return The ID of the created game.
     * @throws IOException If an I/O error occurs.
     * @throws NotExistingPlayerException If the player does not exist.
     * @throws InterruptedException If the thread is interrupted.
     */
    int createGame(ClientHandlerInterface client, int numberOfPlayers) throws IOException, NotExistingPlayerException, InterruptedException;

    /**
     * Returns a list of available games.
     * @return A list of available games.
     * @throws RemoteException If a remote error occurs.
     */
    ArrayList<Game> getAvailableGames() throws RemoteException;

    /**
     * Adds a player to a game.
     * @param gameId The ID of the game.
     * @param username The username of the player.
     * @param client The client's remote object.
     * @return The game to which the player was added.
     * @throws RemoteException If a remote error occurs.
     * @throws GameAlreadyStartedException If the game has already started.
     */
    Game addPlayerToGame(int gameId, String username, ClientHandlerInterface client) throws RemoteException, GameAlreadyStartedException;

    /**
     * Sets the player as ready for the game.
     * @param gameId The ID of the game.
     * @param username The username of the player.
     * @return A list of player IDs who are ready.
     * @throws IOException If an I/O error occurs.
     * @throws DeckIsEmptyException If the deck is empty.
     * @throws NotExistingPlayerException If the player does not exist.
     * @throws InterruptedException If the thread is interrupted.
     * @throws NotEnoughPlayersException If there are not enough players.
     */
    ArrayList<Integer> setReady(int gameId, String username) throws IOException, DeckIsEmptyException, NotExistingPlayerException, InterruptedException, NotEnoughPlayersException;

    /**
     * Returns the ready status of all players in the game.
     * @param gameId The ID of the game.
     * @return A map of usernames to their ready status.
     * @throws RemoteException If a remote error occurs.
     */
    HashMap<String, Boolean> getReadyStatus(int gameId) throws RemoteException;

    /**
     * Subscribes the client to a game.
     * @param client The client's remote object.
     * @param gameId The ID of the game.
     * @throws IOException If an I/O error occurs.
     * @throws GameAlreadyStartedException If the game has already started.
     * @throws GameNotFoundException If the game does not exist.
     */
    void subscribe(ClientHandlerInterface client, int gameId) throws IOException, GameAlreadyStartedException, GameNotFoundException;

    /**
     * Retrieves information about a specific card.
     * @param card The card to retrieve information about.
     * @param gameId The ID of the game.
     * @return The information about the card.
     * @throws RemoteException If a remote error occurs.
     */
    CardInfo getCardInfo(Card card, int gameId) throws RemoteException;

    /**
     * Retrieves a playable card by its ID.
     * @param gameId The ID of the game.
     * @param cardId The ID of the card.
     * @return The playable card.
     * @throws RemoteException If a remote error occurs.
     */
    PlayableCard getPlayableCardById(int gameId, int cardId) throws RemoteException;

    /**
     * Retrieves a player by their username.
     * @param gameId The ID of the game.
     * @param username The username of the player.
     * @return The player.
     * @throws RemoteException If a remote error occurs.
     * @throws NotExistingPlayerException If the player does not exist.
     */
    Player getPlayer(int gameId, String username) throws RemoteException, NotExistingPlayerException;

    /**
     * Sets the marker for a player.
     * @param player The player.
     * @param gameId The ID of the game.
     * @param marker The marker to be set.
     * @throws RemoteException If a remote error occurs.
     * @throws NotAvailableMarkerException If the marker is not available.
     * @throws NotExistingPlayerException If the player does not exist.
     */
    void setMarker(Player player, int gameId, Marker marker) throws RemoteException, NotAvailableMarkerException, NotExistingPlayerException;

    /**
     * Sets the side of the starter card for a player.
     * @param gameId The ID of the game.
     * @param player The player.
     * @param starterCard The starter card.
     * @param side The side to be set.
     * @throws RemoteException If a remote error occurs.
     * @throws NotExistingPlayerException If the player does not exist.
     */
    void setStarterCardSide(int gameId, Player player,StarterCard starterCard, Side side) throws RemoteException, NotExistingPlayerException;

    /**
     * Allows a player to play a card.
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
     * @throws NotTurnException If it's not the player's turn.
     */
    Player playCard(int gameId, String username, PlayableCard card, PlayableCard otherCard, AngleOrientation orientation) throws RemoteException, NotExistingPlayerException, InvalidCardPositionException, RequirementsNotMetException, CardTypeMismatchException, AngleAlreadyLinkedException, NotTurnException;

    /**
     * Sets the secret objective card for a player.
     * @param gameId The ID of the game.
     * @param player The player.
     * @param objectiveCard The objective card to be set.
     * @throws RemoteException If a remote error occurs.
     * @throws NotExistingPlayerException If the player does not exist.
     */
    void setSecretObjectiveCard(int gameId, Player player, ObjectiveCard objectiveCard) throws RemoteException, NotExistingPlayerException;

    /**
     * Allows a player to draw a card.
     * @param gameId The ID of the game.
     * @param username The username of the player.
     * @param cardType The type of the card to be drawn.
     * @param drawPosition The position of the draw.
     * @throws RemoteException If a remote error occurs.
     * @throws NotTurnException If it's not the player's turn.
     * @throws NotExistingPlayerException If the player does not exist.
     * @throws AlreadyThreeCardsInHandException If the player already has three cards in hand.
     * @throws DeckIsEmptyException If the deck is empty.
     */
    void drawCard(int gameId, String username, CardType cardType, DrawPosition drawPosition) throws RemoteException, NotTurnException, NotExistingPlayerException, AlreadyThreeCardsInHandException, DeckIsEmptyException;

    /**
     * Allows a player to end their turn.
     * @param gameId The ID of the game.
     * @param username The username of the player.
     * @throws RemoteException If a remote error occurs.
     * @throws NotExistingPlayerException If the player does not exist.
     * @throws CardTypeMismatchException If the card type is mismatched.
     */
    void endTurn(int gameId, String username) throws RemoteException, NotExistingPlayerException, CardTypeMismatchException;

    /**
     * Sets the username for a player.
     * @param username The username to be set.
     * @throws IOException If an I/O error occurs.
     * @throws InvalidUsernameException If the username is invalid.
     */
    void setUsername(String username) throws IOException, InvalidUsernameException;

    /**
     * Retrieves the other side of a card.
     * @param gameId The ID of the game.
     * @param card The card.
     * @return The other side of the card.
     * @throws RemoteException If a remote error occurs.
     */
    PlayableCard getOtherSideCard(int gameId, PlayableCard card) throws RemoteException;

    /**
     * Retrieves the other side of a gold card.
     * @param gameId The ID of the game.
     * @param card The gold card.
     * @return The other side of the gold card.
     * @throws RemoteException If a remote error occurs.
     */
    GoldCard getOtherSideCard(int gameId, GoldCard card) throws RemoteException;

    /**
     * Retrieves the other side of a ResourceCard.
     * @param gameId The ID of the game.
     * @param card The ResourceCard.
     * @return The other side of the ResourceCard.
     * @throws RemoteException If a remote error occurs.
     */
    ResourceCard getOtherSideCard(int gameId, ResourceCard card) throws RemoteException;

    /**
     * Retrieves the other side of a StarterCard.
     * @param gameId The ID of the game.
     * @param card The StarterCard.
     * @return The other side of the StarterCard.
     * @throws RemoteException If a remote error occurs.
     */
    StarterCard getOtherSideCard(int gameId, StarterCard card) throws RemoteException;

    /**
     * Retrieves the other side of an ObjectiveCard.
     * @param gameId The ID of the game.
     * @param card The ObjectiveCard.
     * @return The other side of the ObjectiveCard.
     * @throws RemoteException If a remote error occurs.
     */
    ObjectiveCard getOtherSideCard(int gameId, ObjectiveCard card) throws RemoteException;

    /**
     * Sends a heartbeat signal to the client.
     * @param time The time of the heartbeat.
     * @param client The client's remote object.
     * @throws RemoteException If a remote error occurs.
     */
    void sendHeartbeat(long time, ClientHandlerInterface client) throws RemoteException;

    /**
     * Reconnects a player to a game.
     * @param gameId The ID of the game.
     * @param username The username of the player.
     * @param client The client's remote object.
     * @return The game to which the player was reconnected.
     * @throws RemoteException If a remote error occurs.
     * @throws NotExistingPlayerException If the player does not exist.
     */
    Game reconnectPlayerToGame(int gameId, String username, ClientHandlerInterface client) throws RemoteException, NotExistingPlayerException;

    /**
     * Allows a player to quit a game.
     * @param gameId The ID of the game.
     * @param client The client's remote object.
     * @throws RemoteException If a remote error occurs.
     * @throws NotExistingPlayerException If the player does not exist.
     */
    void quitGame(int gameId, ClientHandlerInterface client) throws RemoteException, NotExistingPlayerException;

    /**
     * Sends a chat message to all players in a game.
     * @param gameId The ID of the game.
     * @param message The message to be sent.
     * @param username The username of the player sending the message.
     * @throws RemoteException If a remote error occurs.
     */
    void sendChatMessage(int gameId, String message, String username) throws RemoteException;
}
