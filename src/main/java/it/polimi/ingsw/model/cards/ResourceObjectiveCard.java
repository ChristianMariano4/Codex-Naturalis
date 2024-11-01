package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.model.CardVisitor;

import java.io.Serializable;

/**
 * This class represents a resource objective card, which requires the player to have a certain amount of a specific resource.
 */
public class ResourceObjectiveCard extends ObjectiveCard implements Serializable {
    private final Resource cardResource;

    /**
     * Constructor
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
     * Get the resource of the card
     * @return the resource of the card
     */
    public Resource getCardResource()
    {
        return cardResource;
    }

    /**
     * Method used to accept a visitor
     * @param visitor the visitor
     * @return the card info
     */
    @Override
    public CardInfo accept(CardVisitor visitor) {
        return visitor.visitResourceObjectiveCard(this);
    }
}
