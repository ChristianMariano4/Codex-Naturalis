package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.CardType;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.model.CardVisitorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TripleObjectiveCardTest {

    private TripleObjectiveCard tripleObjectiveCard;
    private CardVisitorImpl cardVisitor;

    @BeforeEach
    void setUp() {
        tripleObjectiveCard = new TripleObjectiveCard(1, Side.FRONT, 2);
        cardVisitor = mock(CardVisitorImpl.class);
    }

    @Test
    void shouldReturnCardPoints() {
        assertEquals(2, tripleObjectiveCard.getPoints());
    }

    @Test
    void shouldAcceptVisitor() {
        when(cardVisitor.visitTripleObjectiveCard(tripleObjectiveCard)).thenReturn(new CardInfo(CardType.TRIPLEOBJECTIVE));
        assertNotNull(tripleObjectiveCard.accept(cardVisitor));
    }
}