package it.polimi.ingsw.model;
import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.exceptions.AlreadyExistingPlayerException;
import it.polimi.ingsw.exceptions.AlreadyFourPlayersException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.enumerations.Marker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTest {

    private Game game;
    private Deck<ObjectiveCard> objectiveCardDeck;
    private Deck<StarterCard> starterCardDeck;
    private Player player;

    @BeforeEach
    void setUp() throws InvalidConstructorDataException {
        DrawingField drawingField = mock(DrawingField.class);
        ArrayList<ObjectiveCard> sharedObjectiveCards = new ArrayList<>();
        objectiveCardDeck = mock(Deck.class);
        starterCardDeck = mock(Deck.class);
        player = mock(Player.class);
        game = new Game(1, drawingField, sharedObjectiveCards, objectiveCardDeck, starterCardDeck);
    }

    @Test
    void shouldReturnGameId() {
        assertEquals(1, game.getGameId());
    }

    @Test
    void shouldAddPlayer() throws AlreadyExistingPlayerException, AlreadyFourPlayersException {
        game.addPlayer(player);
        assertEquals(1, game.getNumberOfPlayers());
    }

    @Test
    void shouldThrowExceptionWhenAddingExistingPlayer() {
        assertThrows(AlreadyExistingPlayerException.class, () -> {
            game.addPlayer(player);
            game.addPlayer(player);
        });
    }

    @Test
    void shouldThrowExceptionWhenAddingMoreThanFourPlayers() {
        assertThrows(AlreadyFourPlayersException.class, () -> {
            for(int i = 0; i < 5; i++) {
                Player player = mock(Player.class);
                game.addPlayer(player);
            }
        });
    }

    @Test
    void shouldReturnTableTop() {
        assertNotNull(game.getTableTop());
    }

    @Test
    void shouldReturnObjectiveCardDeck() {
        assertEquals(objectiveCardDeck, game.getObjectiveCardDeck());
    }

    @Test
    void shouldReturnAvailableStarterCards() {
        assertEquals(starterCardDeck, game.getAvailableStarterCards());
    }

    @Test
    void shouldReturnListOfPlayers() throws AlreadyExistingPlayerException, AlreadyFourPlayersException {
        Player player2 = mock(Player.class);
        game.addPlayer(player);
        game.addPlayer(player2);
        ArrayList<Player> players = game.getListOfPlayers();
        assertEquals(2, players.size());
        assertTrue(players.contains(player));
        assertTrue(players.contains(player2));
    }

    @Test
    void shouldReturnAvailableMarkers() {
        ArrayList<Marker> markers = game.getAvailableMarkers();
        assertEquals(Marker.values().length, markers.size());
        assertTrue(markers.contains(Marker.BLUE));
        assertTrue(markers.contains(Marker.BLACK));
        assertTrue(markers.contains(Marker.RED));
        assertTrue(markers.contains(Marker.GREEN));
        assertTrue(markers.contains(Marker.YELLOW));
    }

    @Test
    void shouldRemoveMarker() {
        game.removeMarker(Marker.YELLOW);
        ArrayList<Marker> markers = game.getAvailableMarkers();
        assertEquals(Marker.values().length - 1, markers.size());
        assertFalse(markers.contains(Marker.YELLOW));
    }

    @Test
    void shouldReturnCurrentPlayer() {
        game.setCurrentPlayer(player);
        assertEquals(player, game.getCurrentPlayer());
    }

    @Test
    void shouldSetCurrentPlayer() {
        game.setCurrentPlayer(player);
        assertEquals(player, game.getCurrentPlayer());
    }

    @Test
    void shouldReturnGameStarted() {
        assertFalse(game.getgameStarted());
    }

    @Test
    void shouldShufflePlayers() throws AlreadyExistingPlayerException, AlreadyFourPlayersException {
        Player player1 = mock(Player.class);
        Player player2 = mock(Player.class);
        Player player3 = mock(Player.class);
        Player player4 = mock(Player.class);
        game.addPlayer(player1);
        game.addPlayer(player2);
        game.addPlayer(player3);
        game.addPlayer(player4);
        ArrayList<Player> originalOrder = new ArrayList<>(game.getListOfPlayers());

        game.shufflePlayers();
        ArrayList<Player> shuffledOrder = game.getListOfPlayers();

        assertNotEquals(originalOrder, shuffledOrder);
    }

    @Test
    void shouldInitializeGameWithValidData() {
        DrawingField drawingField = mock(DrawingField.class);
        ArrayList<ObjectiveCard> sharedObjectiveCards = new ArrayList<>();
        Deck<ObjectiveCard> objectiveCardDeck = mock(Deck.class);
        Deck<StarterCard> starterCardDeck = mock(Deck.class);
        assertDoesNotThrow(() -> new Game(1, drawingField, sharedObjectiveCards, objectiveCardDeck, starterCardDeck));
    }

    @Test
    void shouldThrowInvalidConstructorDataExceptionWhenSharedObjectiveCardsIsNull() {
        DrawingField drawingField = mock(DrawingField.class);
        Deck<ObjectiveCard> objectiveCardDeck = mock(Deck.class);
        Deck<StarterCard> starterCardDeck = mock(Deck.class);
        assertThrows(InvalidConstructorDataException.class, () -> new Game(1, drawingField, null, objectiveCardDeck, starterCardDeck));
    }

    @Test
    void shouldThrowInvalidConstructorDataExceptionWhenAllParametersAreNull() {
        assertThrows(InvalidConstructorDataException.class, () -> new Game(1, null, null, null, null));
    }
}