package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.CardType;
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

class ResourceCardTest {

    private ResourceCard resourceCard;
    private CardVisitorImpl cardVisitor;

    @BeforeEach
    void setUp() throws InvalidConstructorDataException {
        ArrayList<Resource> centralResource = new ArrayList<>(Arrays.asList(Resource.FUNGI, Resource.FUNGI, Resource.ANIMAL));
        HashMap<AngleOrientation, Angle> angles = new HashMap<>();
        angles.put(AngleOrientation.TOPLEFT, new Angle(true, Resource.FUNGI, null));
        resourceCard = new ResourceCard(1, Side.FRONT, centralResource, angles, Resource.FUNGI, 1);
        cardVisitor = mock(CardVisitorImpl.class);
    }
    @Test
    void shouldAcceptVisitor() {
        when(cardVisitor.visitResourceCard(resourceCard)).thenReturn(new CardInfo(CardType.RESOURCE));
        assertNotNull(resourceCard.accept(cardVisitor));
    }

}