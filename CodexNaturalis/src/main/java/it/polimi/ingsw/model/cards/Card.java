package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Side;

public abstract class Card {

   private final int cardId;
   private final Side currentSide;
   private Card otherSideCard;

   public Card(int cardId, Side currentSide)
   {
      this.cardId = cardId;
      this.currentSide = currentSide;
   }
   public int getCardId()
   {
      return cardId;
   }
   public Side getCurrentSide()
   {
      return currentSide;
   }
   public Card getOtherSideCard() {
      return otherSideCard;
   }
   public void setOtherSideCard(Card otherSideCard)
   {
      this.otherSideCard = otherSideCard;
   }

}
