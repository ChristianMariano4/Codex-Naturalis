package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameValues;
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
import java.util.List;
import java.util.Scanner;

public class ViewCLI implements View {
    private Game game;
    private RMIClient client;
    private final Scanner scanner = new Scanner(System.in);
    private boolean gameStarted = false;
    TUI ui;

    private void setUsername() {
        ui.setUsername();
        String username = scanner.nextLine();
        client.setUsername(username);
    }
    private void setChoiceGame() {
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
        System.out.println(this);
    }

    private void setReady() {
        do{
            System.out.println("Press 1 when you are ready to start the game.");
        }while(!scanner.nextLine().equals("1"));
        int numberReadyPlayers = client.setReady();
        System.out.println("You are ready to start the game. Waiting for other players to be ready...");
        System.out.println(this);
    }

    public ViewCLI(RMIClient client) {
        this.client = client;
    }

    @Override
    public void update(GameEvent event, Game gameUpdated) {
        System.out.println("We are in ViewCLI update method");
        System.out.println(gameUpdated.getGameId());
        this.game = gameUpdated;
        switch (event) {
            case BOARD_UPDATED -> {
                //TODO: print what happened
                //TODO: print new board
                break;
            }
            case NEW_PLAYER -> {
                System.out.println("New player joined the game. Now there are " + game.getListOfPlayers().size() + " players.");
                break;
            }
            case GAME_BEGIN -> {
                System.out.println("The game is starting. Good luck!");
                gameStarted = true;
                break;
            }
        }
    }

    @Override
    public void run() {

        ui = new TUI();
        ui.showGameTitle();
        ui.showPlayerHand();
/*
        setUsername(); //TODO
        ui.clearScreen();
        setChoiceGame();
        setReady();

        while(!gameStarted){
        }

        while(gameStarted){
            System.out.println("Game Started");
            //TODO
        }*/

        ui.showEndGameScreen();

    }


}
