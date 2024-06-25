package it.polimi.ingsw.controller.cardFactory;

import it.polimi.ingsw.exceptions.CardNotImportedException;

import java.util.ArrayList;

/**
 * This is an abstract class that represents a generic card factory.
 * It is parameterized with a type T, which represents the type of card that the factory produces.
 * It has an abstract method createCardList that is implemented by subclasses to create a list of cards.
 *
 * @param <T> the type of card that the factory produces
 */
public abstract class CardFactory<T> {

    /**
     * This is an abstract method that creates a list of cards.
     * It is implemented by subclasses to create a list of cards of type T.
     * It throws a CardNotImportedException if there is an error creating the cards.
     *
     * @return a list of cards of type T
     * @throws CardNotImportedException if there is an error creating the cards
     */
    public abstract ArrayList<T> createCardList() throws CardNotImportedException;
}