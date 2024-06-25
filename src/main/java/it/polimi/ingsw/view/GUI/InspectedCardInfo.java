package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.enumerations.CardType;
import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.enumerations.Side;

/**
 * This class is used to store information about an inspected card in the game.
 * It includes details such as the side of the card, the type of the card, the position of the card, etc.
 */
public class InspectedCardInfo {
    private boolean frontSide = true;
    private int cardInHandSelected = 3;
    private int sharedObjChoice = 2;
    private Side side = Side.FRONT;
    private CardType cardType;
    private boolean fromDeck;
    private DrawPosition drawPosition;

    /**
     * Sets the side of the card.
     * @param frontSide A boolean indicating whether the front side of the card is being viewed.
     */
    public void setFrontSide(boolean frontSide) {
        this.frontSide = frontSide;
    }

    /**
     * Sets the selected card in hand.
     * @param cardInHandSelected The index of the selected card in hand.
     */
    public void setCardInHandSelected(int cardInHandSelected) {
        this.cardInHandSelected = cardInHandSelected;
    }

    /**
     * Sets the type of the card.
     * @param cardType The type of the card.
     */
    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    /**
     * Sets the position of the card.
     * @param drawPosition The position of the card.
     */
    public void setDrawPosition(DrawPosition drawPosition) {
        this.drawPosition = drawPosition;
    }

    /**
     * Sets whether the card is from the deck.
     * @param fromDeck A boolean indicating whether the card is from the deck.
     */
    public void setFromDeck(boolean fromDeck) {
        this.fromDeck = fromDeck;
    }

    /**
     * Sets the choice of the shared objective.
     * @param sharedObjChoice The choice of the shared objective.
     */
    public void setSharedObjChoice(int sharedObjChoice) {
        this.sharedObjChoice = sharedObjChoice;
    }

    /**
     * Sets the side of the card.
     * @param side The side of the card.
     */
    public void setSide(Side side) {
        this.side = side;
    }

    /**
     * Gets the type of the card.
     * @return The type of the card.
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * Gets the position of the card.
     * @return The position of the card.
     */
    public DrawPosition getDrawPosition() {
        return drawPosition;
    }

    /**
     * Gets the selected card in hand.
     * @return The index of the selected card in hand.
     */
    public int getCardInHandSelected() {
        return cardInHandSelected;
    }

    /**
     * Gets the choice of the shared objective.
     * @return The choice of the shared objective.
     */
    public int getSharedObjChoice() {
        return sharedObjChoice;
    }

    /**
     * Gets the side of the card.
     * @return The side of the card.
     */
    public Side getSide() {
        return side;
    }

    /**
     * Gets whether the card is from the deck.
     * @return A boolean indicating whether the card is from the deck.
     */
    public boolean getFromDeck() {
        return this.fromDeck;
    }

    /**
     * Gets whether the front side of the card is being viewed.
     * @return A boolean indicating whether the front side of the card is being viewed.
     */
    public boolean getFrontSide() {
        return this.frontSide;
    }
}
