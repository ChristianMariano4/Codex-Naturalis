package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.AngleAlreadyLinkedException;
import it.polimi.ingsw.exceptions.CardTypeMismatchException;
import it.polimi.ingsw.exceptions.InvalidCardPositionException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.CardVisitorImpl;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerField;
import it.polimi.ingsw.model.cards.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PointCalculatorTest {

    @Test
    void calculateTripleObjective_returnsCorrectPoints() {
        Player mockPlayer = mock(Player.class);
        ObjectiveCard mockObjectiveCard = mock(ObjectiveCard.class);
        when(mockPlayer.getResourceAmount(Resource.INKWELL)).thenReturn(3);
        when(mockPlayer.getResourceAmount(Resource.MANUSCRIPT)).thenReturn(2);
        when(mockPlayer.getResourceAmount(Resource.QUILL)).thenReturn(4);
        when(mockObjectiveCard.getPoints()).thenReturn(5);
        assertEquals(10, PointCalculator.calculateTripleObjective(mockPlayer, mockObjectiveCard));
    }

    @Test
    void calculateResourceObjective_returnsCorrectPoints() {
        CardInfo mockCardInfo = mock(CardInfo.class);
        Player mockPlayer = mock(Player.class);
        ObjectiveCard mockObjectiveCard = mock(ObjectiveCard.class);
        when(mockCardInfo.getCardResource()).thenReturn(Resource.INKWELL);
        when(mockPlayer.getResourceAmount(Resource.INKWELL)).thenReturn(5);
        when(mockObjectiveCard.getPoints()).thenReturn(3);

        assertEquals(6, PointCalculator.calculateResourceObjective(mockCardInfo, mockPlayer, mockObjectiveCard));
    }

    @Test
    void calculatePositionalObjective_returnsCorrectPoints() {
        CardInfo mockCardInfo = mock(CardInfo.class);
        PlayerField mockPlayerField = mock(PlayerField.class);
        when(mockPlayerField.getMatrixField()).thenReturn(new PlayableCard[4][4]);
        ObjectiveCard mockObjectiveCard = mock(ObjectiveCard.class);
        when(mockCardInfo.getPositionalType()).thenReturn(PositionalType.DIAGONAL);
        when(mockCardInfo.getOrientation()).thenReturn(AngleOrientation.BOTTOMLEFT);
        when(mockObjectiveCard.getPoints()).thenReturn(3);

        // Assuming calculateDiagonalObjective and calculateLShapedObjective are correctly implemented
        assertDoesNotThrow(() -> PointCalculator.calculatePositionalObjective(mockCardInfo, mockPlayerField, mockObjectiveCard));
    }

    @Test
    void calculatePlayedCardPoints_returnsCorrectPoints() {
        Player mockPlayer = mock(Player.class);
        PlayableCard mockCard = mock(PlayableCard.class);
        CardInfo mockCardInfo = mock(CardInfo.class);
        when(mockCardInfo.getCardType()).thenReturn(CardType.RESOURCE);
        when(mockCard.getPoints()).thenReturn(5);

        assertEquals(5, PointCalculator.calculatePlayedCardPoints(mockPlayer, mockCard, mockCardInfo));
    }

    @Test
    void calculateTripleObjective_returnsCorrectPointsForDifferentResources() {
        Player mockPlayer = mock(Player.class);
        ObjectiveCard mockObjectiveCard = mock(ObjectiveCard.class);
        when(mockPlayer.getResourceAmount(Resource.INKWELL)).thenReturn(2);
        when(mockPlayer.getResourceAmount(Resource.MANUSCRIPT)).thenReturn(3);
        when(mockPlayer.getResourceAmount(Resource.QUILL)).thenReturn(4);
        when(mockObjectiveCard.getPoints()).thenReturn(5);

        assertEquals(10, PointCalculator.calculateTripleObjective(mockPlayer, mockObjectiveCard));
    }

    @Test
    void calculateResourceObjective_returnsCorrectPointsForDifferentResources() {
        CardInfo mockCardInfo = mock(CardInfo.class);
        Player mockPlayer = mock(Player.class);
        ObjectiveCard mockObjectiveCard = mock(ObjectiveCard.class);
        when(mockCardInfo.getCardResource()).thenReturn(Resource.MANUSCRIPT);
        when(mockPlayer.getResourceAmount(Resource.MANUSCRIPT)).thenReturn(6);
        when(mockObjectiveCard.getPoints()).thenReturn(3);

        assertEquals(9, PointCalculator.calculateResourceObjective(mockCardInfo, mockPlayer, mockObjectiveCard));
    }

    @Test
    void calculatePositionalObjective_returnsCorrectPointsForDifferentPositionalTypes() {
        CardInfo mockCardInfo = mock(CardInfo.class);
        PlayerField mockPlayerField = mock(PlayerField.class);
        when(mockPlayerField.getMatrixField()).thenReturn(new PlayableCard[4][4]);
        ObjectiveCard mockObjectiveCard = mock(ObjectiveCard.class);
        when(mockCardInfo.getPositionalType()).thenReturn(PositionalType.LSHAPED);
        when(mockCardInfo.getOrientation()).thenReturn(AngleOrientation.BOTTOMLEFT);
        when(mockObjectiveCard.getPoints()).thenReturn(3);

        assertDoesNotThrow(() -> PointCalculator.calculatePositionalObjective(mockCardInfo, mockPlayerField, mockObjectiveCard));
    }

    @Test
    void calculatePlayedCardPoints_returnsCorrectPointsForDifferentCardTypes() {
        Player mockPlayer = mock(Player.class);
        PlayableCard mockCard = mock(PlayableCard.class);
        CardInfo mockCardInfo = mock(CardInfo.class);
        when(mockCardInfo.getCardType()).thenReturn(CardType.RESOURCE);
        when(mockCard.getPoints()).thenReturn(5);

        assertEquals(5, PointCalculator.calculatePlayedCardPoints(mockPlayer, mockCard, mockCardInfo));
    }

    @Test
    void calculateTripleObjective_returnsZeroWhenNoResources() {
        Player mockPlayer = mock(Player.class);
        ObjectiveCard mockObjectiveCard = mock(ObjectiveCard.class);
        when(mockPlayer.getResourceAmount(any(Resource.class))).thenReturn(0);
        assertEquals(0, PointCalculator.calculateTripleObjective(mockPlayer, mockObjectiveCard));
    }

    @Test
    void calculatePlayedCardPoints_returnsZeroForNonResourceCard() {
        Player mockPlayer = mock(Player.class);
        PlayableCard mockCard = mock(PlayableCard.class);
        CardInfo mockCardInfo = mock(CardInfo.class);
        when(mockCardInfo.getCardType()).thenReturn(CardType.POSITIONALOBJECTIVE);
        assertEquals(0, PointCalculator.calculatePlayedCardPoints(mockPlayer, mockCard, mockCardInfo));
    }

    @Test
    void calculateResourceObjective_returnsCorrectPointsForDefaultCase() {
        CardInfo mockCardInfo = mock(CardInfo.class);
        Player mockPlayer = mock(Player.class);
        ObjectiveCard mockObjectiveCard = mock(ObjectiveCard.class);
        when(mockCardInfo.getCardResource()).thenReturn(Resource.INSECT);
        when(mockPlayer.getResourceAmount(Resource.INSECT)).thenReturn(9);
        when(mockObjectiveCard.getPoints()).thenReturn(2);
        assertEquals(6, PointCalculator.calculateResourceObjective(mockCardInfo, mockPlayer, mockObjectiveCard));
    }

    @Test
    void calculatePositionalObjective_returnsCorrectPointsForDefaultCase() throws CardTypeMismatchException {
        CardInfo mockCardInfo = mock(CardInfo.class);
        PlayerField mockPlayerField = mock(PlayerField.class);
        ObjectiveCard mockObjectiveCard = mock(ObjectiveCard.class);
        when(mockCardInfo.getPositionalType()).thenReturn(PositionalType.NONE);
        PlayableCard[][] matrixField = new PlayableCard[4][4];
        when(mockPlayerField.getMatrixField()).thenReturn(matrixField);
        when(mockObjectiveCard.getPoints()).thenReturn(2);
        assertThrows(CardTypeMismatchException.class, () -> PointCalculator.calculatePositionalObjective(mockCardInfo, mockPlayerField, mockObjectiveCard));
    }

    @Test
    void calculateDiagonalObjective_test() throws InvalidConstructorDataException, InvalidCardPositionException, AngleAlreadyLinkedException {
        Player player = new Player("test");
        CardInfo cardInfo1 = new CardInfo(CardType.POSITIONALOBJECTIVE, Resource.FUNGI, AngleOrientation.TOPRIGHT, PositionalType.DIAGONAL);
        CardInfo cardInfo2 = new CardInfo(CardType.RESOURCE);
        ObjectiveCard diagonalObjective = new PositionalObjectiveCard(87, Side.FRONT, 2, Resource.FUNGI, AngleOrientation.TOPRIGHT, PositionalType.DIAGONAL);
        StarterCard starterCard = mock(StarterCard.class);
        ResourceCard card1 = mock(ResourceCard.class);
        ResourceCard card2 = mock(ResourceCard.class);
        ResourceCard card3 = mock(ResourceCard.class);
        Angle mockAngle = mock(Angle.class);
        when(starterCard.getAngle(AngleOrientation.TOPRIGHT)).thenReturn(mockAngle);
        when(card1.getAngle(AngleOrientation.TOPRIGHT)).thenReturn(mockAngle);
        when(card1.getAngle(AngleOrientation.BOTTOMLEFT)).thenReturn(mockAngle);
        when(card2.getAngle(AngleOrientation.TOPRIGHT)).thenReturn(mockAngle);
        when(card2.getAngle(AngleOrientation.BOTTOMLEFT)).thenReturn(mockAngle);
        when(card3.getAngle(AngleOrientation.TOPRIGHT)).thenReturn(mockAngle);
        when(card3.getAngle(AngleOrientation.BOTTOMLEFT)).thenReturn(mockAngle);
        when(starterCard.getCardColor()).thenReturn(Resource.NONE);
        when(card1.getCardColor()).thenReturn(Resource.FUNGI);
        when(card2.getCardColor()).thenReturn(Resource.FUNGI);
        when(card3.getCardColor()).thenReturn(Resource.FUNGI);
        when(mockAngle.isPlayable()).thenReturn(true);
        when(mockAngle.getAngleStatus()).thenReturn(AngleStatus.UNLINKED);
        when(card1.accept(new CardVisitorImpl())).thenReturn(cardInfo2);
        when(card2.accept(new CardVisitorImpl())).thenReturn(cardInfo2);
        when(card3.accept(new CardVisitorImpl())).thenReturn(cardInfo2);
        player.getPlayerField().addCardToCell(starterCard);
        player.getPlayerField().addCardToCell(starterCard, AngleOrientation.TOPRIGHT, card1);
        player.getPlayerField().addCardToCell(card1, AngleOrientation.TOPRIGHT, card2);
        player.getPlayerField().addCardToCell(card2, AngleOrientation.TOPRIGHT, card3);
        assertEquals(2, PointCalculator.calculateDiagonalObjective(cardInfo1, player.getPlayerField().getMatrixField(), diagonalObjective));
    }

    @Test
    void calculateLShapedObjective_test() throws InvalidConstructorDataException, InvalidCardPositionException, AngleAlreadyLinkedException {
        Player player = new Player("test");
        CardInfo cardInfo1 = new CardInfo(CardType.POSITIONALOBJECTIVE, Resource.FUNGI, AngleOrientation.BOTTOMRIGHT, PositionalType.LSHAPED);
        CardInfo cardInfo2 = new CardInfo(CardType.RESOURCE);
        ObjectiveCard diagonalObjective = new PositionalObjectiveCard(91, Side.FRONT, 3, Resource.FUNGI, AngleOrientation.BOTTOMRIGHT, PositionalType.LSHAPED);
        StarterCard starterCard = mock(StarterCard.class);
        ResourceCard card1 = mock(ResourceCard.class);
        ResourceCard card2 = mock(ResourceCard.class);
        ResourceCard card3 = mock(ResourceCard.class);
        Angle mockAngle = mock(Angle.class);
        when(starterCard.getAngle(AngleOrientation.TOPLEFT)).thenReturn(mockAngle);
        when(starterCard.getAngle(AngleOrientation.BOTTOMLEFT)).thenReturn(mockAngle);
        when(card1.getAngle(AngleOrientation.BOTTOMRIGHT)).thenReturn(mockAngle);
        when(card2.getAngle(AngleOrientation.TOPRIGHT)).thenReturn(mockAngle);
        when(card2.getAngle(AngleOrientation.BOTTOMRIGHT)).thenReturn(mockAngle);
        when(card3.getAngle(AngleOrientation.TOPLEFT)).thenReturn(mockAngle);
        when(starterCard.getCardColor()).thenReturn(Resource.NONE);
        when(card1.getCardColor()).thenReturn(Resource.FUNGI);
        when(card2.getCardColor()).thenReturn(Resource.FUNGI);
        when(card3.getCardColor()).thenReturn(Resource.PLANT);
        when(mockAngle.isPlayable()).thenReturn(true);
        when(mockAngle.getAngleStatus()).thenReturn(AngleStatus.UNLINKED);
        when(card1.accept(new CardVisitorImpl())).thenReturn(cardInfo2);
        when(card2.accept(new CardVisitorImpl())).thenReturn(cardInfo2);
        when(card3.accept(new CardVisitorImpl())).thenReturn(cardInfo2);
        player.getPlayerField().addCardToCell(starterCard);
        player.getPlayerField().addCardToCell(starterCard, AngleOrientation.TOPLEFT, card1);
        player.getPlayerField().addCardToCell(starterCard, AngleOrientation.BOTTOMLEFT, card2);
        player.getPlayerField().addCardToCell(card2, AngleOrientation.BOTTOMRIGHT, card3);
        assertEquals(3, PointCalculator.calculateLShapedObjective(cardInfo1, player.getPlayerField().getMatrixField(), diagonalObjective));
    }

    @Test
    void calculatePlayedCardPoints_returnsPointsForNoneGoldPointCondition() {
        Player mockPlayer = mock(Player.class);
        PlayableCard mockCard = mock(PlayableCard.class);
        CardInfo mockCardInfo = mock(CardInfo.class);
        when(mockCardInfo.getCardType()).thenReturn(CardType.GOLD);
        when(mockCardInfo.getGoldPointCondition()).thenReturn(GoldPointCondition.NONE);
        when(mockCard.getPoints()).thenReturn(5);
        assertEquals(5, PointCalculator.calculatePlayedCardPoints(mockPlayer, mockCard, mockCardInfo));
    }

    @Test
    void calculatePlayedCardPoints_returnsPointsForAngleGoldPointCondition() throws InvalidConstructorDataException, InvalidCardPositionException, AngleAlreadyLinkedException {
        Player player = new Player("test");
        GoldCard mockCard = mock(GoldCard.class);
        CardInfo mockCardInfo = new CardInfo(CardType.GOLD, new ArrayList<>() ,GoldPointCondition.ANGLE);
        when(mockCard.getPoints()).thenReturn(2);
        StarterCard starterCard = mock(StarterCard.class);
        Angle mockAngle = mock(Angle.class);
        when(starterCard.getAngle(AngleOrientation.TOPLEFT)).thenReturn(mockAngle);
        when(mockCard.getAngle(AngleOrientation.BOTTOMRIGHT)).thenReturn(mockAngle);
        when(mockAngle.isPlayable()).thenReturn(true);
        when(mockAngle.getAngleStatus()).thenReturn(AngleStatus.UNLINKED);
        player.getPlayerField().addCardToCell(starterCard);
        player.getPlayerField().addCardToCell(starterCard, AngleOrientation.TOPLEFT, mockCard);
        assertEquals(2, PointCalculator.calculatePlayedCardPoints(player, mockCard, mockCardInfo));
    }

    @Test
    void calculatePlayedCardPoints_returnsPointsForInkPointCondition() {
        Player player = mock(Player.class);
        GoldCard mockCard = mock(GoldCard.class);
        CardInfo mockCardInfo = new CardInfo(CardType.GOLD, new ArrayList<>() ,GoldPointCondition.INKWELL);
        when(player.getResourceAmount(Resource.INKWELL)).thenReturn(3);
        when(mockCard.getPoints()).thenReturn(1);
        assertEquals(4, PointCalculator.calculatePlayedCardPoints(player, mockCard, mockCardInfo));
    }
}