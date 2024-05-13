package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.enumerations.ClientMessageType;
import it.polimi.ingsw.enumerations.ServerMessageType;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.network.client.ClientHandlerInterface;
import it.polimi.ingsw.network.messages.GameEvent;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;
import it.polimi.ingsw.network.server.Server;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;

import static java.lang.Thread.sleep;

//oggetto che fa da interfaccia con il client
//tramite cui mando i miei updates al client (come lo stub del client in RMI)
public class SocketClientHandler implements Runnable, ClientHandlerInterface {

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Server server;
    private String username = null;

    public SocketClientHandler(Socket socket, Server server) throws IOException {

        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.server = server;
    }


    @Override
    public void run() {
        try {
           while(true)
           {
               ClientMessage message = (ClientMessage) inputStream.readObject();
               parseMessage(message);
           }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    private void parseMessage(ClientMessage message) throws IOException {
        ClientMessageType messageType = message.getMessageType();
        switch(messageType)
        {
            case SET_USERNAME ->
            {
                this.username = (String) message.getMessageContent()[0];
            }
            case CREATE_GAME ->
            {
                sendMessage(ServerMessageType.GAME_CREATED ,server.createGame(this));
            }
            case SUBSCRIBE ->
            {
                server.subscribe(this, (int) message.getMessageContent()[0]);
            }
            case ADD_PLAYER ->
            {
                sendMessage(ServerMessageType.PLAYER_ADDED ,server.addPlayerToGame((int) message.getMessageContent()[0], (String) message.getMessageContent()[1], this));
            }
            case CHECK_USERNAME ->
            {
                sendMessage(ServerMessageType.USERNAME_CHECK_RESULT, server.checkUsername((String) message.getMessageContent()[0]));
            }

        }
    }
    public void sendMessage(ServerMessageType messageType, Object messageContent) throws IOException {

        outputStream.writeObject(new ServerMessage(messageType, messageContent));
    }



    @Override
    public void update(GameEvent event, Object gameUpdate) throws RemoteException, InterruptedException, NotExistingPlayerException {

    }

    @Override
    public String getUsername() throws IOException {
        return this.username;
    }
}
