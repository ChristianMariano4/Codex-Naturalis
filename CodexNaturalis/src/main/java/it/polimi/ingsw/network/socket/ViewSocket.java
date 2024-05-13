//package it.polimi.ingsw.network;
//
//import it.polimi.ingsw.model.Game;
//import it.polimi.ingsw.model.GameValues;
//import it.polimi.ingsw.network.messages.GameEvent;
//import it.polimi.ingsw.network.messages.userMessages.CreateGameMessage;
//import it.polimi.ingsw.network.messages.userMessages.UserInputEvent;
//import it.polimi.ingsw.network.messages.userMessages.UserMessageWrapper;
//import it.polimi.ingsw.network.Client.Client;
//import it.polimi.ingsw.network.rmi.ServerRMIInterface;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.Socket;
//import java.rmi.NotBoundException;
//import java.rmi.RemoteException;
//import java.rmi.registry.LocateRegistry;
//import java.rmi.registry.Registry;
//import java.util.Scanner;
//
//public class ViewCLI implements View {
//    private String username;
//    private Game game;
//    private Client client;
//    private final Scanner scanner = new Scanner(System.in);
//    private Socket socket;
//    private ObjectInputStream input;
//    private ObjectOutputStream output;
//    static boolean isRMI;
//    private final Thread threadUsername = new Thread(() -> {
//        System.out.println("Insert your username: ");
//        username = scanner.nextLine();
//    });
//    Thread threadGame = new Thread(() -> {
//        if(isRMI){
//            while(true){
//                System.out.println("Do you want to create a game or join an existing one?");
//                System.out.println("1. Create a game\n2. Join a game");
//                String choice = scanner.nextLine();
//                if(choice.equals("1")) {
//                    client.createGame(username);
//                    System.out.println("Game created. Waiting for other players to join...");
//                    break;
//                }
//                if(choice.equals("2")) {
//                    System.out.println("Enter the game id");
//                    client.joinGame(Integer.parseInt(scanner.nextLine()), username);
//                    break;
//                }
//            }
//        } else { //socket connection
//            while(true){
//                System.out.println("Do you want to create a game or join an existing one?");
//                System.out.println("1. Create a game\n2. Join a game");
//                String choice = scanner.nextLine();
//                if(choice.equals("1")) {
//                    try {
//                        output.writeObject(new UserMessageWrapper(UserInputEvent.CREATE_GAME, new CreateGameMessage(username)));
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    System.out.println("Game created. Waiting for other players to join...");
//                    break;
//                }
//                if(choice.equals("2")) {
//                    //TODO: implement join game
//                    System.out.println("Enter the game id");
//                    break;
//                }
//            }
//        }
//
//    });
//
//    public ViewCLI(Client client/*, boolean isRMI*/) {
//        this.client = client;
//        //this.isRMI = isRMI;
//    }
//
//    public ViewCLI(Socket socket, ObjectInputStream input, ObjectOutputStream output) {
//        this.socket = socket;
//        this.input = input;
//        this.output = output;
//    }
//
//    @Override
//    public void update(GameEvent event, Game gameUpdated) {
//        this.game = gameUpdated;
//        switch (event) {
//            case BOARD_UPDATED -> {
//                //TODO: print what happened
//                //TODO: print new board
//                break;
//            }
//            case NEW_PLAYER -> {
//                System.out.println("New player joined the game. Now there are " + game.getListOfPlayers().size() + " players.");
//                break;
//            }
//        }
//    }
//
//    @Override
//    public void run() {
//        threadUsername.start();
//        try {
//            threadUsername.join();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        threadGame.start();
//        try {
//            threadGame.join();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static void main(String[] args) {
//        System.out.println("Do you want to use RMI or Socket connection?\n1->RMI\n2->Socket");
//        if(new Scanner(System.in).nextLine().equals("1")){
//            isRMI = true;
//        } else {
//            isRMI = false;
//        }
//        ViewCLI view;
//        if(isRMI){
//            System.out.println("Connecting to RMI server...");
//            String serverName = "Server";
//            try {
//                Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1234);
//                ServerRMIInterface server = (ServerRMIInterface) registry.lookup(serverName);
//                Runnable client = new Client(server);
//                new Thread(client).start();
//                view = new ViewCLI((Client) client);
//            } catch (RemoteException e) {
//                throw new RuntimeException(e);
//            } catch (NotBoundException e) {
//                throw new RuntimeException(e);
//            }
//        } else {
//            Socket socket = null;
//            try {
//                System.out.println("Connecting to Socket server...1");
//                socket = new Socket(GameValues.SERVER_ADDRESS, GameValues.SERVER_PORT+1);
//                System.out.println("Connecting to server...2");
//                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
//                System.out.println("Connecting to server...3");
//                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
//                System.out.println("Connecting to server...4");
//                view = new ViewCLI(socket, input, output);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        System.out.println("Connecting to server...5");
//        view.run();
//    }
//}
