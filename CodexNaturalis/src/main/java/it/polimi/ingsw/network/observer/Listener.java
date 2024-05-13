package it.polimi.ingsw.network.observer;

import it.polimi.ingsw.network.messages.userMessages.UserMessageWrapper;

public interface Listener<T extends Enum<T>> {
    void update(T event, Object... args);
}
