package it.polimi.ingsw.enumerations;

public enum GameStatus {
    LOBBY_CREATED(1),  //the game is created and players can join
    ALL_PLAYERS_JOINED(2), //all players have joined the game
    ALL_PLAYERS_READY(3), //all players have set their ready status
    GAME_STARTED(5); //the game has started and players can play

    private final int statusNumber;

    GameStatus(int statusNumber) {
        this.statusNumber = statusNumber;
    }

    public int getStatusNumber() {
        return this.statusNumber;
    }

}