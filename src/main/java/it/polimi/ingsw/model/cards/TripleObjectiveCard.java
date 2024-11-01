package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.model.CardVisitor;

import java.io.Serializable;

/**
 * The class represent the ObjectiveCard with 3 different resources
 */
public class TripleObjectiveCard extends ObjectiveCard implements Serializable {
    /**
     * Constructor
     * @param cardId id of the card
     * @param currentSide one of the two side of the card (front or back)
     * @param points points given by a card for each specific pattern on the player field
     */
    public TripleObjectiveCard(int cardId, Side currentSide, int points) {
        super(cardId, currentSide, points);
    }

    /**
     * Method used to accept a visitor
     * @param visitor the visitor
     * @return the card info
     */
    @Override
    public CardInfo accept(CardVisitor visitor) {
        return visitor.visitTripleObjectiveCard(this);
    }
}
