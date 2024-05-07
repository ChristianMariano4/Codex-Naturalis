package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.messages.GameEvent;

import java.rmi.RemoteException;

public interface View  {
    void boardUpdate(Game gameUpdated);
    void newPlayer(Game gameUpdated);
    void gameBegin() throws RemoteException;
    void update(Game gameUpdated);

}
