package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.enumerations.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.network.client.Client;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static it.polimi.ingsw.model.GameValues.DEFAULT_MATRIX_SIZE;

public class ViewCLI implements View, Runnable {
    private Game game;
    private Client client;
    private final Scanner scanner = new Scanner(System.in);
    private String playerId;
    TUI ui = new TUI();;


    public void setUsername() throws IOException, InterruptedException, ServerDisconnectedException {

        ui.setUsername();
        String username = null;
        do {
            username = scanner.nextLine();

            if(client.checkUsername(username))
                break;
            else
                System.out.println("Username already in use, try again: ");
        }while(true);
        client.setUsername(username);
        playerId = username;

    }
    public boolean setChoiceGame() throws ServerDisconnectedException {
        do
        {
            try {
                ui.createOrJoinAGame();
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice == 1) {
                    this.game = client.createGame(this.client.getUsername());
                    System.out.println("Game created.");
                    break;
                }
                else if (choice == 2) {
                    ui.showAllExistingGames(client.getAvailableGames());
                    this.game = client.joinGame(Integer.parseInt(scanner.nextLine()), this.client.getUsername());
                    break;
                }
                else if(choice == 0)
                {
                    return false;
                }
                else
                    throw new NumberFormatException();
            }
            catch(ServerDisconnectedException se)
            {
                throw se;
            }

            catch(Exception e)
            {
                ui.invalidInput();
            }
        }while(true);

