package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.enumerations.ServerMessageType;

import java.io.Serializable;

public class ServerMessage implements Serializable {
    private final ServerMessageType messageType;
    private final Object[] messageContent;

    /**
     * Constructor
     * @param messageType the type of server message to be sent
     * @param messageContent the content of the message to be sent
     */
    public ServerMessage(ServerMessageType messageType, Object ... messageContent)
    {
        this.messageType = messageType;
        this.messageContent = messageContent;
    }

    /**
     * Gets the type of the server message
     * @return the type of the server message
     */
    public ServerMessageType getMessageType()
    {
        return this.messageType;
    }

    /**
     * Gets the content of the server message
     * @return the content of the server message
     */
    public Object[] getMessageContent()
    {
        return this.messageContent;
    }
}


