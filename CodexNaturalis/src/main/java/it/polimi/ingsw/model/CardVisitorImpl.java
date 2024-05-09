package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.CardType;
import it.polimi.ingsw.model.cards.*;

import java.io.Serializable;

public class CardVisitorImpl implements CardVisitor, Serializable {

    @Override
    public CardInfo visitGoldCard(GoldCard card) {
        return new CardInfo(CardType.GOLD, card.getRequirements(), card.getGoldPointCondition());
    }
    @Override
    public CardInfo visitResourceCard(ResourceCard card) {
        return new CardInfo(CardType.RESOURCE);
    }

    @Override
    public CardInfo visitTripleObjectiveCard(TripleObjectiveCard card) {
        return new CardInfo(CardType.TRIPLEOBJECTIVE);

    }

    @Override
    public CardInfo visitPositionalObjectiveCard(PositionalObjectiveCard card) {
        return new CardInfo(CardType.POSITIONALOBJECTIVE, card.getCardColor(), card.getOrientation(), card.getPositionalType());
    }

    @Override
    public CardInfo visitResourceObjectiveCard(ResourceObjectiveCard card) {
        return new CardInfo(CardType.RESOURCEOBJECTIVE, card.getCardResource());
    }

    @Override
    public CardInfo visitStarterCard(StarterCard card) {
        return new CardInfo(CardType.STARTER);
    }

}
