package it.polimi.ingsw.network;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.CardNotFoundException;
import it.polimi.ingsw.exceptions.NotExistingPlayerException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.view.TUI;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static it.polimi.ingsw.model.GameValues.DEFAULT_MATRIX_SIZE;

public class ViewCLI implements View, Runnable {
    private Game game;
    private RMIClient client;
    private final Scanner scanner = new Scanner(System.in);
    private boolean gameStarted = false;

    private String playerId;
    TUI ui = new TUI();;


    public void setUsername() throws RemoteException {

        ui.setUsername();
        String username = null;
        do {
            username = scanner.nextLine();

            if(client.getServer().checkUsername(username))
                break;
            else
                System.out.println("Username already in use, try again: ");

        }while(true);
        client.setUsername(username);
        playerId = username;

    }
    public void setChoiceGame() {
        while(true){
            ui.createOrJoinAGame();
            String choice = scanner.nextLine();
            if(choice.equals("1")) {
                this.game = client.createGame(this.client.getUsername());
                System.out.println("Game created.");
                break;
            }
            if(choice.equals("2")) {
                ui.showAllExistingGames(client.getAvailableGames());
                this.game = client.joinGame(Integer.parseInt(scanner.nextLine()), this.client.getUsername());
                break;
            }
        }
    }

    public void setReady() {
        do{
            System.out.println("Press 1 when you are ready to start the game.");
        }while(!scanner.nextLine().equals("1"));
        int numberReadyPlayers = client.setReady();
        System.out.println("You are ready to start the game. Waiting for other players to be ready...");
    }

    public ViewCLI(RMIClient client) {

        this.client = client;
    }

    @Override
    public void boardUpdate(Game gameUpdated) {
        //these three methods call ui methods and diplay things
    }

    @Override
    public void newPlayer(Game gameUpdated) {

    }

    @Override
    public void update(Game gameUpdated) {
        this.game = gameUpdated;
    }

    @Override
    public void gameLoop() throws IOException, NotExistingPlayerException, InterruptedException {
        boolean inGame = true;
        ui.showMainScreen();
        Object lock = new Object();
        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>();
        AsyncReader reader = new AsyncReader(lock, blockingQueue);
        Thread readerThread = new Thread(reader);
        readerThread.start();
        while(inGame)
        {
            boolean isTurn = game.getPlayer(client.getUsername()).getIsTurn();
            String playerPlaying = game.getCurrentPlayer().getUsername();
            ui.showTurnScreen(playerPlaying, client.getUsername());


            while(!Thread.interrupted()) {
                String command = null;
                if(!blockingQueue.isEmpty()) {
                    synchronized (lock) {
                        command = blockingQueue.take();
                        switch (command) {
                            case "q", "quit":
                                inGame = false;
                                break;
                            case "h", "help":
                                ui.showAllCommands();
                                break;
                            case "showplayers":
                                showAllPlayers();
                                break;
                            case "myhand":
                                showPlayerHand();
                                break;
                            case "showMyField":
                                showPlayerField();
                                break;
                            case "playTurn":
                                if (isTurn) {
                                    playCard();
                                    drawCard();
                                    endTurn();
                                } else
                                    notYourTurn();
                                break;
                            case null:
                                break;
                            default:
                                ui.commandNotFound();
                                break;
                        }
                        lock.notifyAll();
                    }
                }
            }
            Thread.sleep(1);
        }
    }

