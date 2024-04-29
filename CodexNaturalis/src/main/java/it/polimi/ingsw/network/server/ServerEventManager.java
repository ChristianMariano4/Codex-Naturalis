package it.polimi.ingsw.network.server;

import it.polimi.ingsw.network.EventManager;
import it.polimi.ingsw.network.Listener;
import it.polimi.ingsw.network.messages.GameEvent;

import java.util.List;

public class ServerEventManager extends EventManager {
    public <E extends Enum<E>> void sendToClients(GameEvent event) {
        List<Listener<?>> eventListeners = listeners.get(GameEvent.class);
        if(eventListeners != null) {
            for(Listener<?> listener : eventListeners) {
                GameListener gameListener = (GameListener) listener;
                gameListener.update(event);
            }
        }
    }
}
