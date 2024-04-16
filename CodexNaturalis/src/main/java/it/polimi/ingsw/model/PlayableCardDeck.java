package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;

import java.util.ArrayList;

public class PlayableCardDeck extends Deck<PlayableCard>{
    public PlayableCardDeck(ArrayList<PlayableCard> cards) throws InvalidConstructorDataException {
        super(cards);
    }
}
