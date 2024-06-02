package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Side;

import java.io.Serializable;

public class CardPosition implements Serializable {
    private Card card;
    private int positionX;
    private int positionY;

    public CardPosition(Card card, int positionX, int positionY) {
        this.card = card;
        this. positionX = positionX;
        this.positionY = positionY;
    }
    public Card getCard()
    {
        return card;
    }

    public int getPositionX()
    {
        return positionX;
    }

    public int getPositionY()
    {
        return positionY;
    }

}
