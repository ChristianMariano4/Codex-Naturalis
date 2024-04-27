package it.polimi.ingsw.network.messages.userMessages;

import java.io.Serializable;

public abstract class UserMessage implements Serializable {
    private final String username;
    private int gameId;

    public UserMessage(String username, int gameId) {
        this.username = username;
        this.gameId = gameId;
    }

    public UserMessage(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public int getGameId() {
        return gameId;
    }
}
