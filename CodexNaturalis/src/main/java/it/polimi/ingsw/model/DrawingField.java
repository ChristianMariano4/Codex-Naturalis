package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.exceptions.CardTypeMismatchException;
import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.util.HashMap;

public class DrawingField {
    private final Deck goldCardDeck;
    private final Deck resourceCardDeck;
    private final HashMap<DrawPosition, ResourceCard> discoveredGoldCards;
    private final HashMap<DrawPosition, ResourceCard> discoveredResourceCards;

    //@requires controller has to create the decks with correct types
    public DrawingField(Deck goldCardDeck, Deck resourceCardDeck) throws CardTypeMismatchException, DeckIsEmptyException {
        this.goldCardDeck = goldCardDeck;
        this.resourceCardDeck = resourceCardDeck;
        discoveredGoldCards = new HashMap<DrawPosition, ResourceCard>();
        discoveredResourceCards = new HashMap<DrawPosition, ResourceCard>();

        for(int i = 0; i<2; i++) {  //checks to make sure card types are correct
            ResourceCard temp;
            temp = goldCardDeck.getTopCard();  //è necessario fare il check?
            if (temp instanceof GoldCard) {
                if(i == 0)
                    discoveredGoldCards.put(DrawPosition.LEFT, temp);
                else
                    discoveredGoldCards.put(DrawPosition.RIGHT, temp);
            }
            else {
                throw new CardTypeMismatchException();
            }
        }
        for(int i = 0; i<2; i++) {
            ResourceCard temp;
            temp = resourceCardDeck.getTopCard();
            if (temp instanceof ResourceCard) {
                if(i == 0)
                    discoveredResourceCards.put(DrawPosition.LEFT, temp);
                else
                    discoveredResourceCards.put(DrawPosition.RIGHT, temp);
            }
            else {
                throw new CardTypeMismatchException();
            }
        }
    }
    public ResourceCard drawCardFromGoldCardDeck(DrawPosition drawPosition) throws DeckIsEmptyException
    {
        if(drawPosition == DrawPosition.FROMDECK) {
            return goldCardDeck.getTopCard();
        }
        else {
            ResourceCard chosenCard = discoveredGoldCards.get(drawPosition);
            if(chosenCard == null)
                throw new DeckIsEmptyException();
            //Change discovered gold card chosen by the player
            if (!goldCardDeck.isEmpty())
                discoveredGoldCards.put(drawPosition, goldCardDeck.getTopCard());
            else {
                discoveredGoldCards.put(drawPosition, null);
            }

            return chosenCard;
        }

    }
    public ResourceCard drawCardFromResourceCardDeck(DrawPosition drawPosition) throws DeckIsEmptyException
    {
        if(drawPosition == DrawPosition.FROMDECK) {
            return resourceCardDeck.getTopCard();
        }
        else {
            ResourceCard chosenCard = discoveredResourceCards.get(drawPosition);
            if(chosenCard == null)
                throw new DeckIsEmptyException();
            //Change discovered resource card chosen by the player
            if(!resourceCardDeck.isEmpty())
                discoveredResourceCards.put(drawPosition, resourceCardDeck.getTopCard());
            else {
                discoveredResourceCards.put(drawPosition, null);
            }
            return chosenCard;
        }

    }
}
