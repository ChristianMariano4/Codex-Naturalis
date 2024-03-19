package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Side;

public class ObjectiveCard extends Card{
    private final int points;
    public ObjectiveCard(int cardId, Side currentSide, int points) {
        super(cardId, currentSide);
        this.points = points;
    }
    public int getPoints() {
        return points;
    }
}
