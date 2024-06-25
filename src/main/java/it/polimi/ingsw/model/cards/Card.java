package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.model.CardVisitor;

import java.io.Serializable;

/**
 * This class represents a generic card, uniquely identified by an id and a side.
 * It contains the reference to the card that represents the other side of the current card.
 */
public abstract class Card implements Serializable {
   private final int cardId;
   private final Side currentSide;

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

    /**
     * This method is used to accept a visitor
     * @param visitor the visitor
     * @return the card info
     */
   public abstract CardInfo accept(CardVisitor visitor);

   /**
    * Get the id of the card
    * @return the id of the card
    */
   public int getCardId()
   {
      return cardId;
   }

   /**
    * Get the side of the card (front or back)
    * @return the side (front or back) of the card
    */
   public Side getCurrentSide()
   {
      return currentSide;
   }

   /**
    * Equals method
    * @param obj the object
    * @return true if the object passed is a card and the object cardId match the cardId of this.card
    */
   @Override
   public boolean equals(Object obj) {
      if (obj instanceof Card) {
         if (this.cardId == ((Card) obj).getCardId())
            return true;
      }
      return super.equals(obj);
   }
}
