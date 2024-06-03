package it.polimi.ingsw.network.observer;

import it.polimi.ingsw.network.messages.GameEvent;
import it.polimi.ingsw.network.observer.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

    public <E extends Enum<E>> void notify(E event, Object... args) {
        List<Listener<?>> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            for (Listener<?> listener : eventListeners) {
                Listener<E> typedListener = (Listener<E>) listener;
                typedListener.update(event, args);
            }
        } else if(eventListeners == null) {
        }
        System.out.println("Event: " + event);
    }

    public Listener<? extends Enum<?>> getListener(Class<? extends Enum<?>> classToListen, Function<Listener<? extends Enum<?>>, Boolean> func) {
        List<Listener<? extends Enum<?>>> listened = listeners.get(classToListen);
        if (listened == null) {
            return null;
        }
        for (Listener<? extends Enum<?>> l: listened) {
            if (func.apply(l)) {
                return l;
            }
        }
        return null;
    }
}
