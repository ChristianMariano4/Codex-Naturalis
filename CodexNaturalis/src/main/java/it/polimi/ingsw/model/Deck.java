package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.util.List;
import java.util.Stack;

/**
 * This class represents a deck of resource cards in the game. It contains a stack of resource cards.
 * Resource cards can be implemented as both resource and gold cards.
 * The deck is responsible for providing the top card and checking if the deck is empty.
 */
public class Deck {
    private final Stack<ResourceCard> deck;
    /**
     * Constructs a new Deck with the given list of resource cards.
     *
     * @param cards the list of resource cards
     * @throws InvalidConstructorDataException if the data provided to the constructor is invalid
     */
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
    /**
     * Gets the top card from the deck.
     *
     * @return the top card
     * @throws DeckIsEmptyException if the deck is empty
     */
    public ResourceCard getTopCard() throws DeckIsEmptyException {
        if(deck.isEmpty())
        {
            throw new DeckIsEmptyException();
        }
        return deck.pop();
    }

    /**
     *
     * @param cards List of cards
     * @throws InvalidConstructorDataException if the data provided to the constructor is invalid
     */
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
    /**
     * Checks if the deck is empty.
     *
     * @return true if the deck is empty, false otherwise
     */
    public boolean isEmpty()
    {
        return deck.isEmpty();
    }
}