package it.polimi.ingsw.model;
import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.exceptions.InvalidCardPositionException;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.StarterCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.polimi.ingsw.model.GameValues.DEFAULT_MATRIX_SIZE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerFieldTest {

    private PlayerField playerField;
    private PlayableCard card;
    private PlayableCard cardToAdd;

    @BeforeEach
    void setUp() {
        playerField = new PlayerField();
        card = mock(PlayableCard.class);
        cardToAdd = mock(PlayableCard.class);
    }

    @Test
    void shouldAddCardToCellSuccessfully() throws InvalidCardPositionException {
        playerField.addCardToCell(card);
        playerField.addCardToCell(card, AngleOrientation.BOTTOMLEFT, cardToAdd);
        assertEquals(cardToAdd, playerField.getMatrixField()[DEFAULT_MATRIX_SIZE/2 + AngleOrientation.BOTTOMLEFT.mapEnumToX()][DEFAULT_MATRIX_SIZE/2 + AngleOrientation.BOTTOMLEFT.mapEnumToY()]);
    }

    @Test
    void shouldThrowExceptionWhenAddingACardToAnOccupiedPosition() throws InvalidCardPositionException {
        playerField.addCardToCell(card);
        playerField.addCardToCell(card, AngleOrientation.BOTTOMLEFT, cardToAdd);
        assertThrows(InvalidCardPositionException.class, () -> playerField.addCardToCell(card, AngleOrientation.BOTTOMLEFT, cardToAdd));
    }

    @Test
    void shouldAddStarterCardToCellSuccessfully() {
        assertDoesNotThrow(() -> playerField.addCardToCell(card));
    }

    @Test
    void shouldAddStarterCardToCellSuccessfullyWhenCellIsEmpty() {
        playerField.addCardToCell(card);
        assertEquals(card, playerField.getMatrixField()[DEFAULT_MATRIX_SIZE/2][DEFAULT_MATRIX_SIZE/2]);

    }

    @Test
    void shouldThrowOutOfBoundExceptionForOutOfMatrixPlacement() {

    }

    @Test
    void shouldReturnCorrectMatrixField() {
        PlayableCard[][] matrixField = playerField.getMatrixField();
        for (int i = 0; i < matrixField.length; i++) {
            for (int j = 0; j < matrixField[i].length; j++) {
                assertNull(matrixField[i][j]);
            }
        }
    }
}