    private void notYourTurn()
    {
        ui.notYourTurn(game.getCurrentPlayer().getUsername());
    }
    private void endTurn() throws NotExistingPlayerException, RemoteException {
        client.getServer().endTurn(game.getGameId(), client.getUsername());
    }
    private void drawCard() throws RemoteException {
        HashMap<DrawPosition, GoldCard> discoveredGoldCards =  game.getTableTop().getDrawingField().getDiscoveredGoldCards();
        HashMap<DrawPosition, ResourceCard> discoveredResourceCards =  game.getTableTop().getDrawingField().getDiscoveredResourceCards();

        HashMap<GoldCard, CardInfo> uiDiscoveredGoldCards = new HashMap<>();
        HashMap<ResourceCard, CardInfo> uiDiscoveredResourceCards = new HashMap<>();
        for(GoldCard card: discoveredGoldCards.values())
        {
            uiDiscoveredGoldCards.put(card, client.getServer().getCardInfo(card, game.getGameId()));
        }

        for(ResourceCard card: discoveredResourceCards.values())
        {
            uiDiscoveredResourceCards.put(card, client.getServer().getCardInfo(card, game.getGameId()));
        }
        ui.showDiscoveredCards(uiDiscoveredGoldCards, uiDiscoveredResourceCards);
        ui.drawCard();

        do{
            try{
                int choice = Integer.parseInt(scanner.nextLine());
                CardType cardType = null;
                DrawPosition drawPosition = null;
                switch(choice)
                {
                    case 1:
                        cardType = CardType.RESOURCE;
                        drawPosition = DrawPosition.FROMDECK;
                        break;
                    case 2:
                        cardType = CardType.GOLD;
                        drawPosition = DrawPosition.FROMDECK;
                        break;
                    case 3:
                        cardType = CardType.RESOURCE;
                        drawPosition = DrawPosition.LEFT;
                        break;
                    case 4:
                        cardType = CardType.RESOURCE;
                        drawPosition = DrawPosition.RIGHT;
                        break;
                    case 5:
                        cardType = CardType.GOLD;
                        drawPosition = DrawPosition.LEFT;
                        break;
                    case 6:
                        cardType = CardType.GOLD;
                        drawPosition = DrawPosition.RIGHT;
                        break;
                    case 0:
                        break;
                    default: throw new NumberFormatException();
                }
                if(cardType == null)
                {
                    continue;
                }
                client.getServer().drawCard(game.getGameId(), client.getUsername(), cardType, drawPosition);
                break;
            }
            catch(Exception e)
            {
                ui.invalidInput();
            }
        }while(true);
    }
    private void playCard() throws RemoteException, NotExistingPlayerException {
        showPlayerHand();
        showPlayerField();
        ui.chooseCardToPlay();
        do {
            try {
                int cardId = Integer.parseInt(scanner.nextLine());
                PlayableCard card = game.getPlayer(client.getUsername()).getPlayerHand().getCardById(cardId);
                ui.chooseWhereToPlay();
                int cardIdOnBoard = Integer.parseInt(scanner.nextLine());
                PlayableCard cardOnBoard = game.getPlayer(client.getUsername()).getPlayerField().getCardById(cardIdOnBoard);
                ui.showCardInfo(cardOnBoard, client.getServer().getCardInfo(cardOnBoard, game.getGameId()));
                ui.chooseAngle();
                int choice = Integer.parseInt(scanner.nextLine());
                AngleOrientation orientation = switch (choice) {
                    case 1 -> AngleOrientation.TOPRIGHT;
                    case 2 -> AngleOrientation.TOPLEFT;
                    case 3 -> AngleOrientation.BOTTOMRIGHT;
                    case 4 -> AngleOrientation.BOTTOMLEFT;
                    case 0 -> AngleOrientation.NONE;
                    default -> throw new NumberFormatException();
                };
                if(orientation.equals(AngleOrientation.NONE))
                {
                    continue;
                }
                client.getServer().playCard(game.getGameId(),client.getUsername(), cardOnBoard, card , orientation);
                break;

            }
            catch(Exception e)
            {
                ui.invalidInput();
            }
        } while(true);

    }
    private void showAllPlayers()
    {
        ArrayList<String> usernames = new ArrayList<>();
        for(Player player : game.getListOfPlayers())
            usernames.add(player.getUsername());
        ui.showAllPlayers(usernames);
    }
    public void twentyPoints(String username)
    {
        ui.twentyPoints(username);
    }
    public void chooseObjectiveCard(ArrayList<ObjectiveCard> objectiveCardsToChoose)
    {
        try {
            ui.showCardInfo(objectiveCardsToChoose.get(0), client.getServer().getCardInfo(objectiveCardsToChoose.get(0), game.getGameId()));
            ui.showCardInfo(objectiveCardsToChoose.get(1), client.getServer().getCardInfo(objectiveCardsToChoose.get(1), game.getGameId()));
            ui.chooseSecretObjectiveCard();
            try {
                do {
                    int choice = Integer.parseInt(scanner.nextLine());
                    ObjectiveCard chosenObjectiveCard = null;
                    switch(choice)
                    {
                        case 1:
                            chosenObjectiveCard = objectiveCardsToChoose.get(0);
                            break;
                        case 2:
                            chosenObjectiveCard = objectiveCardsToChoose.get(1);
                            break;
                        default: throw new NumberFormatException();
                    }
                    if (chosenObjectiveCard == null)
                        throw new CardNotFoundException();
                    client.getServer().setSecretObjectiveCard(game.getGameId(), game.getPlayer(client.getUsername()), chosenObjectiveCard);
                    break;
                }while(true);
            }
            catch (Exception e)
            {
                ui.invalidInput();
            }

        }
        catch (Exception e)
        {
            ui.somethingWentWrong();
        }
    }

    public void showPlayerHand() throws RemoteException, NotExistingPlayerException {
        HashMap<PlayableCard, CardInfo> cardsInHand = new HashMap<>();


        for(PlayableCard card: game.getPlayer(client.getUsername()).getPlayerHand().getCardsInHand()) {
            CardInfo cardInfo = client.getServer().getCardInfo(card, game.getGameId());
            cardsInHand.put(card, cardInfo);
        }
        ui.showPlayerHand(cardsInHand);
    }

