package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.PlayableCard;

import java.util.List;
import java.util.Stack;

public class Deck {
    private Stack<PlayableCard> deck;

    public Deck(List<PlayableCard> cards) {
        deck = new Stack<PlayableCard>();
        deckInitializer(cards);
    }

    public PlayableCard getTopCard() {
        return deck.pop();
    }
    private void deckInitializer(List<PlayableCard> cards) {
        for(PlayableCard card: cards) {
            deck.push(card);
        }
    }
}