package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.UnlinkedCardException;

import java.io.Serializable;

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
     * Getter
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
     * Getter
     * @return the card ID
     */
    public int getCardsId()
    {
        return cardFront.getCardId();
    }
}
