package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.CardNotImportedException;
import it.polimi.ingsw.exceptions.CardTypeMismatchException;
import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.DrawingField;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;

import java.util.ArrayList;

/**
 * Main controller class
 */
public class Controller {

    private int numberOfGames = 0;
    CardHandler cardHandler;
    public Controller()
    {
        cardHandler = new CardHandler();
    }
    public Game createGame() throws InvalidConstructorDataException, CardNotImportedException, CardTypeMismatchException, DeckIsEmptyException {
        //starts new thread in server and then returns new game
        //TODO: start thread
        //TODO: fix deck inheritance
        //sequential game id starting from 0
        int id = this.numberOfGames;
        this.numberOfGames += 1;
        //new Decks
        Deck goldCardDeck = new Deck(cardHandler.importGoldCards());
        Deck resourceCardDeck = new Deck(cardHandler.importResourceCards());
        //shuffles deck
        goldCardDeck.shuffleDeck();
        resourceCardDeck.shuffleDeck();
        //new DrawingField
        DrawingField drawingField = new DrawingField(goldCardDeck, resourceCardDeck);
        //import objective cards
        ArrayList<ObjectiveCard> positionalObjectiveCards = cardHandler.importPositionalObjectiveCards();
        ArrayList<ObjectiveCard> resourceObjectiveCards = cardHandler.importResourceObjectiveCards();
        ObjectiveCard tripleObjectiveCard = cardHandler.importTripleObjectiveCard();
        //join all objective cards lists
        ArrayList<Card> objectiveCards = new ArrayList<>();
        objectiveCards.addAll(positionalObjectiveCards);
        objectiveCards.addAll(resourceObjectiveCards);
        objectiveCards.add(tripleObjectiveCard);
        //create objectiveCardDeck
        Deck objectiveCardDeck = new Deck(objectiveCards);
        //shuffle deck
        objectiveCardDeck.shuffleDeck();
        //get first 2 objective cards
        ArrayList<Card> sharedObjectiveCards = new ArrayList<>();
        sharedObjectiveCards.add(objectiveCardDeck.getTopCard());
        sharedObjectiveCards.add(objectiveCardDeck.getTopCard());
        return new Game(id, drawingField, sharedObjectiveCards);
    }
}
