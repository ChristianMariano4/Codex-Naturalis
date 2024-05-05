package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.CardType;
import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.EventManager;
import it.polimi.ingsw.network.messages.GameEvent;
import it.polimi.ingsw.network.messages.userMessages.UserMessageWrapper;
import it.polimi.ingsw.network.rmi.RMIServer;

import java.io.IOException;
import java.util.*;

/**
 * MainServer controller class
 */
public class Controller {
    //TODO: metodi per giocare le carte e per pescare le carte
    public String testUsername;
    private CardHandler cardHandler;
    private RMIServer server;
    private Game game;
    private final EventManager eventManager;

    public Controller(EventManager eventManager){
        this.cardHandler = new CardHandler();
        this.eventManager = eventManager;
    }
  /* public Controller(Server server, EventManager eventManager)
    {
        this.cardHandler = new CardHandler();
        this.server = server;
        this.eventManager = eventManager;
    }

    public Controller(Server server, ServerEventManager serverEventManager)
    {
        cardHandler = new CardHandler();
        this.server = server;
        this.serverEventManager = serverEventManager;
    }
*/
    /**
     *
     * @return the Game the method create
     * @throws InvalidConstructorDataException if the ConstructorData are invalid
     * @throws CardNotImportedException if a card is not imported correctly
     * @throws CardTypeMismatchException if the cardType doesn't match
     * @throws DeckIsEmptyException if a deck is empty
     */
    public synchronized Game createGame(int gameId) throws InvalidConstructorDataException, CardNotImportedException, CardTypeMismatchException, DeckIsEmptyException {
        //starts new thread in server and then returns new game
        //create new Decks
        Deck<GoldCard> goldCardDeck = new Deck<GoldCard>(cardHandler.filterGoldCards(cardHandler.importGoldCards()));
        Deck<ResourceCard> resourceCardDeck = new Deck<ResourceCard>(cardHandler.filterResourceCards(cardHandler.importResourceCards()));
        //shuffle decks
        goldCardDeck.shuffleDeck();
        resourceCardDeck.shuffleDeck();
        //new DrawingField
        DrawingField drawingField = new DrawingField(goldCardDeck, resourceCardDeck);
        //import objective cards and filter them by front side
        ArrayList<ObjectiveCard> positionalObjectiveCards = cardHandler.filterObjectiveCards(cardHandler.importPositionalObjectiveCards());
        ArrayList<ObjectiveCard> resourceObjectiveCards = cardHandler.filterObjectiveCards(cardHandler.importResourceObjectiveCards());
        ArrayList<ObjectiveCard> tripleObjectiveCard = cardHandler.filterObjectiveCards(cardHandler.importTripleObjectiveCard());
        //join all objective cards lists
        ArrayList<ObjectiveCard> objectiveCards = new ArrayList<>();
        objectiveCards.addAll(positionalObjectiveCards);
        objectiveCards.addAll(resourceObjectiveCards);
        objectiveCards.addAll(tripleObjectiveCard);
        //create objectiveCardDeck from the list of objective cards
        Deck<ObjectiveCard> objectiveCardDeck = new Deck<ObjectiveCard>(objectiveCards);
        //shuffle deck
        objectiveCardDeck.shuffleDeck();
        //add first two front side objective cards to the shared objective cards
        ArrayList<ObjectiveCard> sharedObjectiveCards = new ArrayList<>();
        sharedObjectiveCards.add(cardHandler.getOtherSideCard(objectiveCardDeck.getTopCard()));
        sharedObjectiveCards.add(cardHandler.getOtherSideCard(objectiveCardDeck.getTopCard()));
        Deck<StarterCard> starterCardDeck = new Deck<StarterCard>(cardHandler.filterStarterCards(cardHandler.importStarterCards()));
        starterCardDeck.shuffleDeck();
        this.game = new Game(gameId, drawingField, sharedObjectiveCards, objectiveCardDeck, starterCardDeck);

        return this.game;
    }

    /**
     * Getter
     * @return the game
     */
    public synchronized Game getGame() {
        return game;
    }

    /**
     * Adds a player to the game specified by gameId
     * @param gameId id of the game to which the player will be added
     * @param username username of the player to be added
     * @throws AlreadyExistingPlayerException when the player we want to add already exists in the game
     * @throws AlreadyFourPlayersException when the game already contains the maximum amount of players
     */
    public synchronized Game addPlayerToGame(int gameId, String username) throws AlreadyExistingPlayerException, AlreadyFourPlayersException {
        try {
            this.game.addPlayer(new Player(username));
        } catch (InvalidConstructorDataException e) {
            throw new RuntimeException(e);
        }

        return this.game;
    }

