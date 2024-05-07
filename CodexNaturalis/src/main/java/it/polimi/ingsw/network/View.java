package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.messages.GameEvent;

public interface View  {
    void boardUpdate(Game gameUpdated);
    void newPlayer(Game gameUpdated);
    void gameBegin();
    void update(Game gameUpdated);
}
