package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.GoldPointCondition;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.CardTypeMismatchException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.CardVisitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents a gold card, which is a special type of card that requires a specific set of resources to be played.
 */
public class GoldCard extends PlayableCard implements Visitable, Serializable {

    private final ArrayList<Resource> requirements;
    private final GoldPointCondition goldPointCondition;
    private final HashMap<Resource, Integer> requirementsMap;

    //if the card has no requirements, controller has to pass requirements with NONE value

    /**
     * Constructor
     * @param cardId id associated to the card
     * @param currentSide indicates one of the two side of the card (front or back)
     * @param centralResource list of the resources in the middle of the card
     * @param angles maps the position of the angle into the angle object
     * @param cardColor one of the four colors, each associated with a resource
     * @param requirements list of the resources needed to play a card
     * @param goldPointCondition condition to satisfied to obtain the points given by the card
     * @param points points given by a card by positioning it
     * @throws InvalidConstructorDataException if an invalid parameter is given
     */
    public GoldCard(int cardId, Side currentSide, ArrayList<Resource> centralResource, HashMap<AngleOrientation, Angle> angles, Resource cardColor, ArrayList<Resource> requirements, GoldPointCondition goldPointCondition, int points) throws InvalidConstructorDataException {
        super(cardId, currentSide, centralResource, angles, cardColor, points);
        try {
            this.requirements = new ArrayList<>(requirements);
            this.requirementsMap = requirementsArrayToMap();
            this.goldPointCondition = goldPointCondition;
        }
        catch(Exception e)
        {
            throw new InvalidConstructorDataException();
        }
    }
    @Override
    public CardInfo accept(CardVisitor visitor)  {
        return visitor.visitGoldCard(this);
    }

    /**
     * Getter
     * @return the list of the resources needed to play a card
     */
    public ArrayList<Resource> getRequirements()
    {
        return new ArrayList<Resource>(requirements);
    }

    /**
     * Getter
     * @return the condition to satisfied to obtain the points given by the card
     */
    public GoldPointCondition getGoldPointCondition()
    {
        return goldPointCondition;
    }

    private HashMap<Resource, Integer> requirementsArrayToMap(){
        HashMap<Resource, Integer> requirementsMap = new HashMap<>();
        for(Resource resource : requirements){
            if(requirementsMap.containsKey(resource)){
                requirementsMap.put(resource, requirementsMap.get(resource) + 1);
            } else {
                requirementsMap.put(resource, 1);
            }
        }
        return requirementsMap;
    }

}
