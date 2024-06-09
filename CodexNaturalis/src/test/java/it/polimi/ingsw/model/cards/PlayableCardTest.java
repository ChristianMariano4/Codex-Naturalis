package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class PlayableCardTest {

    private PlayableCard playableCard;
    private final int cardId = 1;
    private final Side currentSide = Side.FRONT;
    private final Resource cardColor = Resource.FUNGI;
    private final int points = 0;

    @BeforeEach
    void playableCardInit() throws InvalidConstructorDataException {
        ArrayList<Resource> centralResources = new ArrayList<>();
        HashMap<AngleOrientation, Angle> angles = new HashMap<>();
        angles.put(AngleOrientation.TOPRIGHT, new Angle(true, Resource.valueOf("NONE"), null));
        angles.put(AngleOrientation.TOPLEFT, new Angle(true, Resource.valueOf("FUNGI"), null));
        angles.put(AngleOrientation.BOTTOMRIGHT, new Angle(false, Resource.valueOf("NONE"), null));
        angles.put(AngleOrientation.BOTTOMLEFT, new Angle(true, Resource.valueOf("FUNGI"), null));
        playableCard = new ResourceCard(cardId, currentSide, centralResources, angles, cardColor, points);
    }

    @Test
    void shouldReturnCardId() {
        assertEquals(cardId, playableCard.getCardId());
    }

    @Test
    void shouldReturnCurrentSide() {
        assertEquals(currentSide, playableCard.getCurrentSide());
    }

    @Test
    void shouldReturnCardColor() {
        assertEquals(cardColor, playableCard.getCardColor());
    }

    @Test
    void shouldReturnPoints() {
        assertEquals(points, playableCard.getPoints());
    }

    @Test
    void shouldReturnCentralResources() {
        ArrayList<Resource> centralResources = playableCard.getCentralResources();
        assertTrue(centralResources.isEmpty());
    }

    @Test
    void shouldReturnAngle() {
        Angle angleTopRight = playableCard.getAngle(AngleOrientation.TOPRIGHT);
        assertEquals(Resource.NONE, angleTopRight.getResource());
        assertTrue(angleTopRight.isPlayable());

        Angle angleTopLeft = playableCard.getAngle(AngleOrientation.TOPLEFT);
        assertEquals(Resource.FUNGI, angleTopLeft.getResource());
        assertTrue(angleTopLeft.isPlayable());

        Angle angleBottomRight = playableCard.getAngle(AngleOrientation.BOTTOMRIGHT);
        assertEquals(Resource.NONE, angleBottomRight.getResource());
        assertFalse(angleBottomRight.isPlayable());

        Angle angleBottomLeft = playableCard.getAngle(AngleOrientation.BOTTOMLEFT);
        assertEquals(Resource.FUNGI, angleBottomLeft.getResource());
        assertTrue(angleBottomLeft.isPlayable());
    }
    @Test
    void constructorShouldThrowExceptionForInvalidData() {

        assertThrows(InvalidConstructorDataException.class, () -> {
            new ResourceCard(-1, null, null, null, null, -1);
        });
    }
}