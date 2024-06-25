package it.polimi.ingsw.model.cards;

import java.io.Serializable;

/**
 * The class represent the position of the card in the playerField
 */
public class CardPosition implements Serializable {
    private final Card card;
    private final int positionX;
    private final int positionY;

    /**
     * Constructor
     * @param card the card
     * @param positionX X position of the card
     * @param positionY Y position of the card
     */
    public CardPosition(Card card, int positionX, int positionY) {
        this.card = card;
        this. positionX = positionX;
        this.positionY = positionY;
    }

    /**
     * Get the card
     * @return the card
     */
    public Card getCard()
    {
        return card;
    }

    /**
     * Get the X position
     * @return the X position
     */
    public int getPositionX()
    {
        return positionX;
    }

    /**
     * Get the Y position
     * @return the Y position
     */
    public int getPositionY()
    {
        return positionY;
    }
}
