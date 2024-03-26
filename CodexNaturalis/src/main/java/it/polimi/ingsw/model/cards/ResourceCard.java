package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.InvalidCardConstructorData;

import java.util.HashMap;
import java.util.ArrayList;

public class ResourceCard extends StarterCard {

    private final Resource cardColor;
    private final int points;

    public ResourceCard(int cardId, Side currentSide, ArrayList<Resource> centralResource, HashMap<AngleOrientation, Angle> angles, Resource cardColor, int points) throws InvalidCardConstructorData {
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
