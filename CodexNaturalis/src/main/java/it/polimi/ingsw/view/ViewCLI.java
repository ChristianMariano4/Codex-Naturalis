package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.messages.GameEvent;
import it.polimi.ingsw.network.messages.userMessages.UserInputEvent;
import it.polimi.ingsw.network.messages.userMessages.UserMessage;
import it.polimi.ingsw.network.messages.userMessages.UserMessageWrapper;
import it.polimi.ingsw.network.messages.userMessages.UsernameMessage;

import java.util.Scanner;

public class ViewCLI {
    private Client client;
    private Game game;
    private String username;
    private final Scanner scanner = new Scanner(System.in);

    public void insertUsername(){
        System.out.println("Insert your username: ");
        username = scanner.nextLine();
        UserMessageWrapper message = new UserMessageWrapper(UserInputEvent.USERNAME_INSERTED, new UsernameMessage(username));
        client.getEventManager().notify(message);
    }

//    Thread threadUsername = new Thread(() -> {
//        System.out.println("Insert your username: ");
//        username = scanner.nextLine();
//        client.getEventManager().notify(UserInputEvent.USERNAME_INSERTED, username);
//    });

    public void choiceGame(){
        //TODO: user choices if he wants to create a new game or join an existing one
    }
    public ViewCLI(String serverAddress) {
        client = new Client(serverAddress);
        client.connect();
    }

    public void update(Game message, GameEvent event){
        game = message;
        //TODO: view updated based on the event received
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static void main(String[] args) {
        String serverAddress = "localhost";
        ViewCLI view = new ViewCLI(serverAddress);
        view.insertUsername();
    }
}
