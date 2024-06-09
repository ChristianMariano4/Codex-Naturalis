package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.CardVisitor;

public interface Visitable {
    CardInfo accept(CardVisitor visitor);
}
