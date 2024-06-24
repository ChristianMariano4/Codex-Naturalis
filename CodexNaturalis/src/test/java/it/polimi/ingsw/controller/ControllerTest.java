package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.CardType;
import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.DrawingField;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.TableTop;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.network.server.GameHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
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
    int desideredNumberOfPlayers = 3;


    @BeforeEach
    void setUp() throws InvalidConstructorDataException, CardTypeMismatchException, CardNotImportedException, DeckIsEmptyException, AlreadyMaxNumberOfPlayersException, AlreadyExistingPlayerException {
        players.add(new Player("1"));
        players.add(new Player("2"));
        players.add(new Player("3"));
        players.add(new Player("4"));

        gameHandler = mock(GameHandler.class);
        controller = new Controller(gameHandler);

        game = controller.createGame(1);
        when(gameHandler.getGame()).thenReturn(game);

        controller.addPlayerToGame(players.getFirst().getUsername(), desideredNumberOfPlayers);
        controller.addPlayerToGame(players.get(1).getUsername(), desideredNumberOfPlayers);
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
        controller.addPlayerToGame(players.getFirst().getUsername(), desideredNumberOfPlayers);
        assertEquals(1, game.getListOfPlayers().size());
        assertThrows(AlreadyExistingPlayerException.class, () -> controller.addPlayerToGame(players.getFirst().getUsername(), desideredNumberOfPlayers));

        controller.addPlayerToGame(players.get(1).getUsername(), desideredNumberOfPlayers);
        controller.addPlayerToGame(players.get(2).getUsername(), desideredNumberOfPlayers);
        assertEquals(3, game.getListOfPlayers().size());

        assertThrows(AlreadyMaxNumberOfPlayersException.class, () -> controller.addPlayerToGame(players.get(3).getUsername(), desideredNumberOfPlayers));
    }

    @Test
    void testInitializeGame() throws AlreadyMaxNumberOfPlayersException, CardTypeMismatchException, UnlinkedCardException, InvalidConstructorDataException, CardNotImportedException, IOException, AlreadyThreeCardsInHandException, DeckIsEmptyException, AlreadyExistingPlayerException {
        controller.initializeGame();

        assertTrue(game.getListOfPlayers().getFirst().getIsFirst());
        assertEquals(game.getListOfPlayers().getFirst(), game.getCurrentPlayer());
        assertEquals(2, game.getTableTop().getDrawingField().getDiscoveredGoldCards().size());
        assertEquals(2, game.getTableTop().getDrawingField().getDiscoveredResourceCards().size());
        assertEquals(GoldCard.class, game.getTableTop().getDrawingField().getDiscoveredGoldCards().get(DrawPosition.LEFT).getClass());
        assertEquals(ResourceCard.class, game.getTableTop().getDrawingField().getDiscoveredResourceCards().get(DrawPosition.LEFT).getClass());

        for(Player player : game.getListOfPlayers()) {
            assertEquals(3, player.getPlayerHand().getCardsInHand().size());
            assertEquals(2, player.getPlayerHand().getCardsInHand().stream().filter(card -> card instanceof ResourceCard).count());
            assertEquals(1, player.getPlayerHand().getCardsInHand().stream().filter(card -> card instanceof GoldCard).count());
            assertEquals(StarterCard.class, player.getStarterCard().getClass());
        }
    }

    @Test
    void testInitializeGameExceptions() throws DeckIsEmptyException, NotExistingPlayerException {
        assertThrows(NotExistingPlayerException.class, () -> controller.giveStarterCard(new Player("5")));
        for(int i = 0; i < 5; i++){
            controller.giveStarterCard(players.getFirst());
        }
        assertThrows(RuntimeException.class, () -> controller.initializeGame());
    }

    @Test
    void testMarkerSetting() throws NotExistingPlayerException {
        controller.setMarker(game.getPlayer("1"), Marker.BLUE);

        assertEquals(3, game.getAvailableMarkers().size());
        assertEquals(Marker.BLUE, game.getPlayer("1").getMarker());
    }

    @Test
    void testStarterCardInitialization() throws NotExistingPlayerException, DeckIsEmptyException {
        when(gameHandler.getPlayer("1")).thenReturn(game.getPlayer("1"));
        //FROM HERE
        controller.giveStarterCard(players.getFirst());
        assertNotNull(players.getFirst().getStarterCard());
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
