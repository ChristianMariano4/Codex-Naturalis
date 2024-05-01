package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.userMessages.UserInputEvent;
import it.polimi.ingsw.network.messages.userMessages.UserMessageWrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    public Map<Class<?>, List<Listener<? extends Enum<?>>>> listeners = new HashMap<>();

    public <E extends Enum<E>> void subscribe(Class<E> eventType, Listener<E> listener) {
        if (!listeners.containsKey(eventType)) {
            listeners.put(eventType, new ArrayList<>());
        }
        List<Listener<?>> users = listeners.get(eventType);
        users.add(listener);
    }

    public <E extends Enum<E>> void unsubscribe(Class<E> eventType, Listener<E> listener) {
        List<Listener<?>> eventListeners = listeners.get(eventType);
        if (eventListeners != null && listener != null) {
            eventListeners.remove(listener);
        }
    }

    public <E extends Enum<E>> void notify(UserMessageWrapper message) {
        System.out.println("here: " + listeners.size());
        List<Listener<?>> eventListeners = listeners.get(message.getType().getClass());
        if (eventListeners != null) {
            System.out.println("event Listeners not null");
            for (Listener<?> listener : eventListeners) {
                Listener<E> typedListener = (Listener<E>) listener;
                typedListener.update(message);
            }
        }
    }

}
