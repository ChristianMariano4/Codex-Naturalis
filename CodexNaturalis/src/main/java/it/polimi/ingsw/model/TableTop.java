package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyExistingPlayerException;
import it.polimi.ingsw.exceptions.AlreadyFourPlayersException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;

import java.util.HashMap;

import static it.polimi.ingsw.model.GameValues.MAX_PLAYER_NUMBER;

public class TableTop {
    private final DrawingField drawingField;
    private final HashMap<Player, PlayerField> playerFieldHashMap;
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
    public DrawingField getDrawingField()
    {
        return drawingField;
    }
    public HashMap<Player, PlayerField> getPlayerFieldHashMap()
    {
        return new HashMap<>(playerFieldHashMap);
    }
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
