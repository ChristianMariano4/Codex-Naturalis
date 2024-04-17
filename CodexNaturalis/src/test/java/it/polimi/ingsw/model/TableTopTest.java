package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.CardTypeMismatchException;
import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TableTopTest {

    Deck<PlayableCard> d1;
    Deck<PlayableCard> d2;
    TableTop t;
    DrawingField d;
    ArrayList<ObjectiveCard> obc;
    ArrayList<PlayableCard> cards;
/*
    @BeforeEach
    void TableTobInit() throws InvalidConstructorDataException, CardTypeMismatchException, DeckIsEmptyException {
        //Controller method for the Tabletop
        d1 = new Deck<PlayableCard>(cards);
        d2 = new Deck<PlayableCard>(cards);
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
        //Controller method for 4 players
        for(int i = 0; i < 4; i++) {
            //Add the players to the playerfield
            t.addPlayerField();
        }
        for(int i = 0; i < 4; i++) {
            Assertions.assertEquals(, t.getPlayerFieldHashMap().keySet().stream().equals());
        }
    }

    @Test
    void shouldReturnSharedObjectiveCards() {
        for(ObjectiveCard o: obc) {
            Assertions.assertEquals(obc, t.getSharedObjectiveCards());
        }
    }*/
}