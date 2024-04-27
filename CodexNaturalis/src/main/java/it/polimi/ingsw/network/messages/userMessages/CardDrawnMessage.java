package it.polimi.ingsw.network.messages.userMessages;

import it.polimi.ingsw.enumerations.CardType;
import it.polimi.ingsw.enumerations.DrawPosition;

public class CardDrawnMessage extends UserMessage{
    private final CardType cardType;
    private final DrawPosition drawPosition;

    public CardDrawnMessage(String username, int gameId, CardType cardType, DrawPosition drawPosition) {
        super(username, gameId);
        this.cardType = cardType;
        this.drawPosition = drawPosition;;
    }

}
