//package it.polimi.ingsw.network.maybeUseful.client;
//
//import it.polimi.ingsw.model.Game;
//import it.polimi.ingsw.model.GameValues;
//import it.polimi.ingsw.network.EventManager;
//import it.polimi.ingsw.network.messages.EventWrapper;
//import it.polimi.ingsw.network.messages.GameEvent;
//import it.polimi.ingsw.network.messages.userMessages.UserInputEvent;
//import it.polimi.ingsw.network.messages.userMessages.UserMessageWrapper;
//import it.polimi.ingsw.network.View;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.Socket;
//
//public class Client {
//
//    private String username;
//    private static String SERVER_ADDRESS = GameValues.SERVER_ADDRESS;
//    private static int SERVER_PORT = GameValues.SERVER_PORT;
//    private Socket serverSocket;
//    private ObjectOutputStream output;
//    private ObjectInputStream input;
//    //private Thread serverListener;
//    private Socket clientSocket;
//    private EventManager eventManager;
//    private View view;
//
//    public Client(String serverAddress) {
//        this.eventManager = new EventManager();
//        SERVER_ADDRESS = serverAddress;
//        eventManager.subscribe(UserInputEvent.class, new UserInputListener(this));
//    }
//
//    public Client(){
//        this.eventManager = new EventManager();
//    }
//
//    public void connect() {
//        try {
//            this.serverSocket = new Socket(SERVER_ADDRESS, SERVER_PORT);
//            output = new ObjectOutputStream(serverSocket.getOutputStream());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void startListening() {
//        try {
//            while(true) {
//                try {
//                    input = new ObjectInputStream(serverSocket.getInputStream());
//                    EventWrapper ew = (EventWrapper) input.readObject();
//                    if(ew == null) {
//                        break;
//                    }
//
//                    this.update(ew.getMessage(), ew.getType());
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                } catch (ClassNotFoundException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//
//            serverSocket.close();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public void setView(View view) {
//        this.view = view;
//    }
//
//    public View getView() {
//        return this.view;
//    }
//
//    public EventManager getEventManager() {
//        return eventManager;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public void update(Game argument, GameEvent event){
//        view.update(event, argument);
//    }
//
//    public void updateServer(UserMessageWrapper message) {
//        try {
//            output.writeObject(message);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername() {
//        this.username = username;
//    }
//
//    public Socket getServerSocket() {
//        return serverSocket;
//    }
//
//    public Socket getClientSocket() {
//        return clientSocket;
//    }
//
//    public void setClientSocket(Socket clientSocket) {
//        this.clientSocket = clientSocket;
//    }
//}
