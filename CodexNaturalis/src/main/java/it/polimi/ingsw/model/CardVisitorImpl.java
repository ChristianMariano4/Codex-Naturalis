package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.CardType;
import it.polimi.ingsw.model.cards.*;

import java.io.Serializable;

/**
 * The implementation of the CardVisitor used to implement the Visitor Design Pattern
 */
public class CardVisitorImpl implements CardVisitor, Serializable {
    /**
     * Method used to visit a gold card
     * @param card the card to visit
     * @return the card info
     */
    @Override
    public CardInfo visitGoldCard(GoldCard card) {
        return new CardInfo(CardType.GOLD, card.getRequirements(), card.getGoldPointCondition());
    }
    /**
     * Method used to visit a resource card
     * @param card the card to visit
     * @return the card info
     */
    @Override
    public CardInfo visitResourceCard(ResourceCard card) {
        return new CardInfo(CardType.RESOURCE);
    }

    /**
     * Method used to visit a triple objective card
     * @param card the card to visit
     * @return the card info
     */
    @Override
    public CardInfo visitTripleObjectiveCard(TripleObjectiveCard card) {
        return new CardInfo(CardType.TRIPLEOBJECTIVE);

    }

    /**
     * Method used to visit a positional objective card
     * @param card the card to visit
     * @return the card info
     */
    @Override
    public CardInfo visitPositionalObjectiveCard(PositionalObjectiveCard card) {
        return new CardInfo(CardType.POSITIONALOBJECTIVE, card.getCardColor(), card.getOrientation(), card.getPositionalType());
    }

    /**
     * Method used to visit a resource objective card
     * @param card the card to visit
     * @return the card info
     */
    @Override
    public CardInfo visitResourceObjectiveCard(ResourceObjectiveCard card) {
        return new CardInfo(CardType.RESOURCEOBJECTIVE, card.getCardResource());
    }

    /**
     * Method used to visit a starter card
     * @param card the card to visit
     * @return the card info
     */
    @Override
    public CardInfo visitStarterCard(StarterCard card) {
        return new CardInfo(CardType.STARTER);
    }

}
