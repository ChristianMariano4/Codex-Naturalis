package it.polimi.ingsw.network.observer;

import it.polimi.ingsw.exceptions.NotExistingPlayerException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.client.ClientHandlerInterface;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.messages.GameEvent;
import it.polimi.ingsw.network.rmi.ClientRMIInterface;

import java.io.IOException;
import java.rmi.RemoteException;

public class GameListener implements Listener<GameEvent> {

    private Game updatedGame;
    private final ClientHandlerInterface client;
    private final String username;
    private final Server server;

    public GameListener(ClientHandlerInterface client, Server server) throws IOException {
        this.client = client;
        this.server = server;
        this.username = client.getUsername();
    }

    @Override
    public void update(GameEvent event, Object... args) {
        try {
            server.updateClient(client, event, args[0]);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (NotExistingPlayerException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("We are in GameListener update method");
    }

    public ClientHandlerInterface getClient() {
        return client;
    }

    public String getUsername() {
        return username;
    }
}
