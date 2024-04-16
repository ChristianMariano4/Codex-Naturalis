package it.polimi.ingsw.main;

import it.polimi.ingsw.controller.CardHandler;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.model.cards.ObjectiveCard;
import it.polimi.ingsw.model.cards.PlayableCard;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws AlreadyThreeCardsInHandException, InvalidConstructorDataException, CardTypeMismatchException, CardNotImportedException, DeckIsEmptyException, UnlinkedCardException {

       /* CardHandler h = new CardHandler();
        ArrayList<ObjectiveCard> c = h.filterObjectiveCards(h.importResourceObjectiveCards());
        c.forEach(x-> {
            try {
                System.out.println(x.getCardId() +"->" + x.getCurrentSide() + "/" + x.getOtherSideCard().getCardId() + "->" + x.getOtherSideCard().getCurrentSide());
            } catch (UnlinkedCardException e) {
                throw new RuntimeException(e);
            }
        });*/

       /* PlayerHand playerHand = new PlayerHand();
        ArrayList centralResources = new ArrayList();
        centralResources.add(Resource.INSECT);
        HashMap angles = new HashMap();
        ArrayList requirements = new ArrayList();
        requirements.add(Resource.INSECT);
        requirements.add(Resource.INSECT);
        requirements.add(Resource.INSECT);
        GoldCard goldCard = new GoldCard(1, Side.FRONT, centralResources, angles, Resource.INSECT,
                requirements, GoldPointCondition.NONE, 3);
        playerHand.addCardToPlayerHand(goldCard);
        CardVisitorImpl cardVisitor = new CardVisitorImpl();
        playerHand.getCardsInHand().get(0).accept(cardVisitor);


        */

    }
}
