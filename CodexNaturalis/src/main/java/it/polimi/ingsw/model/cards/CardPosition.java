package it.polimi.ingsw.model.cards;

import java.io.Serializable;

/**
 * The class represent the position of the card in the playerField
 */
public record CardPosition(Card card, int positionX, int positionY) implements Serializable {
    /**
     * Constructor
     * @param card a card
     * @param positionX X position of the card
     * @param positionY Y position of the card
     */
    public CardPosition {
    }

    /**
     * Get the card
     * @return the card
     */
    @Override
    public Card card() {
        return card;
    }

    /**
     * Get the X position
     * @return the X position
     */
    @Override
    public int positionX() {
        return positionX;
    }

    /**
     * Get the Y position
     * @return the Y position
     */
    @Override
    public int positionY() {
        return positionY;
    }

}
