package it.polimi.ingsw.network;

import it.polimi.ingsw.enumerations.AngleOrientation;
import it.polimi.ingsw.enumerations.CardType;
import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.exceptions.NotExistingPlayerException;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.view.TUI;

import java.rmi.RemoteException;
import java.util.*;

import static it.polimi.ingsw.model.GameValues.DEFAULT_MATRIX_SIZE;

public class ViewCLI implements View, Runnable {
    private Game game;
    private RMIClient client;
    private final Scanner scanner = new Scanner(System.in);
    private boolean gameStarted = false;

    private String playerId;
    TUI ui = new TUI();;

    public void setUsername() {
        ui.setUsername();
        String username = scanner.nextLine();
        playerId = username;
        client.setUsername(username);
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
    public void gameLoop() throws RemoteException, NotExistingPlayerException {
        boolean inGame = true;
        ui.showMainScreen();
        while(inGame)
        {
            boolean isTurn = game.getPlayer(client.getUsername()).getIsTurn();
            System.out.println("Waiting for input: ");
            ui.showMainChoices();
            String command = scanner.nextLine();
            switch(command){
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
                    if(isTurn) {
                        playCard();
                        drawCard();
                        endTurn();
                    }
                    else
                        notYourTurn();
                    break;

                default:
                    ui.commandNotFound();
                    break;
            }

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
                ui.showPlayableCardInfo(cardOnBoard, client.getServer().getCardInfo(cardOnBoard, game.getGameId()));
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
                client.getServer().playCard(game.getGameId(),client.getUsername(), card, cardOnBoard, orientation);
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

    private int[][] createMatrixFromField(PlayerField  playerField) {
        int i = 0, j = 0;
        int[][] matrix = new int[DEFAULT_MATRIX_SIZE][DEFAULT_MATRIX_SIZE];
        for(PlayableCard[] cards: playerField.getMatrixField()) {
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


    @Override
    public void run() {
        try {
            this.gameLoop();
        } catch (RemoteException | NotExistingPlayerException e) {
            throw new RuntimeException(e);
        }
    }
}
