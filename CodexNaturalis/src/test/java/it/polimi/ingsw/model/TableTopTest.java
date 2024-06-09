package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyExistingPlayerException;
import it.polimi.ingsw.exceptions.AlreadyMaxNumberOfPlayersException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TableTopTest {
    private TableTop tableTop;
    private DrawingField drawingField;

    @BeforeEach
    void setUp() throws InvalidConstructorDataException {
        drawingField = mock(DrawingField.class);
        ArrayList<ObjectiveCard> sharedObjectiveCards = new ArrayList<>();
        Player player = mock(Player.class);
        tableTop = new TableTop(drawingField, sharedObjectiveCards);
    }

    @Test
    void shouldInitializeTableTop() {
        assertNotNull(tableTop);
    }

    @Test
    void shouldReturnDrawingField() {
        assertEquals(drawingField, tableTop.getDrawingField());
    }


    @Test
    void shouldReturnSharedObjectiveCards() throws InvalidConstructorDataException {
        ArrayList<ObjectiveCard> expectedObjectiveCards = new ArrayList<>();
        expectedObjectiveCards.add(mock(ObjectiveCard.class));
        TableTop tableTop = new TableTop(mock(DrawingField.class), expectedObjectiveCards);
        assertEquals(expectedObjectiveCards, tableTop.getSharedObjectiveCards());
    }

    @Test
    void shouldThrowInvalidConstructorDataExceptionWhenSharedObjectiveCardsIsNull() {
        DrawingField drawingField = mock(DrawingField.class);
        assertThrows(InvalidConstructorDataException.class, () -> new TableTop(drawingField, null));
    }

    @Test
    void shouldThrowInvalidConstructorDataExceptionWhenBothParametersAreNull() {
        assertThrows(InvalidConstructorDataException.class, () -> new TableTop(null, null));
    }
}