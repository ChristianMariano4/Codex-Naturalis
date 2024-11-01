package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class AngleTest {

        private Angle angle;
        private PlayableCard ownerCard;
        @BeforeEach
        void angleInit() throws InvalidConstructorDataException {
            ArrayList<Resource> centralResources = new ArrayList<>();
            HashMap<AngleOrientation, Angle> angles = new HashMap<>();
            angles.put(AngleOrientation.TOPRIGHT, new Angle(true, Resource.valueOf("NONE"), null));
            angles.put(AngleOrientation.TOPLEFT, new Angle(true, Resource.valueOf("FUNGI"), null));
            angles.put(AngleOrientation.BOTTOMRIGHT, new Angle(false, Resource.valueOf("NONE"), null));
            angles.put(AngleOrientation.BOTTOMLEFT, new Angle(true, Resource.valueOf("FUNGI"), null));
            ownerCard = new ResourceCard(1, Side.FRONT, centralResources, angles, Resource.FUNGI, 0);
            angle = new Angle(true, Resource.INSECT, ownerCard);
        }

    @Test
    void isPlayableReturnsTrueForPlayableAngle() {
        assertTrue(angle.isPlayable());
    }

    @Test
    void getAngleStatusReturnsUnlinkedForNewAngle() {
        assertEquals(AngleStatus.UNLINKED, angle.getAngleStatus());
    }

    @Test
    void getResourceReturnsCorrectResource() {
        assertEquals(Resource.INSECT, angle.getResource());
    }

    @Test
    void setLinkedAngleSetsAngleStatusAndLinkedAngle() throws AngleAlreadyLinkedException, UnlinkedCardException {
        Angle linkedAngle = new Angle(true, Resource.ANIMAL, null);
        angle.setLinkedAngle(linkedAngle, AngleStatus.OVER);
        assertEquals(AngleStatus.OVER, angle.getAngleStatus());
        assertEquals(linkedAngle, angle.getLinkedAngle());
    }

    @Test
    void setLinkedAngleThrowsExceptionForAlreadyLinkedAngle() throws AngleAlreadyLinkedException {
        Angle linkedAngle = new Angle(true, Resource.ANIMAL, null);
        angle.setLinkedAngle(linkedAngle, AngleStatus.OVER);
        assertThrows(AngleAlreadyLinkedException.class, () -> angle.setLinkedAngle(linkedAngle, AngleStatus.OVER));
    }

    @Test
    void getLinkedAngleThrowsExceptionForUnlinkedAngle() {
        assertThrows(UnlinkedCardException.class, angle::getLinkedAngle);
    }

    @Test
    void getOwnerCardReturnsCorrectCard() {
        assertEquals(ownerCard, angle.getOwnerCard());
    }
}