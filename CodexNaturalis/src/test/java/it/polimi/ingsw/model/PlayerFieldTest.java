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
    private StarterCard starterCard;

    @BeforeEach
    void setUp() {
        playerField = new PlayerField();
        card = mock(PlayableCard.class);
        cardToAdd = mock(PlayableCard.class);
        starterCard = mock(StarterCard.class);
    }

    @Test
    void shouldAddCardToCellSuccessfully() throws InvalidCardPositionException {
        playerField.addCardToCell(starterCard);
        playerField.addCardToCell(starterCard, AngleOrientation.BOTTOMLEFT, cardToAdd);
        assertEquals(cardToAdd, playerField.getMatrixField()[DEFAULT_MATRIX_SIZE/2 + AngleOrientation.BOTTOMLEFT.mapEnumToX()][DEFAULT_MATRIX_SIZE/2 + AngleOrientation.BOTTOMLEFT.mapEnumToY()]);
    }

    @Test
    void shouldThrowExceptionWhenAddingACardToAnOccupiedPosition() throws InvalidCardPositionException {
        playerField.addCardToCell(starterCard);
        playerField.addCardToCell(starterCard, AngleOrientation.BOTTOMLEFT, cardToAdd);
        assertThrows(InvalidCardPositionException.class, () -> playerField.addCardToCell(starterCard, AngleOrientation.BOTTOMLEFT, cardToAdd));
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
        for (int i = 0; i < matrixField.length; i++) {
            for (int j = 0; j < matrixField[i].length; j++) {
                assertNull(matrixField[i][j]);
            }
        }
    }
}
