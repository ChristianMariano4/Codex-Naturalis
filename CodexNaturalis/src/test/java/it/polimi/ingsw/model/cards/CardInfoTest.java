package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CardInfoTest {
    private CardInfo goldCard;
    private CardInfo resourceCard;
    private CardInfo resourceObjectiveCard;
    private CardInfo positionalObjectiveCard;

    @BeforeEach
    void setUp() {
        goldCard = new CardInfo(CardType.GOLD, new ArrayList<>(Arrays.asList(Resource.FUNGI, Resource.FUNGI, Resource.ANIMAL)), GoldPointCondition.MANUSCRIPT);
        resourceCard = new CardInfo(CardType.RESOURCE);
        resourceObjectiveCard = new CardInfo(CardType.RESOURCEOBJECTIVE, Resource.INSECT);
        positionalObjectiveCard = new CardInfo(CardType.POSITIONALOBJECTIVE, Resource.INSECT, AngleOrientation.TOPLEFT, PositionalType.DIAGONAL);
    }

    @Test
    void goldCardShouldReturnCorrectAttributes() {
        assertEquals(CardType.GOLD, goldCard.getCardType());
        assertEquals(Arrays.asList(Resource.FUNGI, Resource.FUNGI, Resource.ANIMAL), goldCard.getRequirements());
        assertEquals(GoldPointCondition.MANUSCRIPT, goldCard.getGoldPointCondition());
        assertEquals(Resource.NONE, goldCard.getCardColor());
        assertEquals(AngleOrientation.NONE, goldCard.getOrientation());
        assertEquals(PositionalType.NONE, goldCard.getPositionalType());
        assertEquals(Resource.NONE, goldCard.getCardResource());
    }

    @Test
    void resourceCardShouldReturnCorrectAttributes() {
        assertEquals(CardType.RESOURCE, resourceCard.getCardType());
        assertTrue(resourceCard.getRequirements().isEmpty());
        assertEquals(GoldPointCondition.NONE, resourceCard.getGoldPointCondition());
        assertEquals(Resource.NONE, resourceCard.getCardColor());
        assertEquals(AngleOrientation.NONE, resourceCard.getOrientation());
        assertEquals(PositionalType.NONE, resourceCard.getPositionalType());
        assertEquals(Resource.NONE, resourceCard.getCardResource());
    }

    @Test
    void resourceObjectiveCardShouldReturnCorrectAttributes() {
        assertEquals(CardType.RESOURCEOBJECTIVE, resourceObjectiveCard.getCardType());
        assertTrue(resourceObjectiveCard.getRequirements().isEmpty());
        assertEquals(GoldPointCondition.NONE, resourceObjectiveCard.getGoldPointCondition());
        assertEquals(Resource.NONE, resourceObjectiveCard.getCardColor());
        assertEquals(AngleOrientation.NONE, resourceObjectiveCard.getOrientation());
        assertEquals(PositionalType.NONE, resourceObjectiveCard.getPositionalType());
        assertEquals(Resource.INSECT, resourceObjectiveCard.getCardResource());
    }

    @Test
    void positionalObjectiveCardShouldReturnCorrectAttributes() {
        assertEquals(CardType.POSITIONALOBJECTIVE, positionalObjectiveCard.getCardType());
        assertTrue(positionalObjectiveCard.getRequirements().isEmpty());
        assertEquals(GoldPointCondition.NONE, positionalObjectiveCard.getGoldPointCondition());
        assertEquals(Resource.INSECT, positionalObjectiveCard.getCardColor());
        assertEquals(AngleOrientation.TOPLEFT, positionalObjectiveCard.getOrientation());
        assertEquals(PositionalType.DIAGONAL, positionalObjectiveCard.getPositionalType());
        assertEquals(Resource.NONE, positionalObjectiveCard.getCardResource());
    }
}