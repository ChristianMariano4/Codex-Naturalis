package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.CardType;
import it.polimi.ingsw.model.cards.CardInfo;
import it.polimi.ingsw.model.cards.GoldCard;

public class CardVisitorImpl implements CardVisitor {

    @Override
    public CardInfo visitGoldCard(GoldCard card) {
        return new CardInfo(CardType.GOLD, card.getRequirements(), card.getGoldPointCondition());
    }
}
