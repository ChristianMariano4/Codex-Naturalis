package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.exceptions.CardTypeMismatchException;
import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.Game;

import java.io.*;
import java.net.Socket;
import java.time.Clock;
import java.util.Date;

import static java.lang.Thread.sleep;

public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket,String name) {
        this.socket = socket;
        System.out.println(socket.toString() + " connected.");
        Thread.currentThread().setName(name);

    }


    @Override
    public void run() {
        try {
            System.out.println("game1");

            ObjectOutputStream outC = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("game2");

            Controller c = new Controller();
            System.out.println("game3");

            Game game = c.createGame();
            System.out.println("game4");

            System.out.println(game);
            System.out.println("game5");

            outC.writeObject(game.getObjectiveCardDeck().getTopCard());

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CardTypeMismatchException e) {
            throw new RuntimeException(e);
        } catch (InvalidConstructorDataException e) {
            throw new RuntimeException(e);
        } catch (CardNotImportedException e) {
            throw new RuntimeException(e);
        } catch (DeckIsEmptyException e) {
            throw new RuntimeException(e);
        }

    }
}
