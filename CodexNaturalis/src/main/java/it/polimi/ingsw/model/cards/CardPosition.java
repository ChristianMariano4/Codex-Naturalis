package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Side;

import java.io.Serializable;

public class CardPosition implements Serializable {
    private Card card;
    private int positionX;
    private int positionY;

    /**
     * Constructor
     * @param card
     * @param positionX X position of the card
     * @param positionY Y position of the card
     */
    public CardPosition(Card card, int positionX, int positionY) {
        this.card = card;
        this. positionX = positionX;
        this.positionY = positionY;
    }

    /**
     * Getter the card
     * @return
     */
    public Card getCard()
    {
        return card;
    }

    /**
     * Getter
     * @return the X position
     */
    public int getPositionX()
    {
        return positionX;
    }

    /**
     * Getter
     * @return the Y position
     */
    public int getPositionY()
    {
        return positionY;
    }

}
