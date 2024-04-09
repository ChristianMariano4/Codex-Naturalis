package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.GoldCard;

public interface CardVisitor {
    void visitGoldCard(GoldCard card);
}
