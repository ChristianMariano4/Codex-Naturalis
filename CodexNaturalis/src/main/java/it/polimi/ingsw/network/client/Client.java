package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameValues;
import it.polimi.ingsw.network.EventManager;
import it.polimi.ingsw.network.messages.EventWrapper;
import it.polimi.ingsw.network.messages.GameEvent;
import it.polimi.ingsw.network.messages.userMessages.UserInputEvent;
import it.polimi.ingsw.network.messages.userMessages.UserMessageWrapper;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    private String username;
    protected static String SERVER_ADDRESS = GameValues.SERVER_ADDRESS;
    protected static int SERVER_PORT = GameValues.SERVER_PORT;
    private Socket serverSocket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    public Thread serverListener;
    protected Socket clientSocket;
    private EventManager eventManager;
    private View view;

    public Client(String serverAddress) {
        this.eventManager = new EventManager();
        SERVER_ADDRESS = serverAddress;
        eventManager.subscribe(UserInputEvent.class, new UserInputListener(this));
    }

    public void connect() {
        try {
            this.serverSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            output = new ObjectOutputStream(serverSocket.getOutputStream());

            serverListener = new SocketServerListener(serverSocket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startServerListener(){
        serverListener.start();
    }

    public void setView(View view) {
        this.view = view;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    class SocketServerListener extends Thread {
        private final Socket serverSocket;

        public SocketServerListener(Socket socket) {
            this.serverSocket = socket;
        }

        public void run() {
            try {
                while (true) {
                    input = new ObjectInputStream(serverSocket.getInputStream());
                    EventWrapper ew = (EventWrapper) input.readObject();
                    if (ew == null) {
                        break;
                    }

                    Client.this.update(ew.getMessage(), ew.getType());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update(Game argument, GameEvent event){
        view.update(argument, event);
    }

    public void run() {
        view.run();
    }

    public void updateServer(UserMessageWrapper message) {
        try {
            output.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object updateServerPreGame(UserMessageWrapper message) {
        try {
            output.writeObject(message);
            input = new ObjectInputStream(serverSocket.getInputStream());
            return input.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsername() {
        return username;
    }

    public Socket getServerSocket() {
        return serverSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
}
