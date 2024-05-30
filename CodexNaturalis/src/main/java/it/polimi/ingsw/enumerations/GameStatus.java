package it.polimi.ingsw.enumerations;

public enum GameStatus {
    LOBBY_CREATED(1),
    ALL_PLAYERS_JOINED(2),
    ALL_PLAYERS_READY(3),
    ALL_MARKERS_ASSIGNED(4),
    GAME_STARTED(5);

    private final int statusNumber;

    GameStatus(int statusNumber) {
        this.statusNumber = statusNumber;
    }

    public int getStatusNumber() {
        return this.statusNumber;
    }
}