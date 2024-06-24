package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.GameStatus;
import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.exceptions.AlreadyExistingPlayerException;
import it.polimi.ingsw.exceptions.AlreadyMaxNumberOfPlayersException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.exceptions.NotExistingPlayerException;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.StarterCard;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * This class represents the game. It contains the list of players and the table.
 * The game is responsible for adding players to the game and managing the table.
 */
public class Game implements Serializable {
    private GameStatus gameStatus;
    private final int gameId;
    private final ArrayList<Player> listOfPlayers;
    private Player currentPlayer;
    private final TableTop tableTop;
    private final Deck<ObjectiveCard> objectiveCardDeck;
    private final Deck<StarterCard> availableStarterCards;
    private final ArrayList<Marker> availableMarkers;
    private boolean isGameEnded = false;
    private boolean isGameEndedForDisconnection = false;

    /**
     * Constructor
     * @param gameId the unique ID of the game
     * @param drawingField reference to the drawing field of the game
     * @throws InvalidConstructorDataException when controller didn't properly create Game
     */
    public Game(int gameId, DrawingField drawingField, ArrayList<ObjectiveCard> sharedObjectiveCards, Deck<ObjectiveCard> objectiveCardDeck, Deck<StarterCard> starterCardDeck) throws InvalidConstructorDataException {
        this.gameStatus = GameStatus.LOBBY_CREATED;
        this.gameId = gameId;
        this.listOfPlayers = new ArrayList<>();
        this.availableMarkers = new ArrayList<>(Arrays.asList(Marker.values()));
        availableMarkers.remove(Marker.BLACK);
        try {
            this.tableTop = new TableTop(drawingField, sharedObjectiveCards);
            this.objectiveCardDeck = objectiveCardDeck;
            this.availableStarterCards = starterCardDeck;
        }
        catch(Exception e)
        {
            throw new InvalidConstructorDataException();
        }
    }

    /**
     * Get the unique ID of the game
     * @return unique ID of the game
     */
    public int getGameId() {
        return this.gameId;
    }

    /**
     * Adds a player to the game
     * @param player the player you want to add
     * @throws AlreadyExistingPlayerException when the player we want to add already exists in the game
     * @throws AlreadyMaxNumberOfPlayersException when the game already contains the maximum amount of players
     */
    public void addPlayer(Player player, int desiredNumberOfPlayers) throws AlreadyExistingPlayerException, AlreadyMaxNumberOfPlayersException {
        if(!(listOfPlayers.stream().map(Player::getUsername).toList().contains(player.getUsername()))) {
            if(listOfPlayers.size() < desiredNumberOfPlayers) {
                this.listOfPlayers.add(player);
                player.setGame(this);
            } else {
                throw new AlreadyMaxNumberOfPlayersException();
            }
        } else {
            throw new AlreadyExistingPlayerException();
        }
    }

    /**
     * Get the list of players in the game
     * @return a copy of the list of player in the game
     */
    public ArrayList<Player> getListOfPlayers()
    {
        return new ArrayList<>(listOfPlayers);
    }

    /**
     * Get the tabletop
     * @return the tabletop associated with the game
     */
    public TableTop getTableTop() {
        return tableTop;
    }

    /**
     * Shuffle the list of players
     */
    public void shufflePlayers() {
        Collections.shuffle(listOfPlayers);
    }

    /**
     * Get objective card deck
     * @return return the objective card deck
     */
    public Deck<ObjectiveCard> getObjectiveCardDeck() {
        return objectiveCardDeck;
    }

    /**
     * Get yet available starter cards
     * @return the available starter cards
     */
    public Deck<StarterCard> getAvailableStarterCards() {
        return availableStarterCards;
    }

    /**
     * Get yet available markers
     * @return the available markers
     */
    public ArrayList<Marker> getAvailableMarkers() {
        return availableMarkers;
    }

    /**
     * Get the current player that is playing
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Set the current player that is going to start the turn
     * @param currentPlayer to set as current player
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Get the game status
     * @return the game status
     */
    public GameStatus getGameStatus() {
        return this.gameStatus;
    }

    /**
     * Set the game status
     * @param gameStatus to set as gameStatus
     */
    public void setGameStatus(GameStatus gameStatus){
        this.gameStatus = gameStatus;
    }

    /**
     * Get the player with the given username
     * @param username of the player
     * @return the player with the right username
     * @throws NotExistingPlayerException if the player with the passed username not exists
     */
    public Player getPlayer(String username) throws NotExistingPlayerException{
        for(Player player : listOfPlayers) {
            if(player.getUsername().equals(username)) {
                return player;
            }
        }
        throw new NotExistingPlayerException();
    }

    /**
     * See if the game is ended
     * @return true if the game is ended, false otherwise
     */
    public boolean getIsGameEnded()
    {
        return this.isGameEnded;
    }

    /**
     * Set game status (ended or not)
     * @param isGameEnded to set as isGameEnded value, true if the game is ended, false if is not ended
     */
    public void setIsGameEnded(boolean isGameEnded)
    {
        this.isGameEnded = isGameEnded;
    }

    /**
     * See if the game is ended by a disconnection
     * @return return true if the game is ended by a disconnection
     */
    public boolean getIsGameEndedForDisconnection()
    {
        return this.isGameEndedForDisconnection;
    }

    /**
     * Set game status (ended or not) for disconnection
     * @param isGameEndedForDisconnection to set as isGameEndedForDisconnection value, true if the game is ended by a disconnection, false if is not ended by a disconnection
     */
    public void setIsGameEndedForDisconnection(boolean isGameEndedForDisconnection)
    {
        this.isGameEndedForDisconnection = isGameEndedForDisconnection;
    }

    /**
     * Remove player with the given username from the game
     * @param username of the player to remove
     */
    public void removePlayer(String username) {
        for(Player player : listOfPlayers) {
            if(player.getUsername().equals(username)) {
                listOfPlayers.remove(player);
                break;
            }
        }
    }
}