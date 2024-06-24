package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyThreeCardsInHandException;
import it.polimi.ingsw.exceptions.CardNotFoundException;
import it.polimi.ingsw.exceptions.InvalidCardPositionException;
import it.polimi.ingsw.model.cards.PlayableCard;

import java.io.Serializable;
import java.util.ArrayList;

import static it.polimi.ingsw.model.GameValues.MAX_CARD_IN_HAND;

/**
 * This class represents the hand of the player.
 * It contains the list of cards in the player's hand.
 */
public class PlayerHand implements Serializable {
    private final ArrayList<PlayableCard> cardsInHand;

    /**
     * Constructor
     */
    public PlayerHand() {
        this.cardsInHand = new ArrayList<>();
    }

    /**
     * Add a card to the player's hand
     * @param card is the reference to the resourceCard
     * @throws AlreadyThreeCardsInHandException when the card is already in the playerHand
     */
    public void addCardToPlayerHand(PlayableCard card) throws AlreadyThreeCardsInHandException {
        if(cardsInHand.size() >= MAX_CARD_IN_HAND){
            throw new AlreadyThreeCardsInHandException();
        }
        cardsInHand.add(card);
    }

    /**
     * Get the list of cards in the player's hand
     * @return a copy the list of cards in the playerHand
     */
    public ArrayList<PlayableCard> getCardsInHand() {
        return new ArrayList<>(cardsInHand);
    }

    /**
     * Remove a card from the player's hand
     * @param playableCard is the reference to the card to remove
     * @throws InvalidCardPositionException when the card is not in the playerHand
     */
    public void removeCardFromHand(PlayableCard playableCard) throws InvalidCardPositionException {
        if(!cardsInHand.remove(playableCard))
        {
            throw new InvalidCardPositionException();
        }
    }

    /**
     * Return the card if the card have the right id
     * @param cardId required
     * @return the card required by the id
     * @throws CardNotFoundException if the card is not found
     */
    public PlayableCard getCardById(int cardId) throws CardNotFoundException {
        for(PlayableCard card: cardsInHand)
        {
            if(card.getCardId() == cardId)
                return card;
        }
        throw new CardNotFoundException();
    }
}
