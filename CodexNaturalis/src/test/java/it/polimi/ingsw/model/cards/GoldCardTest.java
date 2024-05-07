package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.CardVisitorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GoldCardTest {

    private GoldCard goldCard;
    private final Side currentSide = Side.FRONT;
    private final GoldPointCondition goldPointCondition = GoldPointCondition.valueOf("QUILL");
    private final ArrayList<Resource> requirements = new ArrayList<>(Arrays.asList(Resource.FUNGI, Resource.FUNGI, Resource.ANIMAL));

    @BeforeEach
    void goldCardInit() throws InvalidConstructorDataException {
        int points = 1;
        int cardId = 41;
        ArrayList<Resource> centralResources = new ArrayList<>();
        HashMap<AngleOrientation, Angle> angles = new HashMap<>();
        angles.put(AngleOrientation.TOPRIGHT, new Angle(true, Resource.valueOf("NONE"), null));
        angles.put(AngleOrientation.TOPLEFT, new Angle(false, Resource.valueOf("NONE"), null));
        angles.put(AngleOrientation.BOTTOMRIGHT, new Angle(true, Resource.valueOf("QUILL"), null));
        angles.put(AngleOrientation.BOTTOMLEFT, new Angle(true, Resource.valueOf("NONE"), null));
        goldCard = new GoldCard(cardId, currentSide, centralResources, angles, Resource.FUNGI, requirements, goldPointCondition, points);
    }

    @Test
    void shouldReturnGoldPointCondition() {
        assertEquals(goldPointCondition, goldCard.getGoldPointCondition());
    }

    @Test
    void shouldReturnRequirements() {
        assertEquals(requirements, goldCard.getRequirements());
    }

    @Test
    void shouldThrowExceptionForInvalidConstructorData() {
        int points = 1;
        int cardId = 41;
        ArrayList<Resource> centralResources = new ArrayList<>();
        HashMap<AngleOrientation, Angle> angles = new HashMap<>();
        angles.put(AngleOrientation.TOPRIGHT, new Angle(true, Resource.valueOf("NONE"), null));
        angles.put(AngleOrientation.TOPLEFT, new Angle(false, Resource.valueOf("NONE"), null));
        angles.put(AngleOrientation.BOTTOMRIGHT, new Angle(true, Resource.valueOf("QUILL"), null));
        angles.put(AngleOrientation.BOTTOMLEFT, new Angle(true, Resource.valueOf("NONE"), null));
        assertThrows(InvalidConstructorDataException.class, () -> new GoldCard(cardId, currentSide, centralResources, angles, Resource.FUNGI, null, goldPointCondition, points));
    }

    @Test
    void shouldAcceptVisitor() {
        CardVisitorImpl visitor = mock(CardVisitorImpl.class);
        when(visitor.visitGoldCard(goldCard)).thenReturn(new CardInfo(CardType.GOLD));
        assertNotNull(goldCard.accept(visitor));
    }
}