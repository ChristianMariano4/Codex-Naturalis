package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.io.Serializable;
import java.util.HashMap;
/**
 * This class represents the drawing field in the game. It contains two types of decks: gold card deck and resource card deck.
 * It also maintains the discovered gold cards and discovered resource cards.
 * The drawing field is responsible for drawing cards from the decks and managing the discovered cards.
 */
public class DrawingField implements Serializable {
    private final Deck<GoldCard> goldCardDeck;
    private final Deck<ResourceCard> resourceCardDeck;
    private final HashMap<DrawPosition, GoldCard> discoveredGoldCards;
    private final HashMap<DrawPosition, ResourceCard> discoveredResourceCards;

    /**
     * Constructs a new DrawingField with the given gold card deck and resource card deck.
     * It also initializes the discovered cards.
     *
     * @param goldCardDeck the deck of gold cards
     * @param resourceCardDeck the deck of resource cards
     */
    public DrawingField(Deck<GoldCard> goldCardDeck, Deck<ResourceCard> resourceCardDeck) {
        this.goldCardDeck = goldCardDeck;
        this.resourceCardDeck = resourceCardDeck;
        discoveredGoldCards = new HashMap<>();
        discoveredResourceCards = new HashMap<>();
    }

    /**
     * Draws a card from the gold card deck.
     *
     * @param drawPosition the position from which to draw the card
     * @return the drawn card
     * @throws DeckIsEmptyException if the deck is empty
     */
    public GoldCard drawCardFromGoldCardDeck(DrawPosition drawPosition) throws DeckIsEmptyException
    {
        if(drawPosition == DrawPosition.FROMDECK) {
            return goldCardDeck.getTopCard();
        }
        else {
            GoldCard chosenCard = discoveredGoldCards.get(drawPosition);
            if(chosenCard == null)
                throw new DeckIsEmptyException();
            if (!goldCardDeck.isEmpty())
                discoveredGoldCards.put(drawPosition, goldCardDeck.getTopCard());
            else {
                discoveredGoldCards.put(drawPosition, null);
            }

            return chosenCard;
        }

    }
    /**
     * Draws a card from the resource card deck.
     *
     * @param drawPosition the position from which to draw the card
     * @return the drawn card
     * @throws DeckIsEmptyException if the deck is empty
     */
    public ResourceCard drawCardFromResourceCardDeck(DrawPosition drawPosition) throws DeckIsEmptyException
    {
        if(drawPosition == DrawPosition.FROMDECK) {
            return resourceCardDeck.getTopCard();
        }
        else {
            ResourceCard chosenCard = discoveredResourceCards.get(drawPosition);
            if(chosenCard == null)
                throw new DeckIsEmptyException();
            if(!resourceCardDeck.isEmpty())
                discoveredResourceCards.put(drawPosition, resourceCardDeck.getTopCard());
            else {
                discoveredResourceCards.put(drawPosition, null);
            }
            return chosenCard;
        }

    }

    /**
     * Draw a card from the resource card deck
     *
     * @return the drawn card
     * @throws DeckIsEmptyException if the deck is empty
     */
    public ResourceCard seeTopResourceCard() throws DeckIsEmptyException {
        return resourceCardDeck.seeTopCard();
    }

    /**
     * Draw a card from the gold card deck
     * @return The top card of the gold card deck
     * @throws DeckIsEmptyException if the deck is empty
     */
    public GoldCard seeTopGoldCard() throws DeckIsEmptyException {
        return goldCardDeck.seeTopCard();
    }


    /**
     * Set the discovered cards from the decks
     *
     * @throws DeckIsEmptyException if the deck is empty
     */
    public void setDiscoveredCards() throws DeckIsEmptyException {
        discoveredGoldCards.put(DrawPosition.LEFT, drawCardFromGoldCardDeck(DrawPosition.FROMDECK));
        discoveredGoldCards.put(DrawPosition.RIGHT, drawCardFromGoldCardDeck(DrawPosition.FROMDECK));

        discoveredResourceCards.put(DrawPosition.LEFT, drawCardFromResourceCardDeck(DrawPosition.FROMDECK));
        discoveredResourceCards.put(DrawPosition.RIGHT, drawCardFromResourceCardDeck(DrawPosition.FROMDECK));
    }

    /**
     * Get the discovered gold cards
     *
     * @return the HashMap of the discovered goldCards
     */
    public HashMap<DrawPosition, GoldCard> getDiscoveredGoldCards() {
        return this.discoveredGoldCards;
    }

    /**
     * Get the discovered resource cards
     *
     * @return the HashMap of the discovered resourceCards
     */
    public HashMap<DrawPosition, ResourceCard> getDiscoveredResourceCards() {
        return this.discoveredResourceCards;
    }

    /**
     * See if both decks are empty
     *
     * @return true if both decks are empty
     */
    public boolean getBothDecksEmpty()
    {
        return resourceCardDeck.isEmpty() && goldCardDeck.isEmpty();
    }

    /**
     * See if all decks and discovered cards are not there
     *
     * @return true if both decks are empty
     */
    public boolean getNoCardsLeft() {
        return resourceCardDeck.isEmpty() && goldCardDeck.isEmpty() && discoveredResourceCards.isEmpty() && discoveredGoldCards.isEmpty();
    }
}
