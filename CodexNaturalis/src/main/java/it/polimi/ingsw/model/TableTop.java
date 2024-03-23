package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyExistingPlayer;
import it.polimi.ingsw.exceptions.AlreadyFourPlayers;

import java.util.HashMap;

import static it.polimi.ingsw.model.GameValues.MAX_PLAYER_NUMBER;

public class TableTop {
    private final DrawingField drawingField;
    private final HashMap<Player, PlayerField> playerFieldHashMap;
    public TableTop(DrawingField drawingField)
    {
        this.drawingField = drawingField;
        playerFieldHashMap = new HashMap<Player, PlayerField>();
    }
    public DrawingField getDrawingField()
    {
        return drawingField;
    }
    public HashMap<Player, PlayerField> getPlayerFieldHashMap()
    {
        return playerFieldHashMap;
    }
    public void addPlayerField(Player player) throws AlreadyExistingPlayer, AlreadyFourPlayers
    {
        if(playerFieldHashMap.entrySet().size() >= MAX_PLAYER_NUMBER)
        {
            throw new AlreadyFourPlayers();
        }
        if(playerFieldHashMap.containsKey(player))
        {
            throw new AlreadyExistingPlayer();
        }
        playerFieldHashMap.put(player, player.getPlayerField());
    }

}
