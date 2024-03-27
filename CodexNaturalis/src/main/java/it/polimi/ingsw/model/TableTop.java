package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyExistingPlayerException;
import it.polimi.ingsw.exceptions.AlreadyFourPlayersException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;

import java.util.HashMap;

import static it.polimi.ingsw.model.GameValues.MAX_PLAYER_NUMBER;

public class TableTop {
    private final DrawingField drawingField;
    private final HashMap<Player, PlayerField> playerFieldHashMap;

    /**
     * Constructor
     * @param drawingField is the reference to the drawingField
     * @throws InvalidConstructorDataException when controller didn't properly create TableTop
     */
    public TableTop(DrawingField drawingField) throws InvalidConstructorDataException {
        try {
            this.drawingField = drawingField;
            playerFieldHashMap = new HashMap<Player, PlayerField>();
        }
        catch(Exception e)
        {
            throw new InvalidConstructorDataException();
        }
    }

    /**
     *
     * @return the reference to the drawingField
     */
    public DrawingField getDrawingField()
    {
        return drawingField;
    }

    /**
     *
     * @return all the players and their playerFiled
     */
    public HashMap<Player, PlayerField> getPlayerFieldHashMap()
    {
        return new HashMap<>(playerFieldHashMap);
    }

    /**
     *
     * @param player is the reference to the player
     * @throws AlreadyExistingPlayerException when the player is already in the HashMap
     * @throws AlreadyFourPlayersException when the number of players is already four
     */
    public void addPlayerField(Player player) throws AlreadyExistingPlayerException, AlreadyFourPlayersException
    {
        if(playerFieldHashMap.entrySet().size() >= MAX_PLAYER_NUMBER)
        {
            throw new AlreadyFourPlayersException();
        }
        if(playerFieldHashMap.containsKey(player))
        {
            throw new AlreadyExistingPlayerException();
        }
        playerFieldHashMap.put(player, player.getPlayerField());
    }

}
