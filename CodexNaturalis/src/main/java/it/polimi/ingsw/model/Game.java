package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyExistingPlayerException;
import it.polimi.ingsw.exceptions.AlreadyFourPlayersException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;


import java.util.ArrayList;

import static it.polimi.ingsw.model.GameValues.MAX_PLAYER_NUMBER;

public class Game {
    private final int gameId;
    private int numberOfPlayers;
    private final ArrayList<Player> listOfPlayers;
    private final TableTop tableTop;


    //controller has to create the drawing field before creating the game to create drawingField

    /**
     *
     * @param gameId the unique ID of the game
     * @param drawingField reference to the drawing field of the game
     * @throws InvalidConstructorDataException when controller didn't properly create Game
     */
    public Game(int gameId, DrawingField drawingField) throws InvalidConstructorDataException {
        this.gameId = gameId;
        this.listOfPlayers = new ArrayList<Player>();
        this.numberOfPlayers = 0;
        try {
            this.tableTop = new TableTop(drawingField);
        }
        catch(Exception e)
        {
            throw new InvalidConstructorDataException();
        }
    }

    /**
     *
     * @return unique ID of the game
     */
    public int getGameId() {
        return this.gameId;
    }

    /**
     *
     * @param player the player you want to add
     * @throws AlreadyExistingPlayerException when the player we want to add already exists in the game
     * @throws AlreadyFourPlayersException when the game already contains the maximum amount of players
     */
    public void addPlayer(Player player) throws AlreadyExistingPlayerException, AlreadyFourPlayersException {
        if(!(listOfPlayers.contains(player))) {
            if(numberOfPlayers < MAX_PLAYER_NUMBER) {
                this.listOfPlayers.add(player);
                numberOfPlayers++;
                tableTop.addPlayerField(player);
            } else {
                throw new AlreadyFourPlayersException();
            }
        } else {
            throw new AlreadyExistingPlayerException();
        }
    }

    /**
     *
     * @return the number of players currently present in the game
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     *
     * @return a copy of the list of player in the game
     */
    public ArrayList<Player> getListOfPlayers()
    {
        return new ArrayList<Player>(listOfPlayers);
    }

    /**
     *
     * @return the tabletop associated with the game
     */
    public TableTop getTableTop() {
        return tableTop;
    }
}
