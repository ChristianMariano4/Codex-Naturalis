package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.CardTypeMismatchException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.CardVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a started card, which is given to the player at the beginning of the game.
 */
public class PlayableCard extends Card{

    private final ArrayList<Resource> centralResources;
    private final HashMap<AngleOrientation, Angle> angles;
    private final Resource cardColor;
    private final int points;
    /**
     * Constructor
     * @param cardId id associated to the card
     * @param currentSide indicates one of the two side of the card (front or back)
     * @param centralResources array of resources present in the center of the card
     * @param angles hashMap of angle linked with their position
     * @throws InvalidConstructorDataException if an invalid parameter is given
     */
    public PlayableCard(int cardId, Side currentSide, List<Resource> centralResources, Map<AngleOrientation, Angle> angles, Resource cardColor, int points) throws InvalidConstructorDataException {
        super(cardId, currentSide);
        try {
            this.centralResources = new ArrayList<>(centralResources); //controller can pass list with NONE value
            this.angles = new HashMap<>(angles);
            this.cardColor = cardColor;
            this.points = points;
        }
        catch(Exception e)
        {
            throw new InvalidConstructorDataException();
        }

    }



    /**
     * Getter
     * @return the array of resources present in the center of the card
     */
    public ArrayList<Resource> getCentralResources()
    {
        return new ArrayList<Resource>(this.centralResources);
    }
    /**
     * Getter
     * @return the color of the card, associated with a resource
     */
    public Resource getCardColor()
    {
        return cardColor;
    }

    /**
     * Getter
     * @return points given by a card by positioning it
     */
    public int getPoints()
    {
        return points;
    }

    public Angle getAngle(AngleOrientation angleOrientation)
    {
        return angles.get(angleOrientation);
    }

}


