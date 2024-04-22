package it.polimi.ingsw.main;

import it.polimi.ingsw.controller.CardHandler;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws AlreadyThreeCardsInHandException, InvalidConstructorDataException, CardTypeMismatchException, CardNotImportedException, DeckIsEmptyException, UnlinkedCardException, AlreadyExistingPlayerException, AlreadyFourPlayersException, IOException {
/*
        CardHandler h = new CardHandler();
        ArrayList<ResourceCard> gold= h.importResourceCards();
        gold = h.filterResourceCards(gold);
        ResourceCard c = gold.get(4);
        System.out.println(c.getCardId() + " - " + c.getCurrentSide());
        ResourceCard c2 = h.getOthersideCard(c);
        System.out.println(c2.getCardId() + " - " + c2.getCurrentSide());

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

//        Server server = new Server();
//        Controller controller = new Controller(server);
//        Game game = controller.createGame();
//        controller.startGame(game);




    }
}
