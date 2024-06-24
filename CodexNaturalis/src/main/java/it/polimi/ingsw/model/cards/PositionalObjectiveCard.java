package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.PositionalType;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.model.CardVisitor;

import java.io.Serializable;

/**
 * This class represents a positional objective card, which can be diagonal or L-shaped.
 */
public class PositionalObjectiveCard extends ObjectiveCard implements Serializable {
    private final Resource cardColor;
    private final AngleOrientation orientation;
    private final PositionalType positionalType;

    /**
     * Constructor
     * @param cardId unique card ID
     * @param currentSide indicates one of the two side of the card (front or back)
     * @param points number of the points given by the card
     * @param cardColor color type of the card
     * @param orientation ??
     * @param positionalType type of win condition required by the card
     */
    public PositionalObjectiveCard(int cardId, Side currentSide, int points, Resource cardColor, AngleOrientation orientation, PositionalType positionalType) {
        super(cardId, currentSide, points);
        this.cardColor = cardColor;
        this.orientation = orientation;
        this.positionalType = positionalType;
    }

    /**
     * Get the color of the card
     * @return the color of the card
     */
    public Resource getCardColor()
    {
        return cardColor;
    }

    /**
     * Get the angle orientation of the card
     * @return the angle orientation of the card
     */
    public AngleOrientation getOrientation()
    {
        return orientation;
    }

    /**
     * Get the positionalType of the card
     * @return the positionalType of the card
     */
    public PositionalType getPositionalType() {
        return positionalType;
    }

    /**
     * Method used to accept a visitor
     * @param visitor the visitor
     * @return the card info
     */
    @Override
    public CardInfo accept(CardVisitor visitor) {
        return visitor.visitPositionalObjectiveCard(this);
    }
}
