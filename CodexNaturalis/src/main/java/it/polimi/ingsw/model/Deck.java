package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.Random;

/**
 * This class represents a deck of resource cards in the game. It contains a stack of resource cards.
 * Resource cards can be implemented as both resource and gold cards.
 * The deck is responsible for providing the top card and checking if the deck is empty.
 */
public class Deck<T extends Card> implements Serializable{
    /**
     * Constructs a new Deck with the given list of resource cards.
     *
     * @param cards the list of resource cards
     * @throws InvalidConstructorDataException if the data provided to the constructor is invalid
     */
    protected final Stack<T> deck;

    public Deck(ArrayList<T> cards) throws InvalidConstructorDataException {
        deck = new Stack<>();
        deckInitializer(cards);
    }

    /**
     * Initializes the deck with the given list of resource cards.
     *
     * @param cards List of cards
     * @throws InvalidConstructorDataException if the data provided to the constructor is invalid
     */
    private void deckInitializer(ArrayList<T> cards) throws InvalidConstructorDataException {
        for (T card : cards) {
            deck.push(card);
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
        return this.deck.isEmpty();
    }
    public void shuffleDeck() throws UnsupportedOperationException {
        Collections.shuffle(this.deck);
    }

    /**
     * Gets the top card from the deck.
     *
     * @return the top card
     * @throws DeckIsEmptyException if the deck is empty
     */
    public T getTopCard() throws DeckIsEmptyException {
        if(deck.isEmpty())
        {
            throw new DeckIsEmptyException();
        }
        return deck.pop();
    }

    /**
     * Adds a card to the deck.
     *
     * @param card the card to be added
     */
    public void addCard(T card) throws NullPointerException{
        if(card == null) {
            throw new NullPointerException();
        }
        deck.push(card);
    }

    /**
     * Returns the top card from the deck without removing it from the deck.
     *
     * @return the top card
     * @throws DeckIsEmptyException if the deck is empty
     */
    public T seeTopCard() throws DeckIsEmptyException
    {
        if(deck.isEmpty())
        {
            throw new DeckIsEmptyException();
        }
        return deck.peek();
    }

}