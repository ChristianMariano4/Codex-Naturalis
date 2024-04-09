package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.CardTypeMismatchException;
import it.polimi.ingsw.exceptions.UnlinkedCardException;
import it.polimi.ingsw.model.CardVisitor;

/**
 * This class represents a generic card, uniquely identified by an id and a side.
 * It contains the reference to the card that represents the other side of the current card.
 */
public abstract class Card {

   private final int cardId;
   private final Side currentSide;
   private Card otherSideCard;

   /**
    * Constructor
    * @param cardId id associated to the card
    * @param currentSide indicates one of the two side of the card (front or back)
    */
   public Card(int cardId, Side currentSide)
   {
      this.cardId = cardId;
      this.currentSide = currentSide;
   }

   public void accept(CardVisitor visitor) throws CardTypeMismatchException {
      throw new CardTypeMismatchException();
   }

   /**
    * Getter
    * @return the id of the card
    */
   public int getCardId()
   {
      return cardId;
   }

   /**
    * Getter
    * @return the side (front or back) of the card
    */
   public Side getCurrentSide()
   {
      return currentSide;
   }

   /**
    * Getter
    * @return the card that represents the other side of the current card
    * @throws UnlinkedCardException if the card that represents the other side is not set
    */
   public Card getOtherSideCard() throws UnlinkedCardException {
      if(otherSideCard == null)
         throw new UnlinkedCardException();
      return otherSideCard;
   }

   /**
    * Setter
    * @param otherSideCard the card that represents the other side of the current card
    */
   public void setOtherSideCard(Card otherSideCard)
   {
      this.otherSideCard = otherSideCard;
   }
}