    public void showPlayerField() throws RemoteException, NotExistingPlayerException {
        ui.showPlayerField(createMatrixFromField(game.getPlayer(client.getUsername()).getPlayerField()));
    }

    public void showOtherPlayerField() throws RemoteException {
        this.showAllPlayers();
        String username = scanner.nextLine();
        Player p = game.getListOfPlayers().stream().filter(p1 -> p1.getUsername().equals(username)).findFirst().orElse(null);
        if(p != null) {
            ui.showPlayerField(createMatrixFromField(p.getPlayerField()));
        }
    }
    public void markerSelection()
    {
        ArrayList<Marker> markerList = game.getAvailableMarkers();
        ui.showAvailableMarkers(markerList);
        do{
            try {
                Marker chosenMarker;
                int choice = Integer.parseInt(scanner.nextLine());

                if(choice > markerList.size())
                    throw new NumberFormatException();
                chosenMarker = switch (choice) {
                    case 1 -> markerList.get(0);
                    case 2 -> markerList.get(1);
                    case 3 -> markerList.get(2);
                    case 4 -> markerList.get(3);
                    default -> throw new NumberFormatException();
                };
                client.getServer().setMarker(game.getPlayer(client.getUsername()), game.getGameId(), chosenMarker);
                break;
            }
            catch(Exception e)
            {
                ui.invalidInput();
            }


        }while(true);
    }

    public boolean waitingForMarkerTurn()
    {
        if(!game.getCurrentPlayer().getUsername().equals(client.getUsername())) {
            ui.waitingForMarkerTurn();
            return false;
        }
        return true;
    }

    private int[][] createMatrixFromField(PlayerField playerField) {
        int i = 0, j = 0;
        int[][] matrix = new int[DEFAULT_MATRIX_SIZE][DEFAULT_MATRIX_SIZE];
        for(PlayableCard[] cards: playerField.getMatrixField()) {
            j=0;
            for(PlayableCard card: cards) {
                if(card != null) {
                    matrix[i][j] = card.getCardId();
                }
                j++;
            }
            i++;
        }
        return matrix;
    }

    public void showDiscoveredCards() throws RemoteException { //in the drawingFiled
        HashMap<DrawPosition, GoldCard> temp = game.getTableTop().getDrawingField().getDiscoveredGoldCards();
        HashMap<GoldCard, CardInfo> discoveredGoldCards = new HashMap<>();
        for(DrawPosition position: temp.keySet()) {
            discoveredGoldCards.put(temp.get(position), client.getServer().getCardInfo(temp.get(position), game.getGameId()));
        }

        HashMap<DrawPosition, ResourceCard> temp2 = game.getTableTop().getDrawingField().getDiscoveredResourceCards();
        HashMap<ResourceCard, CardInfo> discoveredResourceCards = new HashMap<>();
        for(DrawPosition position: temp2.keySet()) {
            discoveredResourceCards.put(temp2.get(position), client.getServer().getCardInfo(temp2.get(position), game.getGameId()));
        }

        ui.showDiscoveredCards(discoveredGoldCards, discoveredResourceCards);
    }

    public void showEndGameScreen() {
        LinkedHashMap<String, Integer> playersPlacement = new LinkedHashMap<>();
        for(Player p : game.getListOfPlayers()) {
            playersPlacement.put(p.getUsername(), p.getPoints());
        }
        ui.showEndGameScreen(playersPlacement);
    }
    public boolean waitingForOthers()
    {
        if(!game.getListOfPlayers().getLast().getUsername().equals(client.getUsername())) {
            ui.waitingForOthers();
            return true;
        }
        return false;
    }
    public void waitingForGameBegin()
    {
        ui.waitingForGameBegin();
    }
    public void chooseStarterCardSide() throws NotExistingPlayerException, RemoteException {
        StarterCard cardFront = game.getPlayer(client.getUsername()).getStarterCard();
        StarterCard cardBack = client.getServer().getOtherSideCard(game.getGameId(), cardFront);

        ui.chooseStarterCardSide(cardFront,cardBack);
        do{
            try{
                Side side;
                int choice = Integer.parseInt(scanner.nextLine());
                switch(choice)
                {
                    case 1:
                        side = Side.FRONT;
                        break;
                    case 2:
                        side = Side.BACK;
                        break;
                    default: throw new NumberFormatException();
                }
                client.getServer().setStarterCardSide(game.getGameId(), game.getPlayer(client.getUsername()), cardFront, side);
                break;
            }
            catch(Exception e)
            {
                ui.invalidInput();
            }
        }while(true);
    }


    @Override
    public void run() {
        try {
            this.gameLoop();
        } catch (RemoteException | NotExistingPlayerException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
