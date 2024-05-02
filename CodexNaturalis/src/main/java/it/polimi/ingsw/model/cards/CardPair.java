package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.UnlinkedCardException;

import java.io.Serializable;

public class CardPair<T extends Card> implements Serializable {
    private final T cardFront;
    private final T cardBack;
    public CardPair(T cardFront, T cardBack)
    {
        this.cardFront = cardFront;
        this.cardBack = cardBack;
    }
    public T getOtherSideCard(Side side) throws UnlinkedCardException {
        try{
            if (side.equals(Side.FRONT))
                return cardBack;
            else return cardFront;
        } catch (NullPointerException e) {
            throw new UnlinkedCardException();
        }
    }
    public int getCardsId()
    {
        return cardFront.getCardId();
    }
}
