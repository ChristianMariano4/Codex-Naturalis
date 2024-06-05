package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.enumerations.ClientMessageType;
import it.polimi.ingsw.exceptions.NotExistingPlayerException;
import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import it.polimi.ingsw.network.client.SocketClient;
import it.polimi.ingsw.network.messages.GameEvent;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SocketClientMessageHandler implements Runnable {

    private final SocketClient client;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private ErrorAwareQueue messageQueue;
    public SocketClientMessageHandler(SocketClient client, ObjectInputStream inputStream, ObjectOutputStream outputStream, ErrorAwareQueue messageQueue)
    {
        this.client = client;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.messageQueue = messageQueue;
    }

    public void sendMessage(ClientMessageType messageType, Object ... messageContent) throws IOException, ServerDisconnectedException {
        synchronized (this) {
            try {
                outputStream.reset();
                outputStream.writeObject(new ClientMessage(messageType, messageContent));
            } catch (Exception e) {
                throw new ServerDisconnectedException();
            }
        }

    }
    @Override
    public void run() {
        while(true)
        {
            try {
                ServerMessage message = (ServerMessage) this.inputStream.readObject();
                parseMessage(message);


            } catch (IOException | ClassNotFoundException | InterruptedException | NotExistingPlayerException e) {
                try {
                    inputStream.close();
                    outputStream.close();
                } catch (IOException ex) {
                    break;
                }
                break;
            }
        }
    }
    private void parseMessage(ServerMessage message) throws InterruptedException, IOException, NotExistingPlayerException {
            synchronized (this) {
                switch (message.getMessageType()) {
                    case UPDATE -> {
                        client.update((GameEvent) message.getMessageContent()[0], message.getMessageContent()[1]);
                    }
                    case SUCCESS -> {
                        messageQueue.put(message.getMessageContent()[0]);
                    }
                    case ERROR -> {
                        messageQueue.put(message.getMessageContent()[0]);
                    }
                    case GAME_CREATED -> {
                        messageQueue.put(message.getMessageContent()[0]);
                    }
                    case PLAYER_ADDED -> {
                        messageQueue.put(message.getMessageContent()[0]);
                    }
                    case USERNAME_CHECK_RESULT -> {
                        messageQueue.put(message.getMessageContent()[0]);
                    }
                    case AVAILABLE_GAMES -> {
                        messageQueue.put(message.getMessageContent()[0]);
                    }
                    case CARD_INFO -> {
                        messageQueue.put(message.getMessageContent()[0]);
                    }
                    case OTHER_SIDE_STARTER -> {
                        messageQueue.put(message.getMessageContent()[0]);
                    }
                    case OTHER_SIDE_PLAYABLE -> {
                        messageQueue.put(message.getMessageContent()[0]);
                    }
                    case PLAYED_CARD_SUCCESS ->
                    {
                        messageQueue.put(message.getMessageContent()[0]);
                    }
                    case READY_STATUS ->
                    {
                        messageQueue.put(message.getMessageContent()[0]);
                    }
                    case REQUESTED_CARD ->
                    {
                        messageQueue.put(message.getMessageContent()[0]);
                    }

                }
            }

    }

}
