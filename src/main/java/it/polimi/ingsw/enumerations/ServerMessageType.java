package it.polimi.ingsw.enumerations;

import java.io.Serializable;

/**
 * This enumeration represents the type of messages that the server can send to the client.
 */
public enum ServerMessageType implements Serializable {
    HEARTBEAT,
    SUCCESS,
    ERROR,
    UPDATE,
    GAME_CREATED,
    PLAYER_ADDED,
    USERNAME_CHECK_RESULT,
    AVAILABLE_GAMES,
    OTHER_SIDE_PLAYABLE,
    OTHER_SIDE_STARTER,
    CARD_INFO,
    READY_STATUS,
    REQUESTED_CARD,
    PLAYED_CARD_SUCCESS

}
