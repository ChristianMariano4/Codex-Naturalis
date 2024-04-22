package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.network.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Game g;
    ArrayList<Player> listOfPlayers = new ArrayList<>();
    static int gameID = 123;
    static int numOfPlayers = 0;

    private void playersCreation() throws InvalidConstructorDataException {
        for(int i = 0; i < 4; i++) {
            Player p = new Player("test", g);
            listOfPlayers.add(p);
        }
    }

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
    void gameTestInit() throws CardTypeMismatchException, InvalidConstructorDataException, CardNotImportedException, DeckIsEmptyException, AlreadyExistingPlayerException, AlreadyFourPlayersException {
        Server server = new Server();
        Controller controller = new Controller(server);
        g = controller.createGame();
        playersCreation();
        add4Players();
    }
    @Test
    void shouldReturnGameId() {
        Assertions.assertEquals(gameID, g.getGameId());
    }
    @Test
    void shouldNotAddPlayerAlreadyExistingPlayerException() throws InvalidConstructorDataException, AlreadyExistingPlayerException, AlreadyFourPlayersException {
        Player p = new Player("test", g);
        g.addPlayer(p);
        Assertions.assertThrows(AlreadyExistingPlayerException.class, () -> {g.addPlayer(p);});
    }
    @Test
    void shouldNotAddPlayerAlreadyFourPlayersException() throws AlreadyExistingPlayerException, AlreadyFourPlayersException, InvalidConstructorDataException {
        add4Players();
        Player p5 = new Player("test", g);
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
    void shouldReturnNumberOfPlayers() throws AlreadyExistingPlayerException, AlreadyFourPlayersException, InvalidConstructorDataException {
        Player p = new Player("test", g);
        g.addPlayer(p);
        Assertions.assertEquals(numOfPlayers, g.getNumberOfPlayers());
    }
}