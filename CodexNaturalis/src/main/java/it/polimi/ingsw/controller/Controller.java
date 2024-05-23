package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.observer.EventManager;
import it.polimi.ingsw.network.messages.clientMessages.UserMessageWrapper;
import it.polimi.ingsw.network.server.GameHandler;

import java.io.IOException;
import java.util.*;

/**
 * MainServer controller class
 */
public class Controller {
    //TODO: metodi per giocare le carte e per pescare le carte
    public String testUsername;
    private final CardHandler cardHandler;
    private final GameHandler gameHandler;

    public Controller(EventManager eventManager, GameHandler gameHandler){
        this.cardHandler = new CardHandler();
        this.gameHandler = gameHandler;
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
        sharedObjectiveCards.add(objectiveCardDeck.getTopCard());
        sharedObjectiveCards.add(objectiveCardDeck.getTopCard());
        Deck<StarterCard> starterCardDeck = new Deck<StarterCard>(cardHandler.filterStarterCards(cardHandler.importStarterCards()));
        starterCardDeck.shuffleDeck();
        return new Game(gameId, drawingField, sharedObjectiveCards, objectiveCardDeck, starterCardDeck);
    }

    /**
     * Getter
     * @return the game
     */

    /**
     * Adds a player to the game specified by gameId
     * @param username username of the player to be added
     * @throws AlreadyExistingPlayerException when the player we want to add already exists in the game
     * @throws AlreadyMaxNumberOfPlayersException when the game already contains the maximum amount of players
     */
    public synchronized Game addPlayerToGame(String username, int desiredNumberOfPlayers) throws AlreadyExistingPlayerException, AlreadyMaxNumberOfPlayersException {
        try {
            this.gameHandler.getGame().addPlayer(new Player(username), desiredNumberOfPlayers);
        } catch (InvalidConstructorDataException e) {
            throw new RuntimeException(e);
        }

        return this.gameHandler.getGame();
    }

    /**
     * Starts the game when all players are ready
     * @throws CardTypeMismatchException
     * @throws InvalidConstructorDataException
     * @throws CardNotImportedException
     * @throws DeckIsEmptyException
     * @throws AlreadyExistingPlayerException
     * @throws AlreadyMaxNumberOfPlayersException
     * @throws IOException
     * @throws UnlinkedCardException
     */
    public synchronized Game initializeGame() throws CardTypeMismatchException, InvalidConstructorDataException, CardNotImportedException, DeckIsEmptyException, AlreadyExistingPlayerException, AlreadyMaxNumberOfPlayersException, IOException, UnlinkedCardException, AlreadyThreeCardsInHandException {
        //shuffle the list of player to determine the order of play
        gameHandler.getGame().shufflePlayers();
        //set the first player
        gameHandler.getGame().getListOfPlayers().getFirst().setIsFirst(true);
        gameHandler.getGame().setCurrentPlayer(gameHandler.getGame().getListOfPlayers().getFirst());
        //setting discovered cards
        gameHandler.getGame().getTableTop().getDrawingField().setDiscoveredCards();
        for(Player player: gameHandler.getGame().getListOfPlayers())
        {
            this.initializePlayerHand(player);
            try {
                this.giveStarterCard(player);
            } catch (DeckIsEmptyException | NotExistingPlayerException e) {
                throw new RuntimeException(e);
            }
        }
        return gameHandler.getGame();
    }

    /**
     * Set the chosen marker to the player
     * @param player the player that has chosen the marker
     * @param marker the marker chosen by the player
     */
    public synchronized void setMarker(Player player, Marker marker) throws NotExistingPlayerException {
        gameHandler.getGame().getPlayer(player.getUsername()).setMarker(marker);
        gameHandler.getGame().getAvailableMarkers().remove(marker);
    }

    public StarterCard giveStarterCard(Player player) throws DeckIsEmptyException, NotExistingPlayerException {
        StarterCard starterCard = gameHandler.getGame().getAvailableStarterCards().getTopCard();
        gameHandler.getGame().getPlayer(player.getUsername()).setStarterCard(starterCard);
        return starterCard;
    }


    /**
     * Give to the player the initial 3 cards in his hand
     * @param player the player that has to receive the cards
     * @throws DeckIsEmptyException if the deck is empty
     */
    public synchronized void initializeStarterCard(Player player, StarterCard starterCard, Side side) throws NotExistingPlayerException {
        Player playerObj = gameHandler.getPlayer(player.getUsername());
        if(side == Side.FRONT){
            playerObj.getPlayerField().addCardToCell(starterCard);
        } else {
            playerObj.getPlayerField().addCardToCell(cardHandler.getOtherSideCard(starterCard));
        }
    }

