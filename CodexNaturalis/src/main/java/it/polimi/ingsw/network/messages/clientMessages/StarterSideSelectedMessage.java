package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.enumerations.Side;

public class StarterSideSelectedMessage extends UserMessage{
    private final Side side;
    public StarterSideSelectedMessage(String username, int gameId, Side side) {
        super(username, gameId);
        this.side = side;
    }
}
