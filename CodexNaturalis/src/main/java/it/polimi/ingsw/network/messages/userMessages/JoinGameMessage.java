package it.polimi.ingsw.network.messages.userMessages;

public class JoinGameMessage extends UserMessage{
    public JoinGameMessage(String username, int gameId) {
        super(username, gameId);
    }
}
