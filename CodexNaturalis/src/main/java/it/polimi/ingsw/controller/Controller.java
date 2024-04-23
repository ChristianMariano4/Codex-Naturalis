package it.polimi.ingsw.controller;

import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.enumerations.Marker;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.ResourceCard;
import it.polimi.ingsw.model.cards.StarterCard;
import it.polimi.ingsw.network.Server;

import java.io.IOException;
import java.util.*;

/**
 * Main controller class
 */
public class Controller {

    private int numberOfGames = 0;
    private final CardHandler cardHandler;
    private final Server server;

    public Controller(Server server)
    {
        cardHandler = new CardHandler();
        this.server = server;
    }
    public Game createGame() throws InvalidConstructorDataException, CardNotImportedException, CardTypeMismatchException, DeckIsEmptyException {
        //starts new thread in server and then returns new game
        //TODO: start thread in server
        //TODO: fix deck inheritance
        //sequential game id starting from 0
        int id = this.numberOfGames;
        this.numberOfGames += 1;
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
        //create the new game
        return new Game(id, drawingField, sharedObjectiveCards, objectiveCardDeck);
    }

    /**
     * Adds a player to the game specified by gameId
     * @param gameId id of the game to which the player will be added
     * @param player player to add
     * @throws AlreadyExistingPlayerException when the player we want to add already exists in the game
     * @throws AlreadyFourPlayersException when the game already contains the maximum amount of players
     */
    public void addPlayerToGame(int gameId, Player player) throws AlreadyExistingPlayerException, AlreadyFourPlayersException {
        server.getGameById(gameId).addPlayer(player);
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
    public void startGame(Game game) throws CardTypeMismatchException, InvalidConstructorDataException, CardNotImportedException, DeckIsEmptyException, AlreadyExistingPlayerException, AlreadyFourPlayersException, IOException, UnlinkedCardException, AlreadyThreeCardsInHandException {
        //shuffle the list of player to determine the order of play
        game.shufflePlayers();
        //set the first player
        game.getListOfPlayers().getFirst().setIsFirst(true);
        //set the markers and the secret objectives
        for(Player player : game.getListOfPlayers())
        {
            List<Marker> availableMarkers= new ArrayList<>();
            Collections.addAll(availableMarkers, Marker.values());

            //ask the user to choose a marker
            System.out.println(player.getUsername() + " choose one the following markers: " + availableMarkers);
            while(true){
                try {
                    Scanner scanner = new Scanner(System.in);
                    //valueOf throws IllegalArgumentException if the marker is not valid
                    Marker marker = Marker.valueOf(scanner.nextLine());
                    player.setMarker(marker);
                    availableMarkers.remove(marker);
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid marker inserted. Please insert a valid marker.");
                }
            }

            player.setSecretObjective(game.getObjectiveCardDeck().getTopCard());
            Deck<StarterCard> starterCardDeck = new Deck<StarterCard>(cardHandler.filterStarterCards(cardHandler.importStarterCards()));
            starterCardDeck.shuffleDeck();

            //ask the user to choose the side of the starter card and add it to the player field
            StarterCard starterCard = starterCardDeck.getTopCard();
            System.out.println(player.getUsername() + " choose the side on which you want to play your starter card: \n0->Front\n1->Back");
            while(true){
                try {
                    Scanner scanner = new Scanner(System.in);
                    //TODO: print front and back of the starter card
                    //starterCard.printFront();
                    //starterCard.printBack();
                    Side side = Side.valueOf(scanner.nextLine());
                    if(side == Side.FRONT){
                        player.getPlayerField().addCardToCell(starterCard);
                    } else {
                        player.getPlayerField().addCardToCell(cardHandler.getOtherSideCard(starterCard));
                    }
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid value for the side inserted. Please insert 0 or 1");
                }
            }
            player.getPlayerHand().addCardToPlayerHand(game.getTableTop().getDrawingField().drawCardFromGoldCardDeck(DrawPosition.FROMDECK));
            player.getPlayerHand().addCardToPlayerHand(game.getTableTop().getDrawingField().drawCardFromResourceCardDeck(DrawPosition.FROMDECK));
            player.getPlayerHand().addCardToPlayerHand(game.getTableTop().getDrawingField().drawCardFromResourceCardDeck(DrawPosition.FROMDECK));
        }
        game.getTableTop().getDrawingField().setDiscoveredCards();
    }


}
