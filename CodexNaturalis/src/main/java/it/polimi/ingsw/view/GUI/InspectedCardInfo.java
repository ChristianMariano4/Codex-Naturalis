package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.enumerations.CardType;
import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.enumerations.Side;

public class InspectedCardInfo {
    private boolean frontSide = true;
    private int cardInHandSelected = 3;
    private int sharedObjChoice = 2;
    private Side side = Side.FRONT;
    private CardType cardType;
    private boolean fromDeck;
    private DrawPosition drawPosition;

    public void setFrontSide(boolean frontSide) {
        this.frontSide = frontSide;
    }

    public void setCardInHandSelected(int cardInHandSelected) {
        this.cardInHandSelected = cardInHandSelected;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public void setDrawPosition(DrawPosition drawPosition) {
        this.drawPosition = drawPosition;
    }

    public void setFromDeck(boolean fromDeck) {
        this.fromDeck = fromDeck;
    }

    public void setSharedObjChoice(int sharedObjChoice) {
        this.sharedObjChoice = sharedObjChoice;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public CardType getCardType() {
        return cardType;
    }

    public DrawPosition getDrawPosition() {
        return drawPosition;
    }

    public int getCardInHandSelected() {
        return cardInHandSelected;
    }

    public int getSharedObjChoice() {
        return sharedObjChoice;
    }

    public Side getSide() {
        return side;
    }

    public boolean getFromDeck() {
        return this.fromDeck;
    }

    public boolean getFrontSide() {
        return this.frontSide;
    }
}
