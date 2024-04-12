package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.CardTypeMismatchException;
import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableTopTest {

    Deck d1;
    Deck d2;
    TableTop t;
    DrawingField d;
    List<ObjectiveCard> obc;
    List<PlayableCard> cards;

    @BeforeEach
    public void TableTobInit() throws InvalidConstructorDataException, CardTypeMismatchException, DeckIsEmptyException {
        //Constructor for the Tabletop
        d1 = new Deck(cards);
        d2 = new Deck(cards);
        d = new DrawingField(d1, d2);
        obc = new ArrayList<>();
        t = new TableTop(d, obc);
    }

    @Test
    void shouldReturnDrawingField() {
        Assertions.assertEquals(d, t.getDrawingField());
    }

    @Test
    void shouldGetPlayerFieldHashMap() {
        //4 constructor for the players
        for(int i = 0; i < 4; i++) {
            //Add the players to the playerfield
            t.addPlayerField(/*Player*/);
        }
        for(int i = 0; i < 4; i++) {
            Assertions.assertEquals(/*Player*/, t.getPlayerFieldHashMap().keySet().stream().equals(/*Player*/));
        }
    }

    @Test
    void shouldReturnSharedObjectiveCards() {
        for(ObjectiveCard o: obc) {
            Assertions.assertEquals(obc, t.getSharedObjectiveCards());
        }
    }
}