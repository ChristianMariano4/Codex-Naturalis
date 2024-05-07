package it.polimi.ingsw.model.cards;
import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.CardVisitorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StarterCardTest {

    private StarterCard starterCard;
    private CardVisitorImpl cardVisitor;

    @BeforeEach
    void setUp() throws InvalidConstructorDataException {
        ArrayList<Resource> centralResources = new ArrayList<>(Arrays.asList(Resource.FUNGI, Resource.FUNGI, Resource.ANIMAL));
        HashMap<AngleOrientation, Angle> angles = new HashMap<>();
        angles.put(AngleOrientation.TOPRIGHT, new Angle(true, Resource.valueOf("PLANT"), null));
        angles.put(AngleOrientation.TOPLEFT, new Angle(false, Resource.valueOf("NONE"), null));
        angles.put(AngleOrientation.BOTTOMRIGHT, new Angle(true, Resource.valueOf("QUILL"), null));
        angles.put(AngleOrientation.BOTTOMLEFT, new Angle(true, Resource.valueOf("NONE"), null));
        starterCard = new StarterCard(1, Side.FRONT, centralResources, angles, Resource.FUNGI, 1);
        cardVisitor = mock(CardVisitorImpl.class);
    }
    @Test
    void shouldAcceptVisitor() {
        assertNull(starterCard.accept(cardVisitor));
    }
}