    /**
     * Give to the player 2 secret objective cards to choose from
     * @return a list composed of the two secret objective cards
     * @throws DeckIsEmptyException if the deck is empty
     */
    public synchronized ArrayList<ObjectiveCard> takeTwoObjectiveCards() throws DeckIsEmptyException {
        ArrayList<ObjectiveCard> secretObjectiveCards = new ArrayList<>();
        secretObjectiveCards.add(this.gameHandler.getGame().getObjectiveCardDeck().getTopCard());
        secretObjectiveCards.add(this.gameHandler.getGame().getObjectiveCardDeck().getTopCard());
        return secretObjectiveCards;
    }

    /**
     * Add a secret objective card to the deck
     * @param objectiveCard
     */
    public synchronized void addObjectiveCardToDeck(ObjectiveCard objectiveCard) throws NullPointerException {
        this.gameHandler.getGame().getObjectiveCardDeck().addCard(objectiveCard);
    }

    /**
     * Inizialize the playerHand of the player
     * @param player
     * @throws DeckIsEmptyException if the deck is empty
     * @throws AlreadyThreeCardsInHandException if there are already three cards in the player hand
     */
    public synchronized void initializePlayerHand(Player player) throws DeckIsEmptyException, AlreadyThreeCardsInHandException {
        player.getPlayerHand().addCardToPlayerHand(gameHandler.getGame().getTableTop().getDrawingField().drawCardFromGoldCardDeck(DrawPosition.FROMDECK));
        player.getPlayerHand().addCardToPlayerHand(gameHandler.getGame().getTableTop().getDrawingField().drawCardFromResourceCardDeck(DrawPosition.FROMDECK));
        player.getPlayerHand().addCardToPlayerHand(gameHandler.getGame().getTableTop().getDrawingField().drawCardFromResourceCardDeck(DrawPosition.FROMDECK));
    }
    public void setSecretObjectiveCard(Player player, ObjectiveCard chosenObjectiveCard) throws NotExistingPlayerException {
        Player playerObj = gameHandler.getPlayer(player.getUsername());
        playerObj.setSecretObjective(chosenObjectiveCard);
    }


    public synchronized void update(UserMessageWrapper message) {
        switch(message.getType()) {
            case USERNAME_INSERTED -> {
                testUsername = message.getMessage().getUsername();
            }
        }
    }

    public synchronized void playCard(Player player, PlayableCard card, PlayableCard otherCard, AngleOrientation orientation) throws InvalidCardPositionException, CardTypeMismatchException, RequirementsNotMetException, AngleAlreadyLinkedException, NotExistingPlayerException {
        Player playerObj = gameHandler.getPlayer(player.getUsername());
        if(cardHandler.checkRequirements(otherCard, playerObj)) {
            playerObj.getPlayerField().addCardToCell(card, orientation, otherCard);
            playerObj.getPlayerHand().removeCardFromHand(otherCard);
            playerObj.addPoints(otherCard.getPoints());

        }
        else{
            throw new RequirementsNotMetException();
        }
    }


    public synchronized void calculateAndUpdateFinalPoints() throws CardTypeMismatchException {
        Game game = gameHandler.getGame();

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
        game.setIsGameEnded(true);
    }

    public synchronized void drawCard(Player player, CardType cardType, DrawPosition drawPosition) throws DeckIsEmptyException, AlreadyThreeCardsInHandException, NotExistingPlayerException {
        Player playerObj = gameHandler.getPlayer(player.getUsername());
        if(cardType == CardType.RESOURCE) {
            ResourceCard card = gameHandler.getGame().getTableTop().getDrawingField().drawCardFromResourceCardDeck(drawPosition);
            playerObj.getPlayerHand().addCardToPlayerHand(card);
        } else {
            GoldCard card = gameHandler.getGame().getTableTop().getDrawingField().drawCardFromGoldCardDeck(drawPosition);
            playerObj.getPlayerHand().addCardToPlayerHand(card);
        }
    }
    public CardHandler getCardHandler() {
        return cardHandler;
    }
    public void nextTurn(Player player) throws NotExistingPlayerException {
        Player playerObj = gameHandler.getPlayer(player.getUsername());
        playerObj.setIsTurn(false);
        int index = gameHandler.getGame().getListOfPlayers().indexOf(playerObj);
        int nextIndex = index + 1;
        if(nextIndex>= gameHandler.getGame().getListOfPlayers().size())
            nextIndex = 0;
        gameHandler.getGame().getListOfPlayers().get(nextIndex).setIsTurn(true);
        gameHandler.getGame().setCurrentPlayer(gameHandler.getGame().getListOfPlayers().get(nextIndex));

    }

}
