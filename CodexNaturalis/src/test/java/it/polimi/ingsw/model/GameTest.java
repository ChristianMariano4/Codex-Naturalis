package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.exceptions.AlreadyExistingPlayerException;
import it.polimi.ingsw.exceptions.AlreadyFourPlayersException;
import it.polimi.ingsw.exceptions.NoCardAddedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Game g;
    ArrayList<Player> listOfPlayers = new ArrayList<>(4);
    static int gameID = 123;
    static int numOfPlayers = 0;

    private void add4Players() throws AlreadyExistingPlayerException, AlreadyFourPlayersException {
        //p1 Controller method
        g.addPlayer(listOfPlayers.get(0));
        //p2 Controller method
        g.addPlayer(listOfPlayers.get(1));
        //p3 Controller method
        g.addPlayer(listOfPlayers.get(2));
        //p4 Controller method
        g.addPlayer(listOfPlayers.get(3));
    }
    @BeforeEach
    void gameTestInit() {
        //Game Controller method
    }
    @Test
    void shouldReturnGameId() {
        Assertions.assertEquals(gameID, g.getGameId());
    }
    @Test
    void shouldNotAddPlayerAlreadyExistingPlayerException() {
        Player p; //Player Controller method
        g.addPlayer(p);
        Assertions.assertThrows(AlreadyExistingPlayerException.class, () -> {g.addPlayer(p);});
    }
    @Test
    void shouldNotAddPlayerAlreadyFourPlayersException() throws AlreadyExistingPlayerException, AlreadyFourPlayersException {
        add4Players();
        Player p5; //Player Controller method
        Assertions.assertThrows(AlreadyExistingPlayerException.class, () -> {g.addPlayer(p5);;});
    }
    @Test
    void shouldReturnListOfPlayer() throws AlreadyExistingPlayerException, AlreadyFourPlayersException {
        add4Players();
        for(Player p: listOfPlayers) {
            Assertions.assertTrue(g.getListOfPlayers().contains(p));
        }
    }
    @Test
    void shouldReturnNumberOfPlayers() throws AlreadyExistingPlayerException, AlreadyFourPlayersException {
        Player p; //Player Controller method
        g.addPlayer(p);
        Assertions.assertEquals(numOfPlayers, g.getNumberOfPlayers());
    }

    @Test
    void shouldReturnTypeTableTop() {
        Assertions.assertInstanceOf(TableTop, g.getTableTop()); //TODO: should check if it return the right type
    }


}