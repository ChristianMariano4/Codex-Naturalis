package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayableCard;

import java.util.List;
import java.util.Stack;

public class Deck {
    private Stack<PlayableCard> deck;

    public Deck(List<PlayableCard> cards) {
        deck = new Stack<PlayableCard>();
        deckInitializer(cards);
    }
    public Card getTopCardFromDeck() {
        return deck.pop();
    }
    private void deckInitializer(List<PlayableCard> cards) {
        for(PlayableCard c: cards) {
            deck.push(c);
        }
    }
}
