package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.Listener;
import it.polimi.ingsw.network.messages.userMessages.UserInputEvent;
import it.polimi.ingsw.network.messages.userMessages.UserMessageWrapper;

public class UserInputListener implements Listener<UserInputEvent> {
    private final Client client;
    public UserInputListener(Client client) {
        this.client = client;
    }

    public void update(UserMessageWrapper message) {
        switch (message.getType()) {
            case USERNAME_INSERTED -> {
                client.setUsername(message.getMessage().getUsername());
            }
        }
    }
}
