package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;

import java.util.ArrayList;
import java.util.HashMap;

public class StarterCard extends PlayableCard {
    /**
     * Constructor
     *
     * @param cardId           id associated to the card
     * @param currentSide      indicates one of the two side of the card (front or back)
     * @param centralResources array of resources present in the center of the card
     * @param angles           hashMap of angle linked with their position
     * @param cardColor
     * @param points
     * @throws InvalidConstructorDataException if an invalid parameter is given
     */
    public StarterCard(int cardId, Side currentSide, ArrayList<Resource> centralResources, HashMap<AngleOrientation, Angle> angles, Resource cardColor, int points) throws InvalidConstructorDataException {
        super(cardId, currentSide, centralResources, angles, cardColor, points);
    }


}
