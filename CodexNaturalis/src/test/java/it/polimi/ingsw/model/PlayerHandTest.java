package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.exceptions.AlreadyThreeCardsInHandException;
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

   private void addCardsToPlayerHand(int num) throws AlreadyThreeCardsInHandException {
      for(int i = 0; i < num+1; i++) {
         //Card Controller method
         p.addCardToPlayerHand(cards.get(i));
      }
   }
   @BeforeEach
   void playerHandTestInit() {
       //PlayerHand Controller method
      for(int i = 0; i < MAX_CARD_IN_HAND; i++) {
         //Add cards to the list
      }
   }
   @Test
   void shouldAddCardToPlayerHand() throws AlreadyThreeCardsInHandException {
      for(int i = 0; i < 3; i++) {
         p.addCardToPlayerHand(cards.get(i));
      }
   }
   @Test
   void shouldNotAddCardToPlayerHand() throws AlreadyThreeCardsInHandException {
      addCardsToPlayerHand(MAX_CARD_IN_HAND);
      PlayableCard card; //Card constructor
      Assertions.assertThrows(NoCardAddedException.class, () -> {p.addCardToPlayerHand(card);});
   }
   @Test
   void shouldReturnCardsInHand() throws AlreadyThreeCardsInHandException {
      addCardsToPlayerHand(MAX_CARD_IN_HAND);
      Assertions.assertTrue(cards.containsAll(p.getCardsInHand()) && p.getCardsInHand().containsAll(cards));
   }

   @Test
   void shouldRemoveCardFromHand() throws InvalidCardPositionException, AlreadyThreeCardsInHandException {
      addCardsToPlayerHand(MAX_CARD_IN_HAND);
      ResourceCard cardToRemove = (ResourceCard) cards.get(MAX_CARD_IN_HAND-1); //TODO: Remove Cast
      p.removeCardFromHand(cardToRemove);
      Assertions.assertFalse(p.getCardsInHand().contains(cardToRemove));
   }
   @Test
   void shouldNotRemoveCardFromHandEmptyHand() throws InvalidConstructorDataException {
      ResourceCard cardToRemove = new ResourceCard(); //Controller method
      Assertions.assertThrows(InvalidCardPositionException.class, () -> {p.removeCardFromHand(cardToRemove);});
   }
   @Test
   void shouldNotRemoveCardFromHandCardNotPresent() throws AlreadyThreeCardsInHandException, InvalidConstructorDataException {
      addCardsToPlayerHand(MAX_CARD_IN_HAND);
      ResourceCard cardToRemove = new ResourceCard();
      Assertions.assertThrows(InvalidCardPositionException.class, () -> {p.removeCardFromHand(cardToRemove);});
   }
}