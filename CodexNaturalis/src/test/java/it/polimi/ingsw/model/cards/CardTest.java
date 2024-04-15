package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.NoCardAddedException;
import it.polimi.ingsw.exceptions.UnlinkedCardException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    Card c;
    static int cardId = 123;
    static Side currentSide;
    private Card otherSideCard;

    @BeforeEach
    void cardInit() {
        //Card Constructor method
    }

    @Test
    void shouldReturnCardId() {
        Assertions.assertEquals(cardId, c.getCardId());
    }

    @Test
    void shouldReturnCurrentSide() {
        Assertions.assertEquals(currentSide, c.getCurrentSide());
    }

    @Test
    void shouldReturnNullOtherSideCard() {
        Assertions.assertThrows(NoCardAddedException.class, () -> {c.getOtherSideCard();});
    }

    @Test
    void shouldSetOtherSideCard() throws UnlinkedCardException {
        c.setOtherSideCard(otherSideCard);
        Assertions.assertEquals(otherSideCard, c.getOtherSideCard());
    }
}