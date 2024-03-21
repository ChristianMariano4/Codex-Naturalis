package it.polimi.ingsw.model;

import it.polimi.ingsw.enumerations.Draw;
import it.polimi.ingsw.exceptions.AlreadyThreeCardsInHand;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.PlayableCard;

import java.util.LinkedList;
import java.util.List;

public class PlayerHand {
    private List<PlayableCard> cardsInHand;
    private DrawingField drawingField;
    private PlayerField playerField;

    public PlayerHand(List<PlayableCard> cardsInHand) {
        this.cardsInHand = new LinkedList<PlayableCard>();
        try {
            this.cardsInHand.addAll(cardsInHand);
        } catch (NullPointerException e) { }
    }

    public void chooseGoldCardToDraw(Draw draw) throws AlreadyThreeCardsInHand {
        PlayableCard chosenCard = drawingField.drawCardFromGoldCardDeck(draw);
        this.addCardToPlayerHand(chosenCard);
    }

    public void chooseResourceCardToDraw(Draw draw) throws AlreadyThreeCardsInHand {
        PlayableCard chosenCard = drawingField.drawCardFromResourceCardDeck(draw);
        this.addCardToPlayerHand(chosenCard);
    }

    public void addCardToPlayerHand(PlayableCard card) throws AlreadyThreeCardsInHand {
        if(cardsInHand.size() == 3){
            throw new AlreadyThreeCardsInHand();
        }
        cardsInHand.add(card);
    }

}
