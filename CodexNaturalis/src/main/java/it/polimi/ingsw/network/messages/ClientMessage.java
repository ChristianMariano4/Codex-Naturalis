package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.enumerations.ClientMessageType;

import java.io.Serializable;

public class ClientMessage implements Serializable {
    private final ClientMessageType messageType;
    private final Object[] messageContent;

    public ClientMessage(ClientMessageType messageType, Object ... messageContent)
    {
       this.messageType = messageType;
       this.messageContent = messageContent;
    }
    public ClientMessageType getMessageType()
    {
        return this.messageType;
    }
    public Object[] getMessageContent()
    {
        return this.messageContent;
    }
}
