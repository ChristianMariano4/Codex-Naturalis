package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyExistingPlayerException;
import it.polimi.ingsw.exceptions.AlreadyFourPlayersException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static it.polimi.ingsw.model.GameValues.MAX_PLAYER_NUMBER;

/**
 * TableTop class is the class that contains the drawing field and all the players' fields.
 * It is responsible for adding players to the game and managing the drawing field.
 */
public class TableTop {
    private final DrawingField drawingField;
    private final HashMap<Player, PlayerField> playerFieldHashMap;
    private final List<Card> sharedObjectiveCards;

    /**
     * Constructor
     * @param drawingField is the reference to the drawingField
     * @throws InvalidConstructorDataException when controller didn't properly create TableTop
     */
    public TableTop(DrawingField drawingField, ArrayList<Card> sharedObjectiveCards) throws InvalidConstructorDataException {
        try {
            this.drawingField = drawingField;
            playerFieldHashMap = new HashMap<Player, PlayerField>();
            this.sharedObjectiveCards = new ArrayList<>(sharedObjectiveCards);
        }
        catch(Exception e)
        {
            throw new InvalidConstructorDataException();
        }
    }

    /**
     * Getter
     * @return the reference to the drawingField
     */
    public DrawingField getDrawingField() {
        return drawingField;
    }

    /**
     * Getter
     * @return all the players and their playerFiled
     */
    public HashMap<Player, PlayerField> getPlayerFieldHashMap() {
        return new HashMap<>(playerFieldHashMap);
    }

    /**
     * Adds a player to the game
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

    /**
     * Getter
     * @return the shared objective cards
     */
    public List<Card> getSharedObjectiveCards(){
        return sharedObjectiveCards;
    }



}
