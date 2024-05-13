package it.polimi.ingsw.network.messages.clientMessages;

public class JoinGameMessage extends UserMessage{
    public JoinGameMessage(String username, int gameId) {
        super(username, gameId);
    }
}
