package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GoldCardTest {

    GoldCard goldCard;
    int cardId = 41;
    static Side currentSide = Side.FRONT;
    GoldPointCondition goldPointCondition = GoldPointCondition.valueOf("QUILL");
    int points = 1;
    ArrayList<Resource> requirements = new ArrayList<>(Arrays.asList(Resource.FUNGI, Resource.FUNGI, Resource.ANIMAL));

    @BeforeEach
    void goldCardInit() throws InvalidConstructorDataException {
        ArrayList<Resource> centralResources = new ArrayList<>();
        HashMap<AngleOrientation, Angle> angles = new HashMap<>();
        angles.put(AngleOrientation.TOPRIGHT, new Angle(true, Resource.valueOf("NONE"), null));
        angles.put(AngleOrientation.TOPLEFT, new Angle(false, Resource.valueOf("NONE"), null));
        angles.put(AngleOrientation.BOTTOMRIGHT, new Angle(true, Resource.valueOf("QUILL"), null));
        angles.put(AngleOrientation.BOTTOMLEFT, new Angle(true, Resource.valueOf("NONE"), null));
        goldCard = new GoldCard(cardId, currentSide, centralResources, angles, Resource.FUNGI, requirements, goldPointCondition, points);
    }

    @Test
    void shouldReturnCardId() {
        assertEquals(cardId, goldCard.getCardId());
    }

    @Test
    void shouldReturnCurrentSide() {
        assertEquals(currentSide, goldCard.getCurrentSide());
    }

    @Test
    void shouldReturnGoldPointCondition() {
        assertEquals(goldPointCondition, goldCard.getGoldPointCondition());
    }

    @Test
    void shouldReturnRequirements() {
        assertEquals(requirements, goldCard.getRequirements());
    }
}