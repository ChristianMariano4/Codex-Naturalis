package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.network.messages.GameEvent;
import it.polimi.ingsw.network.messages.userMessages.CreateGameMessage;
import it.polimi.ingsw.network.messages.userMessages.UserInputEvent;
import it.polimi.ingsw.network.messages.userMessages.UserMessageWrapper;
import it.polimi.ingsw.network.rmi.RMIClient;
import it.polimi.ingsw.network.rmi.ServerRMIInterface;

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
    private final Thread threadUsername = new Thread(() -> {
        System.out.println("Insert your username: ");
        String username = scanner.nextLine();
        client.setUsername(username);
    });
    Thread threadGame = new Thread(() -> {
        while(true){
            System.out.println("Do you want to create a game or join an existing one?");
            System.out.println("1. Create a game\n2. Join a game");
            String choice = scanner.nextLine();
            if(choice.equals("1")) {
                client.createGame(this.client.getUsername());
                //System.out.println("Game created. Waiting for other players to join...");
                break;
            }
            if(choice.equals("2")) {
                List<Integer> availableGames = client.getAvailableGames();
                System.out.println("Available games:");
                for (Integer gameId : availableGames) {
                    System.out.println(gameId);
                }
                System.out.print("Enter the game id you want to join: ");
                client.joinGame(Integer.parseInt(scanner.nextLine()), this.client.getUsername());
                break;
            }
        }
    });

    Thread threadReady = new Thread( () -> {
        do{
            System.out.println("Press 1 when you are ready to start the game.");
        }while(!scanner.nextLine().equals("1"));
        int numberReadyPlayers = client.setReady();
        System.out.println("You are ready to start the game. Waiting for other players to be ready...");

        while(numberReadyPlayers < game.getListOfPlayers().size()){
            try {
                this.wait();
                //TODO thread has to be woke up when a player is ready
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    });

    public ViewCLI(RMIClient client) {
        this.client = client;
    }

    @Override
    public void update(GameEvent event, Game gameUpdated) {
        this.game = gameUpdated;
        switch (event) {
            case GAME_CREATED -> {
                System.out.println("Game created. Waiting for other players to join...");
                break;
            }
            case BOARD_UPDATED -> {
                //TODO: print what happened
                //TODO: print new board
                break;
            }
            case NEW_PLAYER -> {
                System.out.println("New player joined the game. Now there are " + game.getListOfPlayers().size() + " players.");
                break;
            }
        }
    }

    @Override
    public void run() {
        threadUsername.start();
        try {
            threadUsername.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        threadGame.start();
        try {
            threadGame.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        threadReady.start();
        try {
            threadReady.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //here the game is started

        Thread t = new Thread(() -> {
            while(true){
                //TODO: thread that handles user input for the entire game
            }
        });
        t.start();
    }

    public static void main(String[] args) {
        ViewCLI view;
        System.out.println("Connecting to RMI server...");
        String serverName = "Server";
        try {
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1234);
            ServerRMIInterface server = (ServerRMIInterface) registry.lookup(serverName);
            RMIClient client = new RMIClient(server);
            new Thread(client).start();
            view = new ViewCLI(client);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        }
        view.run();
    }
}
