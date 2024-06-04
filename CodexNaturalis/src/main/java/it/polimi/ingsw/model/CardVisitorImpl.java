package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.CardType;
import it.polimi.ingsw.model.cards.*;

import java.io.Serializable;

public class CardVisitorImpl implements CardVisitor, Serializable {
    /**
     * Visitor
     * @param card goldCard passed to the visitor
     * @return the cardInfo of the goldCard passed
     */
    @Override
    public CardInfo visitGoldCard(GoldCard card) {
        return new CardInfo(CardType.GOLD, card.getRequirements(), card.getGoldPointCondition());
    }
    /**
     * Visitor
     * @param card resourceCard passed to the visitor
     * @return the cardInfo of the resourceCard passed
     */
    @Override
    public CardInfo visitResourceCard(ResourceCard card) {
        return new CardInfo(CardType.RESOURCE);
    }
    /**
     * Visitor
     * @param card TripleObjectiveCard passed to the visitor
     * @return the cardInfo of the TripleObjectiveCard passed
     */
    @Override
    public CardInfo visitTripleObjectiveCard(TripleObjectiveCard card) {
        return new CardInfo(CardType.TRIPLEOBJECTIVE);

    }
    /**
     * Visitor
     * @param card PositionalObjectiveCard passed to the visitor
     * @return the cardInfo of the PositionalObjectiveCard passed
     */
    @Override
    public CardInfo visitPositionalObjectiveCard(PositionalObjectiveCard card) {
        return new CardInfo(CardType.POSITIONALOBJECTIVE, card.getCardColor(), card.getOrientation(), card.getPositionalType());
    }
    /**
     * Visitor
     * @param card ResourceObjectiveCard passed to the visitor
     * @return the cardInfo of the ResourceObjectiveCard passed
     */
    @Override
    public CardInfo visitResourceObjectiveCard(ResourceObjectiveCard card) {
        return new CardInfo(CardType.RESOURCEOBJECTIVE, card.getCardResource());
    }
    /**
     * Visitor
     * @param card StarterCard passed to the visitor
     * @return the cardInfo of the StarterCard passed
     */
    @Override
    public CardInfo visitStarterCard(StarterCard card) {
        return new CardInfo(CardType.STARTER);
    }

}
