package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Side;

import java.io.Serializable;

/**
 * This class represents the objective card that has the three different objects
 * (quill, inkwell, manuscript) as pattern to satisfy to obtain three points
 */
public abstract class ObjectiveCard extends Card implements Serializable {
    private final int points;

    /**
     * Constructor
     * @param cardId id of the card
     * @param currentSide one of the two side of the card (front or back)
     * @param points points given by a card for each specific pattern on the player field
     */
    public ObjectiveCard(int cardId, Side currentSide, int points) {
        super(cardId, currentSide);
        this.points = points;
    }

    /**
     * Get the points given by a card for each specific pattern on the player field
     * @return the points given by a card for each specific pattern on the player field
     */
    public int getPoints() {
        return points;
    }
}
