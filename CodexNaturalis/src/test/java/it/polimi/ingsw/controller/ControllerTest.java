package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.CardType;
import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.DrawingField;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TableTop;
import it.polimi.ingsw.network.server.GameHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    private Controller controller;
    private Game game;
    private GameHandler gameHandler;
    private CardHandler cardHandler = new CardHandler();
    private List<Player> players = new ArrayList<>();

    @BeforeEach
    void setUp() throws InvalidConstructorDataException, CardTypeMismatchException, CardNotImportedException, DeckIsEmptyException {
        players.add(new Player("1"));
        players.add(new Player("2"));
        players.add(new Player("3"));
        players.add(new Player("4"));

        gameHandler = mock(GameHandler.class);
        controller = new Controller(gameHandler);

        game = controller.createGame(1);
        when(gameHandler.getGame()).thenReturn(game);
    }

    @Test
    void testCreateGame() throws CardTypeMismatchException, InvalidConstructorDataException, CardNotImportedException, DeckIsEmptyException {
        int gameId = 2;
        Game game = controller.createGame(gameId);
        assertNotNull(game);
        assertEquals(gameId, game.getGameId());
    }

    @Test
    void testAddPlayerToGame() throws AlreadyMaxNumberOfPlayersException, AlreadyExistingPlayerException {
        int desideredNumberOfPlayers = 3;
        controller.addPlayerToGame(players.getFirst().getUsername(), desideredNumberOfPlayers);
        assertEquals(1, game.getListOfPlayers().size());
        assertThrows(AlreadyExistingPlayerException.class, () -> controller.addPlayerToGame(players.getFirst().getUsername(), desideredNumberOfPlayers));

        controller.addPlayerToGame(players.get(1).getUsername(), desideredNumberOfPlayers);
        controller.addPlayerToGame(players.get(2).getUsername(), desideredNumberOfPlayers);
        assertEquals(3, game.getListOfPlayers().size());

        assertThrows(AlreadyMaxNumberOfPlayersException.class, () -> controller.addPlayerToGame(players.get(3).getUsername(), desideredNumberOfPlayers));

    }

    @Test
    void shouldThrowExceptionWhenAddingExistingPlayer() throws Exception {
        when(gameHandler.getGame()).thenReturn(mock(Game.class));
        doThrow(new AlreadyExistingPlayerException()).when(gameHandler.getGame()).addPlayer(any(), anyInt());

        assertThrows(AlreadyExistingPlayerException.class, () -> controller.addPlayerToGame("username", 2));
    }

    @Test
    void shouldThrowExceptionWhenAddingPlayerToFullGame() throws Exception {
        when(gameHandler.getGame()).thenReturn(mock(Game.class));
        doThrow(new AlreadyMaxNumberOfPlayersException()).when(gameHandler.getGame()).addPlayer(any(), anyInt());

        assertThrows(AlreadyMaxNumberOfPlayersException.class, () -> controller.addPlayerToGame("username", 2));
    }

    @Test
    void shouldInitializeGameSuccessfully() throws Exception {
        when(gameHandler.getGame()).thenReturn(mock(Game.class));
        //when(gameHandler.getGame().getListOfPlayers()).thenReturn(new LinkedList<>());

        assertDoesNotThrow(() -> controller.initializeGame());
    }

    @Test
    void shouldThrowExceptionWhenDrawingCardFromEmptyDeck() throws Exception {
        when(gameHandler.getGame()).thenReturn(mock(Game.class));
        when(gameHandler.getGame().getTableTop()).thenReturn(mock(TableTop.class));
        when(gameHandler.getGame().getTableTop().getDrawingField()).thenReturn(mock(DrawingField.class));
        doThrow(new DeckIsEmptyException()).when(gameHandler.getGame().getTableTop().getDrawingField()).drawCardFromGoldCardDeck(any());

        assertThrows(DeckIsEmptyException.class, () -> controller.drawCard(new Player("username"), CardType.GOLD, DrawPosition.FROMDECK));
    }
}
