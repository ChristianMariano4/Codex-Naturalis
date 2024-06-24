package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.CardType;
import it.polimi.ingsw.enumerations.GoldPointCondition;
import it.polimi.ingsw.enumerations.PositionalType;
import it.polimi.ingsw.model.cards.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardVisitorImplTest {

    private CardVisitorImpl cardVisitor;
    private GoldCard goldCard;
    private ResourceCard resourceCard;
    private TripleObjectiveCard tripleObjectiveCard;
    private PositionalObjectiveCard positionalObjectiveCard;
    private ResourceObjectiveCard resourceObjectiveCard;
    private StarterCard starterCard;

    @BeforeEach
    void setUp() {
        cardVisitor = new CardVisitorImpl();
        goldCard = mock(GoldCard.class);
        when(goldCard.getGoldPointCondition()).thenReturn(GoldPointCondition.ANGLE);
        resourceCard = mock(ResourceCard.class);
        tripleObjectiveCard = mock(TripleObjectiveCard.class);
        positionalObjectiveCard = mock(PositionalObjectiveCard.class);
        when(positionalObjectiveCard.getPositionalType()).thenReturn(PositionalType.DIAGONAL);
        resourceObjectiveCard = mock(ResourceObjectiveCard.class);
        starterCard = mock(StarterCard.class);
    }

    @Test
    void shouldVisitGoldCard() {
        CardInfo cardInfo = cardVisitor.visitGoldCard(goldCard);
        assertEquals(CardType.GOLD, cardInfo.getCardType());
    }

    @Test
    void shouldVisitResourceCard() {
        CardInfo cardInfo = cardVisitor.visitResourceCard(resourceCard);
        assertEquals(CardType.RESOURCE, cardInfo.getCardType());
    }

    @Test
    void shouldVisitTripleObjectiveCard() {
        CardInfo cardInfo = cardVisitor.visitTripleObjectiveCard(tripleObjectiveCard);
        assertEquals(CardType.TRIPLEOBJECTIVE, cardInfo.getCardType());
    }

    @Test
    void shouldVisitPositionalObjectiveCard() {
        CardInfo cardInfo = cardVisitor.visitPositionalObjectiveCard(positionalObjectiveCard);
        assertEquals(CardType.POSITIONALOBJECTIVE, cardInfo.getCardType());
    }

    @Test
    void shouldVisitResourceObjectiveCard() {
        CardInfo cardInfo = cardVisitor.visitResourceObjectiveCard(resourceObjectiveCard);
        assertEquals(CardType.RESOURCEOBJECTIVE, cardInfo.getCardType());
    }

    @Test
    void shouldVisitStarterCard() {
        CardInfo cardInfo = cardVisitor.visitStarterCard(starterCard);
        assertEquals(CardType.STARTER, cardInfo.getCardType());
    }
}