package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.CardType;
import it.polimi.ingsw.model.cards.CardInfo;
import it.polimi.ingsw.model.cards.GoldCard;

public interface CardVisitor {
    CardInfo visitGoldCard(GoldCard card);
}
