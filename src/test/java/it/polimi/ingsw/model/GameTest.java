package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.GameStatus;
import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.exceptions.AlreadyExistingPlayerException;
import it.polimi.ingsw.exceptions.AlreadyMaxNumberOfPlayersException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.exceptions.NotExistingPlayerException;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.StarterCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameTest {

    private Game game;
    private Deck<ObjectiveCard> objectiveCardDeck;
    private Deck<StarterCard> starterCardDeck;
    private Player player;

    @BeforeEach
    @SuppressWarnings("unchecked")
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
    void shouldThrowExceptionWhenAddingExistingPlayer() {
        assertThrows(AlreadyExistingPlayerException.class, () -> {
            game.addPlayer(player, 2);
            game.addPlayer(player, 2);
        });
    }

    @Test
    void shouldThrowExceptionWhenAddingMoreThanFourPlayers() {
        assertThrows(AlreadyMaxNumberOfPlayersException.class, () -> {
            Player player = new Player("P1");
            Player player1 = new Player("P2");
            Player player2 = new Player("P3");
            game.addPlayer(player, 2);
            game.addPlayer(player1, 2);
            game.addPlayer(player2, 2);
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
    void shouldReturnListOfPlayers() throws AlreadyExistingPlayerException, AlreadyMaxNumberOfPlayersException, InvalidConstructorDataException {
        Player player2 = new Player("P1");
        game.addPlayer(player, 2);
        game.addPlayer(player2, 2);
        ArrayList<Player> players = game.getListOfPlayers();
        assertEquals(2, players.size());
        assertTrue(players.contains(player));
        assertTrue(players.contains(player2));
    }

    @Test
    void shouldReturnAvailableMarkers() {
        ArrayList<Marker> markers = game.getAvailableMarkers();
        assertEquals(Marker.values().length - 1, markers.size());
        assertTrue(markers.contains(Marker.BLUE));
        assertTrue(markers.contains(Marker.RED));
        assertTrue(markers.contains(Marker.GREEN));
        assertTrue(markers.contains(Marker.YELLOW));
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
    void shouldReturnCorrectGameStatus() {
        assertEquals(GameStatus.LOBBY_CREATED, game.getGameStatus());

        game.setGameStatus(GameStatus.ALL_PLAYERS_JOINED);
        assertEquals(GameStatus.ALL_PLAYERS_JOINED, game.getGameStatus());

        game.setIsGameEnded(true);
        assertTrue(game.getIsGameEnded());

        game.setIsGameEndedForDisconnection(true);
        assertTrue(game.getIsGameEndedForDisconnection());
    }

    @Test
    void shouldShufflePlayers() throws AlreadyExistingPlayerException, AlreadyMaxNumberOfPlayersException, InvalidConstructorDataException {
        Player player1 = new Player("P1");
        Player player2 = new Player("P2");
        Player player3 = new Player("P3");
        Player player4 = new Player("P4");
        game.addPlayer(player1, 4);
        game.addPlayer(player2, 4);
        game.addPlayer(player3, 4);
        game.addPlayer(player4, 4);
        ArrayList<Player> originalOrder = new ArrayList<>(game.getListOfPlayers());

        game.shufflePlayers();
        ArrayList<Player> shuffledOrder = game.getListOfPlayers();

        assertNotEquals(originalOrder, shuffledOrder);
    }

    @Test
    void shouldRemovePlayer() throws AlreadyExistingPlayerException, AlreadyMaxNumberOfPlayersException, InvalidConstructorDataException {
        Player player1 = new Player("player1");
        Player player2 = mock(Player.class);
        game.addPlayer(player1, 2);
        game.addPlayer(player2, 2);
        game.removePlayer(player1.getUsername());
        assertFalse(game.getListOfPlayers().contains(player1));
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldInitializeGameWithValidData() {
        DrawingField drawingField = mock(DrawingField.class);
        ArrayList<ObjectiveCard> sharedObjectiveCards = new ArrayList<>();
        Deck<ObjectiveCard> objectiveCardDeck = mock(Deck.class);
        Deck<StarterCard> starterCardDeck = mock(Deck.class);
        assertDoesNotThrow(() -> new Game(1, drawingField, sharedObjectiveCards, objectiveCardDeck, starterCardDeck));
    }

    @Test
    @SuppressWarnings("unchecked")
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


    @Test
    void shouldThrowExceptionWhenPlayerDoesNotExist() {
        assertThrows(NotExistingPlayerException.class, () -> game.getPlayer("nonexistent"));
    }

    @Test
    void shouldReturnPlayerWhenPlayerExists() throws AlreadyExistingPlayerException, AlreadyMaxNumberOfPlayersException {
        when(player.getUsername()).thenReturn("existing");
        game.addPlayer(player, 2);
        assertDoesNotThrow(() -> {
            Player existingPlayer = game.getPlayer("existing");
            assertEquals(player, existingPlayer);
        });
    }

    @Test
    void shouldThrowExceptionWhenPlayerNotExists() throws AlreadyExistingPlayerException, AlreadyMaxNumberOfPlayersException {
        when(player.getUsername()).thenReturn("existing");
        game.addPlayer(player, 2);
        assertThrows(NotExistingPlayerException.class,() -> game.getPlayer("notexisting"));
    }


}