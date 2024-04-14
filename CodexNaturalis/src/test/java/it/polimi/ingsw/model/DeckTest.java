package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.exceptions.NoCardAddedException;
import it.polimi.ingsw.model.cards.Card;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    static int number_of_cards; //to assign
    Deck d;

    @BeforeEach
    void DeckInit() {
        //Deck controller method
    }
    @Test
    void isEmptyShouldReturnFalse() throws DeckIsEmptyException {
        for(int i = 0; i < number_of_cards; i++) {
            d.getTopCard();
            Assertions.assertFalse(d.isEmpty());
        }

    }
    @Test
    void shouldThrowDeckIsEmptyException() throws DeckIsEmptyException {
        for(int i = 0; i < number_of_cards; i++) {
            d.getTopCard();
        }
        Assertions.assertThrows(NoCardAddedException.class, () -> {d.getTopCard();});
    }
}