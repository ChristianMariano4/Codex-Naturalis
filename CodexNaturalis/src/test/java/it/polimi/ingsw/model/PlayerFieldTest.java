package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.exceptions.InvalidCardPositionException;
import it.polimi.ingsw.model.cards.PlayableCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    void addCardToCellSuccessfully() {
        assertDoesNotThrow(() -> playerField.addCardToCell(card, AngleOrientation.BOTTOMLEFT, cardToAdd));
    }

    @Test
    void addCardToOccupiedCellThrowsException() throws InvalidCardPositionException {
        playerField.addCardToCell(card, AngleOrientation.BOTTOMLEFT, cardToAdd);
        assertThrows(InvalidCardPositionException.class, () -> playerField.addCardToCell(card, AngleOrientation.BOTTOMLEFT, cardToAdd));
    }

    @Test
    void addCardOutOfBoundsThrowsException() {
        assertThrows(InvalidCardPositionException.class, () -> playerField.addCardToCell(card, AngleOrientation.BOTTOMLEFT, cardToAdd));
    }

    @Test
    void addStarterCardToCellSuccessfully() {
        assertDoesNotThrow(() -> playerField.addCardToCell(card));
    }

    @Test
    void addStarterCardToOccupiedCellThrowsException() {
        playerField.addCardToCell(card);
        assertThrows(InvalidCardPositionException.class, () -> playerField.addCardToCell(card));
    }
}