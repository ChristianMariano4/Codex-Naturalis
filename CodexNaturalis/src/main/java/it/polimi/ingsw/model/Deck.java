package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.ResourceCard;

import java.util.List;
import java.util.Stack;

public class Deck {
    private Stack<ResourceCard> deck;

    public Deck(List<ResourceCard> cards) {
        deck = new Stack<ResourceCard>();
        deckInitializer(cards);
    }

    public ResourceCard getTopCard() {
        return deck.pop();
    }
    private void deckInitializer(List<ResourceCard> cards) {
        for(ResourceCard card: cards) {
            deck.push(card);
        }
    }
    public boolean isEmpty()
    {
        return deck.isEmpty();
    }
}