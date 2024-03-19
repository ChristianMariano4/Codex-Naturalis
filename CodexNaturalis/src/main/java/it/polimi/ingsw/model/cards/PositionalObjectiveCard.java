package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.PositionalType;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;

public class PositionalObjectiveCard extends ObjectiveCard{

    private final Resource cardColor;
    private final AngleOrientation orientation;
    private final PositionalType positionalType;
    public PositionalObjectiveCard(int cardId, Side currentSide, int points, Resource cardColor, AngleOrientation orientation, PositionalType positionalType) {
        super(cardId, currentSide, points);
        this.cardColor = cardColor;
        this.orientation = orientation;
        this.positionalType = positionalType;
    }

    public Resource getCardColor()
    {
        return cardColor;
    }
    public AngleOrientation getOrientation()
    {
        return orientation;
    }
    public PositionalType getPositionalType() {
        return positionalType;
    }
}
