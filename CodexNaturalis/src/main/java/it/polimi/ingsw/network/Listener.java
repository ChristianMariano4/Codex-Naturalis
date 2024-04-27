package it.polimi.ingsw.network;

public interface Listener<T extends Enum<T>> {
    void update(T event, Object... args);
}
