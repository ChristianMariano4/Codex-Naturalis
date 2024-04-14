package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceObjectiveCardTest {
    ResourceObjectiveCard roc;
    static int cardID = 123;
    static Side cardSide = Side.FRONT;
    static int points = 3;
    static Resource resource = Resource.ANIMAL;
    @BeforeEach
    void ResourceObjectiveCardInit() {
        roc = new ResourceObjectiveCard(cardID, cardSide, points, resource);
    }
    @Test
    void getCardResource() {
        Assertions.assertEquals(resource, roc.getCardResource());
    }
}