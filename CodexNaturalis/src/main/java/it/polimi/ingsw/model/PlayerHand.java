package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.Draw;
import it.polimi.ingsw.exceptions.AlreadyThreeCardsInHand;
import it.polimi.ingsw.exceptions.DeckIsEmpty;
import it.polimi.ingsw.exceptions.NoCardAdded;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayableCard;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static it.polimi.ingsw.model.GameValues.MAX_CARD_IN_HAND;

public class PlayerHand {
    private final List<PlayableCard> cardsInHand;
    public PlayerHand() {
        this.cardsInHand = new ArrayList<PlayableCard>();
    }
    public void addCardToPlayerHand(PlayableCard card) throws AlreadyThreeCardsInHand {
        if(cardsInHand.size() == MAX_CARD_IN_HAND){
            throw new AlreadyThreeCardsInHand();
        }
        cardsInHand.add(card);
    }
    public List<PlayableCard> getCardsInHand() {
        return new ArrayList<PlayableCard>(cardsInHand);
    }
    public void removeCardFromHand(PlayableCard playableCard)
    {
        //TODO: implement method
    }
}
