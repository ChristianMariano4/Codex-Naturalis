package it.polimi.ingsw.enumerations;

import java.io.Serializable;

public enum ServerMessageType implements Serializable {
    SUCCESS,
    ERROR,
    UPDATE,
    GAME_CREATED,
    PLAYER_ADDED,
    USERNAME_CHECK_RESULT,
    AVAILABLE_GAMES,
    READY_PLAYERS,
    OTHER_SIDE_PLAYABLE,
    OTHER_SIDE_STARTER,
    CARD_INFO,

}
