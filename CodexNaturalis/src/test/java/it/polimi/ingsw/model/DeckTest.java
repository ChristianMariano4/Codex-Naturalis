package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.cards.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeckTest {

    private Deck<Card> deck;
    private ArrayList<Card> cards;

    @BeforeEach
    void setUp() throws InvalidConstructorDataException {
        cards = new ArrayList<>();
        cards.add(mock(Card.class));
        cards.add(mock(Card.class));
        deck = new Deck<>(cards);
    }

    @Test
    void shouldInitializeDeck() {
        assertFalse(deck.isEmpty());
    }

    @Test
    void shouldThrowInvalidConstructorDataException() {
        assertThrows(InvalidConstructorDataException.class, () -> new Deck<>(null));
    }

    @Test
    void shouldReturnTopCard() throws DeckIsEmptyException {
        assertNotNull(deck.getTopCard());
    }

    @Test
    void shouldThrowDeckIsEmptyException() throws DeckIsEmptyException {
        deck.getTopCard();
        deck.getTopCard();
        assertThrows(DeckIsEmptyException.class, () -> deck.getTopCard());
    }

    @Test
    void shouldShuffleDeck() {
        assertDoesNotThrow(() -> deck.shuffleDeck());
    }
}