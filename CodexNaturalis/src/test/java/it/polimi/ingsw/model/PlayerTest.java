package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.TripleObjectiveCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    public static int POINTS = 4;
    public static int RESAMOUNT = 6;
    Player p1;
    DrawingField d;
    @BeforeEach
    public void playerInit() throws InvalidConstructorDataException, CardTypeMismatchException, DeckIsEmptyException {
        // TODO: the player must be created by the controller
        List<PlayableCard> li = new ArrayList<>();
        Deck d1 = new Deck(li);
        Deck d2 = new Deck(li);
        d = new DrawingField(d1, d2);
        List<TripleObjectiveCard> l = new ArrayList<>();
        Game test = new Game(123, d, l);
        p1 = new Player("test", Marker.BLACK, test);
    }
    @Test
    void shouldReturnUsername()  {
        Assertions.assertEquals("test", p1.getUsername());
    }

    @Test
    void shouldReturnMarker() {
        Assertions.assertEquals(Marker.BLACK, p1.getMarker());
    }

    @Test
    void shouldReturnPlayerPoints() {
        p1.addPoints(4);
        Assertions.assertEquals(POINTS, p1.getPoints());
    }

    @Test
    void shouldReturnIsTurnTrue() {
        p1.setIsTurn(true);
        Assertions.assertTrue(p1.getIsTurn());
    }
    @Test
    void shouldReturnIsTurnFalse() {
        p1.setIsTurn(false);
        Assertions.assertFalse(p1.getIsTurn());
    }
    @Test
    void shouldReturnResourceAmount() {
        for(Resource res: Resource.values()) {
            if(res != Resource.NONE) {
                Assertions.assertEquals(0, p1.getResourceAmount(res));
            } else {
                Assertions.assertEquals(-1, p1.getResourceAmount(res));
            }
        }
    }

    @Test
    void shouldUpdateResourceAmount() throws NoneResourceException {
        for(Resource res: Resource.values()) {
            if(res != Resource.NONE) {
                p1.updateResourceAmount(res, RESAMOUNT);
                Assertions.assertEquals(6, p1.getResourceAmount(res));
            }
        }
    }
    @Test
    void shouldChooseResourceCardToDraw() {
        //Control into the playerHand if the Drawn card is present
    }
    @Test
    void shouldThrowsExceptionChooseResourceCardToDraw() throws NoCardAddedException, AlreadyThreeCardsInHandException {
        Assertions.assertThrows(NoCardAddedException.class, () -> {p1.chooseResourceCardToDraw(DrawPosition.LEFT);});
    }
}