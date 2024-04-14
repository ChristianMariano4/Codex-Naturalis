package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.model.cards.GoldCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DrawingFieldTest {

    DrawingField d;
    public static DrawPosition chosenPosition; //To assign
    @BeforeEach
    void DrawingFiledInit() {
        //Controller method for the creation of the drawing field
    }

    @Test
    void shouldNotDrawCardFromGoldCardDeck() {
        //It must draw all cards from a specific field then check if the exception is thrown
    }

    @Test
    void shouldNotDrawCardFromResourceCardDeck() {
        //It must draw all cards from a specific field then check if the exception is thrown
    }
}