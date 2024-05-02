package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.GoldPointCondition;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class StarterCardTest {

    @Test
    void starterCardConstructorTest() throws InvalidConstructorDataException {
        StarterCard starterCard;
        int cardId = 81;
        Side currentSide = Side.FRONT;
        ArrayList<Resource> centralResources = new ArrayList<>();
        HashMap<AngleOrientation, Angle> angles = new HashMap<>();
        angles.put(AngleOrientation.TOPRIGHT, new Angle(true, Resource.valueOf("PLANT"), null));
        angles.put(AngleOrientation.TOPLEFT, new Angle(false, Resource.valueOf("NONE"), null));
        angles.put(AngleOrientation.BOTTOMRIGHT, new Angle(true, Resource.valueOf("QUILL"), null));
        angles.put(AngleOrientation.BOTTOMLEFT, new Angle(true, Resource.valueOf("NONE"), null));
        Resource cardColor = Resource.FUNGI;
        int points = 0;
        starterCard = new StarterCard(cardId, currentSide, centralResources, angles, cardColor, points);
    }
}