package it.polimi.ingsw.enumerations;

import java.io.Serializable;

public enum ServerMessageType implements Serializable {
    SUCCESS,
    ERROR,
    UPDATE,
    GAME_CREATED,
    PLAYER_ADDED,
    USERNAME_REQUEST,
    USERNAME_CHECK_RESULT,
    AVAILABLE_GAMES,
    READY_PLAYERS,

}
