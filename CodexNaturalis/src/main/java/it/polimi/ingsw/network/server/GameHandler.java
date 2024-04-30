package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.exceptions.CardTypeMismatchException;
import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.Game;

public class GameHandler {
    public Game game;
    private Controller controller;
    public GameHandler() throws CardTypeMismatchException, InvalidConstructorDataException, CardNotImportedException, DeckIsEmptyException {
        this.controller = new Controller();
        this.game = controller.createGame();
    }

}
