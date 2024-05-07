package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.model.Player;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ViewCLI implements View {
    private Game game;
    private RMIClient client;
    private final Scanner scanner = new Scanner(System.in);
    private boolean gameStarted = false;
    TUI ui = new TUI();;

    public void setUsername() {
        ui.setUsername();
        String username = scanner.nextLine();
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
    public void gameBegin()
    {
        boolean inGame = true;
        while(inGame)
        {
            System.out.println("Waiting for input: ");
            ui.showMainScreen();
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
                    ui.showPlayerHand();
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


}