    /**
     * Starts the game when all players are ready
     * @param game game to start
     * @throws CardTypeMismatchException
     * @throws InvalidConstructorDataException
     * @throws CardNotImportedException
     * @throws DeckIsEmptyException
     * @throws AlreadyExistingPlayerException
     * @throws AlreadyFourPlayersException
     * @throws IOException
     * @throws UnlinkedCardException
     */
    public synchronized void inzializeGame(Game game) throws CardTypeMismatchException, InvalidConstructorDataException, CardNotImportedException, DeckIsEmptyException, AlreadyExistingPlayerException, AlreadyFourPlayersException, IOException, UnlinkedCardException, AlreadyThreeCardsInHandException {
        //shuffle the list of player to determine the order of play
        game.shufflePlayers();
        //set the first player
        game.getListOfPlayers().getFirst().setIsFirst(true);
        //setting discovered cards
        this.game.getTableTop().getDrawingField().setDiscoveredCards();
    }

    /**
     * Assign the chosen marker to the player and remove it from the available ones
     * @param player
     * @param marker
     */
//for each player in the game, initialize the marker, the cards, the matrix and the hand
    public synchronized void inizializeMarker(Player player, Marker marker) {
        player.setMarker(marker);
        this.game.removeMarker(marker);
    }

    /**
     * Give to the player the initial 3 cards in his hand
     * @param player
     * @throws DeckIsEmptyException if the deck is empty
     */
    public synchronized void inizializePlayerCards(Player player) throws DeckIsEmptyException {
        player.setSecretObjective(this.game.getObjectiveCardDeck().getTopCard());
        player.setStarterCard(this.game.getAvailableStarterCards().getTopCard());
    }

    /**
     * Add to the playerField the starterCard of the player
     * @param player the selected player
     * @param side the side of the starterCard chosen by the player
     */
    public synchronized void inizializePlayerMatrix(Player player, Side side) {
        if(side == Side.FRONT){
            player.getPlayerField().addCardToCell(player.getStarterCard());
        } else {
            player.getPlayerField().addCardToCell(cardHandler.getOtherSideCard(player.getStarterCard()));
        }
    }

    /**
     * Inizialize the playerHand of the player
     * @param player
     * @throws DeckIsEmptyException if the deck is empty
     * @throws AlreadyThreeCardsInHandException if there are already three cards in the player hand
     */
    public synchronized void inizializePlayerHand(Player player) throws DeckIsEmptyException, AlreadyThreeCardsInHandException {
        player.getPlayerHand().addCardToPlayerHand(game.getTableTop().getDrawingField().drawCardFromGoldCardDeck(DrawPosition.FROMDECK));
        player.getPlayerHand().addCardToPlayerHand(game.getTableTop().getDrawingField().drawCardFromResourceCardDeck(DrawPosition.FROMDECK));
        player.getPlayerHand().addCardToPlayerHand(game.getTableTop().getDrawingField().drawCardFromResourceCardDeck(DrawPosition.FROMDECK));
    }

//    public void add

    public synchronized void update(UserMessageWrapper message) {
        switch(message.getType()) {
            case USERNAME_INSERTED -> {
                testUsername = message.getMessage().getUsername();
            }
        }
    }
    public synchronized void calculateAndUpdateFinalPoints(Game game) throws CardTypeMismatchException {
        ArrayList<ObjectiveCard> objectiveCards = game.getTableTop().getSharedObjectiveCards();

        for(Player player : game.getListOfPlayers())
        {
            objectiveCards.add(player.getSecretObjective());
            for(ObjectiveCard objectiveCard : objectiveCards) {
                CardInfo objectiveCardInfo = cardHandler.getCardInfo(objectiveCard);
                switch(objectiveCardInfo.getCardType())
                {
                    case CardType.TRIPLEOBJECTIVE:
                        player.addPoints(PointCalculator.calculateTripleObjective(player.getPlayerField(), objectiveCard));
                        break;
                    case CardType.RESOURCEOBJECTIVE:
                        player.addPoints(PointCalculator.calculateResourceObjective(objectiveCardInfo, player.getPlayerField(),objectiveCard));
                        break;
                    case CardType.POSITIONALOBJECTIVE:
                        player.addPoints(PointCalculator.calculatePositionalObjective(objectiveCardInfo, player.getPlayerField(),objectiveCard));
                        break;
                    default:
                        throw new CardTypeMismatchException();
                }
            }

            objectiveCards.remove(2); // removes secret player objective
        }
    }

}
