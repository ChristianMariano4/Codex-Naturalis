package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;

public class ResourceObjectiveCard extends TripleObjectiveCard {

    private final Resource cardResource;

    /**
     *
     * @param cardId card ID
     * @param currentSide indicates one of the two side of the card (front or back)
     * @param points number of the points given by the card
     * @param cardResource the resource required by the card
     */
    public ResourceObjectiveCard(int cardId, Side currentSide, int points, Resource cardResource) {
        super(cardId, currentSide, points);
        this.cardResource = cardResource;
    }

    /**
     *
     * @return the resource of the card
     */
    public Resource getCardResource()
    {
        return cardResource;
    }
}