        return true;
    }

    public void setReady() throws ServerDisconnectedException{
        do{
            //TODO: remove println from view methods
            try {
                System.out.println("Press 1 when you are ready to start the game.");
                int choice = Integer.parseInt(scanner.nextLine());
                if(choice == 1) {
                    try {
                        client.setReady();
                        break;

                    }
                    catch(ServerDisconnectedException se)
                    {
                        throw se;
                    }
                    catch (NotEnoughPlayersException e) {
                        ui.notEnoughPlayers();
                    }
                }
                else {
                    throw new NumberFormatException();
                }
            }
            catch(ServerDisconnectedException se)
            {
                throw se;
            }
            catch (Exception e)
            {
                ui.invalidInput();
            }
        }while(true);

        System.out.println("You are ready to start the game. Waiting for other players to be ready...");
    }

    public ViewCLI(Client client) {

        this.client = client;
    }

    @Override
    public void boardUpdate(Game gameUpdated) {
        //these three methods call ui methods and display things
    }

    @Override
    public void newPlayer(Game gameUpdated) {

    }

    @Override
    public void update(Game gameUpdated) {
        this.game = gameUpdated;
    }

    public void gameLoop() throws IOException, NotExistingPlayerException, InterruptedException, CardTypeMismatchException, ServerDisconnectedException {
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
            if(!game.getIsGameEnded()) {
                ui.showTurnScreen(playerPlaying, client.getUsername());
            }
            else
            {
                showEndGameScreen();
            }

            while(!Thread.interrupted()) {
                String command = null;
                if(!blockingQueue.isEmpty()) {
                    synchronized (lock) {
                        command = blockingQueue.take();
                        if(game.getIsGameEnded())
                        {
                            readerThread.interrupt();
                            lock.notifyAll();
                            return;
                        }

                        switch (command) {
                            case "q", "quit":
                                inGame = false;
                                break;
                            case "showPlayers":
                                showAllPlayers();
                                break;
                            case "myHand":
                                showPlayerHand();
                                break;
                            case "showMyField":
                                showPlayerField();
                                break;
                            case "showOtherField":
                                showOtherPlayerField();
                                break;
                            case "scoreboard":
                                showScoreboard();
                                break;
                            case "showSharedObjectiveCards":
                                showSharedObjectiveCards();
                                break;
                            case "showSecretObjectiveCard":
                                showSecretObjectiveCard();
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
                                try
                                {
                                    int cardId = Integer.parseInt(command);
                                    PlayableCard cardFront = client.getPlayableCardById(game.getGameId(), cardId);
                                    PlayableCard cardBack = client.getOtherSideCard(game. getGameId(), cardFront);
                                    ui.showCardInfo(cardFront, client.getCardInfo(cardFront, game.getGameId()));
                                    ui.showCardInfo(cardBack, client.getCardInfo(cardBack, game.getGameId()));

                                }
                                catch(ServerDisconnectedException se)
                                {
                                    throw se;
                                }
                                catch (Exception e)
                                {
                                    ui.commandNotFound();
                                }
                                break;
                        }
                        lock.notifyAll();
                    }
                }
            }
            Thread.sleep(1);
        }
    }

    private void showScoreboard() {
        ui.scoreboardTitle();
        for(Player player : game.getListOfPlayers()) {
            ui.showPoints(player);
        }
    }
    private void notYourTurn()
    {
        ui.notYourTurn(game.getCurrentPlayer().getUsername());
    }
    private void endTurn() throws NotExistingPlayerException, IOException, CardTypeMismatchException, ServerDisconnectedException {
        client.endTurn(game.getGameId(), client.getUsername());
    }
    private void drawCard() throws IOException, ServerDisconnectedException {
        HashMap<DrawPosition, GoldCard> discoveredGoldCards =  game.getTableTop().getDrawingField().getDiscoveredGoldCards();
        HashMap<DrawPosition, ResourceCard> discoveredResourceCards =  game.getTableTop().getDrawingField().getDiscoveredResourceCards();

        HashMap<GoldCard, CardInfo> uiDiscoveredGoldCards = new HashMap<>();
        HashMap<ResourceCard, CardInfo> uiDiscoveredResourceCards = new HashMap<>();
        for(GoldCard card: discoveredGoldCards.values())
        {
            uiDiscoveredGoldCards.put(card, client.getCardInfo(card, game.getGameId()));
        }

        for(ResourceCard card: discoveredResourceCards.values())
        {
            uiDiscoveredResourceCards.put(card, client.getCardInfo(card, game.getGameId()));
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
                client.drawCard(game.getGameId(), client.getUsername(), cardType, drawPosition);
                break;
            }
            catch(ServerDisconnectedException se)
            {
                throw se;
            }
            catch(Exception e)
            {
                ui.invalidInput();
            }
        }while(true);
    }
    private void playCard() throws IOException, NotExistingPlayerException, ServerDisconnectedException {
        showPlayerHand();
        showPlayerField();
        do {
            try {
                ui.chooseCardToPlay();
                int cardId = Integer.parseInt(scanner.nextLine());
                PlayableCard card = game.getPlayer(client.getUsername()).getPlayerHand().getCardById(cardId);
                ui.chooseSide();
                int side = Integer.parseInt(scanner.nextLine());
                switch(side)
                {
                    case 1:
                        break;
                    case 2:
                        card = client.getOtherSideCard(game.getGameId(), card);
                        break;
                    default:
                        throw new NumberFormatException();
                }
                ui.chooseWhereToPlay();
                int cardIdOnBoard = Integer.parseInt(scanner.nextLine());
                PlayableCard cardOnBoard;
                do {
                    cardOnBoard = game.getPlayer(client.getUsername()).getPlayerField().getCardById(cardIdOnBoard);
                    ui.showCardInfo(cardOnBoard, client.getCardInfo(cardOnBoard, game.getGameId()));
                    ui.areYouSure();
                    String newChoice = scanner.nextLine();
                    if(newChoice.equals("y"))
                        break;
                    cardIdOnBoard = Integer.parseInt(newChoice);
                }while(true);

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
                client.playCard(game.getGameId(),client.getUsername(), cardOnBoard, card , orientation);
                break;

            }
            catch(ServerDisconnectedException se)
            {
                throw se;
            }
            catch(Exception e)
            {
                ui.invalidInput();
            }
        } while(true);

    }
    public void showSecretObjectiveCard() throws NotExistingPlayerException, IOException, ServerDisconnectedException {
        ObjectiveCard card = game.getPlayer(client.getUsername()).getSecretObjective();
        ui.showCardInfo(card, client.getCardInfo(card, game.getGameId()));
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
    public void showSharedObjectiveCards() throws IOException, ServerDisconnectedException {
        HashMap<ObjectiveCard, CardInfo> sharedObjectiveCards = new HashMap<>();
        ArrayList<ObjectiveCard> objectiveCards = game.getTableTop().getSharedObjectiveCards();
        sharedObjectiveCards.put(objectiveCards.get(0), client.getCardInfo(objectiveCards.get(0), game.getGameId()));
        sharedObjectiveCards.put(objectiveCards.get(1), client.getCardInfo(objectiveCards.get(1), game.getGameId()));

        ui.showSharedObjectiveCard(sharedObjectiveCards);
    }
    public void chooseObjectiveCard(ArrayList<ObjectiveCard> objectiveCardsToChoose) throws ServerDisconnectedException {
            ui.secretObjectiveCardTitle();
            try {
                ui.showCardInfo(objectiveCardsToChoose.get(0), client.getCardInfo(objectiveCardsToChoose.get(0), game.getGameId()));
                ui.showCardInfo(objectiveCardsToChoose.get(1), client.getCardInfo(objectiveCardsToChoose.get(1), game.getGameId()));
                do {
                    try {
                        ui.chooseSecretObjectiveCard();
                        int choice = Integer.parseInt(scanner.nextLine());
                        ObjectiveCard chosenObjectiveCard = null;
                        switch (choice) {
                            case 1:
                                chosenObjectiveCard = objectiveCardsToChoose.get(0);
                                break;
                            case 2:
                                chosenObjectiveCard = objectiveCardsToChoose.get(1);
                                break;
                            default:
                                throw new NumberFormatException();
                        }
                        if (chosenObjectiveCard == null)
                            throw new CardNotFoundException();
                        client.setSecretObjectiveCard(game.getGameId(), game.getPlayer(client.getUsername()), chosenObjectiveCard);
                        break;
                    }
                    catch(ServerDisconnectedException se)
                    {
                        throw se;
                    }
                    catch(Exception e)
                    {
                        ui.invalidInput();
                    }
                }while(true);
             }
            catch(ServerDisconnectedException se)
            {
                throw se;
            }
        catch (Exception e)
        {
            ui.somethingWentWrong();
        }
    }

    public void showPlayerHand() throws IOException, NotExistingPlayerException, ServerDisconnectedException {
        LinkedHashMap<PlayableCard, CardInfo> cardsInHand = new LinkedHashMap<>();

        for(PlayableCard card: game.getPlayer(client.getUsername()).getPlayerHand().getCardsInHand()) {
            CardInfo cardInfo = client.getCardInfo(card, game.getGameId());
            cardsInHand.put(card, cardInfo);
            PlayableCard otherSide = client.getOtherSideCard(game.getGameId(), card);
            cardsInHand.put(otherSide, client.getCardInfo(otherSide, game.getGameId()));
        }
        ui.showPlayerHand(cardsInHand);
    }

    public void showPlayerField() throws RemoteException, NotExistingPlayerException {
        ui.showPlayerField(createMatrixFromField(game.getPlayer(client.getUsername()).getPlayerField()));
    }

    public void showOtherPlayerField() {
        this.showAllPlayers();
        ui.playerFiledChoice();

        Player player;
        do {
            try {
                String username = scanner.nextLine();
                player = game.getListOfPlayers().stream().filter(p1 -> p1.getUsername().equals(username)).findFirst().orElseThrow();
                break;
            }
            catch(Exception e)
            {
                ui.wrongUsername();
            }
        } while(true);

        ui.showPlayerField(createMatrixFromField(player.getPlayerField()));
    }
    public void markerSelection() throws ServerDisconnectedException {
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
                client.setMarker(game.getPlayer(client.getUsername()), game.getGameId(), chosenMarker);
                break;
            }
            catch(ServerDisconnectedException se)
            {
                throw se;
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
    public void chooseStarterCardSide() throws NotExistingPlayerException, IOException, ServerDisconnectedException {
        StarterCard cardFront = game.getPlayer(client.getUsername()).getStarterCard();
        StarterCard cardBack = client.getOtherSideCard(game.getGameId(), cardFront);

        ui.chooseStarterCardSide(cardFront,cardBack, client.getCardInfo(cardFront, game.getGameId()), client.getCardInfo(cardBack, game.getGameId()));
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
                client.setStarterCardSide(game.getGameId(), game.getPlayer(client.getUsername()), cardFront, side);
                break;
            }
            catch(ServerDisconnectedException se)
            {
                throw se;
            }
            catch(Exception e)
            {
                ui.invalidInput();
            }
        }while(true);
    }

    public void finalRound()
    {
        ui.finalRound();
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
        } catch (CardTypeMismatchException e) {
            throw new RuntimeException(e);
        } catch (ServerDisconnectedException e) {
            return;
        }
    }
}
