package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.CardNotImportedException;
import it.polimi.ingsw.exceptions.CardTypeMismatchException;
import it.polimi.ingsw.exceptions.DeckIsEmptyException;
import it.polimi.ingsw.exceptions.InvalidConstructorDataException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.network.messages.userMessages.UserInputEvent;
import it.polimi.ingsw.network.messages.userMessages.UserMessage;
import it.polimi.ingsw.network.messages.userMessages.UserMessageWrapper;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class Server {


    private final Map<Integer, Game> games;
    private List<Client> clients;
    private Controller controller;
    public static ServerEventManager serverEventManager = new ServerEventManager();
    private String SERVER_ADDRESS = GameValues.SERVER_ADDRESS;
    private ServerSocket serverSocket;


    public Server()
    {
        this.clients = new ArrayList<>();
        this.controller = new Controller(this, serverEventManager);
        this.games = new HashMap<>();
    }

    /**
     * Returns the game with the specified gameId
     * @param gameId the id of the game
     * @return the game with the specified gameId
     */
    public Game getGameById(int gameId)
    {
        return games.get(gameId);
    }

    /**
     * Adds a game to the server
     * @param game the game to be added
     */
    private void addGame(Game game)
    {
        games.put(game.getGameId(), game);
    }
    public Client getClient(String username){
        return clients.stream().filter(c -> c.getUsername().equals(username)).findFirst().orElse(null);
    }
    public void addClient(Client client){
        clients.add(client);
    }

//    public void delClient(Client client){
//        clients.remove(client);
//    }

    public void update(UserMessageWrapper message){
        controller.update(message);
    }

    public void startSocketServer(){
        //TODO: implement
//        try {
//            serverSocket = new ServerSocket(GameValues.SERVER_PORT);
//            while(true){
//                Socket clientSocket = serverSocket.accept();
//                Client client = new Client();
//                client.setClientSocket(clientSocket);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    public void sendTo(Socket clientSocket, Serializable message){
        ObjectOutputStream output;
        try {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
//        Server server = new Server();
//        Controller controller = new Controller(server);
//        try {
//            Game game = controller.createGame();
//            server.addGame(game);
//        } catch (InvalidConstructorDataException | CardTypeMismatchException | CardNotImportedException |
//                 DeckIsEmptyException e) {
//            throw new RuntimeException(e);
//        }
    }

}
