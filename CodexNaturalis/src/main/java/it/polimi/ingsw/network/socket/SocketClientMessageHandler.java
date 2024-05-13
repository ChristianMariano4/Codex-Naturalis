package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.enumerations.ClientMessageType;
import it.polimi.ingsw.network.client.SocketClient;
import it.polimi.ingsw.network.messages.clientMessages.ClientMessage;
import it.polimi.ingsw.network.messages.serverMessages.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.BlockingQueue;

public class SocketClientMessageHandler implements Runnable {

    private SocketClient client;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private BlockingQueue<Object> messageQueue;
    public SocketClientMessageHandler(SocketClient client, ObjectInputStream inputStream, ObjectOutputStream outputStream, BlockingQueue<Object> messageQueue)
    {
        this.client = client;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.messageQueue = messageQueue;
    }

    public void sendMessage(ClientMessageType messageType, Object ... messageContent) throws IOException {

        outputStream.writeObject(new ClientMessage(messageType, messageContent));
    }
    @Override
    public void run() {
        while(true)
        {
            try {
                ServerMessage message = (ServerMessage) this.inputStream.readObject();
                parseMessage(message);


            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void parseMessage(ServerMessage message) throws InterruptedException, IOException {
        switch(message.getMessageType())
        {
            case GAME_CREATED ->
            {
                messageQueue.put(message.getMessageContent()[0]);
            }
            case PLAYER_ADDED ->
            {
                messageQueue.put(message.getMessageContent()[0]);
            }
            case USERNAME_CHECK_RESULT ->
            {
                messageQueue.put(message.getMessageContent()[0]);
            }
        }
    }
}
