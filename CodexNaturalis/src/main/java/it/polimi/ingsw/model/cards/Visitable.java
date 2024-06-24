package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.CardVisitor;

/**
 * Interface of the Visitor Design Pattern
 */
public interface Visitable {
    /**
     * Method used to accept a visitor
     * @param visitor the visitor
     * @return the card info
     */
    CardInfo accept(CardVisitor visitor);
}
