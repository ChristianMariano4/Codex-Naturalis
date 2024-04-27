package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.Listener;
import it.polimi.ingsw.network.messages.userMessages.UserInputEvent;

public class UserInputListener implements Listener<UserInputEvent> {
    private final Client client;
    public UserInputListener(Client client) {
        this.client = client;
    }

    public void update(UserInputEvent event, Object... args) {
        switch (event) {
            case USERNAME -> {
                client.setUsername((String) args[0]);
            }
        }
    }
}
