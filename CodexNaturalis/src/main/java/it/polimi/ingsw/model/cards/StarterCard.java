package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;

import java.util.ArrayList;
import java.util.HashMap;

public class StarterCard extends Card{

    private final ArrayList<Resource> centralResources;
    private final HashMap<AngleOrientation, Angle> angles;

    /**
     * Constructor
     * @param cardId id associated to the card
     * @param currentSide indicates one of the two side of the card (front or back)
     * @param centralResources array of resources present in the center of the card
     * @param angles hashMap of angle linked with their position
     * @throws InvalidConstructorDataException if an invalid parameter is given
     */
    public StarterCard(int cardId, Side currentSide, ArrayList<Resource> centralResources, HashMap<AngleOrientation, Angle> angles) throws InvalidConstructorDataException {
        super(cardId, currentSide);
        try {
            this.centralResources = new ArrayList<>(centralResources); //controller can pass list with NONE value
            this.angles = new HashMap<>(angles);
        }
        catch(Exception e)
        {
            throw new InvalidConstructorDataException();
        }

    }

    /**
     *
     * @return the array of resources present in the center of the card
     */
    public ArrayList<Resource> getCentralResources()
    {
        return new ArrayList<Resource>(this.centralResources);
    }

}


