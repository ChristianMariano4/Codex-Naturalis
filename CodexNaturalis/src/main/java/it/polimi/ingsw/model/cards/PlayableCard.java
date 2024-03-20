package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;

import java.util.HashMap;
import java.util.LinkedList;

public class PlayableCard extends CardWithAngles{

    private final Resource cardColor;

    public PlayableCard(int cardId, Side currentSide, LinkedList<Resource> centralResource, HashMap<AngleOrientation, Angle> angles, Resource cardColor) {
        super(cardId, currentSide, centralResource, angles);
        this.cardColor = cardColor;
    }
    public Resource getCardColor()
    {
        return cardColor;
    }
}
