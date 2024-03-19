package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;

public class ResourceObjectiveCard extends ObjectiveCard{

    private final Resource cardResource;
    public ResourceObjectiveCard(int cardId, Side currentSide, int points, Resource cardResource) {
        super(cardId, currentSide, points);
        this.cardResource = cardResource;
    }
    public Resource getCardResource()
    {
        return cardResource;
    }
}
