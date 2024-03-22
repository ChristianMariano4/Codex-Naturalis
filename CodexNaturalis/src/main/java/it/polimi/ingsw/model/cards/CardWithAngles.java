package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;

import java.util.HashMap;
import java.util.LinkedList;

public class CardWithAngles extends Card{

    private final LinkedList<Resource> centralResources;
    private final HashMap<AngleOrientation, Angle> angles;

    //TODO: add exceptions for empty sets
    public CardWithAngles(int cardId, Side currentSide, LinkedList<Resource> centralResources, HashMap<AngleOrientation, Angle> angles) {
        super(cardId, currentSide);
        this.centralResources = new LinkedList<Resource>();
        this.angles = new HashMap<AngleOrientation, Angle>();
        try {
            this.centralResources.addAll(centralResources);
        } catch (NullPointerException e) { }
        try {
            this.angles.putAll(angles);
        } catch (NullPointerException e) { }
    }

    public LinkedList<Resource> getCentralResources()
    {
        return new LinkedList<Resource>(this.centralResources);
    }

}


