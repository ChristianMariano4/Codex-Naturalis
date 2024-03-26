package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.DeckIsEmpty;
import it.polimi.ingsw.exceptions.InvalidConstructorData;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.util.List;
import java.util.Stack;

public class Deck {
    private final Stack<ResourceCard> deck;

    public Deck(List<ResourceCard> cards) throws InvalidConstructorData{
        try {
            deck = new Stack<ResourceCard>();
        }
        catch(Exception e)
        {
            throw new InvalidConstructorData();
        }
        deckInitializer(cards);
    }

    public ResourceCard getTopCard() throws DeckIsEmpty {
        if(deck.isEmpty())
        {
            throw new DeckIsEmpty();
        }
        return deck.pop();
    }
    private void deckInitializer(List<ResourceCard> cards) throws InvalidConstructorData {
        try {
            for (ResourceCard card : cards) {
                deck.push(card);
            }
        }
        catch(Exception e){
            throw new InvalidConstructorData();
        }
    }
    public boolean isEmpty()
    {
        return deck.isEmpty();
    }
}