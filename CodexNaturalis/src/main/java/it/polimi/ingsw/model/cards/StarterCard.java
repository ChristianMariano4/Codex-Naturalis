package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.CardVisitor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * The class represent the starter card, witch the player have as first card in the playerField
 */
public class StarterCard extends PlayableCard implements Serializable {
    /**
     * Constructor
     * @param cardId id of the card
     * @param currentSide one of the two side of the card (front or back)
     * @param centralResources list of the resources in the middle of the card
     * @param angles maps the position of the angle into the angle object
     * @param cardColor one of the four colors, each associated with a resource
     * @param points points given by a card by positioning it
     * @throws InvalidConstructorDataException if an invalid parameter is given
     */
    public StarterCard(int cardId, Side currentSide, List<Resource> centralResources, Map<AngleOrientation, Angle> angles, Resource cardColor, int points) throws InvalidConstructorDataException {
        super(cardId, currentSide, centralResources, angles, cardColor, points);
    }

    /**
     * Method used to accept a visitor
     * @param visitor the visitor
     * @return the card info
     */
    @Override
    public CardInfo accept(CardVisitor visitor) {
        return visitor.visitStarterCard(this);
    }
}
