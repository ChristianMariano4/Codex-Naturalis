package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.model.CardVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardPairTest {
    private CardPair<Card> cardPair;
    private Card frontCard;
    private Card backCard;

    @BeforeEach
    public void setUp() {
        frontCard = new Card(1, Side.FRONT) {

            @Override
            public CardInfo accept(CardVisitor visitor) {
                return null;
            }
        };
        backCard = new Card(1, Side.BACK) {

            @Override
            public CardInfo accept(CardVisitor visitor) {
                return null;
            }
        };
        cardPair = new CardPair<>(frontCard, backCard);
    }

    @Test
    public void shouldReturnBackCardWhenFrontSideIsGiven() {
        assertEquals(backCard, cardPair.getOtherSideCard(Side.FRONT));
    }

    @Test
    public void shouldReturnFrontCardWhenBackSideIsGiven() {
        assertEquals(frontCard, cardPair.getOtherSideCard(Side.BACK));
    }

    @Test
    public void shouldReturnNullWhenNullSideIsGiven() {
        assertThrows(NullPointerException.class, () -> cardPair.getOtherSideCard(null));
    }

    @Test
    public void shouldReturnCardIdOfFrontCard() {
        assertEquals(1, cardPair.getCardsId());
    }
    @Test
    public void testGetCardsId() {
        assertEquals(1, cardPair.getCardsId());
    }
}