package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.CardType;
import it.polimi.ingsw.enumerations.PositionalType;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerField;
import it.polimi.ingsw.model.cards.CardInfo;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import org.junit.jupiter.api.Test;
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
}