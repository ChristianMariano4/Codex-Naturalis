package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.ResourceCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DrawingFieldTest {

    private DrawingField drawingField;
    private Deck<GoldCard> goldCardDeck;
    private Deck<ResourceCard> resourceCardDeck;
    HashMap<DrawPosition, GoldCard> discoveredGoldCards;
    HashMap<DrawPosition, ResourceCard> discoveredResourceCards;


    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() throws DeckIsEmptyException {
        goldCardDeck = mock(Deck.class);
        resourceCardDeck = mock(Deck.class);
        discoveredResourceCards = mock(HashMap.class);
        discoveredGoldCards = mock(HashMap.class);
        when(goldCardDeck.getTopCard()).thenReturn(mock(GoldCard.class));
        when(resourceCardDeck.getTopCard()).thenReturn(mock(ResourceCard.class));
        when(goldCardDeck.seeTopCard()).thenReturn(mock(GoldCard.class));
        when(resourceCardDeck.seeTopCard()).thenReturn(mock(ResourceCard.class));
        drawingField = new DrawingField(goldCardDeck, resourceCardDeck);
    }

    @Test
    void shouldDrawCardFromGoldCardDeck() throws DeckIsEmptyException {
        assertNotNull(drawingField.drawCardFromGoldCardDeck(DrawPosition.FROMDECK));
    }

    @Test
    void shouldDrawCardFromResourceCardDeck() throws DeckIsEmptyException {
        assertNotNull(drawingField.drawCardFromResourceCardDeck(DrawPosition.FROMDECK));
    }

    @Test
    void shouldThrowDeckIsEmptyExceptionWhenGoldDeckIsEmpty() throws DeckIsEmptyException {
        when(goldCardDeck.getTopCard()).thenThrow(new DeckIsEmptyException());
        assertThrows(DeckIsEmptyException.class, () -> drawingField.drawCardFromGoldCardDeck(DrawPosition.FROMDECK));
    }

    @Test
    void shouldThrowDeckIsEmptyExceptionWhenResourceDeckIsEmpty() throws DeckIsEmptyException {
        when(resourceCardDeck.getTopCard()).thenThrow(new DeckIsEmptyException());
        assertThrows(DeckIsEmptyException.class, () -> drawingField.drawCardFromResourceCardDeck(DrawPosition.FROMDECK));
    }

    @Test
    void shouldSetDiscoveredCards() throws DeckIsEmptyException {
        drawingField.setDiscoveredCards();
        verify(goldCardDeck, times(2)).getTopCard();
        verify(resourceCardDeck, times(2)).getTopCard();
    }

    @Test
    void shouldDrawCardFromDiscoveredGoldCards() throws DeckIsEmptyException {
        when(goldCardDeck.getTopCard()).thenReturn(mock(GoldCard.class), (GoldCard) null);
        drawingField.setDiscoveredCards();
        assertNotNull(drawingField.drawCardFromGoldCardDeck(DrawPosition.LEFT));
    }

    @Test
    void shouldDrawCardFromDiscoveredResourceCards() throws DeckIsEmptyException {
        when(resourceCardDeck.getTopCard()).thenReturn(mock(ResourceCard.class), (ResourceCard) null);
        drawingField.setDiscoveredCards();
        assertNotNull(drawingField.drawCardFromResourceCardDeck(DrawPosition.LEFT));
    }

    @Test
    void shouldPutNullInResourceCardPositionWhenDrawingAndResourceCardDeckIsEmpty() throws DeckIsEmptyException {
        drawingField.setDiscoveredCards();
        when(resourceCardDeck.isEmpty()).thenReturn(true);
        assertNotNull(drawingField.drawCardFromResourceCardDeck(DrawPosition.LEFT));
        assertThrows(DeckIsEmptyException.class, () -> drawingField.drawCardFromResourceCardDeck(DrawPosition.LEFT));
    }

    @Test
    void shouldPutNullInGoldCardPositionWhenDrawingAndGoldCardDeckIsEmpty() throws DeckIsEmptyException {
        drawingField.setDiscoveredCards();
        when(goldCardDeck.isEmpty()).thenReturn(true);
        assertNotNull(drawingField.drawCardFromGoldCardDeck(DrawPosition.LEFT));
        assertThrows(DeckIsEmptyException.class, () -> drawingField.drawCardFromGoldCardDeck(DrawPosition.LEFT));
    }

    @Test
    void shouldThrowDeckIsEmptyExceptionWhenDrawingFromEmptyDiscoveredGoldCards() throws DeckIsEmptyException {
        when(goldCardDeck.getTopCard()).thenReturn(null);
        drawingField.setDiscoveredCards();
        assertThrows(DeckIsEmptyException.class, () -> drawingField.drawCardFromGoldCardDeck(DrawPosition.LEFT));
    }

    @Test
    void shouldThrowDeckIsEmptyExceptionWhenDrawingFromEmptyDiscoveredResourceCards() throws DeckIsEmptyException {
        when(resourceCardDeck.getTopCard()).thenReturn(null);
        drawingField.setDiscoveredCards();
        assertThrows(DeckIsEmptyException.class, () -> drawingField.drawCardFromResourceCardDeck(DrawPosition.LEFT));
    }

    @Test
    void shouldReturnCorrectDiscoveredGoldCards() throws DeckIsEmptyException {
        drawingField.setDiscoveredCards();
        HashMap<DrawPosition, GoldCard> discoveredGoldCards = drawingField.getDiscoveredGoldCards();
        assertNotNull(discoveredGoldCards);
        assertEquals(2, discoveredGoldCards.size());
        assertTrue(discoveredGoldCards.containsKey(DrawPosition.LEFT));
        assertTrue(discoveredGoldCards.containsKey(DrawPosition.RIGHT));
    }

    @Test
    void shouldReturnCorrectDiscoveredResourceCards() throws DeckIsEmptyException {
        drawingField.setDiscoveredCards();
        HashMap<DrawPosition, ResourceCard> discoveredResourceCards = drawingField.getDiscoveredResourceCards();
        assertNotNull(discoveredResourceCards);
        assertEquals(2, discoveredResourceCards.size());
        assertTrue(discoveredResourceCards.containsKey(DrawPosition.LEFT));
        assertTrue(discoveredResourceCards.containsKey(DrawPosition.RIGHT));
    }

    @Test
    void shouldReturnTopCard() throws DeckIsEmptyException {
        assertNotNull(drawingField.seeTopResourceCard());
        assertNotNull(drawingField.seeTopGoldCard());
    }

    @Test
    void shouldReturnTrueIfBothDeckAreEmpty(){
        when(goldCardDeck.isEmpty()).thenReturn(true);
        when(resourceCardDeck.isEmpty()).thenReturn(true);
        assertTrue(drawingField.getBothDecksEmpty());
    }

    @Test
    void shouldReturnTrueIfNoCardsAreLeft() throws DeckIsEmptyException {
        when(goldCardDeck.getTopCard()).thenReturn(mock(GoldCard.class), (GoldCard) null);

        when(goldCardDeck.isEmpty()).thenReturn(true);
        when(resourceCardDeck.isEmpty()).thenReturn(true);
        when(discoveredGoldCards.isEmpty()).thenReturn(true);
        when(discoveredResourceCards.isEmpty()).thenReturn(true);

        assertTrue(drawingField.getNoCardsLeft());
    }
}
  