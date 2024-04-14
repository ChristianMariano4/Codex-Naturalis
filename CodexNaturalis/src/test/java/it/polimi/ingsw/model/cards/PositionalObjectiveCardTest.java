package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.PositionalType;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionalObjectiveCardTest {

    PositionalObjectiveCard poc;
    static AngleOrientation angle = AngleOrientation.BOTTOMLEFT;
    static Side side = Side.FRONT;
    static int points = 2;
    static int cardID = 123;
    static Resource resource = Resource.ANIMAL;
    static PositionalType type = PositionalType.DIAGONAL;

    @BeforeEach
    void PositionalObjectiveCardInit() {
        poc = new PositionalObjectiveCard(cardID, side, points, resource, angle, type);
    }
    @Test
    void shouldReturnCardColor() {
        Assertions.assertEquals(resource, poc.getCardColor());
    }

    @Test
    void shouldReturnOrientation() {
        Assertions.assertEquals(angle, poc.getOrientation());
    }

    @Test
    void ShouldReturnPositionalType() {
        Assertions.assertEquals(type, poc.getPositionalType());
    }
}