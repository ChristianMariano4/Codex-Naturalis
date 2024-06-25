package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CardInfoTest {
    private CardInfo goldCard;
    private CardInfo goldCardFiveResources;
    private CardInfo goldCardThreeResources;
    private CardInfo goldCardAngle;
    private CardInfo goldCardZeroResources;
    private CardInfo resourceCard;
    private CardInfo resourceObjectiveCard;
    private CardInfo positionalObjectiveCardDiagonal;
    private CardInfo positionalObjectiveCardLShaped;


    @BeforeEach
    void setUp() {
        goldCard = new CardInfo(CardType.GOLD, new ArrayList<>(Arrays.asList(Resource.FUNGI, Resource.FUNGI, Resource.ANIMAL)), GoldPointCondition.MANUSCRIPT);
        goldCardFiveResources = new CardInfo(CardType.GOLD, new ArrayList<>(Arrays.asList(Resource.FUNGI, Resource.FUNGI, Resource.FUNGI, Resource.FUNGI, Resource.FUNGI)), GoldPointCondition.NONE);
        goldCardThreeResources = new CardInfo(CardType.GOLD, new ArrayList<>(Arrays.asList(Resource.FUNGI, Resource.FUNGI, Resource.FUNGI)), GoldPointCondition.NONE);
        goldCardAngle = new CardInfo(CardType.GOLD, new ArrayList<>(Arrays.asList(Resource.FUNGI, Resource.FUNGI, Resource.ANIMAL)), GoldPointCondition.ANGLE);
        goldCardZeroResources = new CardInfo(CardType.GOLD, new ArrayList<>(), GoldPointCondition.NONE);
        resourceCard = new CardInfo(CardType.RESOURCE);
        resourceObjectiveCard = new CardInfo(CardType.RESOURCEOBJECTIVE, Resource.INSECT);
        positionalObjectiveCardDiagonal = new CardInfo(CardType.POSITIONALOBJECTIVE, Resource.INSECT, AngleOrientation.TOPLEFT, PositionalType.DIAGONAL);
        positionalObjectiveCardLShaped = new CardInfo(CardType.POSITIONALOBJECTIVE, Resource.INSECT, AngleOrientation.TOPLEFT, PositionalType.LSHAPED);
    }

    @Test
    void goldCardShouldReturnCorrectPoints() {
        assertEquals(1, goldCard.getPoints());
    }

    @Test
    void goldCardThreeResourcesShouldReturnCorrectPoints() {
        assertEquals(3, goldCardThreeResources.getPoints());
    }

    @Test
    void goldCardFiveResourcesShouldReturnCorrectPoints() {
        assertEquals(5, goldCardFiveResources.getPoints());
    }

    @Test
    void goldCardAngleShouldReturnCorrectPoints() {
        assertEquals(2, goldCardAngle.getPoints());
    }

    @Test
    void goldCardZeroResourcesShouldReturnCorrectPoints() {
        assertEquals(0, goldCardZeroResources.getPoints());
    }

    @Test
    void resourceCardShouldReturnCorrectPoints() {
        assertEquals(2, resourceCard.getPoints());
    }

    @Test
    void resourceObjectiveCardShouldReturnCorrectPoints() {
        assertEquals(2, resourceObjectiveCard.getPoints());
    }

    @Test
    void positionalObjectiveCardShouldReturnCorrectPoints() {
        assertEquals(2, positionalObjectiveCardDiagonal.getPoints());
    }

    @Test
    void positionalObjectiveCardLShapedShouldReturnCorrectPoints() {
        assertEquals(3, positionalObjectiveCardLShaped.getPoints());
    }

    @Test
    void cardShouldReturnCorrectType() {
        assertEquals("Gold", goldCard.getCardTypeString());
        assertEquals("Resource", resourceCard.getCardTypeString());
        assertEquals("Objective", resourceObjectiveCard.getCardTypeString());
        assertEquals("Objective", positionalObjectiveCardDiagonal.getCardTypeString());
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
        assertEquals(CardType.POSITIONALOBJECTIVE, positionalObjectiveCardDiagonal.getCardType());
        assertTrue(positionalObjectiveCardDiagonal.getRequirements().isEmpty());
        assertEquals(GoldPointCondition.NONE, positionalObjectiveCardDiagonal.getGoldPointCondition());
        assertEquals(Resource.INSECT, positionalObjectiveCardDiagonal.getCardColor());
        assertEquals(AngleOrientation.TOPLEFT, positionalObjectiveCardDiagonal.getOrientation());
        assertEquals(PositionalType.DIAGONAL, positionalObjectiveCardDiagonal.getPositionalType());
        assertEquals(Resource.NONE, positionalObjectiveCardDiagonal.getCardResource());
    }
}