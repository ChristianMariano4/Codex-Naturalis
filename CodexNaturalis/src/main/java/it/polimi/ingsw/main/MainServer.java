package it.polimi.ingsw.main;

import it.polimi.ingsw.enumerations.GoldPointCondition;
import it.polimi.ingsw.enumerations.Resource;
import it.polimi.ingsw.enumerations.Side;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.CardVisitorImpl;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.model.PlayerHand;
import it.polimi.ingsw.model.cards.GoldCard;
import it.polimi.ingsw.network.server.ClientHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainServer {
    public static void main(String[] args) throws AlreadyThreeCardsInHandException, InvalidConstructorDataException, CardTypeMismatchException, CardNotImportedException, DeckIsEmptyException, UnlinkedCardException, AlreadyExistingPlayerException, AlreadyFourPlayersException, IOException {


        try {
            ExecutorService executor = Executors.newCachedThreadPool();
            ServerSocket serverSocket = new ServerSocket(GameValues.SERVER_PORT);
            while(true)
            {
                try{
                    int threadNo = 0;
                    Socket clientSocket = serverSocket.accept();
                    threadNo += 1;
                    String name = "Thread" + threadNo;
                    executor.submit(new ClientHandler(clientSocket, name));
                }
                catch(Exception e)
                {
                    break;
                }
            }
            }
            catch(Exception e)
            {
                System.err.println("Socket error");
                return;
            }






        /*





        CardHandler h = new CardHandler();
        ArrayList<ResourceCard> gold= h.importResourceCards();
        gold = h.filterResourceCards(gold);
        ResourceCard c = gold.get(4);
        System.out.println(c.getCardId() + " - " + c.getCurrentSide());
        ResourceCard c2 = h.getOthersideCard(c);
        System.out.println(c2.getCardId() + " - " + c2.getCurrentSide());



        PlayerHand playerHand = new PlayerHand();
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




//        Server server = new Server();
//        Controller controller = new Controller(server);
//        Game game = controller.createGame();
//        Player p1 = new Player("p1", game);
//        Player p2 = new Player("p2", game);
//        game.addPlayer(p1);
//        game.addPlayer(p2);
//        controller.startGame(game);
//        System.out.println(game.getListOfPlayers().getFirst().getPlayerHand().getCardsInHand().getFirst());

    */
    }
}
