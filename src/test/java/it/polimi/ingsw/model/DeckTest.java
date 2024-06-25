package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.model.cards.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeckTest {

    private Deck<Card> deck;

    @BeforeEach
    void setUp() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(mock(Card.class));
        cards.add(mock(Card.class));
        deck = new Deck<>(cards);
    }

    @Test
    void shouldInitializeDeck() {
        assertFalse(deck.isEmpty());
    }

    @Test
    void shouldReturnTopCard() throws DeckIsEmptyException {
        assertNotNull(deck.getTopCard());
        assertNotNull(deck.seeTopCard());
    }

    @Test
    void shouldThrowDeckIsEmptyException() throws DeckIsEmptyException {
        deck.getTopCard();
        deck.getTopCard();
        assertThrows(DeckIsEmptyException.class, () -> deck.getTopCard());
        assertThrows(DeckIsEmptyException.class, () -> deck.seeTopCard());

    }

    @Test
    void shouldShuffleDeck() {
        assertDoesNotThrow(() -> deck.shuffleDeck());
    }

    @Test
    void shouldInitializeDeckWithValidCards() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(mock(Card.class));
        assertDoesNotThrow(() -> new Deck<>(cards));
    }

    @Test
    void shouldAddCardSuccessfully() {
        Card card = mock(Card.class);
        assertDoesNotThrow(() -> deck.addCard(card));
        assertFalse(deck.isEmpty());
    }

    @Test
    void shouldThrowNullPointerExceptionWhenAddNullCard() {
        assertThrows(NullPointerException.class, () -> deck.addCard(null));
    }
}