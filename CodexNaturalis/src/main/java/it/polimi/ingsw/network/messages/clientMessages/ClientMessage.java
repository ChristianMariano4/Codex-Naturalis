package it.polimi.ingsw.network.messages.clientMessages;

import it.polimi.ingsw.enumerations.ClientMessageType;
import it.polimi.ingsw.enumerations.ServerMessageType;

import java.io.Serializable;

public class ClientMessage implements Serializable {
    private ClientMessageType messageType;
    private Object[] messageContent;

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
