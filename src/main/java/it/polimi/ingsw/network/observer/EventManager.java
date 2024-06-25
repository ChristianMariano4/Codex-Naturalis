package it.polimi.ingsw.network.observer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
/**
 * The EventManager class is responsible for managing event listeners and notifications.
 * It maintains a map of event types to listeners, allowing for efficient event dispatching.
 * Each event type is represented by a class that extends Enum, and each listener is an instance of the Listener interface.
 * The class provides methods for subscribing and unsubscribing listeners, notifying listeners of events, and retrieving listeners.
 */
public class EventManager {
    public Map<Class<?>, List<Listener<? extends Enum<?>>>> listeners = new HashMap<>();

    /**
     * Subscribes a listener to a specific event type.
     * If the event type is not already in the map, it is added with an empty list of listeners.
     * The listener is then added to the list of listeners for that event type.
     * @param eventType The class of the event type to subscribe to.
     * @param listener The listener to subscribe.
     * @param <E> The type of the event.
     */
    public <E extends Enum<E>> void subscribe(Class<E> eventType, Listener<E> listener) {
        if (!listeners.containsKey(eventType)) {
            listeners.put(eventType, new ArrayList<>());
        }
        List<Listener<?>> users = listeners.get(eventType);
        users.add(listener);
    }

    /**
     * Unsubscribes a listener from a specific event type.
     * If the event type and listener are in the map, the listener is removed from the list of listeners for that event type.
     * @param eventType The class of the event type to unsubscribe from.
     * @param listener The listener to unsubscribe.
     * @param <E> The type of the event.
     */
    public <E extends Enum<E>> void unsubscribe(Class<E> eventType, Listener<E> listener) {
        List<Listener<?>> eventListeners = listeners.get(eventType);
        if (eventListeners != null && listener != null) {
            eventListeners.remove(listener);
        }
        System.out.println("Listener removed: " + listeners.get(eventType).size());
    }

    /**
     * Notifies all listeners of a specific event type.
     * Each listener's update method is called with the event and any additional arguments.
     * @param event The event to notify listeners of.
     * @param args Any additional arguments to pass to the listeners.
     * @param <E> The type of the event.
     */
    @SuppressWarnings("unchecked")
    public <E extends Enum<E>> void notify(E event, Object... args) {
        List<Listener<?>> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            for (Listener<?> listener : eventListeners) {
                Listener<E> typedListener = (Listener<E>) listener;
                typedListener.update(event, args);
            }
        }
        System.out.println("Event: " + event);
    }

    /**
     * Retrieves a listener for a specific event type that matches a given condition.
     * The condition is specified by a function that takes a listener and returns a boolean.
     * The function is applied to each listener for the event type until it returns true or all listeners have been checked.
     * If a matching listener is found, it is returned. Otherwise, null is returned.
     * @param classToListen The class of the event type to retrieve a listener for.
     * @param func The function to apply to each listener.
     * @return The first listener for which the function returns true, or null if no such listener is found.
     */
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
