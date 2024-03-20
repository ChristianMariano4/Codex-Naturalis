package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.Draw;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayableCard;

import java.util.LinkedList;
import java.util.List;

public class PlayerHand {
    private List<PlayableCard> cardsInHand;
    private DrawingField drawingField;
    private PlayerField playerField;

    public PlayerHand(List<PlayableCard> cardsInHand) {
        this.cardsInHand = new LinkedList<PlayableCard>();
        try {
            this.cardsInHand.addAll(cardsInHand);
        } catch (NullPointerException e) { }
    }

    public void chooseCardToDraw(Draw draw, Deck deck) {

    }
    public void addCardToPlayerHand(Card card) {

    }

}
