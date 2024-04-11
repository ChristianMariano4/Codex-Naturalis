package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Side;

public class TripleObjectiveCard extends ObjectiveCard{
    /**
     * Constructor
     *
     * @param cardId      id of the card
     * @param currentSide one of the two side of the card (front or back)
     * @param points      points given by a card for each specific pattern on the player field
     */
    public TripleObjectiveCard(int cardId, Side currentSide, int points) {
        super(cardId, currentSide, points);
    }
}
