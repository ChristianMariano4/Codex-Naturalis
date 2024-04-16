package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;

import java.util.ArrayList;
import java.util.Stack;

public class ObjectiveCardDeck extends Deck<ObjectiveCard>{

    /**
     * Constructs a new Deck with the given list of resource cards.
     *
     * @param cards the list of resource cards
     * @throws InvalidConstructorDataException if the data provided to the constructor is invalid
     */
    public ObjectiveCardDeck(ArrayList<ObjectiveCard> cards) throws InvalidConstructorDataException {
        super(cards);

    }


}
