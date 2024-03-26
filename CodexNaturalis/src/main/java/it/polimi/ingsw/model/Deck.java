package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.util.List;
import java.util.Stack;

public class Deck {
    private final Stack<ResourceCard> deck;

    public Deck(List<ResourceCard> cards) throws InvalidConstructorDataException {
        try {
            deck = new Stack<ResourceCard>();
        }
        catch(Exception e)
        {
            throw new InvalidConstructorDataException();
        }
        deckInitializer(cards);
    }

    public ResourceCard getTopCard() throws DeckIsEmptyException {
        if(deck.isEmpty())
        {
            throw new DeckIsEmptyException();
        }
        return deck.pop();
    }
    private void deckInitializer(List<ResourceCard> cards) throws InvalidConstructorDataException {
        try {
            for (ResourceCard card : cards) {
                deck.push(card);
            }
        }
        catch(Exception e){
            throw new InvalidConstructorDataException();
        }
    }
    public boolean isEmpty()
    {
        return deck.isEmpty();
    }
}