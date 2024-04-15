package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleStatus;
import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.exceptions.AngleAlreadyLinkedException;
import it.polimi.ingsw.exceptions.NoCardAddedException;
import it.polimi.ingsw.exceptions.UnlinkedCardException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AngleTest {

    static Angle angle;
    static boolean playable;
    static AngleStatus angleStatus;
    static Resource resource;
    static Angle linkedAngle;
    static PlayableCard ownerCard;
    @BeforeEach
    void angleInit() {
        //Angle constructor method
    }
    @Test
    void shouldReturnAnglePlayableCondition() {
        Assertions.assertEquals(playable, angle.isPlayable());
    }

    @Test
    void shouldReturnAngleStatus() {
        Assertions.assertEquals(angleStatus, angle.getAngleStatus());
    }

    @Test
    void shouldReturnResource() {
        Assertions.assertEquals(resource, angle.getResource());
    }

    @Test
    void shouldSetLinkedAngle() throws UnlinkedCardException, AngleAlreadyLinkedException {
        angle.setLinkedAngle(linkedAngle, angleStatus);
        Assertions.assertEquals(linkedAngle, angle.getLinkedAngle());
    }

    @Test
    void shouldThrowAngleAlreadyLinkedException() throws AngleAlreadyLinkedException {
        angle.setLinkedAngle(linkedAngle, angleStatus);
        Assertions.assertThrows(NoCardAddedException.class, () -> {angle.setLinkedAngle(linkedAngle, angleStatus);});

    }

    @Test
    void ShouldThrowUnlinkedCardException() {
        Assertions.assertThrows(NoCardAddedException.class, () -> {angle.getLinkedAngle();});
    }

    @Test
    void shouldReturnOwnerCard() {
        Assertions.assertEquals(ownerCard, angle.getOwnerCard());
    }
}