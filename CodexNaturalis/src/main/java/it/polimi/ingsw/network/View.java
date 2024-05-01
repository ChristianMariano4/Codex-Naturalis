package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.messages.GameEvent;

public interface View extends Runnable {
    void update(GameEvent event, Game gameUpdated);
    void run();
}
