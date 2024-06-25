package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.GoldPointCondition;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.CardVisitor;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents a gold card, which is a special type of card that requires a specific set of resources to be played.
 */
public class GoldCard extends PlayableCard implements Visitable, Serializable {
    private final ArrayList<Resource> requirements;
    private final GoldPointCondition goldPointCondition;

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
            this.goldPointCondition = goldPointCondition;
        }
        catch(Exception e)
        {
            throw new InvalidConstructorDataException();
        }
    }

    /**
     * Method used to accept a visitor
     * @param visitor the visitor
     * @return the card info
     */
    @Override
    public CardInfo accept(CardVisitor visitor)  {
        return visitor.visitGoldCard(this);
    }

    /**
     * Get the requirements of the card
     * @return the list of the resources needed to play a card
     */
    public ArrayList<Resource> getRequirements()
    {
        return new ArrayList<>(requirements);
    }

    /**
     * Get the condition to satisfied to obtain the points given by the card
     * @return the condition to satisfied to obtain the points given by the card
     */
    public GoldPointCondition getGoldPointCondition()
    {
        return goldPointCondition;
    }

    /**
     * Get the requirements of the card
     * @return the requirements of the card
     */
    public ArrayList<Pair<Resource, Integer>> getRequirementsPairList(){
        ArrayList<Pair<Resource, Integer>> requirementsPairList = new ArrayList<>();
        int counter1 = 0;
        int counter2 = 0;

        Resource res1 = requirements.getFirst();
        Resource res2 = null;
        for(Resource resource : requirements){
            if(resource == res1) {
                counter1++;
            } else {
                res2 = resource;
                counter2++;
            }
        }
        requirementsPairList.add(new Pair<>(res1, counter1));

        if(counter2 != 0) {
            requirementsPairList.add(new Pair<>(res2, counter2));
        }

        return requirementsPairList;
    }

}
