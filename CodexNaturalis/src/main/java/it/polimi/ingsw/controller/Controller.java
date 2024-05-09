package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.cardFactory.ProductionFilePathProvider;
import it.polimi.ingsw.enumerations.*;
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
        this.cardHandler = new CardHandler(new ProductionFilePathProvider());
        this.eventManager = eventManager;
    }

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
     * @param username username of the player to be added
     * @throws AlreadyExistingPlayerException when the player we want to add already exists in the game
     * @throws AlreadyFourPlayersException when the game already contains the maximum amount of players
     */
    public synchronized Game addPlayerToGame(String username) throws AlreadyExistingPlayerException, AlreadyFourPlayersException {
        try {
            this.game.addPlayer(new Player(username));
        } catch (InvalidConstructorDataException e) {
            throw new RuntimeException(e);
        }

        return this.game;
    }

    /**
     * Starts the game when all players are ready
     * @throws CardTypeMismatchException
     * @throws InvalidConstructorDataException
     * @throws CardNotImportedException
     * @throws DeckIsEmptyException
     * @throws AlreadyExistingPlayerException
     * @throws AlreadyFourPlayersException
     * @throws IOException
     * @throws UnlinkedCardException
     */
    public synchronized void initializeGame() throws CardTypeMismatchException, InvalidConstructorDataException, CardNotImportedException, DeckIsEmptyException, AlreadyExistingPlayerException, AlreadyFourPlayersException, IOException, UnlinkedCardException, AlreadyThreeCardsInHandException {
        //shuffle the list of player to determine the order of play
        game.shufflePlayers();
        //set the first player
        game.getListOfPlayers().getFirst().setIsFirst(true);
        //setting discovered cards
        game.getTableTop().getDrawingField().setDiscoveredCards();
    }

    /**
     * Set the chosen marker to the player
     * @param player the player that has chosen the marker
     * @param marker the marker chosen by the player
     */
    public synchronized void setMarker(Player player, Marker marker) {
        player.setMarker(marker);
        game.getAvailableMarkers().remove(marker);
    }

    public StarterCard giveStarterCard(Player player) throws DeckIsEmptyException {
        StarterCard starterCard = game.getAvailableStarterCards().getTopCard();
        player.setStarterCard(starterCard);
        return starterCard;
    }


    /**
     * Give to the player the initial 3 cards in his hand
     * @param player the player that has to receive the cards
     * @throws DeckIsEmptyException if the deck is empty
     */
    public synchronized void initializeStarterCard(Player player, StarterCard starterCard, Side side) {
        if(side == Side.FRONT){
            player.getPlayerField().addCardToCell(starterCard);
        } else {
            player.getPlayerField().addCardToCell(cardHandler.getOtherSideCard(starterCard));
        }
    }

    /**
     * Give to the player 2 secret objective cards to choose from
     * @param player the player that has to choose the secret objective card
     * @return a list composed of the two secret objective cards
     * @throws DeckIsEmptyException if the deck is empty
     */
    public synchronized List<ObjectiveCard> takeTwoObjectiveCards(Player player) throws DeckIsEmptyException {
        List<ObjectiveCard> secretObjectiveCards = new ArrayList<>();
        secretObjectiveCards.add(this.game.getObjectiveCardDeck().getTopCard());
        secretObjectiveCards.add(this.game.getObjectiveCardDeck().getTopCard());
        return secretObjectiveCards;
    }

    /**
     * Add a secret objective card to the deck
     * @param objectiveCard
     */
    public synchronized void addObjectiveCardToDeck(ObjectiveCard objectiveCard) throws NullPointerException {
        this.game.getObjectiveCardDeck().addCard(objectiveCard);
    }
    /**
     * Add to the playerField the starterCard of the player
     * @param player the selected player
     * @param side the side of the starterCard chosen by the player
     */
    public synchronized void initializePlayerMatrix(Player player, Side side) {
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
    public synchronized void initializePlayerHand(Player player) throws DeckIsEmptyException, AlreadyThreeCardsInHandException {
        player.getPlayerHand().addCardToPlayerHand(game.getTableTop().getDrawingField().drawCardFromGoldCardDeck(DrawPosition.FROMDECK));
        player.getPlayerHand().addCardToPlayerHand(game.getTableTop().getDrawingField().drawCardFromResourceCardDeck(DrawPosition.FROMDECK));
        player.getPlayerHand().addCardToPlayerHand(game.getTableTop().getDrawingField().drawCardFromResourceCardDeck(DrawPosition.FROMDECK));
    }

    public synchronized void update(UserMessageWrapper message) {
        switch(message.getType()) {
            case USERNAME_INSERTED -> {
                testUsername = message.getMessage().getUsername();
            }
        }
    }

    public synchronized void playCard(Player player, PlayableCard card, PlayableCard otherCard, AngleOrientation orientation) throws InvalidCardPositionException {
        player.getPlayerField().addCardToCell(card,orientation,otherCard);
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

    public synchronized void drawCard(Player player, CardType cardType, DrawPosition drawPosition) throws DeckIsEmptyException, AlreadyThreeCardsInHandException {
        if(cardType == CardType.RESOURCE) {
            ResourceCard card = game.getTableTop().getDrawingField().drawCardFromResourceCardDeck(drawPosition);
            player.getPlayerHand().addCardToPlayerHand(card);
        } else {
            GoldCard card = game.getTableTop().getDrawingField().drawCardFromGoldCardDeck(drawPosition);
            player.getPlayerHand().addCardToPlayerHand(card);
        }
    }
    public CardHandler getCardHandler() {
        return cardHandler;
    }

}
