package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.messages.GameEvent;

public interface View extends Runnable{
    void update(Game message, GameEvent event);
    void run();

}
