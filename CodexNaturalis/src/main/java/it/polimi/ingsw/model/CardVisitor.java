package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.CardType;
import it.polimi.ingsw.model.cards.*;

import java.io.Serializable;

/**
 * Interface of the Visitor Design Pattern
 */
public interface CardVisitor {
    CardInfo visitResourceCard(ResourceCard card);
    CardInfo visitGoldCard(GoldCard card);
    CardInfo visitTripleObjectiveCard(TripleObjectiveCard card);
    CardInfo visitPositionalObjectiveCard(PositionalObjectiveCard card);
    CardInfo visitResourceObjectiveCard(ResourceObjectiveCard card);
    CardInfo visitStarterCard(StarterCard card);
}
