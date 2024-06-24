package it.polimi.ingsw.network.observer;

/**
 * The Listener interface defines a single method, update, which is used to update the state of an object when an event of type T occurs.
 * This is a generic interface where T is an Enum. This allows the Listener to be used with any type of Enum to represent different kinds of events.
 *
 * @param <T> The type of the event. This is an Enum.
 */
public interface Listener<T extends Enum<T>> {
    /**
     * This method is called when an event of type T occurs. The specific implementation of this method would define what action should be taken.
     *
     * @param event The event that occurred. This is of type T.
     * @param args Additional arguments that may be needed for the update.
     */
    void update(T event, Object... args);
}