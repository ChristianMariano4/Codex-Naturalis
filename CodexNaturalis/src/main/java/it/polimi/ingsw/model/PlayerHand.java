package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyThreeCardsInHandException;
import it.polimi.ingsw.exceptions.CardNotFoundException;
import it.polimi.ingsw.exceptions.InvalidCardPositionException;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
        this.cardsInHand = new ArrayList<PlayableCard>();
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
     *  Getter
     * @return the list of cards in the playerHand
     */
    public ArrayList<PlayableCard> getCardsInHand() {
        return new ArrayList<PlayableCard>(cardsInHand);
    }

    /**
     * Remove a card from the player's hand
     * @param resourceCard is the reference to the card to remove
     * @throws InvalidCardPositionException when the card is not in the playerHand
     */
    public void removeCardFromHand(ResourceCard resourceCard) throws InvalidCardPositionException {
        if(!cardsInHand.remove(resourceCard))
        {
            throw new InvalidCardPositionException();
        }
    }

    public PlayableCard getCardById(int cardId) throws CardNotFoundException {
        for(PlayableCard card: cardsInHand)
        {
            if(card.getCardId() == cardId)
                return card;
        }
        throw new CardNotFoundException();
    }
}
