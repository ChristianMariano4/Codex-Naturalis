package it.polimi.ingsw.network.client;

/**
 * Class representing the information of a client.
 * This class includes the username, last heartbeat, and game ID of a client.
 */
public class ClientInfo {
    private String username;
    private long lastHeartbeat;
    private int gameId = -1;

    /**
     * Gets the username of the client.
     * @return the username of the client.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Constructor for the ClientInfo class.
     * @param lastHeartbeat the last heartbeat of the client.
     */
    public ClientInfo(long lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    /**
     * Gets the last heartbeat of the client.
     * @return the last heartbeat of the client.
     */
    public long getLastHeartbeat() {
        return lastHeartbeat;
    }

    /**
     * Sets the last heartbeat of the client.
     * @param lastHeartbeat the last heartbeat to be set.
     */
    public void setLastHeartbeat(long lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    /**
     * Gets the game ID of the client.
     * @return the game ID of the client.
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Sets the game ID of the client.
     * @param gameId the game ID to be set.
     */
    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    /**
     * Sets the username of the client.
     * @param username the username to be set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Resets the game ID of the client to -1.
     */
    public void reset() {
        gameId = -1;
    }
}