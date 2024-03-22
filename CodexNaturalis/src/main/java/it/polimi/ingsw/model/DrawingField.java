package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.Draw;
import it.polimi.ingsw.exceptions.CardTypeMismatch;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.PlayableCard;
import it.polimi.ingsw.model.cards.ResourceCard;

import java.util.HashMap;

public class DrawingField {
    private final Deck goldCardDeck;
    private final Deck resourceCardDeck;
    private final HashMap<Draw, PlayableCard> discoveredGoldCards;
    private final HashMap<Draw, PlayableCard> discoveredResourceCards;

    //@requires controller has to create the decks with correct types
    public DrawingField(Deck goldCardDeck, Deck resourceCardDeck) throws CardTypeMismatch
    {
        this.goldCardDeck = goldCardDeck;
        this.resourceCardDeck = resourceCardDeck;
        discoveredGoldCards = new HashMap<Draw, PlayableCard>();
        discoveredResourceCards = new HashMap<Draw, PlayableCard>();

        for(int i = 0; i<2; i++) {  //checks to make sure card types are correct
            PlayableCard temp;
            temp = goldCardDeck.getTopCard();  //è necessario fare il check?
            if (temp instanceof GoldCard) {
                if(i == 0)
                    discoveredGoldCards.put(Draw.LEFT, temp);
                else
                    discoveredGoldCards.put(Draw.RIGHT, temp);
            }
            else {
                throw new CardTypeMismatch();
            }
        }
        for(int i = 0; i<2; i++) {
            PlayableCard temp;
            temp = resourceCardDeck.getTopCard();
            if (temp instanceof ResourceCard) {
                if(i == 0)
                    discoveredResourceCards.put(Draw.LEFT, temp);
                else
                    discoveredResourceCards.put(Draw.RIGHT, temp);
            }
            else {
                throw new CardTypeMismatch();
            }
        }
    }
    public PlayableCard drawCardFromGoldCardDeck(Draw draw)
    {
        if(draw == Draw.FROMDECK)
            return goldCardDeck.getTopCard();
        else {
            PlayableCard chosenCard = discoveredGoldCards.get(draw);
            //Change discovered gold card chosen by the player
            discoveredGoldCards.put(draw, goldCardDeck.getTopCard());
            return chosenCard;
        }

    }
    public PlayableCard drawCardFromResourceCardDeck(Draw draw)
    {
        if(draw == Draw.FROMDECK)
            return resourceCardDeck.getTopCard();
        else {
            PlayableCard chosenCard = discoveredResourceCards.get(draw);
            //Change discovered resource card chosen by the player
            discoveredResourceCards.put(draw, resourceCardDeck.getTopCard());
            return chosenCard;
        }

    }
}
