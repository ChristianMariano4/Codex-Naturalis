package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Game;

import java.io.Serializable;

public class EventWrapper implements Serializable {
    private final GameEvent type;
    private final Game message;

    public EventWrapper(GameEvent type, Game message) {
        this.type = type;
        this.message = message;
    }

    public GameEvent getType() {
        return type;
    }

    public Game getMessage() {
        return message;
    }
}
