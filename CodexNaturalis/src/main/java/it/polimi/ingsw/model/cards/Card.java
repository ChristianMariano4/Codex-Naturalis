package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.UnlinkedCardException;

public abstract class Card {

   private final int cardId;
   private final Side currentSide;
   private Card otherSideCard;

   /**
    *
    * @param cardId id associated to the card
    * @param currentSide Indicates one of the two side of the card (front or back)
    */
   public Card(int cardId, Side currentSide)
   {
      this.cardId = cardId;
      this.currentSide = currentSide;
   }

   /**
    *
    * @return the id of the card
    */
   public int getCardId()
   {
      return cardId;
   }

   /**
    *
    * @return the side (front or back) of the card
    */
   public Side getCurrentSide()
   {
      return currentSide;
   }

   /**
    *
    * @return the card that represents the other side of the current card
    * @throws UnlinkedCardException if the card that represents the other side is not set
    */
   public Card getOtherSideCard() throws UnlinkedCardException {
      if(otherSideCard == null)
         throw new UnlinkedCardException();
      return otherSideCard;
   }

   /**
    *
    * @param otherSideCard the card that represents the other side of the current card
    */
   public void setOtherSideCard(Card otherSideCard)
   {
      this.otherSideCard = otherSideCard;
   }
}
