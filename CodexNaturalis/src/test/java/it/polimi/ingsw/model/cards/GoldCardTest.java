package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.GoldPointCondition;
import it.polimi.ingsw.enumerations.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GoldCardTest {

    GoldCard gc;
    static ArrayList<Resource> requirements;
    static GoldPointCondition goldPointCondition;
    @BeforeEach
    void goldCardInit() {
        //GoldCard Controller method
    }
    @Test
    void getRequirements() {
        Assertions.assertTrue(requirements.containsAll(gc.getRequirements()) && gc.getRequirements().containsAll(requirements));
    }

    @Test
    void getGoldPointCondition() {
        Assertions.assertEquals(goldPointCondition, gc.getGoldPointCondition());
    }
}