package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.model.cards.Angle;

public class CardPlayedMessage extends UserMessage{
    private final Angle playedAngle;
    private final Angle where;

    public CardPlayedMessage(String username, int gameId, Angle playedAngle, Angle where) {
        super(username, gameId);
        this.playedAngle = playedAngle;
        this.where = where;
    }

    public Angle getPlayedAngle() {
        return playedAngle;
    }

    public Angle getWhere() {
        return where;
    }

}
