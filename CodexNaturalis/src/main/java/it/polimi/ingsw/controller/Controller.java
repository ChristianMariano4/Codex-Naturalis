package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.StarterCard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

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
        PlayableCardDeck goldCardDeck = new PlayableCardDeck(cardHandler.filterPlayableCards(cardHandler.importGoldCards()));
        PlayableCardDeck resourceCardDeck = new PlayableCardDeck(cardHandler.filterPlayableCards(cardHandler.importResourceCards()));
        //shuffles deck
        goldCardDeck.shuffleDeck();
        resourceCardDeck.shuffleDeck();
        //new DrawingField
        DrawingField drawingField = new DrawingField(goldCardDeck, resourceCardDeck);
        //import objective cards
        ArrayList<ObjectiveCard> positionalObjectiveCards = cardHandler.filterObjectiveCards(cardHandler.importPositionalObjectiveCards());
        ArrayList<ObjectiveCard> resourceObjectiveCards = cardHandler.filterObjectiveCards(cardHandler.importResourceObjectiveCards());
        ArrayList<ObjectiveCard> tripleObjectiveCard = cardHandler.filterObjectiveCards(cardHandler.importTripleObjectiveCard());
        //join all objective cards lists
        ArrayList<ObjectiveCard> objectiveCards = new ArrayList<>();
        objectiveCards.addAll(positionalObjectiveCards);
        objectiveCards.addAll(resourceObjectiveCards);
        objectiveCards.addAll(tripleObjectiveCard);
        //create objectiveCardDeck
        ObjectiveCardDeck objectiveCardDeck = new ObjectiveCardDeck(objectiveCards);
        //shuffle deck
        objectiveCardDeck.shuffleDeck();
        //get first 2 objective cards
        ArrayList<ObjectiveCard> sharedObjectiveCards = new ArrayList<>();
        sharedObjectiveCards.add(objectiveCardDeck.getTopCard());
        sharedObjectiveCards.add(objectiveCardDeck.getTopCard());
        return new Game(id, drawingField, sharedObjectiveCards, objectiveCardDeck);
    }

    public void addPlayerToGame(Game game, Player player) throws AlreadyExistingPlayerException, AlreadyFourPlayersException {
        game.addPlayer(player);
    }
    public void startGame(Game game) throws CardTypeMismatchException, InvalidConstructorDataException, CardNotImportedException, DeckIsEmptyException, AlreadyExistingPlayerException, AlreadyFourPlayersException, IOException, UnlinkedCardException {
        game.shufflePlayers();
        game.getListOfPlayers().getFirst().setIsFirst(true);

        for(Player player : game.getListOfPlayers())
        {
            List<Marker> availableMarkers= new ArrayList<>();
            Collections.addAll(availableMarkers, Marker.values());

            // send request to the Player to choose his Marker
            while(true){
                try {
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Choose one the follow marker: " + availableMarkers);
                    Marker marker = Marker.valueOf(scanner.nextLine());
                    player.setMarker(marker);
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid marker inserted");
                }
            }

            player.setSecretObjective(game.getObjectiveCardDeck().getRandomCard());
            PlayableCardDeck starterCardDeck = new PlayableCardDeck(cardHandler.filterPlayableCards(cardHandler.importStarterCards()));
            starterCardDeck.shuffleDeck();

            //play the starter card
            //temporary casting
            StarterCard starterCard = (StarterCard) starterCardDeck.getRandomCard();

            while(true){
                try {
                    Scanner scanner = new Scanner(System.in);
                    //TODO: print front and back of the starter card
                    //starteCard.printFront();
                    //starteCard.printBack();
                    System.out.println("Choose the side on which you want to play your starter card: \n0->Front\n1->Back"+ availableMarkers);
                    Side side = Side.valueOf(scanner.nextLine());
                    if(side == Side.FRONT){
                        player.getPlayerField().addCardToCell(starterCard);
                    } else {
                        //temporary casting
                        player.getPlayerField().addCardToCell((StarterCard)starterCard.getOtherSideCard());
                    }
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid value for the side inserted");
                }
            }

        }

    }
}
