package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.*;

/**
 * Interface of the Visitor Design Pattern
 */
public interface CardVisitor {
    /**
     * Method used to visit a resource card
     * @param card the card to visit
     * @return the card info
     */
    CardInfo visitResourceCard(ResourceCard card);

    /**
     * Method used to visit a gold card
     * @param card the card to visit
     * @return the card info
     */
    CardInfo visitGoldCard(GoldCard card);

    /**
     * Method used to visit a triple objective card
     * @param card the card to visit
     * @return the card info
     */
    CardInfo visitTripleObjectiveCard(TripleObjectiveCard card);

    /**
     * Method used to visit a positional objective card
     * @param card the card to visit
     * @return the card info
     */
    CardInfo visitPositionalObjectiveCard(PositionalObjectiveCard card);

    /**
     * Method used to visit a resource objective card
     * @param card the card to visit
     * @return the card info
     */
    CardInfo visitResourceObjectiveCard(ResourceObjectiveCard card);

    /**
     * Method used to visit a starter card
     * @param card the card to visit
     * @return the card info
     */
    CardInfo visitStarterCard(StarterCard card);
}
