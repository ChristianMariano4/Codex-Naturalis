package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.exceptions.CardTypeMismatchException;
import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.TripleObjectiveCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void shouldReturnUsername() throws InvalidConstructorDataException, CardTypeMismatchException, DeckIsEmptyException {
        List<PlayableCard> li = new ArrayList<>();
        Deck d1 = new Deck(li);
        Deck d2 = new Deck(li);
        DrawingField d = new DrawingField(d1, d2);
        List<TripleObjectiveCard> l = new ArrayList<>();
        Game test = new Game(123, d, l);
        Player p1 = new Player("test", Marker.BLACK, test);
    }
}