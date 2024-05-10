package it.polimi.ingsw.network;

import it.polimi.ingsw.exceptions.NotExistingPlayerException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.messages.GameEvent;

import java.io.IOException;
import java.rmi.RemoteException;

public interface View  {
    void boardUpdate(Game gameUpdated);
    void newPlayer(Game gameUpdated);
    void update(Game gameUpdated);
    void gameLoop() throws IOException, NotExistingPlayerException, InterruptedException;

    void twentyPoints(String username);
}
