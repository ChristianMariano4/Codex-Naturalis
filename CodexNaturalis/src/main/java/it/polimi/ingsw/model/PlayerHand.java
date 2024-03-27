package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyThreeCardsInHandException;
import it.polimi.ingsw.exceptions.InvalidCardPositionException;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.GameValues.MAX_CARD_IN_HAND;

public class PlayerHand {
    private final List<ResourceCard> cardsInHand;

    /**
     * Constructor
     */
    public PlayerHand() {
        this.cardsInHand = new ArrayList<ResourceCard>();
    }

    /**
     *
     * @param card is the reference to the resourceCard
     * @throws AlreadyThreeCardsInHandException when the card is already in the playerHand
     */
    public void addCardToPlayerHand(ResourceCard card) throws AlreadyThreeCardsInHandException {
        if(cardsInHand.size() >= MAX_CARD_IN_HAND){
            throw new AlreadyThreeCardsInHandException();
        }
        cardsInHand.add(card);
    }

    /**
     *
     * @return the list of cards in the playerHand
     */
    public List<ResourceCard> getCardsInHand() {
        return new ArrayList<ResourceCard>(cardsInHand);
    }

    /**
     *
     * @param resourceCard is the reference to the card to remove
     * @throws InvalidCardPositionException when the card is not in the playerHand
     */
    public void removeCardFromHand(ResourceCard resourceCard) throws InvalidCardPositionException {
        if(!cardsInHand.remove(resourceCard))
        {
            throw new InvalidCardPositionException();
        }
    }
}
