package it.polimi.ingsw.network;

import it.polimi.ingsw.enumerations.DrawPosition;
import it.polimi.ingsw.enumerations.PositionalType;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.network.messages.GameEvent;
import it.polimi.ingsw.network.messages.userMessages.CreateGameMessage;
import it.polimi.ingsw.network.messages.userMessages.UserInputEvent;
import it.polimi.ingsw.network.messages.userMessages.UserMessageWrapper;
import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.network.rmi.ServerRMIInterface;
import it.polimi.ingsw.view.TUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

import static it.polimi.ingsw.model.GameValues.DEFAULT_MATRIX_SIZE;

public class ViewCLI implements View {
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
        client.setPlayer(game.getListOfPlayers().stream().filter(p1 -> p1.getUsername().equals(playerId)).findFirst().orElse(null));
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
    public void gameBegin() throws RemoteException {
        boolean inGame = true;
        ui.showMainScreen();

        while(inGame)
        {
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
                case "showMyFiled":
                    showPlayerField();
                    break;
                default:
                    ui.commandNotFound();
                    break;
            }

        }
    }
    private void showAllPlayers()
    {
        ArrayList<String> usernames = new ArrayList<>();
        for(Player player : game.getListOfPlayers())
            usernames.add(player.getUsername());
        ui.showAllPlayers(usernames);
    }

    public void showPlayerHand() throws RemoteException {
        HashMap<PlayableCard, CardInfo> cardsInHand = new HashMap<>();
        for(PlayableCard card: client.getPlayer().getPlayerHand().getCardsInHand()) {
            CardInfo cardInfo = client.getServer().getCardInfo(card, game.getGameId());
            cardsInHand.put(card, cardInfo);
        }
        ui.showPlayerHand(cardsInHand);
    }

    public void showPlayerField() throws RemoteException {
        ui.showPlayerField(createMatrixFromField(client.getPlayer().getPlayerField()));
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

}
