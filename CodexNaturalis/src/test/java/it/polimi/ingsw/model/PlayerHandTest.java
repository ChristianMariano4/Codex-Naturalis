package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyThreeCardsInHandException;
import it.polimi.ingsw.exceptions.CardNotFoundException;
import it.polimi.ingsw.exceptions.InvalidCardPositionException;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.ResourceCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

   class PlayerHandTest {

      private PlayerHand playerHand;
      private PlayableCard playableCard;
      private ResourceCard resourceCard;

      @BeforeEach
      void setUp() {
         playerHand = new PlayerHand();
         playableCard = mock(PlayableCard.class);
         resourceCard = mock(ResourceCard.class);
      }

      @Test
      void shouldAddCardToPlayerHand() throws AlreadyThreeCardsInHandException {
         playerHand.addCardToPlayerHand(playableCard);
         assertTrue(playerHand.getCardsInHand().contains(playableCard));
      }

      @Test
      void shouldThrowAlreadyThreeCardsInHandException() {
         assertThrows(AlreadyThreeCardsInHandException.class, () -> {
            for (int i = 0; i < 4; i++) {
               playerHand.addCardToPlayerHand(playableCard);
            }
         });
      }

      @Test
      void shouldReturnCardsInHand() throws AlreadyThreeCardsInHandException {
         ArrayList<PlayableCard> cards = new ArrayList<>();
         for (int i = 0; i < 3; i++) {
            playerHand.addCardToPlayerHand(playableCard);
            cards.add(playableCard);
         }
         assertTrue(cards.containsAll(playerHand.getCardsInHand()) && playerHand.getCardsInHand().containsAll(cards));
      }

      @Test
      void shouldRemoveCardFromHand() throws InvalidCardPositionException, AlreadyThreeCardsInHandException {
         playerHand.addCardToPlayerHand(resourceCard);
         playerHand.removeCardFromHand(resourceCard);
         assertFalse(playerHand.getCardsInHand().contains(resourceCard));
      }

      @Test
      void shouldThrowInvalidCardPositionException() {
         assertThrows(InvalidCardPositionException.class, () -> playerHand.removeCardFromHand(resourceCard));
      }

      @Test
        void shouldGetCardById() throws AlreadyThreeCardsInHandException, CardNotFoundException {
             playerHand.addCardToPlayerHand(resourceCard);
             when(resourceCard.getCardId()).thenReturn(0);
             assertEquals(resourceCard, playerHand.getCardById(0));

             assertThrows(CardNotFoundException.class, () -> playerHand.getCardById(1));
        }
   }