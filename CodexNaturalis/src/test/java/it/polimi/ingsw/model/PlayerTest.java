package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerTest {

    private Player player;
    private Game game;
    private PlayableCard card;

    @BeforeEach
    void setUp() throws InvalidConstructorDataException {
        game = mock(Game.class);
        TableTop tableTop = mock(TableTop.class);
        DrawingField drawingField = mock(DrawingField.class);
        when(game.getTableTop()).thenReturn(tableTop);
        when(tableTop.getDrawingField()).thenReturn(drawingField);
        player = new Player("testPlayer");
        player.setGame(game);
        card = mock(PlayableCard.class);
    }

    @Test
    void addPointsIncreasesPlayerPoints() {
        player.addPoints(5);
        assertEquals(5, player.getPoints());
    }

    @Test
    void updateResourceAmountIncreasesResourceAmount() throws NoneResourceException {
        player.updateResourceAmount(Resource.ANIMAL, 3);
        assertEquals(3, player.getResourceAmount(Resource.ANIMAL));
    }

    @Test
    void updateResourceAmountThrowsExceptionForNoneResource() {
        assertThrows(NoneResourceException.class, () -> player.updateResourceAmount(Resource.NONE, 3));
    }

    @Test
    void chooseGoldCardToDrawAddsCardToPlayerHand() throws AlreadyThreeCardsInHandException, NoCardAddedException, DeckIsEmptyException {
        GoldCard goldCard = mock(GoldCard.class);
        when(game.getTableTop().getDrawingField().drawCardFromGoldCardDeck(any())).thenReturn(goldCard);
        player.chooseGoldCardToDraw(DrawPosition.LEFT);
        assertTrue(player.getPlayerHand().getCardsInHand().contains(goldCard));
    }

    @Test
    void chooseResourceCardToDrawAddsCardToPlayerHand() throws AlreadyThreeCardsInHandException, NoCardAddedException, DeckIsEmptyException {
        ResourceCard resourceCard = mock(ResourceCard.class);
        when(game.getTableTop().getDrawingField().drawCardFromResourceCardDeck(any())).thenReturn(resourceCard);
        player.chooseResourceCardToDraw(DrawPosition.LEFT);
        assertTrue(player.getPlayerHand().getCardsInHand().contains(resourceCard));
    }

    @Test
    void testSetIsTurn() {
        player.setIsTurn(true);
        assertTrue(player.getIsTurn());
    }

    @Test
    void testSetIsFirst() {
        player.setIsFirst(true);
        assertTrue(player.getIsFirst());
    }

    @Test
    void testSetMarker() {
        Marker marker = Marker.BLUE;
        player.setMarker(marker);
        assertEquals(marker, player.getMarker());
    }

    @Test
    void testGetUsername() {
        assertEquals("testPlayer", player.getUsername());
    }

    @Test
    void testGetPlayerField() {
        assertNotNull(player.getPlayerField());
    }

    @Test
    void testGetPlayerHand() {
        assertNotNull(player.getPlayerHand());
    }

    @Test
    void testSetAndGetSecretObjective() {
        ObjectiveCard objectiveCard = mock(ObjectiveCard.class);
        player.setSecretObjective(objectiveCard);
        assertEquals(objectiveCard, player.getSecretObjective());
    }

    @Test
    void testSetAndGetStarterCard() {
        StarterCard starterCard = mock(StarterCard.class);
        player.setStarterCard(starterCard);
        assertEquals(starterCard, player.getStarterCard());
    }

    @Test
    void testUpdateResourceAmountWithNoneResource() {
        assertThrows(NoneResourceException.class, () -> player.updateResourceAmount(Resource.NONE, 3));
    }

    @Test
    void testUpdateResourceAmountWithValidResource() throws NoneResourceException {
        player.updateResourceAmount(Resource.ANIMAL, 3);
        assertEquals(3, player.getResourceAmount(Resource.ANIMAL));
    }

    @Test
    void testChooseGoldCardToDrawWithDeckIsEmptyException() throws AlreadyThreeCardsInHandException, NoCardAddedException, DeckIsEmptyException {
        when(game.getTableTop().getDrawingField().drawCardFromGoldCardDeck(any())).thenThrow(new DeckIsEmptyException());
        assertThrows(NoCardAddedException.class, () -> player.chooseGoldCardToDraw(DrawPosition.LEFT));
    }

    @Test
    void testChooseResourceCardToDrawWithDeckIsEmptyException() throws AlreadyThreeCardsInHandException, NoCardAddedException, DeckIsEmptyException {
        when(game.getTableTop().getDrawingField().drawCardFromResourceCardDeck(any())).thenThrow(new DeckIsEmptyException());
        assertThrows(NoCardAddedException.class, () -> player.chooseResourceCardToDraw(DrawPosition.LEFT));
    }
}