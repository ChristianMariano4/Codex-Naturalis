package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.CardType;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.model.CardVisitorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResourceObjectiveCardTest {

    private ResourceObjectiveCard resourceObjectiveCard;
    private CardVisitorImpl cardVisitor;

    @BeforeEach
    void setUp() {
        resourceObjectiveCard = new ResourceObjectiveCard(1, Side.FRONT, 2, Resource.ANIMAL);
        cardVisitor = mock(CardVisitorImpl.class);
    }

    @Test
    void shouldReturnCardResource() {
        assertEquals(Resource.ANIMAL, resourceObjectiveCard.getCardResource());
    }

    @Test
    void shouldAcceptVisitor() {
        when(cardVisitor.visitResourceObjectiveCard(resourceObjectiveCard)).thenReturn(new CardInfo(CardType.RESOURCEOBJECTIVE));
        assertNotNull(resourceObjectiveCard.accept(cardVisitor));
    }
}