package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.*;
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
    private final List<Player> players = new ArrayList<>();
    int desiredNumberOfPlayers = 3;
    Angle angle = mock(Angle.class);

    ArrayList<StarterCard> starterCards;
    ArrayList<ResourceCard> resourceCards;
    ArrayList<GoldCard> goldCards;


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

        controller.addPlayerToGame(players.getFirst().getUsername(), desiredNumberOfPlayers);
        controller.addPlayerToGame(players.get(1).getUsername(), desiredNumberOfPlayers);

        starterCards = controller.getCardHandler().importStarterCards();
        resourceCards = controller.getCardHandler().importResourceCards();
        goldCards = controller.getCardHandler().importGoldCards();
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
        assertEquals(2, game.getListOfPlayers().size());
        assertThrows(AlreadyExistingPlayerException.class, () -> controller.addPlayerToGame(players.getFirst().getUsername(), desiredNumberOfPlayers));

        controller.addPlayerToGame(players.get(2).getUsername(), desiredNumberOfPlayers);
        assertEquals(3, game.getListOfPlayers().size());

        assertThrows(AlreadyMaxNumberOfPlayersException.class, () -> controller.addPlayerToGame(players.get(3).getUsername(), desiredNumberOfPlayers));
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
    void testStarterCardFrontInitialization() throws NotExistingPlayerException, CardNotFoundException {
        when(gameHandler.getPlayer("1")).thenReturn(game.getPlayer("1"));

        StarterCard starterCard1 = mock(StarterCard.class);
        StarterCard starterCard2 = mock(StarterCard.class);
        when(starterCard1.getCurrentSide()).thenReturn(Side.FRONT);
        when(starterCard2.getCurrentSide()).thenReturn(Side.BACK);

        when(starterCard1.getAngle(AngleOrientation.BOTTOMLEFT)).thenReturn(angle);
        when(starterCard1.getAngle(AngleOrientation.BOTTOMRIGHT)).thenReturn(angle);
        when(starterCard1.getAngle(AngleOrientation.TOPLEFT)).thenReturn(angle);
        when(starterCard1.getAngle(AngleOrientation.TOPRIGHT)).thenReturn(angle);
        when(starterCard2.getAngle(AngleOrientation.BOTTOMLEFT)).thenReturn(angle);
        when(starterCard2.getAngle(AngleOrientation.BOTTOMRIGHT)).thenReturn(angle);
        when(starterCard2.getAngle(AngleOrientation.TOPLEFT)).thenReturn(angle);
        when(starterCard2.getAngle(AngleOrientation.TOPRIGHT)).thenReturn(angle);

        when(starterCard1.getCardId()).thenReturn(0);
        when(starterCard2.getCardId()).thenReturn(0);
        ArrayList<Resource> centraLResources = new ArrayList<>();
        centraLResources.add(Resource.INSECT);
        when(starterCard1.getCentralResources()).thenReturn(centraLResources);
        when(starterCard2.getCentralResources()).thenReturn(new ArrayList<>());

        when(angle.getResource()).thenReturn(Resource.INSECT);
        when(angle.getAngleStatus()).thenReturn(AngleStatus.UNLINKED);
        when(angle.isPlayable()).thenReturn(true);

        controller.initializeStarterCard(game.getPlayer("1"), starterCard1, Side.FRONT);
        assertEquals(starterCard1, game.getPlayer("1").getPlayerField().getCardById(0));
        assertEquals(5, game.getPlayer("1").getResourceAmount(Resource.INSECT));
    }

    @Test
    void testStarterCardBackInitialization() throws NotExistingPlayerException, CardNotFoundException {
        when(gameHandler.getPlayer("1")).thenReturn(game.getPlayer("1"));

        StarterCard starterCard1 = starterCards.getFirst();

        controller.initializeStarterCard(game.getPlayer("1"), starterCard1, Side.BACK);
        assertEquals(Side.BACK, game.getPlayer("1").getPlayerField().getCardById(starterCard1.getCardId()).getCurrentSide());
        assertEquals(1, game.getPlayer("1").getResourceAmount(Resource.INSECT));
        assertEquals(1, game.getPlayer("1").getResourceAmount(Resource.FUNGI));
        assertEquals(1, game.getPlayer("1").getResourceAmount(Resource.PLANT));
        assertEquals(1, game.getPlayer("1").getResourceAmount(Resource.ANIMAL));
    }

    @Test
    void testTakeTwoObjectiveCards() throws DeckIsEmptyException {
        ArrayList<ObjectiveCard> objectiveCards = controller.takeTwoObjectiveCards();
        assertEquals(2, objectiveCards.size());
        assertEquals(ObjectiveCard.class, objectiveCards.get(0).getClass().getSuperclass());
        assertEquals(ObjectiveCard.class, objectiveCards.get(1).getClass().getSuperclass());
    }

    @Test
    void testSecretObjectiveCardSetting() throws NotExistingPlayerException {
        ObjectiveCard mockObjectiveCard = mock(ObjectiveCard.class);
        when(mockObjectiveCard.getCardId()).thenReturn(1);
        when(gameHandler.getPlayer("1")).thenReturn(game.getPlayer("1"));
        controller.setSecretObjectiveCard(game.getPlayer("1"), mockObjectiveCard);
        assertEquals(1, game.getPlayer("1").getSecretObjective().getCardId());
    }

    @Test
    void testPlayCard() throws NotExistingPlayerException, InvalidCardPositionException, RequirementsNotMetException, CardTypeMismatchException, AngleAlreadyLinkedException, CardNotFoundException, AlreadyThreeCardsInHandException, DeckIsEmptyException {
        when(gameHandler.getPlayer("1")).thenReturn(game.getPlayer("1"));
        controller.initializePlayerHand(game.getPlayer("1"));

        StarterCard starterCard = starterCards.get(1);
        game.getPlayer("1").getPlayerField().addCardToCell(starterCard);

        assertThrows(RequirementsNotMetException.class, () -> controller.playCard(game.getPlayer("1"), starterCard, game.getPlayer("1").getPlayerHand().getCardsInHand().getFirst(), AngleOrientation.TOPLEFT));
        PlayableCard resourceCard = game.getPlayer("1").getPlayerHand().getCardsInHand().get(1);
        controller.playCard(game.getPlayer("1"), starterCard, resourceCard, AngleOrientation.TOPLEFT);
        assertEquals(resourceCard, game.getPlayer("1").getPlayerField().getCardById(resourceCard.getCardId()));
    }

    @Test
    void testSetPlayerDisconnected() throws NotExistingPlayerException {
        when(gameHandler.getPlayer("1")).thenReturn(game.getPlayer("1"));
        controller.setPlayerDisconnected("1");
        assertTrue(game.getPlayer("1").getIsDisconnected());
    }

    @Test
    void testDrawResourceCard() throws NotExistingPlayerException, AlreadyThreeCardsInHandException, DeckIsEmptyException {
        when(gameHandler.getPlayer("1")).thenReturn(game.getPlayer("1"));
        game.getTableTop().getDrawingField().getDiscoveredResourceCards().put(DrawPosition.LEFT, resourceCards.getFirst());
        controller.drawCard(game.getListOfPlayers().getFirst(), CardType.RESOURCE, DrawPosition.LEFT);
        assertEquals(1, game.getListOfPlayers().getFirst().getPlayerHand().getCardsInHand().size());
    }

    @Test
    void testDrawGoldCard() throws NotExistingPlayerException, AlreadyThreeCardsInHandException, DeckIsEmptyException {
        when(gameHandler.getPlayer("1")).thenReturn(game.getPlayer("1"));
        game.getTableTop().getDrawingField().getDiscoveredGoldCards().put(DrawPosition.LEFT, goldCards.getFirst());
        controller.drawCard(game.getListOfPlayers().getFirst(), CardType.GOLD, DrawPosition.LEFT);
        assertEquals(1, game.getListOfPlayers().getFirst().getPlayerHand().getCardsInHand().size());
    }

    @Test
    void testNextTurn() throws NotExistingPlayerException {
        when(gameHandler.getPlayer("1")).thenReturn(game.getPlayer("1"));
        when(gameHandler.getPlayer("2")).thenReturn(game.getPlayer("2"));

        controller.nextTurn(game.getPlayer("1"));
        assertEquals(game.getPlayer("2"), game.getCurrentPlayer());
    }

    @Test
    void testNextTurnWithDisconnections() throws NotExistingPlayerException, AlreadyMaxNumberOfPlayersException, AlreadyExistingPlayerException {
        controller.addPlayerToGame(players.get(2).getUsername(), desiredNumberOfPlayers);
        when(gameHandler.getPlayer("1")).thenReturn(game.getPlayer("1"));
        when(gameHandler.getPlayer("2")).thenReturn(game.getPlayer("2"));
        when(gameHandler.getPlayer("3")).thenReturn(game.getPlayer("3"));
        game.getPlayer("2").setDisconnected();
        controller.nextTurn(game.getPlayer("1"));
        assertEquals(game.getPlayer("3"), game.getCurrentPlayer());
    }

    @Test
    void testNextTurnReturnsToFirstPlayer() throws NotExistingPlayerException, AlreadyMaxNumberOfPlayersException, AlreadyExistingPlayerException {
        controller.addPlayerToGame(players.get(2).getUsername(), desiredNumberOfPlayers);
        when(gameHandler.getPlayer("1")).thenReturn(game.getPlayer("1"));
        when(gameHandler.getPlayer("2")).thenReturn(game.getPlayer("2"));
        when(gameHandler.getPlayer("3")).thenReturn(game.getPlayer("3"));
        game.getPlayer("2").setDisconnected();
        controller.nextTurn(game.getPlayer("1"));
        controller.nextTurn(game.getPlayer("3"));
        assertEquals(game.getPlayer("1"), game.getCurrentPlayer());
    }

    @Test
    void testNextTurnReturnsToFirstPlayerWhenLastIsDisconnected() throws NotExistingPlayerException, AlreadyMaxNumberOfPlayersException, AlreadyExistingPlayerException {
        controller.addPlayerToGame(players.get(2).getUsername(), desiredNumberOfPlayers);
        when(gameHandler.getPlayer("1")).thenReturn(game.getPlayer("1"));
        when(gameHandler.getPlayer("2")).thenReturn(game.getPlayer("2"));
        when(gameHandler.getPlayer("3")).thenReturn(game.getPlayer("3"));
        game.getPlayer("3").setDisconnected();
        controller.nextTurn(game.getPlayer("1"));
        controller.nextTurn(game.getPlayer("2"));
        assertEquals(game.getPlayer("1"), game.getCurrentPlayer());
    }
}
