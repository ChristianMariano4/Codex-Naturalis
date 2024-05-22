package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.exceptions.AlreadyExistingPlayerException;
import it.polimi.ingsw.exceptions.AlreadyFourPlayersException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.exceptions.NotExistingPlayerException;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.StarterCard;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static it.polimi.ingsw.model.GameValues.MAX_PLAYER_NUMBER;

/**
 * This class represents the game. It contains the list of players and the table.
 * The game is responsible for adding players to the game and managing the table.
 */
public class Game implements Serializable {
    private final boolean isGameStarted;
    private final int gameId;
    private int numberOfPlayers;
    private final ArrayList<Player> listOfPlayers;
    private Player currentPlayer;
    private final TableTop tableTop;
    private final Deck<ObjectiveCard> objectiveCardDeck;
    private final Deck<StarterCard> availableStarterCards;
    private final ArrayList<Marker> availableMarkers;
    private boolean isGameEnded = false;


    //controller has to create the drawing field before creating the game to create drawingField

    /**
     * Constructor
     * @param gameId the unique ID of the game
     * @param drawingField reference to the drawing field of the game
     * @throws InvalidConstructorDataException when controller didn't properly create Game
     */
    public Game(int gameId, DrawingField drawingField, ArrayList<ObjectiveCard> sharedObjectiveCards, Deck<ObjectiveCard> objectiveCardDeck, Deck<StarterCard> starterCardDeck) throws InvalidConstructorDataException {
        this.isGameStarted = false;
        this.gameId = gameId;
        this.listOfPlayers = new ArrayList<Player>();
        this.numberOfPlayers = 0;
        this.availableMarkers = new ArrayList<Marker>(Arrays.asList(Marker.values()));
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
     * Getter
     * @return unique ID of the game
     */
    public int getGameId() {
        return this.gameId;
    }

    /**
     *  Adds a player to the game
     * @param player the player you want to add
     * @throws AlreadyExistingPlayerException when the player we want to add already exists in the game
     * @throws AlreadyFourPlayersException when the game already contains the maximum amount of players
     */
    public void addPlayer(Player player, int desiredNumberOfPlayers) throws AlreadyExistingPlayerException, AlreadyFourPlayersException {
        if(!(listOfPlayers.contains(player))) {
            if(numberOfPlayers < desiredNumberOfPlayers) {
                this.listOfPlayers.add(player);
                numberOfPlayers++;
                tableTop.addPlayerField(player);
                player.setGame(this);
            } else {
                throw new AlreadyFourPlayersException();
            }
        } else {
            throw new AlreadyExistingPlayerException();
        }
    }

    /**
     * Getter
     * @return the number of players currently present in the game
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * Getter
     * @return a copy of the list of player in the game
     */
    public ArrayList<Player> getListOfPlayers()
    {
        return new ArrayList<Player>(listOfPlayers);
    }

    /**
     *  Getter
     * @return the tabletop associated with the game
     */
    public TableTop getTableTop() {
        return tableTop;
    }
    public void shufflePlayers() {
        Collections.shuffle(listOfPlayers);
    }

    public Deck<ObjectiveCard> getObjectiveCardDeck() {
        return objectiveCardDeck;
    }

    public Deck<StarterCard> getAvailableStarterCards() {
        return availableStarterCards;
    }

    public ArrayList<Marker> getAvailableMarkers() {
        return availableMarkers;
    }

    public void removeMarker(Marker marker) {
        this.availableMarkers.remove(marker);
    }
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean getgameStarted() {
        return isGameStarted;
    }

    public Player getPlayer(String username) throws NotExistingPlayerException{
        for(Player player : listOfPlayers) {
            if(player.getUsername().equals(username)) {
                return player;
            }
        }
        throw new NotExistingPlayerException();
    }

    public boolean getIsGameEnded()
    {
        return this.isGameEnded;
    }
    public void setIsGameEnded(boolean isGameEnded)
    {
        this.isGameEnded = isGameEnded;
    }
}