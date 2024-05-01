package it.polimi.ingsw.network.messages.userMessages;

import java.io.Serializable;

public enum UserInputEvent implements Serializable {
    USERNAME_INSERTED,
    CREATE_GAME,
    JOIN_GAME,
    CARD_PLAYED,
    AVAILABLE_GAMES_REFRESHED,
    CARD_DRAWN,
    CARD_SIDE_CHOSEN

}
