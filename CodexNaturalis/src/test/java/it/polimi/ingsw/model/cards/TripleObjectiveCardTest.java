package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Side;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TripleObjectiveCardTest {

    TripleObjectiveCard toc;
    static int cardID = 123;
    static Side choosenSide = Side.FRONT;
    static int points = 10;

    @BeforeEach
    void TripleObjectiveCardInit() {
        toc = new TripleObjectiveCard(cardID, choosenSide, points);
    }

    @Test
    void shouldReturnPoints() {
        Assertions.assertEquals(points, toc.getPoints());
    }
}