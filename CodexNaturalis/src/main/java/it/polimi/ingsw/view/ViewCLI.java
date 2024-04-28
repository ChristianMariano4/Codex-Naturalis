package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.messages.userMessages.UserInputEvent;

import java.util.Scanner;

public class ViewCLI {
    private Client client;
    private Game game;
    private final Scanner scanner = new Scanner(System.in);

    Thread threadUsername = new Thread(() -> {
        System.out.println("Insert your username: ");
        String username = scanner.nextLine();
        client.getEventManager().notify(UserInputEvent.USERNAME_INSERTED, username);
    });

    public ViewCLI(String serverAddress) {
        client = new Client(serverAddress);
        client.connect();
        threadUsername.start();
    }

    public static void main(String[] args) {
        String serverAddress = args[1];
       // ViewCLI view = new ViewCLI();
    }
}
