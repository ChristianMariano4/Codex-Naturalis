package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.exceptions.InvalidCardPositionException;
import it.polimi.ingsw.exceptions.NoCardAddedException;
import it.polimi.ingsw.model.cards.StarterCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

import static it.polimi.ingsw.model.GameValues.DEFAULT_MATRIX_SIZE;
import static org.junit.jupiter.api.Assertions.*;

class PlayerFieldTest {
/*
    PlayerField p;

    @BeforeEach
    void PlayerFieldInit() {
        p = new PlayerField();
    }
    @Test
    void shouldReturnMatrixField() {
        StarterCard[][] returnedMatrixField = p.getMatrixField();
        StarterCard[][] emptyMatrixFiled = new StarterCard[DEFAULT_MATRIX_SIZE][DEFAULT_MATRIX_SIZE];
        for(int i = 0; i < DEFAULT_MATRIX_SIZE; i++) {
            for(int j = 0; j < DEFAULT_MATRIX_SIZE; j++) {
                Assertions.assertEquals(emptyMatrixFiled[i][j], returnedMatrixField[i][j]);
            }
        }
    }

    @Test
    void shouldNotAddCardToCell() {
        Assertions.assertThrows(InvalidCardPositionException.class, () -> {p.addCardToCell();});
    }*/
}