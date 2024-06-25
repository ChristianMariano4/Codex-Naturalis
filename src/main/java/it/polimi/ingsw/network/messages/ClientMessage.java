package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.enumerations.ClientMessageType;

import java.io.Serializable;

public class ClientMessage implements Serializable {
    private final ClientMessageType messageType;
    private final Object[] messageContent;

    /**
     * Constructor
     * @param messageType the type of client message to be sent
     * @param messageContent the content of the message to be sent
     */
    public ClientMessage(ClientMessageType messageType, Object ... messageContent)
    {
       this.messageType = messageType;
       this.messageContent = messageContent;
    }

    /**
     * Gets the type of the client message
     * @return the type of the client message
     */
    public ClientMessageType getMessageType()
    {
        return this.messageType;
    }

    /**
     * Gets the content of the client message
     * @return the content of the client message
     */
    public Object[] getMessageContent()
    {
        return this.messageContent;
    }
}
