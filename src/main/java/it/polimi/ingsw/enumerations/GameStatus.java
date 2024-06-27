package it.polimi.ingsw.enumerations;

/**
 * This enumeration represents the status of a game.
 */
public enum GameStatus {
    LOBBY_CREATED(1),  //the game is created and players can join
    ALL_PLAYERS_JOINED(2), //all players have joined the game
    ALL_PLAYERS_READY(3), //all players have set their ready status
    GAME_STARTED(5); //the game has started and players can play

    private final int statusNumber;

    /**
     * Constructor.
     * @param statusNumber the number associated with the status
     */
    GameStatus(int statusNumber) {
        this.statusNumber = statusNumber;
    }

    /**
     * This method returns the number associated with the status.
     * @return the number associated with the status
     */
    public int getStatusNumber() {
        return this.statusNumber;
    }

}