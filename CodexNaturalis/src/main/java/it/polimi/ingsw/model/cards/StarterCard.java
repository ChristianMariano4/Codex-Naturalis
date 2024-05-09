package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.CardVisitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StarterCard extends PlayableCard implements Serializable {
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
    public StarterCard(int cardId, Side currentSide, List<Resource> centralResources, Map<AngleOrientation, Angle> angles, Resource cardColor, int points) throws InvalidConstructorDataException {
        super(cardId, currentSide, centralResources, angles, cardColor, points);
    }


    @Override
    public CardInfo accept(CardVisitor visitor) {
        return visitor.visitStarterCard(this);
    }
}
