package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.UnlinkedCardException;

import java.io.Serializable;

/**
 * This class is used to link the front and the back of the same card
 * @param <T> Need the class of the card, allow to use the same class for all card types
 */
public class CardPair<T extends Card> implements Serializable {
    private final T cardFront;
    private final T cardBack;

    /**
     * Constructor
     * @param cardFront front of the card
     * @param cardBack back of the card
     */
    public CardPair(T cardFront, T cardBack)
    {
        this.cardFront = cardFront;
        this.cardBack = cardBack;
    }

    /**
     * Get the other side of the card (front or back)
     * @param side current side
     * @return the other side
     * @throws UnlinkedCardException if the card is unlinked
     */
    public T getOtherSideCard(Side side) throws UnlinkedCardException {
        try{
            if (side.equals(Side.FRONT))
                return cardBack;
            else return cardFront;
        } catch (NullPointerException e) {
            throw new UnlinkedCardException();
        }
    }

    /**
     * Get the id of the card
     * @return the card ID
     */
    public int getCardsId()
    {
        return cardFront.getCardId();
    }
}
