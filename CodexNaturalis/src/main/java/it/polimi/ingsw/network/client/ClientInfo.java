package it.polimi.ingsw.network.client;

public class ClientInfo {
    private String username;
    private long lastHeartbeat;
    private int gameId = -1;

    public String getUsername() {
        return username;
    }

    public ClientInfo(long lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    public long getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(long lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}