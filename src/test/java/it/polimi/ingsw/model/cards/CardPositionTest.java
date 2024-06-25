package it.polimi.ingsw.model.cards;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CardPositionTest {

    @Test
    void getCard_returnsCorrectCard() {
        Card card = mock(Card.class);
        CardPosition cardPosition = new CardPosition(card, 1, 1);
        assertEquals(card, cardPosition.getCard());
    }

    @Test
    void getPositionX_returnsCorrectPositionX() {
        Card card = mock(Card.class);
        CardPosition cardPosition = new CardPosition(card, 1, 1);
        assertEquals(1, cardPosition.getPositionX());
    }

    @Test
    void getPositionY_returnsCorrectPositionY() {
        Card card = mock(Card.class);
        CardPosition cardPosition = new CardPosition(card, 1, 1);
        assertEquals(1, cardPosition.getPositionY());
    }

    @Test
    void getPositionX_returnsNegativePositionX() {
        Card card = mock(Card.class);
        CardPosition cardPosition = new CardPosition(card, -1, 1);
        assertEquals(-1, cardPosition.getPositionX());
    }

    @Test
    void getPositionY_returnsNegativePositionY() {
        Card card = mock(Card.class);
        CardPosition cardPosition = new CardPosition(card, 1, -1);
        assertEquals(-1, cardPosition.getPositionY());
    }
}