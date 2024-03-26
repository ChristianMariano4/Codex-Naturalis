package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyThreeCardsInHandException;
import it.polimi.ingsw.exceptions.InvalidCardPositionException;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.util.ArrayList;
import java.util.List;

import static it.polimi.ingsw.model.GameValues.MAX_CARD_IN_HAND;

public class PlayerHand {
    private final List<ResourceCard> cardsInHand;
    public PlayerHand() {
        this.cardsInHand = new ArrayList<ResourceCard>();
    }
    public void addCardToPlayerHand(ResourceCard card) throws AlreadyThreeCardsInHandException {
        if(cardsInHand.size() >= MAX_CARD_IN_HAND){
            throw new AlreadyThreeCardsInHandException();
        }
        cardsInHand.add(card);
    }
    public List<ResourceCard> getCardsInHand() {
        return new ArrayList<ResourceCard>(cardsInHand);
    }
    public void removeCardFromHand(ResourceCard resourceCard) throws InvalidCardPositionException {
        if(!cardsInHand.remove(resourceCard))
        {
            throw new InvalidCardPositionException();
        }
    }
}
