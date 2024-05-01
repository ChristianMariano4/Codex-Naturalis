package it.polimi.ingsw.network.messages.userMessages;

import java.io.Serializable;

public class UserMessageWrapper implements Serializable {
    private final UserInputEvent type;
    private final UserMessage message;

    public UserMessageWrapper(UserInputEvent type, UserMessage message) {
        this.type = type;
        this.message = message;
    }

    public UserInputEvent getType() {
        return type;
    }

    public UserMessage getMessage() {
        return message;
    }
}
