package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.exceptions.InvalidCardPositionException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.exceptions.NoCardAddedException;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.ResourceCard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static it.polimi.ingsw.model.GameValues.MAX_CARD_IN_HAND;
import static org.junit.jupiter.api.Assertions.*;

class PlayerHandTest {

   PlayerHand p;
   List<PlayableCard> cards;

   private void addCardsToPlayerHand(int num) {
      for(int i = 0; i < num+1; i++) {
         //Card constructor
         p.addCardToPlayerHand(cards);
      }
   }
   @BeforeEach
   public void playerHandTestInit() {
       //PlayerHand constructor
      for(int i = 0; i < MAX_CARD_IN_HAND; i++) {
         //Add cards to the list
      }
   }
   @Test
   void shouldAddCardToPlayerHand() {
      for(int i = 0; i < 3; i++) {
         PlayableCard c; //Card constructor
         p.addCardToPlayerHand(c);
      }
   }
   @Test
   void shouldNotAddCardToPlayerHand() {
      addCardsToPlayerHand(MAX_CARD_IN_HAND);
      PlayableCard card; //Card constructor
      Assertions.assertThrows(NoCardAddedException.class, () -> {p.addCardToPlayerHand(card);});
   }
   @Test
   void shouldReturnCardsInHand() {
      addCardsToPlayerHand(MAX_CARD_IN_HAND);
      Assertions.assertTrue(cards.containsAll(p.getCardsInHand()) && p.getCardsInHand().containsAll(cards));
   }

   @Test
   void shouldRemoveCardFromHand() throws InvalidCardPositionException {
      addCardsToPlayerHand(MAX_CARD_IN_HAND);
      ResourceCard cardToRemove = (ResourceCard) cards.get(MAX_CARD_IN_HAND-1); //TODO: Remove Cast
      p.removeCardFromHand(cardToRemove);
      Assertions.assertFalse(p.getCardsInHand().contains(cardToRemove));
   }
   @Test
   void shouldNotRemoveCardFromHandEmptyHand() {
      ResourceCard cardToRemove = new ResourceCard(); //Constructor
      Assertions.assertThrows(InvalidCardPositionException.class, () -> {p.removeCardFromHand(cardToRemove);});
   }
   @Test
   void shouldNotRemoveCardFromHandCardNotPresent() {
      addCardsToPlayerHand(MAX_CARD_IN_HAND);
      ResourceCard cardToRemove = new ResourceCard();
      Assertions.assertThrows(InvalidCardPositionException.class, () -> {p.removeCardFromHand(cardToRemove);});
   }
}