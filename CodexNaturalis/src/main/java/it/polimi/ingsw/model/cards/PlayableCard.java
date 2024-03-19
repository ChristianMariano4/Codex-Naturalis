package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;

import java.util.HashMap;
import java.util.LinkedList;

public class PlayableCard extends CardWithAngles{

    public PlayableCard(int cardId, Side currentSide, LinkedList<Resource> centralResource, HashMap<AngleOrientation, Angle> angles) {
        super(cardId, currentSide, centralResource, angles);
    }
}
