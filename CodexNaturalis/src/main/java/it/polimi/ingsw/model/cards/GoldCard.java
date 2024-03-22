package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.GoldPointCondition;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class GoldCard extends PlayableCard{

    private final ArrayList<Resource> requirements;
    private final GoldPointCondition goldPointCondition;

    public GoldCard(int cardId, Side currentSide, LinkedList<Resource> centralResource, HashMap<AngleOrientation, Angle> angles, Resource cardColor, ArrayList<Resource> requirements, GoldPointCondition goldPointCondition, int points) {
        super(cardId, currentSide, centralResource, angles, cardColor, points);
        this.requirements = requirements;
        this.goldPointCondition = goldPointCondition;
    }

    public ArrayList<Resource> getRequirements()
    {
        return new ArrayList<Resource>(requirements);
    }
    public GoldPointCondition getGoldPointCondition()
    {
        return goldPointCondition;
    }


}
