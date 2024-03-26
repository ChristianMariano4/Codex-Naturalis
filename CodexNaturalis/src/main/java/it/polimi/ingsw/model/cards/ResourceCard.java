package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;

import java.util.HashMap;
import java.util.LinkedList;

public class ResourceCard extends StarterCard {

    private final Resource cardColor;
    private final int points;

    public ResourceCard(int cardId, Side currentSide, LinkedList<Resource> centralResource, HashMap<AngleOrientation, Angle> angles, Resource cardColor, int points) {
        super(cardId, currentSide, centralResource, angles);
        this.cardColor = cardColor;
        this.points = points;
    }
    public Resource getCardColor()
    {
        return cardColor;
    }
    public int getPoints()
    {
        return points;
    }
}
