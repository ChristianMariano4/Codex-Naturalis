package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayableCardTest {
    PlayableCard pc;
    ArrayList<Resource> centralResources;
    static Resource cardColor = Resource.ANIMAL;
    static int points = 3;

    @BeforeEach
    void PlayableCardInit() {
        //Controller method
    }
    @Test
    void shouldReturnCentralResourcesList() {
        Assertions.assertTrue(pc.getCentralResources().containsAll(centralResources) && centralResources.containsAll(pc.getCentralResources()));
    }
    @Test
    void shouldReturnCardColor() {
        Assertions.assertEquals(cardColor, pc.getCardColor());
    }
    @Test
    void shouldReturnCardPoints() {
        Assertions.assertEquals(points, pc.getPoints());
    }

    //TODO: getAngle method test

}