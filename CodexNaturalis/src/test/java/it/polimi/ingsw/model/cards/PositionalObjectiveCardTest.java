package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.model.CardVisitorImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PositionalObjectiveCardTest {

    private PositionalObjectiveCard positionalObjectiveCard;
    private CardVisitorImpl cardVisitor;

    @BeforeEach
    void setUp() {
        positionalObjectiveCard = new PositionalObjectiveCard(1, Side.FRONT, 2, Resource.ANIMAL, AngleOrientation.BOTTOMLEFT, PositionalType.DIAGONAL);
        cardVisitor = mock(CardVisitorImpl.class);
    }

    @Test
    void shouldReturnCardColor() {
        assertEquals(Resource.ANIMAL, positionalObjectiveCard.getCardColor());
    }

    @Test
    void shouldReturnOrientation() {
        assertEquals(AngleOrientation.BOTTOMLEFT, positionalObjectiveCard.getOrientation());
    }

    @Test
    void shouldReturnPositionalType() {
        assertEquals(PositionalType.DIAGONAL, positionalObjectiveCard.getPositionalType());
    }

    @Test
    void shouldAcceptVisitor() {
        when(cardVisitor.visitPositionalObjectiveCard(positionalObjectiveCard)).thenReturn(new CardInfo(CardType.POSITIONALOBJECTIVE));
        assertNotNull(positionalObjectiveCard.accept(cardVisitor));
    }
}