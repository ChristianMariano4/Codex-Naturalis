package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.enumerations.ClientMessageType;
import it.polimi.ingsw.exceptions.ServerDisconnectedException;
import it.polimi.ingsw.network.client.SocketClient;
import it.polimi.ingsw.network.messages.GameEvent;
import it.polimi.ingsw.network.messages.ClientMessage;
import it.polimi.ingsw.network.messages.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * Client side Class used to receive messages and sent messages to the Server
 * Each SocketClient has its own thread of SocketClientMessageHandler
 */
public class SocketClientMessageHandler implements Runnable {
    private final SocketClient client;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final ErrorAwareQueue messageQueue;

    /**
     * Constructor of the SocketClientMessageHandler class
     * @param client the SocketClient
     * @param inputStream the ObjectInputStream
     * @param outputStream the ObjectOutputStream
     * @param messageQueue the ErrorAwareQueue
     */
    public SocketClientMessageHandler(SocketClient client, ObjectInputStream inputStream, ObjectOutputStream outputStream, ErrorAwareQueue messageQueue)
    {
        this.client = client;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.messageQueue = messageQueue;
    }
    /**
     * This sends a message to the Server through the ObjectOutputStream
     *
     * @param messageType the type of client message to be sent
     * @param messageContent the content of the message to be sent
     * @throws IOException when the message can't be sent through the ObjectOutputStream
     */
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
    /**
     * This runs the instance of SocketClientMessageHandler and waits for incoming messages
     */
    @Override
    public void run() {
        while(true)
        {
            try {
                ServerMessage message = (ServerMessage) this.inputStream.readObject();
                parseMessage(message);
            } catch (IOException | ClassNotFoundException | InterruptedException e) {
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

    /**
     * After receiving a message the message type is interpreted and used to choose to either update the SocketClient or to put the message in the queue
     *
     * @param message the message received containing both its type and content
     * @throws InterruptedException thrown by superclass
     * @throws IOException when a reply can't be sent back
     */
    private void parseMessage(ServerMessage message) throws InterruptedException, IOException {
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
