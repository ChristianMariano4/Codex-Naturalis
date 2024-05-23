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
    void shouldReturnPlayerFieldHashMap() throws AlreadyExistingPlayerException, AlreadyMaxNumberOfPlayersException {
        HashMap<Player, PlayerField> expectedPlayerFieldHashMap = new HashMap<>();
        Player player = mock(Player.class);
        PlayerField playerField = mock(PlayerField.class);
        expectedPlayerFieldHashMap.put(player, playerField);
        when(player.getPlayerField()).thenReturn(playerField);
        tableTop.addPlayerField(player);
        assertEquals(expectedPlayerFieldHashMap, tableTop.getPlayerFieldHashMap());
    }

    @Test
    void shouldThrowAlreadyFourPlayersException() {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Player player3 = mock(Player.class);
        Player player4 = mock(Player.class);
        Player player5 = mock(Player.class);
        assertThrows(AlreadyMaxNumberOfPlayersException.class, () -> {
            tableTop.addPlayerField(player1);
            tableTop.addPlayerField(player2);
            tableTop.addPlayerField(player3);
            tableTop.addPlayerField(player4);
            tableTop.addPlayerField(player5);
        });
    }

    @Test
    void shouldThrowAlreadyExistingPlayerException() throws AlreadyExistingPlayerException, AlreadyMaxNumberOfPlayersException {
        Player player = mock(Player.class);
        tableTop.addPlayerField(player);
        assertThrows(AlreadyExistingPlayerException.class, () -> {
            tableTop.addPlayerField(player);
        });
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