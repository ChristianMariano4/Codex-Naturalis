package it.polimi.ingsw.model;
import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.AngleStatus;
import it.polimi.ingsw.exceptions.AngleAlreadyLinkedException;
import it.polimi.ingsw.exceptions.CardNotFoundException;
import it.polimi.ingsw.exceptions.InvalidCardPositionException;
import it.polimi.ingsw.model.cards.Angle;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.StarterCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.GameValues.DEFAULT_MATRIX_SIZE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerFieldTest {

    private PlayerField playerField;
    private PlayableCard cardToAdd1;

    private PlayableCard cardToAdd2;
    private PlayableCard cardToAdd3;
    private StarterCard starterCard;

    @BeforeEach
    void setUp() {
        playerField = new PlayerField();
        cardToAdd1 = mock(PlayableCard.class);
        cardToAdd2 = mock(PlayableCard.class);
        cardToAdd3 = mock(PlayableCard.class);
        starterCard = mock(StarterCard.class);

        when(starterCard.getAngle(AngleOrientation.BOTTOMLEFT)).thenReturn(mock(Angle.class));
        when(starterCard.getAngle(AngleOrientation.BOTTOMLEFT).isPlayable()).thenReturn(true);
        when(starterCard.getAngle(AngleOrientation.BOTTOMLEFT).getAngleStatus()).thenReturn(AngleStatus.UNLINKED);
        when(starterCard.getAngle(AngleOrientation.TOPLEFT)).thenReturn(mock(Angle.class));
        when(starterCard.getAngle(AngleOrientation.TOPLEFT).isPlayable()).thenReturn(true);
        when(starterCard.getAngle(AngleOrientation.TOPLEFT).getAngleStatus()).thenReturn(AngleStatus.UNLINKED);

        when(cardToAdd1.getAngle(AngleOrientation.BOTTOMLEFT)).thenReturn(mock(Angle.class));
        when(cardToAdd1.getAngle(AngleOrientation.TOPRIGHT)).thenReturn(mock(Angle.class));
        when(cardToAdd1.getAngle(AngleOrientation.BOTTOMLEFT).isPlayable()).thenReturn(false);
        when(cardToAdd1.getAngle(AngleOrientation.TOPLEFT)).thenReturn(mock(Angle.class));
        when(cardToAdd1.getAngle(AngleOrientation.TOPLEFT).isPlayable()).thenReturn(true);
        when(cardToAdd1.getAngle(AngleOrientation.TOPLEFT).getAngleStatus()).thenReturn(AngleStatus.UNLINKED);


        when(cardToAdd2.getAngle(AngleOrientation.TOPRIGHT)).thenReturn(mock(Angle.class));
        when(cardToAdd2.getAngle(AngleOrientation.TOPRIGHT).isPlayable()).thenReturn(true);
        when(cardToAdd2.getAngle(AngleOrientation.TOPRIGHT).getAngleStatus()).thenReturn(AngleStatus.UNLINKED);
        when(cardToAdd2.getAngle(AngleOrientation.BOTTOMRIGHT)).thenReturn(mock(Angle.class));
        when(cardToAdd2.getAngle(AngleOrientation.BOTTOMRIGHT).isPlayable()).thenReturn(true);
        when(cardToAdd2.getAngle(AngleOrientation.BOTTOMLEFT)).thenReturn(mock(Angle.class));
        when(cardToAdd2.getAngle(AngleOrientation.BOTTOMLEFT).isPlayable()).thenReturn(true);
        when(cardToAdd2.getAngle(AngleOrientation.BOTTOMLEFT).getAngleStatus()).thenReturn(AngleStatus.UNLINKED);
        when(cardToAdd2.getAngle(AngleOrientation.TOPLEFT)).thenReturn(mock(Angle.class));
        when(cardToAdd2.getAngle(AngleOrientation.TOPLEFT).isPlayable()).thenReturn(true);
        when(cardToAdd2.getAngle(AngleOrientation.TOPLEFT).getAngleStatus()).thenReturn(AngleStatus.OVER);

        when(cardToAdd3.getAngle(AngleOrientation.TOPRIGHT)).thenReturn(mock(Angle.class));
        when(cardToAdd3.getAngle(AngleOrientation.BOTTOMRIGHT)).thenReturn(mock(Angle.class));
    }

    @Test
    void shouldAddCardToCellSuccessfully() throws InvalidCardPositionException, AngleAlreadyLinkedException {
        playerField.addCardToCell(starterCard);
        playerField.addCardToCell(starterCard, AngleOrientation.BOTTOMLEFT, cardToAdd2);
        assertEquals(cardToAdd2, playerField.getMatrixField()[DEFAULT_MATRIX_SIZE/2 + AngleOrientation.BOTTOMLEFT.mapEnumToX()][DEFAULT_MATRIX_SIZE/2 + AngleOrientation.BOTTOMLEFT.mapEnumToY()]);
    }

    @Test
    void shouldThrowExceptionWhenAddingACardToAnOccupiedPosition() throws InvalidCardPositionException, AngleAlreadyLinkedException {
        playerField.addCardToCell(starterCard);
        playerField.addCardToCell(starterCard, AngleOrientation.BOTTOMLEFT, cardToAdd2);
        assertThrows(InvalidCardPositionException.class, () -> playerField.addCardToCell(starterCard, AngleOrientation.BOTTOMLEFT, cardToAdd2));
    }

    @Test
    void shouldAddStarterCardToCellSuccessfully() {
        assertDoesNotThrow(() -> playerField.addCardToCell(starterCard));
    }

    @Test
    void shouldAddStarterCardToCellSuccessfullyWhenCellIsEmpty() {
        playerField.addCardToCell(starterCard);
        assertEquals(starterCard, playerField.getMatrixField()[DEFAULT_MATRIX_SIZE/2][DEFAULT_MATRIX_SIZE/2]);

    }

    @Test
    void shouldThrowOutOfBoundExceptionForOutOfMatrixPlacement() {

    }

    @Test
    void shouldReturnCorrectMatrixField() {
        PlayableCard[][] matrixField = playerField.getMatrixField();
        for (PlayableCard[] playableCards : matrixField) {
            for (PlayableCard playableCard : playableCards) {
                assertNull(playableCard);
            }
        }
    }

    @Test
    void shouldAddPlayedCards() throws InvalidCardPositionException, AngleAlreadyLinkedException {
        playerField.addCardToCell(starterCard);
        playerField.addCardToCell(starterCard, AngleOrientation.BOTTOMLEFT, cardToAdd1);

        assertEquals(2, playerField.getPlayedCards().size());

        assertThrows(InvalidCardPositionException.class, () -> playerField.addCardToCell(cardToAdd1, AngleOrientation.BOTTOMLEFT, cardToAdd2));

        playerField.addCardToCell(starterCard, AngleOrientation.TOPLEFT, cardToAdd2);

        assertThrows(InvalidCardPositionException.class, () -> playerField.addCardToCell(cardToAdd2, AngleOrientation.TOPLEFT, cardToAdd3));
        playerField.addCardToCell(cardToAdd2, AngleOrientation.BOTTOMLEFT, cardToAdd3);
    }

    @Test
    void shouldReturnCorrectCardById() throws InvalidCardPositionException, AngleAlreadyLinkedException, CardNotFoundException {
        when(cardToAdd2.getCardId()).thenReturn(1);
        playerField.addCardToCell(starterCard);
        playerField.addCardToCell(starterCard, AngleOrientation.BOTTOMLEFT, cardToAdd2);
        assertEquals(cardToAdd2, playerField.getCardById(1));

        assertThrows(CardNotFoundException.class, () -> playerField.getCardById(2));
    }
}
