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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws AlreadyThreeCardsInHandException, InvalidConstructorDataException, CardTypeMismatchException, CardNotImportedException, DeckIsEmptyException, UnlinkedCardException, AlreadyExistingPlayerException, AlreadyFourPlayersException, IOException {
/*
        CardHandler h = new CardHandler();
        ArrayList<GoldCard> gold= h.importGoldCards();
        gold.forEach(x -> System.out.println(x.getCardId()));
        h.goldCards.forEach(x -> System.out.println(x.getOtherSideCard(Side.FRONT).getCardId() + " ->" + x.getOtherSideCard(Side.BACK).getCardId()));
*/

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


        /*Controller test

        Controller controller = new Controller();
        Game game = controller.createGame();
        controller.startGame(game);


         */

    }
}
