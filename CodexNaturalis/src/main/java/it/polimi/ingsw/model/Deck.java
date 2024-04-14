package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.util.List;
import java.util.Stack;

/**
 * This class represents a deck of resource cards in the game. It contains a stack of resource cards.
 * Resource cards can be implemented as both resource and gold cards.
 * The deck is responsible for providing the top card and checking if the deck is empty.
 */
public class Deck {
    private final Stack<PlayableCard> deck;
    /**
     * Constructs a new Deck with the given list of resource cards.
     *
     * @param cards the list of resource cards
     * @throws InvalidConstructorDataException if the data provided to the constructor is invalid
     */
    public Deck(List<PlayableCard> cards) throws InvalidConstructorDataException {
        try {
            deck = new Stack<PlayableCard>();
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
    public PlayableCard getTopCard() throws DeckIsEmptyException {
        if(deck.isEmpty())
        {
            throw new DeckIsEmptyException();
        }
        return deck.pop();
    }

    /**
     * Initializes the deck with the given list of resource cards.
     *
     * @param cards List of cards
     * @throws InvalidConstructorDataException if the data provided to the constructor is invalid
     */
    private void deckInitializer(List<PlayableCard> cards) throws InvalidConstructorDataException {
        try {
            for (PlayableCard card : cards) {
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
    //TODO: check if isEmpty value is updated
    public boolean isEmpty()
    {
        return deck.isEmpty();
    }
}